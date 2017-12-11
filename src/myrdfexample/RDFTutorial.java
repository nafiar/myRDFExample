package myrdfexample;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.PrintUtil;
import org.apache.jena.vocabulary.VCARD;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nafiar
 */
public class RDFTutorial extends Object {
    
    static final String inputFileName = "test/File_RDF/Hamengkubuwono_IV.rdf";
    static final String inputFile2 = "test/vc-db-1.rdf";
    static final String inputFile3 = "test/owlDemoData.rdf";
    static final String fragment = "test/fragment.rdf";
    static final String amangkurat1 = "test/File_RDF/Amangkurat_I.rdf";
    static final String amangkurat2 = "test/File_RDF/Amangkurat_II.rdf";
    static final String hayamWuruk = "test/File_RDF/Hayam_Wuruk.rdf";
    static final String amirHamzah = "test/File_RDF/Amir_Hamzah.rdf";
    static final String dbppropid = "http://id.dbpedia.org/property/";
    static final String dbpediaowl = "http://dbpedia.org/ontology/";
    static final String dbpidRdf = "http://id.dbpedia.org/data/";
    static final String dbpediaRdf = "http://dbpedia.org/data/";
    
    public static void main(String[] args){
        //tutorial01();
        //tutorial02();
//        tutorial03();
//        tutorial04();
//        tutorial05();
        //specialTutorial();
//        tutorial06();
//        tutorial07();
//        tutorial08();
//        countTriples();
//        substractTest();
        readGen();
    }
    
    public static void tutorial01(){
        String personURI = "http://somewhere/JohnSmith";
        String fullName     = "John Smith";
        
        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        // create the resource
        Resource johnSmith = model.createResource(personURI);

        // add the property
        johnSmith.addProperty(VCARD.FN, fullName);
    }
    
    public static void tutorial02(){
        // some definitions
        String personURI    = "http://somewhere/JohnSmith";
        String givenName    = "John";
        String familyName   = "Smith";
        String fullName     = givenName + " " + familyName;

        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        // create the resource
        //   and add the properties cascading style
        Resource johnSmith  = model.createResource(personURI)
             .addProperty(VCARD.FN, fullName)
             .addProperty(VCARD.N, 
                      model.createResource()
                           .addProperty(VCARD.Given, givenName)
                           .addProperty(VCARD.Family, familyName));
    }
    
    public static void tutorial03(){
        // some definitions
        String personURI    = "http://somewhere/JohnSmith";
        String givenName    = "John";
        String familyName   = "Smith";
        String fullName     = givenName + " " + familyName;
        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        // create the resource
        //   and add the properties cascading style
        Resource johnSmith 
          = model.createResource(personURI)
                 .addProperty(VCARD.FN, fullName)
                 .addProperty(VCARD.N, 
                              model.createResource()
                                   .addProperty(VCARD.Given, givenName)
                                   .addProperty(VCARD.Family, familyName));
        
        // list the statements in the graph
        StmtIterator iter = model.listStatements();
        
        // print out the predicate, subject and object of each statement
        while (iter.hasNext()) {
            Statement stmt      = iter.nextStatement();         // get next statement
            Resource  subject   = stmt.getSubject();   // get the subject
            Property  predicate = stmt.getPredicate(); // get the predicate
            RDFNode   object    = stmt.getObject();    // get the object
            
            System.out.print(subject.toString());
            System.out.print(" " + predicate.toString() + " ");
            if (object instanceof Resource) {
                System.out.print(object.toString());
            } else {
                // object is a literal
                System.out.print(" \"" + object.toString() + "\"");
            }
            System.out.println(" .");
        }
        
        model.write(System.out);
    }
    
    public static void tutorial04(){
        String tutorialURI  = "http://hostname/rdf/tutorial/";
        String briansName   = "Brian McBride";
        String briansEmail1 = "brian_mcbride@hp.com";
        String briansEmail2 = "brian_mcbride@hpl.hp.com";
        String title        = "An Introduction to RDF and the Jena API";
        String date         = "23/01/2001";
        
        // some definitions
        String personURI    = "http://somewhere/JohnSmith";
        String givenName    = "John";
        String familyName   = "Smith";
        String fullName     = givenName + " " + familyName;
        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        // create the resource
        //   and add the properties cascading style
        Resource johnSmith 
          = model.createResource(personURI)
                 .addProperty(VCARD.FN, fullName)
                 .addProperty(VCARD.N, 
                              model.createResource()
                                   .addProperty(VCARD.Given, givenName)
                                   .addProperty(VCARD.Family, familyName));
        
        // now write the model in XML form to a file
        model.write(System.out,"N-TRIPLES");
    }
    
    public static void tutorial05(){

        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        InputStream in = FileManager.get().open( inputFileName );
        if (in == null) {
            throw new IllegalArgumentException( "File: " + inputFileName + " not found");
        }
        
        // read the RDF/XML file
        model.read(in, "");
                    
        // write it to standard out
        model.write(System.out,"N-TRIPLES");
    }
    
    public static void specialTutorial(){
        Model m = ModelFactory.createDefaultModel();
        String nsA = "http://somewhere/else#";
        String nsB = "http://nowhere/else#";
        
        Resource root = m.createResource( nsA + "root" );
        Property P = m.createProperty( nsA + "P" );
        Property Q = m.createProperty( nsB + "Q" );
        Resource x = m.createResource( nsA + "x" );
        Resource y = m.createResource( nsA + "y" );
        Resource z = m.createResource( nsA + "z" );
        
        m.add( root, P, x ).add( root, P, y ).add( y, Q, z );
        System.out.println( "# -- no special prefixes defined" );
        m.write( System.out );
        
        System.out.println( "# -- nsA defined" );
        m.setNsPrefix( "nsA", nsA );
        m.write( System.out );
        
        System.out.println( "# -- nsA and cat defined" );
        m.setNsPrefix( "cat", nsB );
        m.write( System.out );
        
        Model m2 = ModelFactory.createDefaultModel();
        m2.read( "test/fragment.rdf" );
        m2.write( System.out );
    }
    
    public static void tutorial06(){
        String johnSmithURI = "http://somewhere/JohnSmith/";
        
        // create an empty model
        Model model = ModelFactory.createDefaultModel();
       
        // use the FileManager to find the input file
        InputStream in = FileManager.get().open(inputFile2);
        if (in == null) {
            throw new IllegalArgumentException( "File: " + inputFile2 + " not found");
        }
        
        // read the RDF/XML file
        model.read(in, "RDF/XML");
        
        // retrieve the Adam Smith vcard resource from the model
        Resource vcard = model.getResource(johnSmithURI);

        // retrieve the value of the N property
        Resource name = vcard.getRequiredProperty(VCARD.N)
                                        .getResource();
        // retrieve the given name property
        String fullName = vcard.getRequiredProperty(VCARD.FN)
                               .getString();
        // add two nick name properties to vcard
        vcard.addProperty(VCARD.NICKNAME, "Smithy")
             .addProperty(VCARD.NICKNAME, "Adman");
        
        // set up the output
        System.out.println("The nicknames of \"" + fullName + "\" are:");
        // list the nicknames
        StmtIterator iter = vcard.listProperties(VCARD.NICKNAME);
        while (iter.hasNext()) {
            System.out.println("    " + iter.nextStatement().getObject()
                                            .toString());
        }
    }
    
    public static void tutorial07(){
        // create an empty model
        Model model = ModelFactory.createDefaultModel();
       
        // use the FileManager to find the input file
        InputStream in = FileManager.get().open(inputFile2);
        if (in == null) {
            throw new IllegalArgumentException( "File: " + inputFile2 + " not found");
        }
        
        // read the RDF/XML file
        model.read( in, "");
        
        // select all the resources with a VCARD.FN property
        ResIterator iter = model.listResourcesWithProperty(VCARD.FN);
        if (iter.hasNext()) {
            System.out.println("The database contains vcards for:");
            while (iter.hasNext()) {
                System.out.println("  " + iter.nextResource()
                                              .getRequiredProperty(VCARD.FN)
                                              .getString() );
            }
        } else {
            System.out.println("No vcards were found in the database");
        }
    }
    
    public static void tutorial08(){
        // create an empty model
        Model model = ModelFactory.createDefaultModel();
       
        // use the FileManager to find the input file
        InputStream in = FileManager.get().open(hayamWuruk);
        if (in == null) {
            throw new IllegalArgumentException( "File: " + hayamWuruk + " not found");
        }
        
        // read the RDF/XML file
        model.read( in, "" );
        
        // select all the resources with a VCARD.FN property
        // whose value ends with "Smith"
        StmtIterator iter = model.listStatements(
            new 
                SimpleSelector(null, VCARD.FN, (RDFNode) null) {
                    @Override
                    public boolean selects(Statement s) {
                            return s.getString().endsWith("Smith");
                    }
                });
        if (iter.hasNext()) {
            System.out.println("The database contains vcards for:");
            while (iter.hasNext()) {
                Statement smt = iter.nextStatement();
                System.out.println("  " + PrintUtil.print(smt) );
            }
        } else {
            System.out.println("No Smith's were found in the database");
        }
    }
    
    public static void countTriples(){
        
        
       Model model = ModelFactory.createDefaultModel();
       
        // use the FileManager to find the input file
//        InputStream in = FileManager.get().open(myInputFile);
//        if (in == null) {
//            throw new IllegalArgumentException( "File: " + myInputFile + " not found");
//        }
        
        InputStream in2 = FileManager.get().open(inputFile2);
        InputStream in3 = FileManager.get().open(inputFile3);
        if (in2 == null || in3 == null) {
            throw new IllegalArgumentException( "File: " + inputFile2 + " not found");
        }
        
        // read the RDF/XML file
        //model.read( in, "");
        model.read( in2, "");
        model.read( in3, "");
        model.write(System.out, "N-TRIPLES");
        
//        int count = 0;
//        StmtIterator iter = model.listStatements();
//        while(iter.hasNext()){
//            Statement stmt      = iter.nextStatement();  
//            count ++;
//        }
//        
//        System.out.println("Total triples dari file "+ f.getName()+" = "+count);
    }
    
    public static void readDB() {
        Model dbModel = ModelFactory.createDefaultModel();
        
    }
    
    public static void substractTest() {
        Model model = ModelFactory.createDefaultModel();
        Model model2 = ModelFactory.createDefaultModel();
        
        model.read(inputFile2, "");
        Long panjangModel1 = model.size();
        System.out.println("Isi model : " + panjangModel1);
        
        model.read(fragment, "");
        panjangModel1 = model.size();
        System.out.println("Isi model : " + panjangModel1);
        
//        System.out.println("Isi model : ");
//        model.write(System.out, "N-TRIPLES");
        
        model2.read(inputFile2, "");
        Long panjangModel2 = model2.size();
        System.out.println("Isi model2 : " + panjangModel2);
        
        model2.read(fragment, "");
        panjangModel2 = model2.size();
        System.out.println("Isi model2 : " + panjangModel2);
//        printStatement(model2, null, null, null);
//        System.out.println("Panjang model2 : " + panjangModel2);
        
//        System.out.println("Hasil differece :");
        Model difference = model.difference(model2);
//        difference.write(System.out, "N-TRIPLES");
        Long panjangDifference = difference.size();
        System.out.println("Panjang difference : " + panjangDifference);
    }
    
    public static void readGen() {
        Model model = ModelFactory.createDefaultModel();
        model.read(amirHamzah, "");
        Model genRelation = getFirstGenRelation(model);
        printJumlah(genRelation, "genRelation akhir ");
    }
    
    public static void printStatement(Model m, Resource s, Property p, Resource o) {
        
        for (StmtIterator i = m.listStatements(s,p,o); i.hasNext(); ) {
            
            Statement stmt = i.nextStatement();
            System.out.println(" - " + PrintUtil.print(stmt));
        }
    }
    
    public static Model getFirstGenRelation(Model model) {
        Model relatedModel = ModelFactory.createDefaultModel();
        Property father = model.createProperty(dbppropid, "father");
        Property mother = model.createProperty(dbppropid, "mother");
        Property children = model.createProperty(dbppropid, "children");
        Property child = model.createProperty(dbpediaowl, "child");
        
        if (model.contains(null, father, (RDFNode) null)) {
            Model fatherModel = getModel(model, father);
            relatedModel.add(fatherModel);
        }
        printJumlah(relatedModel, "relatedModel setelah father ");
        if (model.contains(null, mother, (RDFNode) null)) {
            Model motherModel = getModel(model, mother);
            relatedModel.add(motherModel);
        }
        printJumlah(relatedModel, "relatedModel setelah mother");
        if (model.contains(null, children, (RDFNode) null)) {
            Model childModel = getModel(model, children);
            relatedModel.add(childModel);
        }
        printJumlah(relatedModel, "model setelah children");
        if (model.contains(null, child, (RDFNode) null)) {
            Model childModel = getModel(model, child);
            relatedModel.add(childModel);
        }
        printJumlah(relatedModel, "model setelah child");
        
        return relatedModel;
    }
    
    public static Model getModel(Model model, Property property) {
        Model tempModel = ModelFactory.createDefaultModel();
        
        for (StmtIterator i = model.listStatements(null, property, (RDFNode) null); i.hasNext(); ) {
            
            Statement stmt = i.nextStatement();
            if (stmt.getObject().isURIResource()) {
                String nama = validasiNama(getNama(stmt.getObject().toString()));
                Model temp = ModelFactory.createDefaultModel();
                temp.read(dbpediaRdf(nama));
                tempModel.add(temp);
                System.out.println(" resource : " + dbpediaRdf(nama));
            }
            else{
                String[] literal = stmt.getObject().toString().split("@");
                if (literal[0] != null && literal[1] != null) {
                    String nama = validasiNama(literal[0]);
                    Model temp = ModelFactory.createDefaultModel();
                    temp.read(dbpediaRdf(nama));
                    tempModel.add(temp);
                    System.out.println(" resource : " + dbpediaRdf(nama));
                }
            }
        }
        
        return tempModel;
    }
    
    public static String getNama(String string) {
        String[] uri = string.split("/");
//        System.out.println("nama : " + uri[uri.length-1]);
        
        return uri[uri.length-1];
    }
    
    public static String validasiNama(String string) {
        String[] nama = string.split(" ");
        if (nama.length > 0) {
            String namaBaru = ""; 
            for(int i = 0; i < nama.length ; i++ ) {
               if ( i > 0 ) {
                   namaBaru = namaBaru + "_" + nama[i];
               }
               else {
                   namaBaru = namaBaru + nama[i];
               }
            }
            return namaBaru;
        }
        else {
            return string;
        }
    }
    
    public static String dbpidRdf(String string) {
        String rdf = dbpidRdf + string + ".rdf";
        
        return rdf;
    }
    
    public static String dbpediaRdf(String string) {
        String rdf = dbpediaRdf + string + ".rdf";
        
        return rdf;
    }
    
    public static void printJumlah(Model model, String string) {
        Long jumlahModel = model.size();
        System.out.println("Jumlah " + string + " : " + jumlahModel);
    }
}
