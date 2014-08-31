package edu.uap.tripla.parser;

/**
 * @see AbstractSyntaxTree
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class FunctionDeclaration extends AbstractSyntaxTree {

    Identifier name;
    Identifier[] parameters;
    AbstractSyntaxTree body;

    public FunctionDeclaration(Identifier i, Identifier[] p,
            AbstractSyntaxTree e) {
        name = i;
        parameters = p;
        body = e;
    }
    
    public String getSignature() {
        return String.format("%s/%d", name.getName(), parameters.length);
    }
    
    public Identifier[] getParameters() {
        return parameters;
    }
    
    public AbstractSyntaxTree getBody() {
        return body;
    }
}
