

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.icefaces.ace.component.fileentry.FileEntry;
import org.icefaces.ace.component.fileentry.FileEntryEvent;
import org.icefaces.ace.component.fileentry.FileEntryResults;

import eu.learnpad.verification.plugin.bpmn.guideline.factory.GuidelinesFactory;





@ManagedBean(name="ContentBean")
@RequestScoped
public class ContentBean implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 2574895928017048791L;

	private int id;
	private String Content;
	private String restid;
	private String filecontent;
	private String filename;
	private String status;
	private String Language;
	private GuidelinesFactory guidelinesfactory;



	public ContentBean(){
		
	}

	@PostConstruct
    public void initialize() {

    }

	public void changeLanguage(ValueChangeEvent e) {
		this.Language = e.toString();
	}
	
	public String getLanguage() {
		return this.Language ;
	}

	public void setLanguage(String l) {
		this.Language = l;
	}


	public GuidelinesFactory getGuidelinesfactory() {
		return guidelinesfactory;
	}






	public void setGuidelinesfactory(GuidelinesFactory guidelinefactory) {
		this.guidelinesfactory = guidelinefactory;
	}






	public String getFilename() {
		return filename;
	}

	public void setName(String name) {
		 filename = name;
	}











	public String getFilecontent() {
		return filecontent;
	}


	public void setFilecontent(String filecontent) {
		this.filecontent = filecontent;
	}


	public String getRestid() {
		return restid;
	}



	public void setRestid(String restid) {
		this.restid = restid;
	}




	public String getContent() {
		return Content;
	}



	public void setContent(String content) {
		Content = content;
	}

	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}

	public void sampleListener(FileEntryEvent e) {
		FileEntry fe = (FileEntry)e.getComponent();
		FileEntryResults results = fe.getResults();

		for (FileEntryResults.FileInfo fileInfo : results.getFiles()) {
			System.out.println(fileInfo);


			if (fileInfo.isSaved()) {
				// Process the file. Only save cloned copies of results or fileInfo,StandardCharsets.UTF_8

				Path path = Paths.get(fileInfo.getFile().getAbsolutePath());
				try {
					filecontent = 	new String(Files.readAllBytes(path), "UTF8");
					filename = fileInfo.getFile().toString();// fileInfo.getFileName();
					System.out.println(filecontent);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

	}

	
	
	
	public void GENExample(String name) {
		
		test(name);
	}
	


	public List<String> getAllResouce(){
		String sep = File.separator;
		List<String> resourcesDir = new ArrayList<String>();
		resourcesDir.add("journal"+sep);
		resourcesDir.add("test"+sep);
		resourcesDir.add("model"+sep);
		List<String> files = new ArrayList<String>();
		try {
		
			
			for(String resourceDir: resourcesDir){
			URL url = ContentBean.class.getResource(resourceDir);
			if (url == null) {
			     // error - missing folder
			} else {
			    File dir = new File(url.toURI());
			    for (File nextFile : dir.listFiles()) {
			        // Do something with nextFile
			    	files.add(resourceDir+nextFile.getName());
			    }
			}
			}
		
			return files;
		} catch ( URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*for(String f : files){
		  String data= IOUtils.toString(Thread.currentThread().getClass().getClassLoader().getResourceAsStream(resourceDir + f));
		  ....process data
		}*/
		
		return null;
	}

	private void test(String name) {
		try {
			
			URL is = ContentBean.class.getClassLoader().getResource(name);
			Path path = Paths.get(is.toURI());

			filecontent = 	new String(Files.readAllBytes(path), "UTF8");

			filename = path.toFile().toString();// fileInfo.getFileName();
			send();
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}






	public void submitButton(ActionEvent event) {
		send();
	}

	public void send(){
		if(filecontent!=null){

			Client client = ClientBuilder.newClient();
			String lang = "en";
			if(Language!=null){
				if( Language.equals("Italian")){
					lang = "it";
				}
			}
			WebTarget target = client.target("http://localhost:8080").path("verification-component-understandability-plugin/validatemodel/put").queryParam("lang", lang);


			Entity<String> entity = Entity.entity(filecontent,MediaType.TEXT_PLAIN);

			Response response =  target.request(MediaType.TEXT_PLAIN).post(entity);

			String id = response.readEntity(String.class);

			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getRequestMap().put("rest", id);

			//context.getApplication().evaluateExpressionGet(context, "#{ContentAnalysisBean.setId("+id+")}", String.class);

			this.setRestid(id);

			System.out.println("Submit Clicked: " + Content + ", " + id + "; ");


			target = client.target("http://localhost:8080").path("verification-component-understandability-plugin/validatemodel/"+id+"/status");
			String 	status ="";
			while (!status.equals("OK")) {


				status = target.request().get(String.class);

				this.setStatus(status);
			}
			System.out.println("Status: "+status);


			target = client.target("http://localhost:8080").path("verification-component-understandability-plugin/validatemodel/"+id);
			Response response2 =  target.request().get();

			GuidelinesFactory glres = response2.readEntity(GuidelinesFactory.class);
			this.setGuidelinesfactory(glres);
			// context = FacesContext.getCurrentInstance();
			//context.getExternalContext().getRequestMap().put("guidelinefactory", glres);

			//GuidelinesFactory res = context.
			//getApplication().
			//evaluateExpressionGet(context, "#{ContentAnalysis.guidelinefactory}", GuidelinesFactory.class);



			//System.out.println("Guideline: "+this.getGuidelinesfactory());
			//this.setCollectionannotatedcontent(
			//annotatecontent.readEntity(
			//new GenericType<Collection<AnnotatedCollaborativeContentAnalysis>>() {}));



		}
	}
}
