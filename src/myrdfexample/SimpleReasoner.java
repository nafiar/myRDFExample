/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myrdfexample;

import java.io.InputStream;
import java.util.Iterator;
import org.apache.jena.ontology.Individual;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.reasoner.ValidityReport;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.PrintUtil;
import org.apache.jena.vocabulary.RDF;

/**
 *
 * @author nafiar
 */
public class SimpleReasoner {
    public static void main(String[] argv){
        Model schema = FileManager.get().loadModel("test/owlDemoSchema.owl");
        Model data = ModelFactory.createDefaultModel();
//        InputStream in = FileManager.get().open("test/vc-db-1.rdf");
        InputStream in2 = FileManager.get().open("test/owlDemoData.rdf");
//        data.read(in, "");
        data.read(in2, "");
        //Resource nForce1 = data.getResource("urn:x-hp:eg/nForce");
        //data.write(System.out, "N-TRIPLES");
        printStatements(data, null, null, null);
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schema);
        Model infmodel = ModelFactory.createInfModel(reasoner, data);
//        infmodel.write(System.out, "N-TRIPLES");
        
        Resource whiteBox = infmodel.getResource("urn:x-hp:eg/whiteBoxZX");
        System.out.println("nForce *:");
        printStatements(infmodel, whiteBox, null, null);
        
//        checkWhiteBox(infmodel);
    }
    
    public static void printStatements(Model m, Resource s, Property p, Resource o) {
        for (StmtIterator i = m.listStatements(s,p,o); i.hasNext(); ) {
            Statement stmt = i.nextStatement();
            System.out.println(" - " + PrintUtil.print(stmt));
        }
    }
    
    public static void checkWhiteBox(InfModel infmodel){
        Resource gamingComputer = infmodel.getResource("urn:x-hp:eg/GamingComputer");
        Resource whiteBox = infmodel.getResource("urn:x-hp:eg/whiteBoxZX");
        if (infmodel.contains(whiteBox, RDF.type, gamingComputer)) {
            System.out.println("White box recognized as gaming computer");
        } else {
            System.out.println("Failed to recognize white box correctly");
        }
        
        ValidityReport validity = infmodel.validate();
        if (validity.isValid()) {
            System.out.println("OK");
        } else {
            System.out.println("Conflicts");
            for (Iterator i = validity.getReports(); i.hasNext(); ) {
                ValidityReport.Report report = (ValidityReport.Report)i.next();
                System.out.println(" - " + report);
            }
        }
    }
}
