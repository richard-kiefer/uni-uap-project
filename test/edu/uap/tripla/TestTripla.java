package edu.uap.tripla;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Provides very high-level tests. Given TRIPLA source code
 * and the expected results, all components are run through
 * at once, i.e. parser, compiler, address environment and
 * abstract machine. 
 *  
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */

public class TestTripla {
    // For testing System.out output,
    // thanks to http://stackoverflow.com/a/1119559/1242922
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;;  
    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));        
    }    
    @After
    public void tearDown() {
        System.setOut(originalOut);        
    }
    
    @Test
    public void testParameterAsLocalVariable() {
        int result = Tripla.run("let x(a) { a = 3; a } in x(5)");
        assertEquals(3, result);
    }

    @Test
    public void testFunctionMasking() {
        int result = Tripla.run("let x(a) { 1 } x(b) { 2 } in x(5)");
        assertEquals(2, result);

        result = Tripla.run("let x(a) { 42 } in let x(b) { 23 } in x(5)");
        assertEquals(23, result);

        result = Tripla.run("let x(a,b) { 42 } in let x(c) { 23 } in x(1,2)");
        assertEquals(42, result);

        result = Tripla.run("let x(a) { let y(b) { 13 } in y(a) } in let y(c) { 7 } in x(1)");
        assertEquals(13, result);
    }

    @Test
    public void testNestedFunctions() {
        int result = Tripla.run("let x(a) { let y(b) { let z(c) { if (c == 0) then a else y(c) } in z(b-1) } in y(a) } in x(3)");
        assertEquals(3, result);
        
        result = Tripla.run("let x(a) { let y(b) { a } in y(3*a) } in x(5)");
        assertEquals(5, result);
    }
    
    @Test
    public void testBuiltInFunctions() {
        int result = Tripla.run("let x(a) { print(a) } in x(23)");
        assertEquals(23, result);

        result = Tripla.run("let x(a) { print(a) } in print(13)");
        assertEquals(13, result);

        result = Tripla.run("let print(a) { 42 } in print(23)");
        assertEquals(42, result);
    }
    
    @Test
    public void testVariables() {
        int result = Tripla.run("let var pi = 3 in pi");
        assertEquals(3, result);
        
        result = Tripla.run("let var pi = 3 x(a) { a*pi } in x(2)");
        assertEquals(6, result);
        
        result = Tripla.run("let var pi = 3 x(pi) { pi } in x(4)");
        assertEquals(4, result);
        
        result = Tripla.run("let var pi = 42 x(pi) { pi = 5 } y(b) { pi } in x(1); y(23)");
        assertEquals(42, result);
        
        result = Tripla.run("let var pi = 3 x(a) { pi = a } y(b) { pi } in x(23); y(42)");
        assertEquals(23, result);
    }

    
    @Test
    public void testLazyVariables() {
        String code;
        int result;
        
        code = "let " +
                "  lazy x = y(0)" +
                "  var x = 3" +
                "  y(a) {" +
                "    print(23)" +
                "  }" +
                "in" +
                "  x";
        result = Tripla.run(code);
        assertEquals("", outContent.toString());
        outContent.reset();
        
        code = "let " +
                "  var x = 3" +
                "  lazy x = y(0)" +
                "  y(a) {" +
                "    print(23)" +
                "  }" +
                "in" +
                "  x";
        result = Tripla.run(code);
        assertEquals("23\n", outContent.toString());
        outContent.reset();
        
        code = "let " +
                "  lazy x = z(0)" +
                "  z(a) {" +
                "    print(23)" +
                "  }" +
                "in" +
                "  x = 42; x";
        result = Tripla.run(code);
        assertEquals(42, result);
        assertEquals("", outContent.toString());
        outContent.reset();
    }
    
    @Test
    public void testLazyVariablesMoreComplex() {
        String code;
        int result;
        
        code =
                "let " + 
                "  var result = 0 " + 
                "  lazy end = mult(2, 5) " + 
                "  mult(x, y) {" +
                "    x * y" +
                "  } " + 
                "  count(start) {" +
                "    let" +
                "      lazy final = mult(2, end) " + 
                "    in" +
                "      if (start == final) then" +
                "        result = start " + 
                "      else" +
                "        count(start + 1)" +
                "  } " + 
                "in" +
                "  count(0) " +
                ""; 
        result = Tripla.run(code);
        assertEquals(20, result);
            
        code =
            "let " +
            "  lazy one = one(0)" +
            "  var two = two(0)" +
            "  lazy three = three(0)" +
            "  one(a) {" +
            "    print(1)" +
            "  }" +
            "  two(a) {" +
            "    print(2)" +
            "  }" +
            "  three(a) {" +
            "    print(3);" +
            "    3 * one" +
            "  }" +
            "in" +
            "  one; two; three"; 
        result = Tripla.run(code);
        assertEquals("2\n1\n3\n", outContent.toString());
        assertEquals(3, result);
    }
}
