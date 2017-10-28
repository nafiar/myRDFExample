/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package describeClass;

import java.util.Iterator;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

/**
 *
 * @author nafiar
 */
public class Main {
    public static void main( String[] args ) {
        // read the argument file, or the default
        String source = (args.length == 0) ? "http://www.w3.org/2001/sw/WebOnt/guide-src/wine" : args[0];

        OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, null );

        // we have a local copy of the wine ontology
        addFoodWineAltPaths( m.getDocumentManager() );

        // read the source document
        m.read( source );

        DescribeClass dc = new DescribeClass();

        if (args.length >= 2) {
            // we have a named class to describe
            OntClass c = m.getOntClass( args[1] );
            dc.describeClass( System.out, c );
        }
        else {
            for (Iterator<OntClass> i = m.listClasses();  i.hasNext(); ) {
                // now list the classes
                dc.describeClass( System.out, i.next() );
            }
        }
    }
    
    protected static void addFoodWineAltPaths( OntDocumentManager odm ) {
        odm.addAltEntry( "http://www.w3.org/2001/sw/WebOnt/guide-src/wine",
                         "test/wine.owl" );
        odm.addAltEntry( "http://www.w3.org/2001/sw/WebOnt/guide-src/wine.owl",
                         "file:testing/reasoners/bugs/wine.owl" );
        odm.addAltEntry( "http://www.w3.org/2001/sw/WebOnt/guide-src/food",
                         "file:testing/reasoners/bugs/food.owl" );
        odm.addAltEntry( "http://www.w3.org/2001/sw/WebOnt/guide-src/food.owl",
                         "file:testing/reasoners/bugs/food.owl" );
    }
}
