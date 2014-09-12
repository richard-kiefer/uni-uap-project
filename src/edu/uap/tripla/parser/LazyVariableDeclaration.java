package edu.uap.tripla.parser;

/**
 * @see AbstractSyntaxTree
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class LazyVariableDeclaration extends AbstractSyntaxTree {

    Identifier variable;
    AbstractSyntaxTree expression;

    public LazyVariableDeclaration(Identifier i, AbstractSyntaxTree e) {
        variable = i;
        expression = e;
    }
    
    public Identifier getVariable() {
        return variable;
    }
    
    public AbstractSyntaxTree getExpression() {
        return expression;
    }
}
