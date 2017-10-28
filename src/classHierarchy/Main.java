/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classHierarchy;

import java.io.InputStream;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

/**
 *
 * @author nafiar
 */
public class Main {
   
    static String wineFile = "test/wine.owl";
    
    public static void main( String[] args ) {
        // Create an empty in-memory ontology model 
	OntDocumentManager mgr = new OntDocumentManager();
	OntModelSpec s = new OntModelSpec( OntModelSpec.OWL_MEM );
	s.setDocumentManager( mgr );
        OntModel m = ModelFactory.createOntologyModel( s, null );
        
        InputStream in = FileManager.get().open(wineFile);
        if(in == null) {
            System.out.println("File not found");
        }
        
        
        m.read(in, "");

        new ClassHierarchy().showHierarchy( System.out, m );
    }
    
}
