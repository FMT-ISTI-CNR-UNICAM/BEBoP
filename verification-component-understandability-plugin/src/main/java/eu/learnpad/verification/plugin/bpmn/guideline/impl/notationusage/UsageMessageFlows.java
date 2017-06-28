package eu.learnpad.verification.plugin.bpmn.guideline.impl.notationusage;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.InteractionNode;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.ReceiveTask;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.SendTask;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.impl.InteractionNodeImpl;
import org.eclipse.emf.common.util.URI;

import eu.learnpad.verification.plugin.bpmn.guideline.Messages;
import eu.learnpad.verification.plugin.bpmn.guideline.impl.abstractGuideline;
import eu.learnpad.verification.plugin.bpmn.reader.BPMNUtils;

public class UsageMessageFlows extends abstractGuideline{


	public UsageMessageFlows(Definitions diagram, Locale l){
		super(diagram,l);
		this.l=l;
		this.id = "24"; //$NON-NLS-1$
		this.Description = Messages.getString("UsageMessageFlows.Description",l); //$NON-NLS-1$
		this.Name = Messages.getString("UsageMessageFlows.Name",l); //$NON-NLS-1$
	}

	@Override
	protected void findGL(Definitions diagram) {
		/*StringBuilder temp = new StringBuilder();
		Collection<FlowElement> elementsBPMNtemp = new ArrayList<FlowElement>();
		Collection<ElementID> Elementstemp = new ArrayList<ElementID>();*/
		int num = 0;

		List<MessageFlow> BPMNEdges = BPMNUtils.getAllMessageFlow(diagram);
		for (RootElement rootElement : diagram.getRootElements()) {
			if (rootElement instanceof Process) {
				Process process = (Process) rootElement;
				//System.out.format("Found a process: %s\n", process.getName());

				IDProcess = process.getId();
				for (FlowElement fe : process.getFlowElements()) {
					if(fe instanceof SubProcess){
						SubProcess sub = (SubProcess) fe;
						//System.out.format("Found a SubProcess: %s\n", sub.getName());
						this.searchSubProcess(sub,BPMNEdges);
					}else
						if (fe instanceof Activity) {
							Activity a = (Activity) fe;
							if ( a instanceof ReceiveTask || a instanceof SendTask){
							boolean	flag = searchMessageFlow(a,BPMNEdges);
							if (!flag) {
								num++;
									elementsBPMN.add(fe);
									String name = fe.getName()!=null? fe.getName() : Messages.getString("Generic.LabelEmpty",l);  //$NON-NLS-1$
									setElements(fe.getId(),IDProcess,name);
								//	temp.append("* name=" + name + " ID=" + fe.getId()
									//		+ "\n");
								
								/*elementsBPMNtemp.add(fe);
								Elementstemp.add(new ElementID(fe.getId(),IDProcess));
								temp.append("* name=" + fe.getName() + " ID=" + fe.getId()
										+ "\n");*/
							}
							}
						} 
				}
			}
		}
		if (num>0) {
			/*elementsBPMN.addAll(elementsBPMNtemp);
			setAllElements(Elementstemp);*/
			this.Suggestion += Messages.getString("UsageDefaultFlows.SuggestionKO",l); //$NON-NLS-1$
			this.status = false;
		}else{
			this.status = true;
			this.Suggestion += Messages.getString("UsageDefaultFlows.SuggestionOK",l); //$NON-NLS-1$
		}
	}

	private boolean searchMessageFlow(Activity a,
			List<MessageFlow> bPMNEdges) {
		if(bPMNEdges!=null)
		for (MessageFlow messflow : bPMNEdges) {
			if(messflow.getSourceRef() instanceof InteractionNodeImpl){
			InteractionNodeImpl source = (InteractionNodeImpl) messflow.getSourceRef();
			InteractionNodeImpl target = (InteractionNodeImpl) messflow.getTargetRef();
				String fragments = source.eProxyURI().fragment();
				String fragmentt = target.eProxyURI().fragment();
				String aid = a.getId();
				if(aid.equals(fragmentt) || aid.equals(fragments)){
					return true;
				}
			}else{
				if(messflow.getSourceRef() instanceof Activity){
					if(a.equals(messflow.getSourceRef()) || a.equals(messflow.getTargetRef())){
						return true;
					}
				}
			}
				
				
			
		}
		return false;
	}

	protected void searchSubProcess(SubProcess sub, List<MessageFlow> BPMNEdges){
		/*StringBuilder temp = new StringBuilder();
			Collection<FlowElement> elementsBPMNtemp = new ArrayList<FlowElement>();
		Collection<ElementID> Elementstemp = new ArrayList<ElementID>();*/
		int num = 0;
		for ( FlowElement fe : sub.getFlowElements()) {
			if(fe instanceof SubProcess){
				SubProcess ssub = (SubProcess) fe;
				//System.out.format("Found a SubProcess: %s\n", ssub.getName());
				this.searchSubProcess(ssub,BPMNEdges);
			}else
				if (fe instanceof Activity) {
					Activity a = (Activity) fe;
					if ( a instanceof ReceiveTask || a instanceof SendTask){
					boolean	flag = searchMessageFlow(a,BPMNEdges);
					if (!flag) {
							num++;
							elementsBPMN.add(fe);
							String name = fe.getName()!=null? fe.getName() : Messages.getString("Generic.LabelEmpty",l);  //$NON-NLS-1$
							setElements(fe.getId(),IDProcess,name);
					}
				}
				}
		}
		if ( num>0) {
			/*elementsBPMN.addAll(elementsBPMNtemp);
			setAllElements(Elementstemp);*/
			this.Suggestion += Messages.getString("UsageDefaultFlows.SuggestionSubprocessKO",l)+sub.getName()+" "; //$NON-NLS-1$ //$NON-NLS-2$
			this.status = false;
		}

	}

}
