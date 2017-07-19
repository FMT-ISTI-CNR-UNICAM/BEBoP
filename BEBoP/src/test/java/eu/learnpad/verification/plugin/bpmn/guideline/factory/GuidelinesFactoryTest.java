package eu.learnpad.verification.plugin.bpmn.guideline.factory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.FileUtils;
import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.SubProcess;
import org.glassfish.jersey.server.Uri;
import org.junit.Test;

import eu.learnpad.verification.plugin.bpmn.guideline.impl.abstractGuideline;
import eu.learnpad.verification.plugin.bpmn.reader.MyBPMN2ModelReader;

public class GuidelinesFactoryTest {

	@Test
	public void testGuidelinesFactoryListOfRootElement() {
		/*genarateTestforFile("lanes.bpmn","11");
		genarateTestforFile("lanes.bpmn","29");
		genarateTestforFile("pizza.bpmn","16");
		genarateTestforFile("24485.bpmn","16");
		genarateTestforFile("ExplicitStartEndEvents.bpmn","12");
		genarateTestforFile("annidategateway.bpmn","16");
		genarateTestforFile("SplitAndJoinFlows.bpmn","18");
		genarateTestforFile("modelloTest.bpmn","18");
		genarateTestforFile("modelloTestO.bpmn","18");
		//genarateTestforFile("test7.bpmn","13");
		genarateTestforFile("test7.bpmn","13");
		genarateTestforFile("test8.bpmn","15");
		genarateTestforFile("MeaningfulGateways.bpmn","20");
		genarateTestforFile("testorg.bpmn","21");
		genarateTestforFile("test7.bpmn","30");
		genarateTestforFile("test7.bpmn","31");
		genarateTestforFile("test7.bpmn","33");
		genarateTestforFile("test7.bpmn","32");
		genarateTestforFile("test7.bpmn","34");
		genarateTestforFile("ConvergingGateways.bpmn","35");
		genarateTestforFile("ConvergingGateways.bpmn","36");
		genarateTestforFile("ConvergingGateways.bpmn","37");
		genarateTestforFile("LoopMarkerAnnotation.bpmn","39");*/
		String sep = File.separator;
		genarateTestforFile("journal"+sep+"Example_Fig1.bpmn","14");

	}

	private void genarateTestforFile(String NameFile,String id){

		try {

			URL is = GuidelinesFactoryTest.class.getClassLoader().getResource(NameFile);
			assertNotNull(is);
			// File temp = File.createTempFile("tempfiletest", ".tmp"); 
			//temp.deleteOnExit();

			//Files.copy(is,temp.toPath(),java.nio.file.StandardCopyOption.REPLACE_EXISTING);


			MyBPMN2ModelReader readerBPMN = new MyBPMN2ModelReader();





			GuidelinesFactory eg = new GuidelinesFactory(readerBPMN.readJavaURIModel(is.toURI().toString()));
			eg.setVerificationType("UNDERSTANDABILITY");
			eg.StartSequential();
			//System.out.println(eg);

			for ( abstractGuideline iterable_element : eg.getGuidelines()) {
				if(iterable_element.getid().equals(id)){
					if(iterable_element.getStatus()){
						System.out.println(NameFile+" "+ id);
						fail();
					}
				}
			}
			JAXBContext jaxbContext = JAXBContext.newInstance(GuidelinesFactory.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			OutputStream os = new FileOutputStream( "nosferatu"+ NameFile.substring(0, NameFile.length()-19)+".xml" );
			jaxbMarshaller.marshal( eg, os );

			// jaxbMarshaller.marshal(eg, System.out);

			assertTrue(eg.getStatus().equals("KO"));
		} catch (JAXBException  | URISyntaxException  | IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

	@Test
	public void testGuidelinesFactoryListOfRootElementGood() {
		genarateTestforFileOk2("TitoloUnico_MontiAzzurrSUB.bpmn","2");
		genarateTestforFileOk2("EPBR - Business Process.bpmn","8");
		genarateTestforFileOk2("EPBR - Business Process.bpmn","35");
		genarateTestforFileOk2("EPBR - Business Process.bpmn","36");
		genarateTestforFileOk2("test7.bpmn","2");
		genarateTestforFileOk2("TitoloUnico/19311.bpmn","14");
	}
	/*	private void genarateTestforFileOk(String NameFile,String id){
		try {

			URL is = GuidelinesFactoryTest.class.getClassLoader().getResource(NameFile);
			assertNotNull(is);
			 File temp = File.createTempFile("tempfiletest", ".tmp"); 
            temp.deleteOnExit();

            Files.copy(is,temp.toPath(),java.nio.file.StandardCopyOption.REPLACE_EXISTING);


			MyBPMN2ModelReader readerBPMN = new MyBPMN2ModelReader();





			GuidelinesFactory eg = new GuidelinesFactory(readerBPMN.readJavaURIModel(is.toURI().toString()));
			eg.setVerificationType("UNDERSTANDABILITY");
			eg.StartSequential();
			//System.out.println(eg);

			JAXBContext jaxbContext = JAXBContext.newInstance(GuidelinesFactory.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			for ( abstractGuideline iterable_element : eg.getGuidelines()) {
				if(iterable_element.getid().equals(id)){
					if(!iterable_element.getStatus()){
						fail();
					}
				}
			}

			//OutputStream os = new FileOutputStream( "nosferatuB"+ NameFile.substring(0, NameFile.length()-4)+".xml" );
			//jaxbMarshaller.marshal( eg, os );

			//jaxbMarshaller.marshal(eg, System.out);
			assertTrue(eg.getStatus().equals("OK"));

		} catch (JAXBException  | URISyntaxException  | IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 


	}*/


	private void genarateTestforFileOk2(String NameFile,String id){
		try {

			URL is = GuidelinesFactoryTest.class.getClassLoader().getResource(NameFile);
			assertNotNull(is);
			/* File temp = File.createTempFile("tempfiletest", ".tmp"); 
            temp.deleteOnExit();

            Files.copy(is,temp.toPath(),java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			 */

			MyBPMN2ModelReader readerBPMN = new MyBPMN2ModelReader();





			GuidelinesFactory eg = new GuidelinesFactory(readerBPMN.readJavaURIModel(is.toURI().toString()));
			eg.setVerificationType("UNDERSTANDABILITY");
			eg.StartSequential();
			//System.out.println(eg);

			JAXBContext jaxbContext = JAXBContext.newInstance(GuidelinesFactory.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			for ( abstractGuideline iterable_element : eg.getGuidelines()) {
				if(iterable_element.getid().equals(id)){
					if(!iterable_element.getStatus()){
						fail();
					}
				}
			}

			//OutputStream os = new FileOutputStream( "nosferatuBB"+ NameFile.substring(0, NameFile.length()-4)+".xml" );
			//jaxbMarshaller.marshal( eg, os );

			jaxbMarshaller.marshal(eg, System.out);
			//assertTrue(eg.getStatus().equals("OK"));

		} catch (JAXBException  | URISyntaxException  | IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 


	}


	@Test
	public void testGuidelinesFactoryListGood() {
		List<String> ldirectory = new ArrayList<String>();
		String sep = File.separator;
		//	ldirectory.add("EPBR-Coordinator"+sep+"20903.bpmn");
		//ldirectory.add("EPBR-Coordinator"+sep+"21099.bpmn");
		//ldirectory.add("EPBR-Coordinator"+sep+"21203.bpmn");
		//ldirectory.add("EPBR-Coordinator"+sep+"21385.bpmn");
		//	ldirectory.add("EPBR-Coordinator"+sep+"21417.bpmn");
		//	ldirectory.add("EPBR-Coordinator"+sep+"21823.bpmn");
		//ldirectory.add("TitoloUnico"+sep+"diagram.bpmn");
		//ldirectory.add("TitoloUnico"+sep+"20250.bpmn");
		//ldirectory.add("TitoloUnico"+sep+"20386.bpmn");
		//		ldirectory.add("TitoloUnico"+sep+"20461.bpmn");
		for(String filename: ldirectory){
			//	genarateTestforFileOk2(filename);
		}

	}

	private void genarateTestforFileOk2(String NameFile){
		try {
			System.out.println(NameFile);
			URL is = GuidelinesFactoryTest.class.getClassLoader().getResource(NameFile);
			assertNotNull(is);

			/* File temp = File.createTempFile("tempfiletest", ".tmp"); 
            temp.deleteOnExit();

            Files.copy(is,temp.toPath(),java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			 */

			MyBPMN2ModelReader readerBPMN = new MyBPMN2ModelReader();





			GuidelinesFactory eg = new GuidelinesFactory(readerBPMN.readJavaURIModel(is.toURI().toString()));
			eg.setVerificationType("UNDERSTANDABILITY");
			eg.StartSequential();
			//System.out.println(eg);

			JAXBContext jaxbContext = JAXBContext.newInstance(GuidelinesFactory.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			/*		String nFile = NameFile.replace(File.separator, "");
		OutputStream os = new FileOutputStream( "nosferatuvv"+ nFile.substring(0, nFile.length()-4)+"xml" );
			jaxbMarshaller.marshal( eg, os );
			 */
			if(!eg.getStatus().equals("OK")){

				fail();

			}




			//jaxbMarshaller.marshal(eg, System.out);
			//assertTrue(eg.getStatus().equals("OK"));

		} catch (JAXBException  | URISyntaxException  | IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 


	}

	@Test
	public void testSignavioModels() {
	/*	try{
			String path = "C:\\Users\\winspa\\Google Drive\\isti\\modelCollection_1499071506741\\";
			String sep = File.separator;
			File folder = new File(path);



			List<File> list = Arrays.asList(folder.listFiles(new FilenameFilter(){
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".bpmn"); // or something else
				}}));
			List<String> ls  = new ArrayList<>();

			String[] SUFFIX = {"bpmn"};  

			Collection<File> files = FileUtils.listFiles(folder, SUFFIX , true);


			for(File f: files){

				try{



					String content = FileUtils.readFileToString(f, "UTF-8");
					content = content.replaceAll("-", ".");
					content = content.replaceAll("UTF.8", "UTF-8");
					File tempFile = new File(f.getName()+"1");
					FileUtils.writeStringToFile(tempFile, content, "UTF-8");

					URL is = tempFile.toURI().toURL();
					assertNotNull(is);

					MyBPMN2ModelReader readerBPMN = new MyBPMN2ModelReader();




					Definitions d = readerBPMN.readJavaURIModel(is.toURI().toString());
					int i = 0;
					for (RootElement rootElement : d.getRootElements()) {
						if (rootElement instanceof Process) {
							Process process = (Process) rootElement;
							//System.out.format("Found a process: %s\n", process.getName());
							
							
							for (FlowElement fe : process.getFlowElements()) {
								if (fe instanceof Activity || fe instanceof org.eclipse.bpmn2.Event|| fe instanceof Gateway || fe instanceof SubProcess) {

									i++;
								}  
							}

						}
					}
					
					GuidelinesFactory eg = new GuidelinesFactory(d);
					eg.setVerificationType("UNDERSTANDABILITY");
					eg.StartSequential();
					//System.out.println(eg);

					JAXBContext jaxbContext = JAXBContext.newInstance(GuidelinesFactory.class);
					Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

					// output pretty printed
					jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

					String item = f.getName()+";";
					for ( abstractGuideline iterable_element : eg.getGuidelines()) {
						boolean status = iterable_element.getStatus();
						item+=status+";";

					}
					item+=i+"\n\r";
					ls.add(item);
					new File("esaminati").mkdirs();
					OutputStream os = new FileOutputStream("esaminati/BEPoP_"+ f.getName().substring(0, f.getName().length()-4)+".xml" );
					jaxbMarshaller.marshal( eg, os );

					//jaxbMarshaller.marshal(eg, System.out);
					//assertTrue(eg.getStatus().equals("OK"));
				}catch(JAXBException  | URISyntaxException e){
					System.out.println(f.getName());
					System.out.println(e.getMessage());
				}

			}

			System.out.println(ls);
		}catch(IOException e){

		}*/
	}
}
