package edu.uap.tripla.parser;


/**
 * @see AbstractSyntaxTree
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class FunctionCall extends AbstractSyntaxTree {

    Identifier name;
    AbstractSyntaxTree[] arguments;
    
    public FunctionCall(Identifier name, AbstractSyntaxTree[] arguments) {
        this.name = name;
        this.arguments = arguments;
    }
    
    public AbstractSyntaxTree[] getArguments() {
        return arguments;
    }
    
    public String getSignature() {
        return String.format("%s/%d", name.getName(), arguments.length);
    }
}
