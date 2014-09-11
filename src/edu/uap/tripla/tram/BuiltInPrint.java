package edu.uap.tripla.tram;

/**
 * Implements a print-function. Takes one argument
 * and prints it.
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class BuiltInPrint extends BuiltInFunction {

    public BuiltInPrint() {
        super("print", 1);
    }
    
    protected int execute(int[] parameters) {
        System.out.println(parameters[0]);
        return parameters[0];
    }
}
