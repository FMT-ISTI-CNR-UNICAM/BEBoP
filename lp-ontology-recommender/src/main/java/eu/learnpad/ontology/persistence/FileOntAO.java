/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.learnpad.ontology.persistence;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import eu.learnpad.ontology.config.APP;
import eu.learnpad.ontology.persistence.util.OntologyFileLoader;
import eu.learnpad.ontology.transformation.SimpleModelTransformator;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jena.riot.Lang;

/**
 *
 * @author sandro.emmenegger
 */
class FileOntAO extends OntAO {

    @Override
    protected OntModel loadMetaModel() {
        OntModel model = null;
        try {
            model = OntologyFileLoader.loadModel(APP.CONF.getStringArray("ontology.metamodel.path"), Lang.TTL);
        } catch (IOException ex) {
            Logger.getLogger(FileOntAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return model;
    }

    @Override
    protected OntModel loadModelSet(String modelSetId) {
        File modelSetFile = SimpleModelTransformator.getInstance().getLatestVersionFile(modelSetId);
        if(modelSetFile == null){
            return null;
        }
        
        OntModel modelSet = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        modelSet.read(modelSetFile.toURI().toString(), "TTL");
        return modelSet;
    }
    
}
