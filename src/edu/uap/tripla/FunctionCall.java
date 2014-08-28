package edu.uap.tripla;


/**
 * @see AbstractSyntaxTree
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class FunctionCall extends AbstractSyntaxTree {

    Identifier name;
    AbstractSyntaxTree[] arguments;
    
    public FunctionCall(Identifier name, AbstractSyntaxTree[] parameters) {
        this.name = name;
        this.arguments = parameters;
    }
}
