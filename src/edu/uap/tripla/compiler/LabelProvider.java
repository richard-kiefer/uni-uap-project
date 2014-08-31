package edu.uap.tripla.compiler;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import edu.uap.tripla.tram.Instruction;

/**
 * Provides functionality for labeling machine instructions.
 * 
 * A label can be requested and used as a location in a jump-instructions.
 * During the request or later, the label can be associated with any
 * machine instruction.
 * After finishing the generation of all machine instructions, the labels
 * can be resolved. That is, for each jump-instruction the label is replaced
 * by the final location of the label's associated machine instruction.
 * 
 * Since labels are simple integers, using labels from more than
 * one LabelProvider causes mistakes. Firstly, the same label might be handed
 * out several times. Secondly, when resolving the labels, labels from
 * different LabelProviders cannot be distinguished.
 * Therefore, use one and the same LabelProvider for generating the
 * address environment (i.e. labels for declared functions) and for
 * generating the code (i.e. labels for conditional jumps etc.).
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
class LabelProvider {
    
    /** The next label to be given. */
    int label = -1;
    /** Association between labels and machine instructions. */
    Hashtable<Integer, Instruction> associatedInstructions = new Hashtable<Integer,Instruction>();

    /**
     * Request a new label.
     * 
     * @return The new label. 
     */
    int getNewLabel() {
        // negative labels are easier to distinguish from
        // proper (resolved) locations which are always positive.
        return label--;
    }
    
    /**
     * Associates a label with a machine instruction.
     * 
     * @param label The label.
     * @param instruction The machine instruction.
     */
    void registerInstruction(int label, Instruction instruction) {
        associatedInstructions.put(label, instruction);
    }
    
    /**
     * Requests a new label and associates it with a machine instruction.
     *  
     * @param instruction The machine instruction.
     * @return The new and already associated label.
     */
    int getNewLabel(Instruction instruction) {
        int label = getNewLabel();
        registerInstruction(label, instruction);
        return label;
    }
    
    
    /**
     * Takes a program of machine instructions with labels and
     * resolves those labels to proper locations.
     * 
     * @param program Machine instructions containing labels.
     */
    void resolveLabels(List<Instruction> program) {
        // iterate over the whole program
        for (Instruction i: program) {
            int opcode = i.getOpcode();
            // look for jump-instructions,
            // and replace the label of those with the location of
            // the associated machine instruction.
            if (opcode == Instruction.IFZERO
             || opcode == Instruction.GOTO) {
                int label = i.getArg1();
                Instruction ri = associatedInstructions.get(label);
                int absolutePosition = program.indexOf(ri);
                i.setArg1(absolutePosition);
            }
            else if (opcode == Instruction.INVOKE) {
                int label = i.getArg2();
                Instruction ri = associatedInstructions.get(label);
                int absolutePosition = program.indexOf(ri);
                i.setArg2(absolutePosition);
            }
        }
    }
    
}
