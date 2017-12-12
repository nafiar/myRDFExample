/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package penelitian;

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
    private static String fileRdf;
    private static final String dbppropid = "http://id.dbpedia.org/property/";
    private static final String dbpediaowl = "http://dbpedia.org/ontology/";
    private static final String dbpidRdf = "http://id.dbpedia.org/data/";
    private static final String dbpediaRdf = "http://dbpedia.org/data/";
    private static Model modelAkhir;
    
    public Scalling(String file){
        fileRdf = file;
    }
    
    public static void readGen() {
        Model model = ModelFactory.createDefaultModel();
        model.read(fileRdf, "");
        Model genRelation = getFirstGenRelation(model);
        modelAkhir = genRelation;
    }
    
    public static Model getModelRadius() {
        return modelAkhir;
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
//        printJumlah(relatedModel, "relatedModel setelah father ");
        if (model.contains(null, mother, (RDFNode) null)) {
            Model motherModel = getModel(model, mother);
            relatedModel.add(motherModel);
        }
//        printJumlah(relatedModel, "relatedModel setelah mother");
        if (model.contains(null, children, (RDFNode) null)) {
            Model childModel = getModel(model, children);
            relatedModel.add(childModel);
        }
//        printJumlah(relatedModel, "model setelah children");
        if (model.contains(null, child, (RDFNode) null)) {
            Model childModel = getModel(model, child);
            relatedModel.add(childModel);
        }
//        printJumlah(relatedModel, "model setelah child");
        
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
                    temp2.read(dbpidRdf(nama));
                    
                    tempModel.add(temp);
                    tempModel.add(temp2);
                }
            }
        }
        
        return tempModel;
    }
    
    public static String getNama(String string) {
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
