/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package penelitian;

import java.io.InputStream;
import org.apache.jena.query.*;
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
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.VCARD;

/**
 *
 * @author nafiar
 */
public class MyReasoner {
    static final String inputFile1 = "test/vc-db-1.rdf";
    static final String inputFile2 = "test/owlDemoData.rdf";
    static final String owlFile = "test/owlDemoSchema.owl";
    
    public static void main( String[] argv ) {
        Model mainModel = ModelFactory.createDefaultModel();
        readRdf(mainModel);
        Model reasoningResult = reasonModel(mainModel);
//        reasoningResult.write(System.out, "N-TRIPLES");
        Model queryResult = queryModel(reasoningResult); 
//        queryResult.write(System.out, "");
    }
    
    public static void readRdf( Model model ) {
        InputStream in1 = FileManager.get().open(inputFile1);
        InputStream in2 = FileManager.get().open(inputFile2);
        
        if ( in1 == null || in2 == null ) {
            throw new IllegalArgumentException( "Salah satu file tidak ditemukan" );
        }
        model.read( in1, "" );
        model.read( in2, "" );
//        model.write(System.out, "TTL");
        
    }
    
    public static void readDB( Model model ) {
        
    }
    
    public static Model reasonModel( Model model ) {
        Model owl = FileManager.get().loadModel( owlFile );
        
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema( owl );
        Model reasoningResult = ModelFactory.createInfModel( reasoner, model );
        
        return reasoningResult;
    }
    
    public static Model queryModel( Model model ) {
        String NL = System.getProperty("line.separator"); 
        String queryString = 
                "SELECT ?subject ?property ?object" + NL +
                "WHERE {" + NL +
                "   ?subject ?property ?object ." + NL +
                "}";
        Query query = QueryFactory.create(queryString);
        try( QueryExecution qexec = QueryExecutionFactory.create(query, model) ) {
            Model resultModel = ModelFactory.createDefaultModel();
            
            // Assumption: it's a SELECT query.
            ResultSet rs = qexec.execSelect();

            // The order of results is undefined. 
            while ( rs.hasNext() ) {
                QuerySolution rb = rs.nextSolution();

                // Get title - variable names do not include the '?' (or '$')
                RDFNode x = rb.get("object");
                
//                Literal titleStr = (Literal) x;
                Literal titleStr = rb.getLiteral("object");
                System.out.println("    " + titleStr);
            }
            qexec.close();
            return resultModel;
        }       
    }
}
