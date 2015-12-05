package eu.learnpad.verification.plugin.bpmn.guideline.impl.labeling;


import java.util.List;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.SubProcess;

import eu.learnpad.verification.plugin.bpmn.guideline.impl.abstractGuideline;


public class LabelingMessageEvent extends abstractGuideline{


	public LabelingMessageEvent(Definitions diagram) {
		super(diagram);
		this.id = "33";
		this.Description = "Whenever the modeler uses a message event he should draw the message flow, and label the event. When a focus on the message itself is required, the modeler can represent a message icon and label it with the name of the message.";
		this.Name = "Labeling Message Event";


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
						if (fe instanceof StartEvent) {
							StartEvent a = (StartEvent) fe;
							List<EventDefinition> def = a.getEventDefinitions();
							for (EventDefinition eventDefinition : def) {
								if(eventDefinition instanceof MessageEventDefinition){
									if(a.getName()==null || (a.getName().length()>0) ){
										//System.out.println(fe.eClass().getName() + ": name="+ fe.getName() + " ID=" + fe.getId());
										num++;

										elementsBPMN.add(fe);
										setElements(fe.getId(),IDProcess);
										temp.append("* name=" + fe.getName() + " ID=" + fe.getId()
												+ "\n");
									}
								}
							}

						} else
							if (fe instanceof EndEvent) {
								EndEvent a = (EndEvent) fe;
								List<EventDefinition> def = a.getEventDefinitions();
								for (EventDefinition eventDefinition : def) {
									if(eventDefinition instanceof MessageEventDefinition){
										if(a.getName()==null || (a.getName().length()>0) ){
											//System.out.println(fe.eClass().getName() + ": name="+ fe.getName() + " ID=" + fe.getId());
											num++;

											elementsBPMN.add(fe);
											setElements(fe.getId(),IDProcess);
											temp.append("* name=" + fe.getName() + " ID=" + fe.getId()
													+ "\n");
										}
									}
								}

							} 
				}
			}
		}
		if (num>0) {

			this.Suggestion = "\nLabelling Start/End Events :" + temp;
			this.status = false;
		}else{
			this.status = true;
			this.Suggestion = "Well done!";
		}
	}

	protected void searchSubProcess(SubProcess sub){
		StringBuilder temp = new StringBuilder();

		int num = 0;
		for ( FlowElement fe : sub.getFlowElements()) {
			if(fe instanceof SubProcess){
				SubProcess ssub = (SubProcess) fe;
				//System.out.format("Found a SubProcess: %s\n", ssub.getName());
				this.searchSubProcess(ssub);
			}else
				if (fe instanceof StartEvent) {

					StartEvent a = (StartEvent) fe;
					List<EventDefinition> def = a.getEventDefinitions();
					for (EventDefinition eventDefinition : def) {
						if(eventDefinition instanceof MessageEventDefinition){
							if(a.getName()==null || (a.getName().length()>0) ){
								//System.out.println(fe.eClass().getName() + ": name="+ fe.getName() + " ID=" + fe.getId());
								num++;

								elementsBPMN.add(fe);
								setElements(fe.getId(),IDProcess);
								temp.append("* name=" + fe.getName() + " ID=" + fe.getId()
										+ "\n");
							}
						}
					}
					

				} else
					if (fe instanceof EndEvent) {
						EndEvent a = (EndEvent) fe;
						List<EventDefinition> def = a.getEventDefinitions();
						for (EventDefinition eventDefinition : def) {
							if(eventDefinition instanceof MessageEventDefinition){
								if(a.getName()==null || (a.getName().length()>0) ){
									//System.out.println(fe.eClass().getName() + ": name="+ fe.getName() + " ID=" + fe.getId());
									num++;

									elementsBPMN.add(fe);
									setElements(fe.getId(),IDProcess);
									temp.append("* name=" + fe.getName() + " ID=" + fe.getId()
											+ "\n");
								}
							}
						}

					} 
		}
		if ( num>0) {
			this.Suggestion += "\nLabelling Start/End Events in SubProcess "+sub.getName()+" " + temp;
			this.status = false;
		}

	}

}