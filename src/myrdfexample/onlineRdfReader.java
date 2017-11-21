/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myrdfexample;

import java.util.ArrayList;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.PrintUtil;

/**
 *
 * @author nafiar
 */
public class onlineRdfReader {
    
    static final String dbpedia = "http://dbpedia.org/data/";
    static final String dbpediaid = "http://id.dbpedia.org/data/";
    static final List<List<String>> myList = null;
    
    public static void main(String[] argv) {
   
        String nama = "Adam_Malik";
        String rdf = dbpediaid + nama + ".rdf";
        Model model = ModelFactory.createDefaultModel();
        model.read(rdf);
        printStatement(model, null, null, null);
        printArray();
    }
    
    public static void printStatement(Model m, Resource s, Property p, Resource o) {
        
        for (StmtIterator i = m.listStatements(s,p,o); i.hasNext(); ) {
            
            Statement stmt = i.nextStatement();
            List<String> statement = new ArrayList<String>();
            statement.add(stmt.getSubject().toString());
            statement.add(stmt.getPredicate().toString());
            statement.add(stmt.getObject().toString());
            myList.add(statement);
            //System.out.println(" - " + PrintUtil.print(stmt));
        }
    }
    
    public static void printArray(){
        
        for(List<String> statement : myList) {
            String subject = statement.get(0);
            String predicate = statement.get(1);
            String object = statement.get(2);
            System.out.println(subject + " " + predicate + " " + object);
        }
    }
}

