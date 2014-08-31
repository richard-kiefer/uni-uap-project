package edu.uap.tripla.parser;

/**
 * @see AbstractSyntaxTree
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class Program extends AbstractSyntaxTree {

    AbstractSyntaxTree[] declarations;
    AbstractSyntaxTree body;
    
    public Program(AbstractSyntaxTree[] f, AbstractSyntaxTree e) {
        declarations = f;
        body = e;
    }
    
    public AbstractSyntaxTree[] getDeclarations() {
        return declarations;
    }
    
    public AbstractSyntaxTree getBody() {
        return body;
    }
}
