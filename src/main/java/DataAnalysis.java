
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import eu.learnpad.verification.plugin.bpmn.guideline.factory.GuidelinesFactory;
import eu.learnpad.verification.plugin.bpmn.guideline.impl.abstractGuideline;
import eu.learnpad.verification.plugin.utils.ElementID;

@ManagedBean(name="DataAnalysis")
@SessionScoped
public class DataAnalysis implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4802290213871735048L;

	private GuidelinesFactory guidelinesfactory;
	private String filename;
	private String xmldata;

	public DataAnalysis(){

		System.out.println("DataAnalysisbean "+guidelinesfactory);
		//	listdata = new ArrayList<DataContent>();
	}



	public void setFilename(String filename) {
		this.filename = filename;
	}


	public void setXmldata(String xml){
		xmldata=xml; 
	}

	public void setGuidelinesfactory(GuidelinesFactory guidelinesfactory) {
		this.guidelinesfactory = guidelinesfactory;
	}

	public Collection<abstractGuideline> getGuidelines() {
		if(this.guidelinesfactory!=null)
			return this.guidelinesfactory.getGuidelines();
		else
			return null;
	}

	public Collection<ElementID>  getElements(){
		Collection<ElementID> Elements = new ArrayList<ElementID>();
		Collection<abstractGuideline> guidelines = this.getGuidelines();
		if(guidelines!=null)
			for (abstractGuideline abstractGuideline : guidelines) {
				if(abstractGuideline.getElements()!=null)
					Elements.addAll(abstractGuideline.getElements());
			}
		return Elements;
	}

	public String getXml() {

		JAXBContext jaxbCtx;
		StringWriter sw = new StringWriter();
		if(guidelinesfactory!=null){
			try {
				jaxbCtx = javax.xml.bind.JAXBContext.newInstance(GuidelinesFactory.class);

				Marshaller marshaller = jaxbCtx.createMarshaller();
				marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
				marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				marshaller.marshal(guidelinesfactory, sw);


			} catch (JAXBException  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return sw.toString();
	}

	public String getUri(){

		return filename;
		//return "/Users/isiu/Downloads/24485.bpmn";
	}

	public String getData(){
		String res = xmldata!=null ?  xmldata.replaceAll("\"","\\\\\"").replaceAll("\n", "").replaceAll("\r", "") : "";
		return res;
	}

	/*public void setXml(String xml) {
		this.xml = xml;
	}



	public String getElement() {
		return element;
	}





	public String executeListener(){

		return "";
	}

	public void listener(ActionEvent event){
		//this.setAcca((AnnotatedCollaborativeContentAnalysis) event.getComponent().getAttributes().get("valdata"));
		System.out.println(event);
	}*/

}
