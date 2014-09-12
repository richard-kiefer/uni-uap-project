package edu.uap.tripla.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java_cup.runtime.Symbol;

/**
 * Demonstrates the usage of the @see TriplaParser and
 * offers simple example programs which exhibit how the
 * parser resolves ambiguous code.
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class ExemplaryParse
{
    
    public static void main(String argv[])
    {
        String code =
                "42;" +
                "let" +
                "  lazy lazyVariable = 23 * 42" +
                "  foo(b, a, r) {" +
                "    (b + a) + r " +
                "  }" +
                "  x(y) {" +
                "    42; " + 
                "    (if 42 == 23" +
                "    then pi = 4; 3" +
                "    else ok = true); false" +
                "  }" +
                "  a(b) {" +
                "    foo = bar; 1" +
                "  }" +
                "in foo(6 * 6; 3, 6, 6)";
        try 
        {
            AbstractSyntaxTree ast = TriplaParser.parse(code);
            ast.print();
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}

