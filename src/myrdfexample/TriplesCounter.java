/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myrdfexample;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;

/**
 *
 * @author nafiar
 */
public class TriplesCounter {
    public static void main(String[] args){
        //countAllFiles(new File("test/File_RDF"));
    }
    
    public static void countAllFiles(File folder){
        File[] listOfFile = folder.listFiles();
        
        try{
            PrintWriter writer = new PrintWriter("test/jumlah_triples.csv");
            writer.println("Nama File,Jumlah Triples");
            for(int i =0; i< listOfFile.length; i++){
                if(listOfFile[i].isFile()){
                    int total = countTriples(listOfFile[i]);
                    writer.println(listOfFile[i].getName()+","+total);
                }
                else{
                }
            }
            writer.close();
        }
        catch(IOException e){
        }
    }
    
    public static int countTriples(File myInputFile){
        Model model = ModelFactory.createDefaultModel();
       
        InputStream in = FileManager.get().open(myInputFile.toString());
        if (in == null) {
            throw new IllegalArgumentException( "File: " + myInputFile.getName() + " not found");
        }
        
        model.read( in, "");
        model.write(System.out, "N-TRIPLES");
        int count = 0;
        StmtIterator iter = model.listStatements();
        while(iter.hasNext()){
            iter.nextStatement(); 
            
            count ++;
        }
        return count;
    }
}
