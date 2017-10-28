/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myrdfexample;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.RDFWriter;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;

/**
 *
 * @author nafiar
 */
public class OntologyTutorial {
    static final String inputFileName = "test/camera.owl";
    static String camNS = "http://www.xfront.com/owl/ontologies/camera/#";
    static String xmlbase = "http://www.xfront.com/owl/ontologies/camera/";
    static String outputFileName = "test/camera2.owl";
        
    public static void main (String args[]) {
//	tutorial01(); 
      tutorial02();
//        tutorial03();
        
    }
    
    public static void tutorial01() {
        // Create an empty in-memory ontology model 
	OntDocumentManager mgr = new OntDocumentManager();
	OntModelSpec s = new OntModelSpec( OntModelSpec.RDFS_MEM );
	s.setDocumentManager( mgr );
	OntModel m = ModelFactory.createOntologyModel( s, null );
		
	// use the FileManager to open the ontology from the filesystem
    	InputStream in = FileManager.get().open(inputFileName);
    	if (in == null) {
            throw new IllegalArgumentException( "File: " + inputFileName + " not found"); 
        }
        
    	// read the ontology file
    	m.read( in, "" );
        
    	// write it to standard out (RDF/XML)
    	m.write(System.out, "N-TRIPLES");
    }
    
    public static void tutorial02() {
        // Create an empty in-memory ontology model 
        OntDocumentManager mgr = new OntDocumentManager();
        OntModelSpec s = new OntModelSpec(OntModelSpec.OWL_MEM);
        s.setDocumentManager(mgr);
        OntModel m = ModelFactory.createOntologyModel(s, null);
        
        InputStream in = FileManager.get().open(inputFileName);
        if(in == null) {
            System.out.println("File : " + inputFileName + " cannot be found");
        }
        //read the ontology file
        m.read(in, "");
        //list the subclass of class camera
        OntClass camera = m.getOntClass(camNS + "Camera");
        for(ExtendedIterator i = camera.listSubClasses(); i.hasNext(); ){
            OntClass c = (OntClass) i.next();
            System.out.println(c.getLocalName() + " subclass of class Camera ");
        }
        
    }
    
    public static void tutorial03() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        Resource NAMESPACE = m.createResource(camNS);
        m.setNsPrefix("camera", camNS);
        
        RDFWriter rdfw = m.getWriter("RDF/XML-ABBERV");
        rdfw.setProperty("xmlbase", xmlbase);
        
        OntClass Camera = m.createClass( camNS + "Camera" );

        // create the throughTheLens window instance
    	OntClass Window = m.createClass( camNS + "Window" );
    	Individual throughTheLens = m.createIndividual( camNS + "ThroughTheLens", Window );

  	// create the viewfinder property
  	ObjectProperty viewfinder = m.createObjectProperty( camNS + "viewfinder"    );

  	// now the anonymous hasValue restriction
  	HasValueRestriction viewThroughLens =
                      m.createHasValueRestriction( null, viewfinder, throughTheLens );

  	// finally create the intersection class to define SLR  
  	IntersectionClass SLR = m.createIntersectionClass( camNS + "SLR",
                      m.createList( new RDFNode[] {viewThroughLens, Camera} ) );
        // now write the model in XML form to a file
        try(FileOutputStream camera_File = new FileOutputStream(outputFileName)){
            //OutputStream out = (OutputStream) camera_File;
            m.write(camera_File, "RDF/XML-ABBREV", xmlbase);
        }
        catch(IOException e){
            
        }
    }
}
    