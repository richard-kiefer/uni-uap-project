package edu.uap.tripla.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.uap.tripla.TriplaParser;


/**
 * Some basic tests for @see TriplaParser. Tests only parsability
 * of code-fragments, without checking their abstract syntax trees.
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class TestTriplaParser {

    @Test
    public void testSimpleFunctionDefinition() throws Exception {
        TriplaParser.parse("let x(a) { a } in x(3)");
    }

    @Test
    public void testSimpleAssignment() throws Exception {
        TriplaParser.parse("a = 1");
    }

    @Test(expected=Exception.class)
    public void testSyntaxerrorInSimpleAssignment() throws Exception {
        TriplaParser.parse("a = 3 1");
    }

}
