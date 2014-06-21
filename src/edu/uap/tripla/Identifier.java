package edu.uap.tripla;

/**
 * @see AbstractSyntaxTree
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class Identifier extends AbstractSyntaxTree {
    String name;
    
    public Identifier(String id) {
        this.name = id;
    }
    
    /**
     * Appends this object to a given array of identifiers
     * and returns the result as a new array.
     * 
     * @param array The array this object shall be appended to.
     * @return The new array with this object appended to.
     */
    public Identifier[] appendTo(Identifier[] array) {
        Identifier[] new_array = new Identifier[array.length + 1];
        
        int n = 0;
        for (Identifier i: array) {
            new_array[n++] = i;
        }
        new_array[n] = this;
        
        return new_array;
    }
    
}
