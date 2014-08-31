package edu.uap.tripla;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestTripla {
    static String code1 = "42;" +
            "let" +
            "  foo(b, a, r) {" +
            "    (b + a) + r; " +
            "    let " +
            "       bar(f, o) {" +
            "         f*o*b*a*r" +
            "       }" +
            "    in bar(2, r)" +
            "  }" +
            "in let x(i) { i } in foo(1, 2, 3)";
    
   
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
        /** @fixme ... the following doesn't work. Something's wrong with
         *         retrieving the right frame for accessing the parameter 'a'.
         */
        int result = Tripla.run("let x(a) { let y(b) { let z(c) { if (c == 0) then a else y(c) } in z(b-1) } in y(a) } in x(3)");
        assertEquals(3, result);
        
        result = Tripla.run("let x(a) { let y(b) { a } in y(3*a) } in x(5)");
        assertEquals(5, result);
    }

}
