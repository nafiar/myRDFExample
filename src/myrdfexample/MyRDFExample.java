/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myrdfexample;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileManager;

/**
 *
 * @author nafiar
 */
public class MyRDFExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //sparqlTest();
        helloWorld();
    }
    
    public static void sparqlTest(){
        FileManager.get().addLocatorClassLoader(MyRDFExample.class.getClassLoader());
        Model model = FileManager.get().loadModel("/home/nafiar/NetBeansProjects/myRDFExample/src/myrdfexample/Adam_Malik.rdf", null, "TRIPLES");
        String queryString = 
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
                "SELECT * WHERE { " +
                " ?subject foaf:name ?object ." +
                "}";
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            while( results.hasNext() ){
                QuerySolution soln = results.nextSolution();
                Literal name = soln.getLiteral("object");
                System.out.println(name);
            }
        }
    }
    
    public static void helloWorld(){
        Model m = ModelFactory.createDefaultModel();
        String NS = "http://example.com/test/";
        
        Resource r = m.createResource(NS + "r");
        Property p = m.createProperty(NS + "p");
        
        r.addProperty( p, "hello world", XSDDatatype.XSDstring);
        
        m.write( System.out, "turtle");
    }
}
