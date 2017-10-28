/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package describeClass;

import java.io.PrintStream;
import java.util.*;

import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.shared.PrefixMapping;
/**
 *
 * @author nafiar
 */
public class DescribeClass {
    private Map<AnonId,String> m_anonIDs = new HashMap<AnonId,String>();
    private int m_anonCount = 0;


    // Constructors
    //////////////////////////////////

    // External signature methods
    //////////////////////////////////

    /**
     * <p>Describe the given ontology class in texttual form. The description
     * produced has the following form (approximately):
     * <pre>
     * Class foo:Bar
     *    is a sub-class of foo:A, ex:B
     *    is a super-class of ex:C
     * </pre>
     * </p>
     *
     * @param out The print stream to write the description to
     * @param cls The ontology class to describe
     */
    public void describeClass( PrintStream out, OntClass cls ) {
        renderClassDescription( out, cls );

        // sub-classes
        for (Iterator<OntClass> i = cls.listSuperClasses( true ); i.hasNext(); ) {
            out.print( "  is a sub-class of " );
            renderClassDescription( out, i.next() );
            out.println();
        }

        // super-classes
        for (Iterator<OntClass> i = cls.listSubClasses( true ); i.hasNext(); ) {
            out.print( "  is a super-class of " );
            renderClassDescription( out, i.next() );
            out.println();
        }
    }

    /**
     * <p>Render a description of the given class to the given output stream.</p>
     * @param out A print stream to write to
     * @param c The class to render
     */
    public void renderClassDescription( PrintStream out, OntClass c ) {
        if (c.isUnionClass()) {
            renderBooleanClass( out, "union", c.asUnionClass() );
        }
        else if (c.isIntersectionClass()) {
            renderBooleanClass( out, "intersection", c.asIntersectionClass() );
        }
        else if (c.isComplementClass()) {
            renderBooleanClass( out, "complement", c.asComplementClass() );
        }
        else if (c.isRestriction()) {
            renderRestriction( out, c.asRestriction() );
        }
        else {
            if (!c.isAnon()) {
                out.print( "Class " );
                renderURI( out, prefixesFor( c ), c.getURI() );
                out.print( ' ' );
            }
            else {
                renderAnonymous( out, c, "class" );
            }
        }
    }


    // Internal implementation methods
    //////////////////////////////////

    /**
     * <p>Handle the case of rendering a restriction.</p>
     * @param out The print stream to write to
     * @param r The restriction to render
     */
    protected void renderRestriction( PrintStream out, Restriction r ) {
        if (!r.isAnon()) {
            out.print( "Restriction " );
            renderURI( out, prefixesFor( r ), r.getURI() );
        }
        else {
            renderAnonymous( out, r, "restriction" );
        }

        out.println();

        renderRestrictionElem( out, "    on property", r.getOnProperty() );
        out.println();

        if (r.isAllValuesFromRestriction()) {
            renderRestrictionElem( out, "    all values from", r.asAllValuesFromRestriction().getAllValuesFrom() );
        }
        if (r.isSomeValuesFromRestriction()) {
            renderRestrictionElem( out, "    some values from", r.asSomeValuesFromRestriction().getSomeValuesFrom() );
        }
        if (r.isHasValueRestriction()) {
            renderRestrictionElem( out, "    has value", r.asHasValueRestriction().getHasValue() );
        }
    }

    protected void renderRestrictionElem( PrintStream out, String desc, RDFNode value ) {
        out.print( desc );
        out.print( " " );
        renderValue( out, value );
    }

    protected void renderValue( PrintStream out, RDFNode value ) {
        if (value.canAs( OntClass.class )) {
            renderClassDescription( out, value.as( OntClass.class ) );
        }
        else if (value instanceof Resource) {
            Resource r = (Resource) value;
            if (r.isAnon()) {
                renderAnonymous( out, r, "resource" );
            }
            else {
                renderURI( out, r.getModel(), r.getURI() );
            }
        }
        else if (value instanceof Literal) {
            out.print( ((Literal) value).getLexicalForm() );
        }
        else {
            out.print( value );
        }
    }

    protected void renderURI( PrintStream out, PrefixMapping prefixes, String uri ) {
        out.print( prefixes.shortForm( uri ) );
    }

    protected PrefixMapping prefixesFor( Resource n ) {
        return n.getModel().getGraph().getPrefixMapping();
    }

    protected void renderAnonymous( PrintStream out, Resource anon, String name ) {
        String anonID = m_anonIDs.get( anon.getId() );
        if (anonID == null) {
            anonID = "a-" + m_anonCount++;
            m_anonIDs.put( anon.getId(), anonID );
        }

        out.print( "Anonymous ");
        out.print( name );
        out.print( " with ID " );
        out.print( anonID );
    }

    protected void renderBooleanClass( PrintStream out, String op, BooleanClassDescription boolClass ) {
        out.print( op );
        out.println( " of {" );

        for (Iterator<? extends OntClass> i = boolClass.listOperands(); i.hasNext(); ) {
            out.print( "      " );
            renderClassDescription( out, i.next() );
            out.println();
        }
        out.print( "  } " );
    }

}
