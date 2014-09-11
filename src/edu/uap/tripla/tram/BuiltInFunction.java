package edu.uap.tripla.tram;

/**
 * Provides functionality for functions which are built
 * into the abstract machine and directly available
 * within TRIPLA.
 * 
 * Components like the abstract machine, address environment
 * and label provider can access the whole definition of
 * built-in functions via static methods which are here
 * defined.
 * 
 * Each Subclass extending this class implements one concrete
 * built-in function.
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 *
 */
public abstract class BuiltInFunction {
    /** The built-in function's name. */
    private String name;
    /** The built-in function's number of parameters. */
    private int parameters;
    /** The built-in function's label. Determined later. */
    private int label;
    /** Holds all available built-in functions. */
    // Index within the array corresponds to the label
    // as used by address environment and abstract machine,
    // cf. @getLabel(int) and getIndex(int).
    private static final BuiltInFunction[] functions = new BuiltInFunction[] {
        new BuiltInPrint()
    };
    
    static {
        // compute the functions' labels and assign it to them.
        for (int i = 0; i < functions.length; i++) {
            functions[i].setLabel(getLabel(i));
        }
    }
    
    /**
     * Sets the function's name and the number of parameters. 
     * @param name The name.
     * @param parameters The number of parameters.
     */
    protected BuiltInFunction(String name, int parameters) {
        this.name = name;
        this.parameters = parameters;
    }
    
    
    /**
     * Executes a built-in function identified by its label
     * and returns the result.
     * 
     * @param label The built-in function's label.
     * @param parameters Array of parameters.
     * @return The result.
     */
    public static int execute(int label, int[] parameters) {
        int index = getIndex(label);
        if (index < 0 || index >= functions.length) {
            throw new Error(String.format(
                "Undefined built-in function with label %d.",
                label
            ));                
        }
        BuiltInFunction function = functions[index];
        if (function.parameters != parameters.length) {
            throw new Error(String.format(
                    "Wrong number of parameters supplied to built-in " +
                    "function %s/%d.",
                    function.name,
                    function.parameters
            ));                                
        }
        return function.execute(parameters);
    }


    /**
     * Returns the array of available built-in functions.
     * 
     * @return All available built-in functions.
     */
    public static BuiltInFunction[] getFunctionDefinitions() {
        return functions;
    }
    

    /**
     * Given a function's index in the array -- @see getFunctionDefinitions()
     * -- returns the corresponding label.
     * 
     * @param i The index.
     * @return The corresponding label.
     */
    private static int getLabel(int i) {
        // cf. getIndex(int).
        // Labels are negative so the abstract machine can
        // easily distinguish built-in functions. 
        return -(i + 1);
    }
    /**
     * Given a function's label returns the corresponding
     * index in the array -- @see getFunctionDefinitions().
     * 
     * @param label The label.
     * @return The corresponding index.
     */
    private static int getIndex(int label) {
        // the label represents the location
        // within the array of built-in functions.
        // (just inverted and shifted by one in order to start at zero).
        return -(label + 1);
    }

    /**
     * Considering all labels used by built-in functions, returns
     * the next free label which can be used by a @see LabelProvider.
     * @return
     */
    public static int getNextFreeLabel() {
        return -(functions.length + 1);
    }

    
    
    protected void setLabel(int label) {
        this.label = label;
    }
    /**
     * @return The function's label.
     */
    public int getLabel() {
        return label;
    }
    
    /**
     * @return The function's name.
     */
    public String getName() {
        return name;
    }
    /**
     * @return The function's number of parameters.
     */
    public int getParameters() {
        return parameters;
    }
    
    /**
     * To be implemented by a concrete built-in function.
     * 
     * @param parameters Array of parameters of checked length.
     * @return The function's return value.
     */
    protected abstract int execute(int[] parameters);

}
