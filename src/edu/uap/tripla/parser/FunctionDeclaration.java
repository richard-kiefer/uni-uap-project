package edu.uap.tripla.parser;

/**
 * @see AbstractSyntaxTree
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class FunctionDeclaration extends AbstractSyntaxTree {

    Identifier name;
    Identifier[] parameters;
    AbstractSyntaxTree body;

    public FunctionDeclaration(Identifier i, Identifier[] p,
            AbstractSyntaxTree e) {
        name = i;
        parameters = p;
        body = e;
    }
    
    /**
     * Appends this object to a given array of function-declarations
     * and returns the result as a new array.
     * 
     * @param array The array this object shall be appended to.
     * @return The new array with this object appended to.
     */
    public FunctionDeclaration[] appendTo(FunctionDeclaration[] array) {
        FunctionDeclaration[] new_array = new FunctionDeclaration[array.length + 1];
        
        int n = 0;
        for (FunctionDeclaration i: array) {
            new_array[n++] = i;
        }
        new_array[n] = this;
        
        return new_array;
    }
    
    public String getSignature() {
        return String.format("%s/%d", name.getName(), parameters.length);
    }
    
    public Identifier[] getParameters() {
        return parameters;
    }
    
    public AbstractSyntaxTree getBody() {
        return body;
    }
}
