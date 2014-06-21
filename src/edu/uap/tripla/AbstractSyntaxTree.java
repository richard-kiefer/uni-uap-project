package edu.uap.tripla;

import java.lang.reflect.Field;

/**
 * This class represents a node in the abstract syntax tree as
 * returned by the TRIPLA-parser. Different node types are
 * implemented as subclasses.
 * This class does not dedicate any specific member variables
 * as pointers to child nodes, but each subclass defines such
 * variables on its own. 
 * The resulting tree cannot be traversed by client-code. Instead,
 * only the serializing output method @see print() traverses this
 * data structure.
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class AbstractSyntaxTree {

    /**
     * Traverses the tree in depth-first order and outputs
     * a serialized representation in YAML on System.out.
     * 
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public void print() throws IllegalArgumentException, IllegalAccessException {
        System.out.println("---");
        print(0);
    }
    
    // used for indentation when generating serialized output. 
    private static final String indentation = "  ";
    /**
     * Helper function. Adds the ability to specify the
     * recursion depth for proper indentation in the
     * serialized YAML output.
     * 
     * The tree is traversed by making use of reflection, i.e.
     * each member-variable of class 'AbstractSyntaxTree' is
     * expanded by calling this function recursively.
     * Thereby, we omit the need for an explicit way of
     * listing the child nodes, e.g. by implementing an iterator.
     * 
     * @param depth The recursion depth.
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private void print(int depth) throws IllegalArgumentException, IllegalAccessException {
        String classname = this.getClass().getName();
        // get rid of the package-path
        classname = classname.substring(classname.lastIndexOf(".") + 1, classname.length());
        System.out.println("!" + classname  + " {");
        
        // repeat 'indentation' 'depth'-times;
        // cf. http://stackoverflow.com/a/4903603
        String indent = new String(new char[depth]).replace("\0", indentation);
        String indent2 = indent + indentation; 
        for (Field f : getClass().getDeclaredFields()) {
            String name = f.getName();
            System.out.print(indent2 + name + ": ");
            
            Object value = f.get(this);
            if (value == null) {
                System.out.println();
                continue;
            }
            
            // if member variable is a subclass
            // Cf. http://stackoverflow.com/a/4584552 for subclass-check.
            if (AbstractSyntaxTree.class.isAssignableFrom(value.getClass())) {
                // then further expand.
                ((AbstractSyntaxTree)value).print(depth + 1);
                System.out.println();
            }
            else if (value.getClass().isArray()) {
                System.out.println();
                for (AbstractSyntaxTree field: (AbstractSyntaxTree[])value) {
                    System.out.print(indent2 + "  - ");
                    field.print(depth + 3);
                    System.out.println();
                }
            }
            else {
                System.out.println(value);
            }
        }
        
        System.out.println(indent + "}");
    }
    
}
