/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package penelitian;

import java.io.FileWriter;
import java.io.IOException;
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
import org.apache.jena.graph.Triple;
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
    static final String radenWijaya = "test/File_RDF/Raden_Wijaya.rdf";
    static final String jayanagara = "test/File_RDF/Jayanagara.rdf";
    static final String gayatri = "test/File_RDF/Gayatri.rdf";
    static final String iheroOwl = "test/ihero.owl";
    static final String fkhbOwl = "test/fhkb.owl";
    static final String owlFile = "test/owlDemoSchema.owl";
    static final String simpleFamilyOwl = "test/simpleFamilyRDF.owl";
    static final String cobaHasil = "test/cobahasil.rdf";
    static final String dsFusekiSparql = "http://10.151.34.43:3030/ds/sparql";
    static final String penelitianFusekiSparql = "http://10.151.34.43:3030/penelitian/data"; 
    
    public static void main( String[] argv ) {
        Model mainModel = ModelFactory.createDefaultModel();
        Model dbModel = ModelFactory.createDefaultModel();
        
        mainModel = readDB(mainModel);
//        mainModel.write(System.out, "N-TRIPLES");
        Long jumlahMain = mainModel.size();
        System.out.println("Jumlah mainModel : " + jumlahMain);
        
        dbModel = mainModel;
        Long jumlahDbModel = dbModel.size();
        System.out.println("Jumlah dbModel : " + jumlahDbModel);
        
        mainModel = readRdf(mainModel);
        Long jumlahMain2 = mainModel.size();
        System.out.println("Jumlah mainModel sekarang : " + jumlahMain2);
        
        Model reasoningResult = reasonModel(mainModel);
//        reasoningResult.write(System.out, "N-TRIPLES");
//        try {
//            FileWriter out = new FileWriter( cobaHasil );
//            reasoningResult.write( out, "RDF/XML" );
//            out.close();
//        }
//        catch (IOException closeException) {
//            // ignore
//        }
        Long jumlahReasoningResult = reasoningResult.size();
        System.out.println("Jumlah reasoningResult : " + jumlahReasoningResult);
        
//        System.out.println("hasil reasoning dari (rdf + db) dan owl");
//        printStatement(reasoningResult, null, null, null);
//        printURI(reasoningResult, null, null, null);
//        Model finishedModel = queryModel(reasoningResult); 
//        Model finishedModel = reasoningResult.difference(dbModel);
//        finishedModel.write(System.out, "N-TRIPLES");
        
//        
        
        
//        Long jumlahFinished = finishedModel.size();
//        System.out.println("Jumlah finishedModel : " + jumlahFinished);
//        insertData(finishedModel);
    }
    
    public static Model readDB( Model model ) {
        
        String queryStr = "construct { ?s ?p ?o } where { ?s ?p ?o }";
        Query query = QueryFactory.create(queryStr);
        
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(dsFusekiSparql, query)) {
            // Set remote exec timeout
            ((QueryEngineHTTP) qexec).addParam("timeout", "10000");
            
            // Execute.
            model = qexec.execConstruct();
            return model;
        }
    }
    
    public static Model readRdf( Model model ) {

        InputStream in1 = FileManager.get().open( radenWijaya );
        InputStream in2 = FileManager.get().open( jayanagara );
//        InputStream in3 = FileManager.get().open( amangkurat3 );
        
        if ( in1 == null || in2 == null  ) {
            System.out.println( "File tidak ditemukan" );
        }
        model.read( in1, "" );
        model.read( in2, "" );
//        model.read( in3, "" );
//        model.write(System.out, "N-TRIPLES");
        return model;
    }
    
    public static Model reasonModel( Model model ) {
        Long jumlahModel = model.size();
        System.out.println("Jumlah model : " + jumlahModel);
        OntModel owl = loadOwl(fkhbOwl);
        
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
    
    public static void printURI(Model m, Resource s, Property p, Resource o) {
        
        for (StmtIterator i = m.listStatements(s,p,o); i.hasNext(); ) {
            
            Statement stmt = i.nextStatement();
            Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();
            if( !subject.isURIResource() || !predicate.isURIResource() || !object.isURIResource() ) {
                
            }
            else {
                System.out.println(" - " + PrintUtil.print(stmt));
            }
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
