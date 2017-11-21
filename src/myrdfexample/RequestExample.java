/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myrdfexample;

/**
 *
 * @author nafiar
 */
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import static myrdfexample.onlineRdfReader.dbpediaid;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

public class RequestExample extends HttpServlet{
    
    static final String dbpedia = "http://dbpedia.org/data/";
    static final String dbpediaid = "http://id.dbpedia.org/data/";
    static final List<List<String>> myList = new ArrayList<List<String>>();
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("nameSearch") != null && request.getParameter("nameSearch") != "") {
            String nama = request.getParameter("nameSearch");
            String rdf = dbpediaid + nama + ".rdf";
            Model model = ModelFactory.createDefaultModel();
            model.read(rdf);
            printStatement(model, null, null, null);

        } else {
            
        }

        getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
    }
    
    public static void printStatement(Model m, Resource s, org.apache.jena.rdf.model.Property p, Resource o) {
        
        for (StmtIterator i = m.listStatements(s,p,o); i.hasNext(); ) {
            
            Statement stmt = i.nextStatement();
            List<String> statement = new ArrayList<String>();
            statement.add(stmt.getSubject().toString());
            statement.add(stmt.getPredicate().toString());
            statement.add(stmt.getObject().toString());
            myList.add(statement);
            System.out.println(" - " + PrintUtil.print(stmt));
        }
    }
}
