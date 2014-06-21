package edu.uap.tripla;

/**
 * @see AbstractSyntaxTree
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class Operation extends AbstractSyntaxTree {
    AbstractSyntaxTree operand_left;
    String operator;
    AbstractSyntaxTree operand_right;

    public Operation(AbstractSyntaxTree ol, String op, AbstractSyntaxTree or) {
        operand_left = ol;
        operator = op;
        operand_right = or;
    }
}
