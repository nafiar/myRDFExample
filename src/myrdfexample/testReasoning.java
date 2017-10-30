/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myrdfexample;

import java.io.InputStream;
import static org.apache.jena.assembler.Assembler.ontModelSpec;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.PrintUtil;
import org.apache.jena.vocabulary.RDF;

/**
 *
 * @author nafiar
 */
public class testReasoning {
    
    static String camOwl = "test/camera.owl";
    static String camNS = "http://www.xfront.com/owl/ontologies/camera/#";
    static String owl = "http://www.co-ode.org/ontologies/ont.owl#";
    
    public static void main(String[] arg) {
        OntModel camera = loadOwl(camOwl);
        
        Resource cameraClass = camera.getOntClass(camNS + "Camera");
        Property viewFinder = (Property)camera.getObjectProperty(camNS + "viewFinder");
        RDFNode throughTheLens = (RDFNode)camera.getIndividual(camNS + "ThroughTheLens");
        
        Resource nikkon = camera.getIndividual(owl + "Nikkon");
        System.out.println("nikkon *:");
        printStatement(camera, nikkon, null, null);
        
        Model data = ModelFactory.createDefaultModel();
        
        data.createResource(camNS + "Canon")
                .addProperty(RDF.type, cameraClass)
                .addProperty(viewFinder, throughTheLens);
        System.out.println("isi data :");
        printStatement(data, null, null, null);
        
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(camera);
        InfModel infModel = ModelFactory.createInfModel(reasoner, data);
        
            Resource canon = infModel.getResource(camNS + "Canon");
        System.out.println("canon *:");
        printStatement((Model)infModel, canon, null, null);
        
        System.out.println("nikkon after :");
        printStatement((Model)infModel, nikkon, null, null);
    }
    
    public static OntModel loadOwl(String file) {
        
        OntDocumentManager manager = new OntDocumentManager();
        OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM_TRANS_INF);
        spec.setDocumentManager(manager);
        OntModel model = ModelFactory.createOntologyModel(spec, null);
        
        InputStream in = FileManager.get().open(camOwl);
        if(in == null) {
            System.out.println("File not Found");
        }
        
        model.read(in, "");
        
        return model;
    }
    
    
    public static void printStatement(Model m, Resource s, Property p, Resource o) {
        
        for (StmtIterator i = m.listStatements(s,p,o); i.hasNext(); ) {
            
            Statement stmt = i.nextStatement();
            System.out.println(" - " + PrintUtil.print(stmt));
        }
    }
}
