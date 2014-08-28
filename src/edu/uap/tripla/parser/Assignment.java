package edu.uap.tripla.parser;

/**
 * @see AbstractSyntaxTree
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class Assignment extends AbstractSyntaxTree {
    Identifier variable;
    AbstractSyntaxTree expression;

    public Assignment(Identifier id, AbstractSyntaxTree exp) {
        this.variable = id;
        this.expression = exp;
    }
    
}
