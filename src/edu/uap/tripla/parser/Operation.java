package edu.uap.tripla.parser;

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
    
    public AbstractSyntaxTree getOperandLeft() {
        return operand_left;
    }

    public AbstractSyntaxTree getOperandRight() {
        return operand_right;
    }
    
    public String getOperator() {
        return operator;
    }
}
