package edu.uap.tripla.parser;

/**
 * @see AbstractSyntaxTree
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class StatementSequence extends AbstractSyntaxTree {

    AbstractSyntaxTree[] statements;
    
    public StatementSequence(AbstractSyntaxTree s1, AbstractSyntaxTree s2) {
        int n = 0;
        if (s1.getClass() == StatementSequence.class) {
            n += ((StatementSequence)s1).statements.length;
        }
        else {
            n++;
        }
        
        if (s2.getClass() == StatementSequence.class) {
            n += ((StatementSequence)s2).statements.length;
        }
        else {
            n++;
        }
        
        statements = new AbstractSyntaxTree[n];
        int i = 0;
        if (s1.getClass() == StatementSequence.class) {
            for (AbstractSyntaxTree statement: ((StatementSequence)s1).statements) {
                statements[i] = statement;
                i++;
            }
        }
        else {
            statements[i] = s1;
            i++;
        }
        
        if (s2.getClass() == StatementSequence.class) {
            for (AbstractSyntaxTree statement: ((StatementSequence)s2).statements) {
                statements[i] = statement;
                i++;
            }
        }
        else {
            statements[i] = s2;
            i++;
        }
    }
    
    public AbstractSyntaxTree[] getStatements() {
        return statements;
    }
}
