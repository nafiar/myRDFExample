/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package penelitian;

import java.io.InputStream;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.PrintUtil;
import org.apache.jena.vocabulary.VCARD;

/**
 *
 * @author nafiar
 */
public class MyReasoner {
    static final String inputFile1 = "test/vc-db-1.rdf";
    static final String inputFile2 = "test/owlDemoData.rdf";
    static final String amangkurat1 = "test/File_RDF/Amangkurat_I.rdf";
    static final String amangkurat2 = "test/File_RDF/Amangkurat_II.rdf";
    static final String amangkurat3 = "test/File_RDF/Amangkurat_III.rdf";
    static final String iheroOwl = "test/ihero.owl";
    static final String owlFile = "test/owlDemoSchema.owl";
    static final String simpleFamilyOwl = "test/simpleFamilyRDF.owl";
    static final String dsFusekiSparql = "http://10.151.34.43:3030/ds/sparql";
    static final String penelitianFusekiSparql = "http://10.151.34.43:3030/penelitian/data"; 
    
    public static void main( String[] argv ) {
        Model mainModel = ModelFactory.createDefaultModel();
        readDB(mainModel);
        readRdf(mainModel);
        Model reasoningResult = reasonModel(mainModel);
        
        System.out.println("hasil reasoning dari (rdf + db) dan owl");
//        printStatement(reasoningResult, null, null, null);
        Model finishedModel = queryModel(reasoningResult); 
        insertData(finishedModel);
    }
    
    public static void readDB( Model model ) {
        
        String queryStr = "construct { ?s ?p ?o } where { ?s ?p ?o }";
        Query query = QueryFactory.create(queryStr);
        
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(dsFusekiSparql
                , query)) {
            // Set remote exec timeout
            ((QueryEngineHTTP) qexec).addParam("timeout", "10000");
            
            // Execute.
            model = qexec.execConstruct();
        }
    }
    
    public static void readRdf( Model model ) {

        InputStream in1 = FileManager.get().open( amangkurat1 );
        InputStream in2 = FileManager.get().open( amangkurat2 );
        InputStream in3 = FileManager.get().open( amangkurat3 );
        
        if ( in1 == null || in2 == null  || in3 == null ) {
            System.out.println( "File tidak ditemukan" );
        }
        model.read( in1, "" );
        model.read( in2, "" );
        model.read( in3, "" );
    }
    
    public static Model reasonModel( Model model ) {
        OntModel owl = loadOwl(simpleFamilyOwl);
        
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema( owl );
        InfModel reasoningResult = ModelFactory.createInfModel( reasoner, model );
        
        return (Model)reasoningResult;
    }
    
    public static Model queryModel( Model model ) {
        String queryStr = "construct { ?s ?p ?o } where { ?s ?p ?o } limit 10";
        Query query = QueryFactory.create(queryStr);
        
        try( QueryExecution qexec = QueryExecutionFactory.create(query, model) ) {
            
            Model queryResult = ModelFactory.createDefaultModel();
            
            queryResult = qexec.execConstruct();
            queryResult.write(System.out, "N-TRIPLES");
            return queryResult;
        }
    }
    
    public static void insertData(Model model) {
        DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(penelitianFusekiSparql);
        if (accessor != null) {
            accessor.add(model);
        }
    }
    
    public static OntModel loadOwl(String file) {
        
        OntDocumentManager manager = new OntDocumentManager();
        OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM_TRANS_INF);
        spec.setDocumentManager(manager);
        OntModel model = ModelFactory.createOntologyModel(spec, null);
        
        InputStream in = FileManager.get().open(file);
        if(in == null) {
            System.out.println("File not Found");
        }
        
        model.read(in, "RDF");
        
        return model;
    }
    
    public static void printStatement(Model m, Resource s, Property p, Resource o) {
        
        for (StmtIterator i = m.listStatements(s,p,o); i.hasNext(); ) {
            
            Statement stmt = i.nextStatement();
            System.out.println(" - " + PrintUtil.print(stmt));
        }
    }
    
//    public static void testQuery( Model myModel ) {
//        String queryStr = "select distinct ?s ?p ?o where { ?s ?p ?o . } limit 10";
//        Query query = QueryFactory.create(queryStr);
//        
//        try( QueryExecution qexec = QueryExecutionFactory.create(query, model) ) {
//            
//            Model queryResult = ModelFactory.createDefaultModel();
//            
//            queryResult = qexec.execConstruct();
//            queryResult.write(System.out, "N-TRIPLES");
//            return queryResult;
//        }
//    }
}
