package edu.uap.tripla;

import edu.uap.tripla.compiler.TriplaCompiler;
import edu.uap.tripla.parser.AbstractSyntaxTree;
import edu.uap.tripla.parser.TriplaParser;
import edu.uap.tripla.tram.AbstractMachine;
import edu.uap.tripla.tram.Instruction;


/**
 * Provides an easy interface for parsing, compiling and
 * running TRIPLA-code.
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class Tripla {

    /**
     * Runs a piece of TRIPLA-code. Debug mode turned off.
     * 
     * @param sourcecode TRIPLA-code.
     * @return The result of the run code.
     */
    static int run(String sourcecode) {
        return run(sourcecode, false);
    }
    /**
     * Runs a piece of TRIPLA-code.
     * 
     * @param sourcecode TRIPLA-code.
     * @param debug Whether or not to output debug information.
     * @return The result of the run code.
     */
    static int run(String sourcecode, boolean debug) {
        if (debug) {
            System.out.println(sourcecode);
        }
        AbstractSyntaxTree ast = parse(sourcecode, debug);
        Instruction[] program = compile(ast, debug);
        return run(program, debug);
    }

    /**
     * Returns the abstract syntax tree for a given piece of TRIPLA-code.
     * 
     * @param sourcecode TRIPLA-code.
     * @param debug If true, the abstract syntax tree is printed.
     * @return The abstract syntax tree.
     */
    static AbstractSyntaxTree parse(String sourcecode, boolean debug) {
        try {
            AbstractSyntaxTree ast = TriplaParser.parse(sourcecode);
            if (debug) {
                ast.print();
            }
            return ast;
        } catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Returns the machine instructions for a given abstract syntax tree.
     * 
     * @param ast The abstract syntax tree.
     * @param debug If true, the instructions are printed.
     * @return The machine instructions.
     */
    static Instruction[] compile(AbstractSyntaxTree ast, boolean debug) {
        Instruction[] program = TriplaCompiler.compile(ast);
        if (debug) {
            print(program);
        }
        return program;
    }
    
    /**
     * Runs the given machine instructions on the abstract machine TRAM.
     * 
     * @param program The machine instructions.
     * @param debug If true, the abstract machine is run in debug mode.
     * @return The result.
     */
    static int run(Instruction[] program, boolean debug) {
        AbstractMachine am = new AbstractMachine(program);
        am.debug = debug;
        return am.run();
    }
    
    
    /**
     * Prints given machine instructions to System.out.
     * 
     * @param program The machine instructions.
     */
    static void print(Instruction[] program) {
        int ln = 0;
        for (Instruction i: program) {                
            System.out.println(String.format("%4d: %s", ln++, i.toString()));
        }
    }
}
