package edu.uap.tripla.parser;

/**
 * @see AbstractSyntaxTree
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class Program extends AbstractSyntaxTree {

    FunctionDeclaration[] declarations;
    AbstractSyntaxTree body;
    
    public Program(FunctionDeclaration[] f, AbstractSyntaxTree e) {
        declarations = f;
        body = e;
    }
}
