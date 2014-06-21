package edu.uap.tripla;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java_cup.runtime.Symbol;

/**
 * Demonstrates the usage of the @see TriplaParser.
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class ExemplaryParse
{
    public static void main(String argv[])
    {
        try 
        {
            String code = 
"42;" +
"let" +
"  foo(b, a, r) {" +
"    (b + a) + r " +
"  }" +
"  x(y) {" +
"    if 42 == 23" +
"    then pi = 4" +
"    else ok = true" +
"  }" +
"  a(b) {" +
"    1" +
"  }" +
"in foo(6, 6, 6)" + 
"";
            AbstractSyntaxTree ast = TriplaParser.parse(code);
            ast.print();
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}

