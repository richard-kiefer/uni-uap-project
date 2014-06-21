package edu.uap.tripla;

/**
 * @see AbstractSyntaxTree
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class ArgumentSequence extends AbstractSyntaxTree {

    AbstractSyntaxTree[] arguments;
    
    public ArgumentSequence(AbstractSyntaxTree p) {
        arguments = new AbstractSyntaxTree[] { p };
    }
    
    public ArgumentSequence(AbstractSyntaxTree p1, AbstractSyntaxTree p2) {
        int n = 0;
        if (p1.getClass() == ArgumentSequence.class) {
            n += ((ArgumentSequence)p1).arguments.length;
        }
        else {
            n++;
        }
        
        if (p2.getClass() == ArgumentSequence.class) {
            n += ((ArgumentSequence)p2).arguments.length;
        }
        else {
            n++;
        }
        
        arguments = new AbstractSyntaxTree[n];
        int i = 0;
        if (p1.getClass() == ArgumentSequence.class) {
            for (AbstractSyntaxTree statement: ((ArgumentSequence)p1).arguments) {
                arguments[i] = statement;
                i++;
            }
        }
        else {
            arguments[i] = p1;
            i++;
        }
        
        if (p2.getClass() == ArgumentSequence.class) {
            for (AbstractSyntaxTree statement: ((ArgumentSequence)p2).arguments) {
                arguments[i] = statement;
                i++;
            }
        }
        else {
            arguments[i] = p2;
            i++;
        }
    }
}
