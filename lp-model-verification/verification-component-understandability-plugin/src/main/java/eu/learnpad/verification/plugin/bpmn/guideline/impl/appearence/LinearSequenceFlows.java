package eu.learnpad.verification.plugin.bpmn.guideline.impl.appearence;


import java.util.List;
import java.util.Map;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.dd.dc.Point;

import eu.learnpad.verification.plugin.bpmn.guideline.impl.abstractGuideline;
import eu.learnpad.verification.plugin.bpmn.reader.BPMNUtils;

public class LinearSequenceFlows extends abstractGuideline{


	public LinearSequenceFlows(Definitions diagram) {
		super(diagram);
		this.id = "45";
		this.Description = "Linear sequence flows without useless foldings help to maintain the model clear.";
		this.Name = "Linear sequence flows";


	}

	@Override
	protected void findGL(Definitions diagram) {
		/*StringBuilder temp = new StringBuilder();
		Collection<FlowElement> elementsBPMNtemp = new ArrayList<FlowElement>();
		Collection<ElementID> Elementstemp = new ArrayList<ElementID>();
*/

		int num = 0;
		Map<BaseElement,BPMNEdge> BPMNEdges = BPMNUtils.getAllBPMNEdge(diagram);
		for (RootElement rootElement : diagram.getRootElements()) {
			if (rootElement instanceof Process) {
				Process process = (Process) rootElement;
				// System.out.format("Found a process: %s\n",
				// process.getName());

				IDProcess = process.getId();
				for (FlowElement fe : process.getFlowElements()) {
					if(fe instanceof SequenceFlow){
						SequenceFlow sf = (SequenceFlow) fe;
						//BPMNEdge res = BPMNUtils.findBPMNEdge(diagram, sf);
						BaseElement base= (BaseElement)sf;
						boolean resd = BPMNEdges.containsKey(base);
						if(resd){
							List<Point> points = BPMNEdges.get(base).getWaypoint();
							if(points!=null)
								if(points.size()==2 | points.size()==4 | points.size()==6){
									num++;

									elementsBPMN.add(fe);
									String name = sf.getName()!=null? sf.getName() : "Unlabeled"; 
									setElements(fe.getId(),IDProcess,name);
									//ret.append(i++ +") name=" + name + " ID=" + fe.getId()
									//		+ "\n");
								}
						}
					}

				}
			}
		}


	//
		/*	for (BPMNEdge bpmnEdge : BPMNEdges.values()) {
			BaseElement bpmnelement = bpmnEdge.getBpmnElement();
			if(bpmnelement instanceof SequenceFlow){
				bpmnelement.getId();
				List<Point> points = bpmnEdge.getWaypoint();
				if(points!=null)
					if(points.size()>2){

					}
			}
		}

		*/
		if (num>1) {
			
			this.Suggestion += "\nUse Linear sequence flows: ";
			this.status = false;
		}else{
			this.status = true;
			this.Suggestion += "Well done!";
		}
	}



}
