package edu.uap.tripla.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import edu.uap.tripla.parser.Tokenizer;
import edu.uap.tripla.parser.parser;

import java_cup.runtime.Symbol;


/**
 * Parser for the Trier Programming Language (TRIPLA).
 * 
 * Delegates its work to the jFlex-Tokenizer 'Tokenizer'
 * and CUP-Parser 'parser'.
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class TriplaParser {

    /**
     * Parses given code and returns the corresponding abstract syntax
     * tree on success. Throws exception if parsing fails.
     * 
     * @param is The code.
     * @return The abstract syntax tree.
     * @throws java.lang.Exception
     */
    public static AbstractSyntaxTree parse(String string) throws java.lang.Exception {
        InputStream input_stream = new ByteArrayInputStream(string.getBytes());
        return parse(input_stream);
    }
    
    /**
     * Parses given code and returns the corresponding abstract syntax
     * tree on success. Throws exception if parsing fails.
     * 
     * @param is The code.
     * @return The abstract syntax tree.
     * @throws java.lang.Exception
     */
    public static AbstractSyntaxTree parse(InputStream is) throws java.lang.Exception {
        Tokenizer Tokenizer = new Tokenizer(is);
        parser par  = new parser(Tokenizer);
        return (AbstractSyntaxTree)par.parse().value;
    }
}
