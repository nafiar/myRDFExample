/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myrdfexample;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.query.* ;
import org.apache.jena.query.ARQ;
import org.apache.jena.sparql.*;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.vocabulary.DC;

/**
 *
 * @author nafiar
 */
public class SparqlTutorial {
    
    static public final String dataFileTtl = "test/bloggers.ttl";
    static public final String queryFile = "test/bloggers1.rq";
    static public final String queryFile2 = "test/bloggers2.rq";
    
    public static void main(String[] args) {
//        queryFile();
        
//        dbpediaExample1();
        dbpediaExample2();
//        dbpediaExample3();
        
    }
    
    public static void queryFile(){
        Model model = ModelFactory.createDefaultModel();
        
        InputStream in = FileManager.get().open(dataFileTtl);
        if(in == null) {
            throw new IllegalArgumentException("File tidak ditemukan");
        }
        
        model.read(in, null, "TTL");
        model.write(System.out, "TTL");
        
        Query query = QueryFactory.read(queryFile2);
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();
        
        ResultSetFormatter.out(System.out, results, query);
        
        qe.close();
    }
    
    public static void dbpediaExample1(){
        String queryStr = "select distinct ?Concept where {[] a ?Concept} LIMIT 10";
        Query query = QueryFactory.create(queryStr);

        // Remote execution.
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query)) {
            // Set the DBpedia specific timeout.
            ((QueryEngineHTTP) qexec).addParam("timeout", "10000");

            // Execute.
            ResultSet rs = qexec.execSelect();
            ResultSetFormatter.out(System.out, rs, query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void dbpediaExample2() {
        String queryString
                = "SELECT * WHERE { "
                + "    SERVICE <http://dbpedia-live.openlinksw.com/sparql?timeout=2000> { "
                + "        SELECT DISTINCT ?company where {?company a <http://dbpedia.org/ontology/Company>} LIMIT 20"
                + "    }"
                + "}";
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, ModelFactory.createDefaultModel())) {
            ResultSet rs = qexec.execSelect();
            ResultSetFormatter.out(System.out, rs, query);
        }
    }
    
    public static void dbpediaExample3() {
        String serviceURI = "http://dbpedia-live.openlinksw.com/sparql";
        String queryString
                = "SELECT * WHERE { "
                + "    SERVICE <" + serviceURI + "> { "
                + "        SELECT DISTINCT ?company where {?company a <http://dbpedia.org/ontology/Company>} LIMIT 20"
                + "    }"
                + "}";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, ModelFactory.createDefaultModel())) {
            Map<String, Map<String, List<String>>> serviceParams = new HashMap<>();
            Map<String, List<String>> params = new HashMap<>();
            List<String> values = new ArrayList<>();
            values.add("2000");
            params.put("timeout", values);
            serviceParams.put(serviceURI, params);
            qexec.getContext().set(ARQ.serviceParams, serviceParams);
            ResultSet rs = qexec.execSelect();
            ResultSetFormatter.out(System.out, rs, query);
        }
    }
}
