package edu.uap.tripla.parser.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.uap.tripla.parser.*;


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
    
    @Test
    public void testLazyVariables() throws Exception {
        AbstractSyntaxTree ast = TriplaParser.parse("let lazy l = 3 in l");
        if (ast instanceof Program) {
            Program p = (Program)ast;
            AbstractSyntaxTree firstDeclaration = p.getDeclarations()[0];
            if (firstDeclaration instanceof LazyVariableDeclaration) {
                LazyVariableDeclaration lazyVariable =
                        (LazyVariableDeclaration)firstDeclaration; 
                if (lazyVariable.getVariable().getName().equals("l")) {
                    // success!
                    return;
                    // we did not test the expression... :o :)
                }
            }
        }
        fail("Error on parsing lazy variables.");
    }

}
