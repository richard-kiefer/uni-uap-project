package edu.uap.tripla.compiler;

import java.util.Hashtable;
import java.util.Map;

import edu.uap.tripla.parser.*;
import edu.uap.tripla.tram.BuiltInFunction;

/**
 * Provides the generation of address environments
 * and lookup of addresses within those.
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
class AddressEnvironment {

    /** The current level of nesting. */
    int nestingLevel = 0;
    /** Association between functions or identifiers and their addresses. */
    Map<String, Address> addresses = new Hashtable<String, Address>();
    /** Keeps track of the next free location within the stack frame for parameters. */
    int nextFreeLocation = 0;
    LabelProvider labelProvider;


    /**
     * Creates an empty address environment, already
     * including definition of built-in functions, cf.
     * @see BuiltInFunction.
     * 
     * A LabelProvider must be given, as it is used to
     * request labels for declared functions. The same
     * LabelProvider must later be used for code generation
     * and resolving the labels, cf. @see LabelProvider.
     * 
     * @param lp The LabelProvider.
     */
    AddressEnvironment(LabelProvider lp) {
        labelProvider = lp;
        
        // add definitions of built-in functions
        for (BuiltInFunction f: BuiltInFunction.getFunctionDefinitions()) {
            addresses.put(String.format("%s/%d",
                                        f.getName(),
                                        f.getParameters()
                                       ),
                          new Address(f.getLabel(), 0));
        }
    }

    
    /**
     * Creates a new address environment by copying
     * the given one.
     * 
     * Used for each new scope (i.e. when
     * entering a new let-in node or when entering a
     * function block). Addresses in the stack frame
     * are assigned starting from zero again.
     *  
     * @param ae The address environment to copy from.
     */
    AddressEnvironment(AddressEnvironment ae) {
        // nextFreeLocation gets reset to zero on purpose
        this.labelProvider = ae.labelProvider;
        this.addresses.putAll(ae.addresses);
        this.nestingLevel = ae.nestingLevel; 
    }

    /**
     * Increases the nesting level.
     */
    void increaseNestingLevel() {
        nestingLevel++;
    }
    
    
    /**
     * Returns a function's address from the environment
     * for a given function declaration.
     * 
     * No error handling, this method is usually only called
     * after the elab_def()-method has been called for the
     * given function-declaration and, thus, an address
     * has been added to the environment.
     * 
     * @param id The function declaration.
     * @return The function's address.
     */
    public Address get(FunctionDeclaration fd) {
        return addresses.get(fd.getSignature());
    }
    
    /**
     * Returns a function's address from the environment
     * for a given function call.
     * Throws an error if no address was assigned, i.e. the
     * function has not been declared.
     * 
     * @param id The function call.
     * @return The function's address.
     */
    public Address get(FunctionCall fc) {
        Address a = addresses.get(fc.getSignature());
        if (a != null) {
            return a;
        }
        else {
            throw new Error(String.format("Function %s has not been declared.", fc.getSignature()));
        }
    }
    
    /**
     * Returns a parameter's address from the environment
     * for a given identifier.
     * Throws an error if no address was assigned, i.e. the
     * parameter has not been declared.
     * 
     * @param id The identifier.
     * @return The parameter's address.
     */
    public Address get(Identifier id) {
        Address a = addresses.get(id.getName());
        if (a != null) {
            return a;
        }
        else {
            throw new Error(String.format("Identifier %s has not been declared.", id.getName()));
        }
    }
    
    
    /**
     * Assigns a new label to a function and adds
     * it to the environment.
     * 
     * @param fd The function declaration.
     */
    private void put(FunctionDeclaration fd) {
        int label = labelProvider.getNewLabel();
        Address a = new Address(label, nestingLevel);
        addresses.put(fd.getSignature(), a);
    }
    
    /**
     * Assigns a new address to a parameter and adds
     * it to the environment.
     * 
     * @param id The identifier.
     */
    private void put(Identifier id) {
        Address a = new Address(nextFreeLocation++, nestingLevel);
        addresses.put(id.getName(), a);
    }
    

    /**
     * Generates the labels of functions declared in a let-in node.
     * 
     * @param p The let-in node.
     */
    public void elab_def(Program p) {
        for (AbstractSyntaxTree d : p.getDeclarations()) {
            if (d instanceof FunctionDeclaration) {
                put((FunctionDeclaration)d);
            }
            else if (d instanceof VariableDeclaration) {
                put(((VariableDeclaration)d).getVariable());
            }
            else if (d instanceof LazyVariableDeclaration) {
                put(((LazyVariableDeclaration)d).getVariable());
            }
            else {
                throw new Error("Illegal element encountered in declaration.");
            }
        }
    }

    /**
     * Generates the addresses of function parameters.
     * 
     * @param p The function declaration.
     */
    public void elab_def(FunctionDeclaration fd) {
        for (Identifier parameter: fd.getParameters()) {
            put(parameter);
        }
    }    
}
