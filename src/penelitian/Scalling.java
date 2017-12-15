/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package penelitian;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.PrintUtil;

/**
 *
 * @author nafiar
 */
public class Scalling {
    
    static final String hayamWuruk = "test/File_RDF/Hayam_Wuruk.rdf";
    static final String amirHamzah = "test/File_RDF/Amir_Hamzah.rdf";
    public static Model scallingModel;
    private static String fileRdf;
    private static final String dbppropid = "http://id.dbpedia.org/property/";
    private static final String dbpediaowl = "http://dbpedia.org/ontology/";
    private static final String dbpidRdf = "http://id.dbpedia.org/data/";
    private static final String dbpediaRdf = "http://dbpedia.org/data/";
    
    public Scalling(String file){
        fileRdf = file;
        scallingModel = ModelFactory.createDefaultModel();
        scallingModel.read(fileRdf, "");
    }
    
    public static void printStatement(Model m, Resource s, Property p, Resource o) {
        
        for (StmtIterator i = m.listStatements(s,p,o); i.hasNext(); ) {
            
            Statement stmt = i.nextStatement();
            System.out.println(" - " + PrintUtil.print(stmt));
        }
    }
    
    public static Model getFirstGenRelation(Model model) {
        
        Model relatedModel = ModelFactory.createDefaultModel();
        List<Property> properties = new ArrayList<Property>();
        
        properties = getModelProperty(model);
        
        for( Iterator<Property> i = properties.iterator(); i.hasNext(); ) {
            Property property = i.next();
            
            if(model.contains(null, property, (RDFNode) null)) {
                Model tempModel = getModel(model, property);
                relatedModel.add(tempModel);
            }
        }
                
        return relatedModel;
    }
    
    public static Model getSecondGenRelation(Model model) {
        
        Model relatedModel = ModelFactory.createDefaultModel();
        List<Property> properties = new ArrayList<Property>();
        
        properties = getModelProperty(model);
        
        for( Iterator<Property> i = properties.iterator(); i.hasNext(); ) {
            Property property = i.next();
            
            if(model.contains(null, property, (RDFNode) null)) {
                Model tempModel = getModel(model, property);
                Model firstGen = getFirstGenRelation(tempModel);
                relatedModel.add(firstGen);
            }
        }
                
        return relatedModel;
    }
    
    public static Model getModel(Model model, Property property) {
        Model tempModel = ModelFactory.createDefaultModel();
        
        for (StmtIterator i = model.listStatements(null, property, (RDFNode) null); i.hasNext(); ) {
            
            Statement stmt = i.nextStatement();
            if (stmt.getObject().isURIResource()) {
                String nama = validasiNama(getNama(stmt.getObject().toString()));
                
                Model temp = ModelFactory.createDefaultModel();
                temp.read(dbpidRdf(nama));
                Model temp2 = ModelFactory.createDefaultModel();
                temp2.read(dbpediaRdf(nama));
                
                tempModel.add(temp);
                tempModel.add(temp2);
            }
            else{
                String[] literal = stmt.getObject().toString().split("@");
                if (literal[0] != null && literal[1] != null) {
                    String nama = validasiNama(literal[0]);
                    
                    Model temp = ModelFactory.createDefaultModel();
                    temp.read(dbpidRdf(nama));
                    Model temp2 = ModelFactory.createDefaultModel();
                    temp2.read(dbpediaRdf(nama));
                    
                    tempModel.add(temp);
                    tempModel.add(temp2);
                }
            }
        }
        
        return tempModel;
    }
    
    public static List<Property> getModelProperty(Model model) {
        List<Property> properties = new ArrayList<Property>();
        
        Property parent = model.createProperty(dbpediaowl, "parent");
        properties.add(parent);
        Property parents = model.createProperty(dbppropid, "parents");
        properties.add(parents);
        Property father = model.createProperty(dbppropid, "father");
        properties.add(father);
        Property mother = model.createProperty(dbppropid, "mother");
        properties.add(mother);
        Property children = model.createProperty(dbppropid, "children");
        properties.add(children);
        Property child = model.createProperty(dbpediaowl, "child");
        properties.add(child);
        Property spouse = model.createProperty(dbpediaowl, "spouse");
        properties.add(spouse);
        Property spouses = model.createProperty(dbppropid, "spouse");
        properties.add(spouses);
        Property queen = model.createProperty(dbppropid, "queen");
        properties.add(queen);
        
        return properties;
    }
    
    public static String getNama(String string) {
        String[] file = string.split(".");
        if (file.length > 1) {
            string = file[0];
        }
        String[] uri = string.split("/");
        
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
