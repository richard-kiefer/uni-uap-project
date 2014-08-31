package edu.uap.tripla.parser;

/**
 * @see AbstractSyntaxTree
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class Constant extends AbstractSyntaxTree {
    Integer value;
    
    public Constant(Integer value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }


}
