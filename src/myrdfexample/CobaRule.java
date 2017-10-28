/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myrdfexample;

import java.util.Iterator;
import org.apache.jena.util.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.*;
import org.apache.jena.reasoner.rulesys.*;

/**
 *
 * @author nafiar
 */
public class CobaRule {
    private final static String fName = "test/cobaData.ttl";
    private final static String NS = "urn:x-hp:eg/";
  
    public static void main(String args[]) {
    Model rawData = FileManager.get().loadModel(fName);
    String rules = 
        "[r1: (?c eg:concatFirst ?p), (?c eg:concatSecond ?q) -> " +
        "[r1b: (?x ?c ?y) <- (?x ?p ?z) (?z ?q ?y)] ]";
    Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
        //reasoner.setParameter(ReasonerVocabulary.PROPtraceOn,Boolean.TRUE);
    InfModel inf = ModelFactory.createInfModel(reasoner, rawData);
    Resource A = inf.getResource(NS + "A");
    System.out.println("A * * =>");
    Iterator list = inf.listStatements(A, null, (RDFNode)null);
        while (list.hasNext()) {
        System.out.println(" - " + list.next());}
    }
}
