package edu.uap.tripla;


/**
 * @see AbstractSyntaxTree
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class FunctionCall extends AbstractSyntaxTree {

    Identifier name;
    ArgumentSequence argument_list;
    
    public FunctionCall(Identifier name, ArgumentSequence parameters) {
        this.name = name;
        this.argument_list = parameters;
    }
}
