/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.learnpad.ontology.persistence.util;


import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.jena.riot.Lang;

/**
 * Loads ontology files of given type in given directory and subdirectories.
 * 
 * @author sandro.emmenegger
 */
public class OntologyFileLoader extends SimpleFileVisitor<Path> {
    
    public static OntModel loadModel(String[] rootDirectories, Lang format) throws IOException{
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        model.getDocumentManager().setProcessImports(false);
        for (String rootDirectory : rootDirectories) {
            OntologyFileLoaderVisitor visitor = new OntologyFileLoaderVisitor(format, model);
            Files.walkFileTree(Paths.get(rootDirectory), visitor);
        }
        return model;
    }
    
    private static class OntologyFileLoaderVisitor extends SimpleFileVisitor<Path>{

        private final Lang format;
        private final OntModel model;
                
        OntologyFileLoaderVisitor(Lang format, OntModel model){
            this.model = model;
            this.format = format;
        }
        
        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {

            String filename = path.toString();
            if (checkFileEnding(filename)) {
              model.read(filename, format.getName());
            }
            return FileVisitResult.CONTINUE;
        }
        
        private boolean checkFileEnding(String filename){
            return (format.getFileExtensions().stream().anyMatch((fileExtension) -> (filename.endsWith(fileExtension))));
        }

    }
}
