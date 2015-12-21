package eu.learnpad.verification.plugin.bpmn.guideline.impl;


import java.util.List;
import java.util.Locale;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Documentation;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.SubProcess;

import eu.learnpad.verification.plugin.bpmn.guideline.Messages;
import eu.learnpad.verification.plugin.bpmn.guideline.impl.abstractGuideline;

public class ActivityDescription extends abstractGuideline{


	public ActivityDescription(Definitions diagram, Locale l){
		super(diagram,l);
		this.l=l;
		this.id = "8"; //$NON-NLS-1$
		this.Description = Messages.getString("ActivityDescription.Description",l); //$NON-NLS-1$
		this.Name = Messages.getString("ActivityDescription.Name",l); //$NON-NLS-1$


	}

	@Override
	protected void findGL(Definitions diagram) {
		StringBuilder temp = new StringBuilder();

		int num = 0;


		for (RootElement rootElement : diagram.getRootElements()) {
			if (rootElement instanceof Process) {
				Process process = (Process) rootElement;
				//System.out.format("Found a process: %s\n", process.getName());

				IDProcess = process.getId();
				for (FlowElement fe : process.getFlowElements()) {
					if(fe instanceof SubProcess){
						SubProcess sub = (SubProcess) fe;
						//System.out.format("Found a SubProcess: %s\n", sub.getName());
						this.searchSubProcess(sub);
					}else
						if (fe instanceof Activity) {
							Activity a = (Activity) fe;
							List<Documentation> doc = a.getDocumentation();
							if(doc.isEmpty() ){
								num++;

								elementsBPMN.add(fe);
								
								String name = fe.getName()!=null? fe.getName() : Messages.getString("Generic.LabelEmpty",l);  //$NON-NLS-1$
								setElements(fe.getId(),IDProcess,name);
								temp.append("* name=" + name + " ID=" + fe.getId() //$NON-NLS-1$ //$NON-NLS-2$
										+ "\n"); //$NON-NLS-1$

							}

						} 
				}
			}
		}
		if (num>0) {

			this.Suggestion += Messages.getString("ActivityDescription.SuggestionKO",l); //$NON-NLS-1$
			this.status = false;
		}else{
			this.status = true;
			this.Suggestion += Messages.getString("ActivityDescription.SuggestionOK",l); //$NON-NLS-1$
		}
	}

	protected void searchSubProcess(SubProcess sub){
		

		int num = 0;
		for ( FlowElement fe : sub.getFlowElements()) {
			if(fe instanceof SubProcess){
				SubProcess ssub = (SubProcess) fe;
				//System.out.format("Found a SubProcess: %s\n", ssub.getName());
				this.searchSubProcess(ssub);
			}else
				if (fe instanceof Activity) {

					Activity a = (Activity) fe;
					List<Documentation> doc = a.getDocumentation();
					if(doc.isEmpty() ){
						//System.out.println(fe.eClass().getName() + ": name="+ fe.getName()!=null? fe.getName() : "Unlabeled" + " ID=" + fe.getId());
						num++;

						elementsBPMN.add(fe);
						String name = fe.getName()!=null? fe.getName() : Messages.getString("Generic.LabelEmpty",l);  //$NON-NLS-1$
						setElements(fe.getId(),IDProcess,name); 
						
					}

				}
		}
		if ( num>0) {
			this.Suggestion += Messages.getString("ActivityDescription.SuggestionSubprocessKO",l)+sub.getName(); //$NON-NLS-1$
			this.status = false;
		}

	}

}
