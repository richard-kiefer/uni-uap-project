package edu.uap.tripla.parser;

/**
 * @see AbstractSyntaxTree
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class Conditional extends AbstractSyntaxTree {

    AbstractSyntaxTree condition;
    AbstractSyntaxTree consequent;
    AbstractSyntaxTree alternative;

    public Conditional(AbstractSyntaxTree condition,
            AbstractSyntaxTree consequent, AbstractSyntaxTree alternative) {
        this.condition = condition;
        this.consequent = consequent;
        this.alternative = alternative;
    }
    
    public AbstractSyntaxTree getCondition() {
        return condition;
    }
    
    public AbstractSyntaxTree getConsequent() {
        return consequent;
    }
    
    public AbstractSyntaxTree getAlternative() {
        return alternative;
    }
}
