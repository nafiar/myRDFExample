/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myrdfexample;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import static myrdfexample.RDFTutorial.inputFileName;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;

/**
 *
 * @author nafiar
 */
public class CariPrefix {
    
    public static String inputFileName = "test/File_RDF/Adam_Malik.rdf";
    public static String outputFileName = "test/listPredicate.txt";
    
    public static void main(String[] args){
        propertySemuaRdf(new File("test/File_RDF"));
    }
    
    public static void propertySemuaRdf(File folder){
        File[] listOfFile = folder.listFiles();
        
        for(int i =0; i< listOfFile.length; i++){
            if(listOfFile[i].isFile()){
                propertyDariRdf(listOfFile[i]);
            }
            else{
            }
        }
    }
    
    public static void propertyDariRdf(File file){
        Model model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(file.toString());
        if (in == null) {
            throw new IllegalArgumentException( "File: " + inputFileName + " not found");
        }
        model.read( in, "" );
        StmtIterator iter = model.listStatements(new SimpleSelector(null, null,(RDFNode) null));

        try(FileWriter fw = new FileWriter(outputFileName, true)){
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            
            while(iter.hasNext()) {
                Statement stmt = iter.nextStatement();
                Resource subject = stmt.getSubject();
                Property predicate = stmt.getPredicate();
                //System.out.println(predicate);
                out.println(predicate.toString());
            }
            out.close();
        }
        catch(IOException e){
            
        }
    }
}
