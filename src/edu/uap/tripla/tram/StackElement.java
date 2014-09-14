package edu.uap.tripla.tram;

/**
 * The building block of the abstract machine's stack.
 * Each stack element is a value-tag pair. Within the
 * abstract machine, the kind of tag defines how to interpret
 * the value.
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
class StackElement {
    
    /** The kind of tags.
     *  Each tag kind defines a different meaning for the value. */
    enum Tag {
        /** The value is an integer. */
        I,
        /** The value is the address of a program instruction. */
        P,
        /** The value is an unevaluated constant,
         *  i.e. an unevaluated lazy variable.
         */
        C
    }
    
    /** The element's value. */
    int value = 0;
    /** The element's tag, @see StackElement.Tag . */
    Tag tag = Tag.I;
    
    /** Assigns value and tag to this stack element. */
    void assign(int i, Tag t) {
        value = i;
        tag = t;
    }
    
    /** Copies value and pair from another stack element. */
    void assign(StackElement other) {
        this.value = other.value;
        this.tag = other.tag;
    }
    
    public String toString() {
        String t = "";
        switch (tag) {
        case I:
            t = "I";
            break;
        case P:
            t = "P";
            break;
        case C:
            t = "C";
            break;
        }
        return t + this.value;
    }
}
