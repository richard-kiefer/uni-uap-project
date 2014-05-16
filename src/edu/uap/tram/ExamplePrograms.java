package edu.uap.tram;

/**
 * Collection of example programs for the TRAM.
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 *
 */
public class ExamplePrograms {

    /** @see Instruction.program1 to 4. Makes those package-friendly
     *  programs publicly available.
     */
    public static Instruction[] program1 = Instruction.program1;
    public static Instruction[] program2 = Instruction.program2;
    public static Instruction[] program3 = Instruction.program3;
    public static Instruction[] program4 = Instruction.program4;

    /**
     * Returns a program which determines the factorial of a given number.
     * The given number gets hard-written into the instructions.
     *
     * Code in TRIPLA: 
     * let factorial(number) {
     *   if (number == 0)
     *      1
     *   else
     *      number * factorial(number - 1) 
     * }
     * in factorial(5)
     * 
     * @param number The number whose factorial is calculated in the program.
     * @return The program.
     *
     */
    public static Instruction[] factorial(int number) {
        return new Instruction[] {
                new Instruction(Instruction.CONST, number),
                new Instruction(Instruction.INVOKE, 1, 3, 0),
                new Instruction(Instruction.HALT),
                
                new Instruction(Instruction.LOAD, 0, 0),
                new Instruction(Instruction.CONST, 0),
                new Instruction(Instruction.EQ),
                new Instruction(Instruction.IFZERO, 9), // if                
                new Instruction(Instruction.CONST, 1),
                new Instruction(Instruction.GOTO, 15),
                                                        // else
                new Instruction(Instruction.LOAD, 0, 0),
                new Instruction(Instruction.CONST, 1),
                new Instruction(Instruction.SUB),
                new Instruction(Instruction.INVOKE, 1, 3, 0),
                new Instruction(Instruction.LOAD, 0, 0),
                new Instruction(Instruction.MUL),
                new Instruction(Instruction.NOP),       // endif
                new Instruction(Instruction.RETURN),    
        };
    }
}
