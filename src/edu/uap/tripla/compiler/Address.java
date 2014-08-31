package edu.uap.tripla.compiler;

/**
 * The very basic information about a function or
 * identifier in the address environment.
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
class Address {
    /** Either an identifiers location in the stack frame,
     *  or a function's label.
     */
    public int location;
    /** The nesting level within the whole program. */
    public int nestingLevel;
    
    public Address(int location, int nestingLevel) {
        this.location = location;
        this.nestingLevel = nestingLevel;
    }
}
