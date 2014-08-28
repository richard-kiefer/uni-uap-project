package edu.uap.tripla.tram;

/**
 * Trier Abstract Machine (TRAM) is a virtual machine for the functional
 * Trier Programming Language (TRIPLA).
 * 
 * TRAM and TRIPLA are concepts used at University Trier for educational
 * purposes in the course "compilation and analysis of programs",
 * summer term 2014.
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 *
 */
public class AbstractMachine {
    public boolean debug = true;
    
    private static final int stackSize = 256;
    
    /** The set of instructions to be executed. */
    private Instruction[] text;
    private int[] stack = new int[stackSize];
    /** Program Counter. Points to the current instruction in 'text'. */
    private int PC;
    /** Parameter Pointer. Points to the first parameter. */
    private int PP;
    /** Frame Pointer. Points to the stack frame's begin (after the parameters). */ 
    private int FP;
    /** Points to the upmost used stack cell. */ 
    private int TOP;
    
    public AbstractMachine() {
        reset();
    }
    
    public AbstractMachine(Instruction[] text) {
        load(text);
    }
    
    public void load(Instruction[] text) {
       reset();
       this.text = text;
    }
    
    public int run() {
        while (PC >= 0) {
            debug();
            execute(text[PC]);
        }
        debug();

        return stack[TOP]; // may throw an array-out-of-bounce error
    }
    
    private void reset() {
        PC = 0;
        PP = 0;
        FP = 0;
        TOP = -1;
    }
    
    private void execute(Instruction instruction) {
       
        switch (instruction.getOpcode())
        {
            case Instruction.CONST:
                _const(instruction.getArg1());
                break;
            case Instruction.LOAD:
                _load(instruction.getArg1(), instruction.getArg2());
                break;
            case Instruction.STORE:
                _store(instruction.getArg1(), instruction.getArg2());
                break;
            case Instruction.ADD:
                _add();
                break;
            case Instruction.SUB:
                _sub();
                break;
            case Instruction.MUL:
                _mul();
                break;
            case Instruction.DIV:
                _div();
                break;
            case Instruction.LT:
                _lt();
                break;
            case Instruction.GT:
                _gt();
                break;
            case Instruction.EQ:
                _eq();
                break;
            case Instruction.NEQ:
                _neq();
                break;
            case Instruction.IFZERO:
                _ifzero(instruction.getArg1());
                break;
            case Instruction.GOTO:
                _goto(instruction.getArg1());
                break;
            case Instruction.HALT:
                _halt();
                break;
            case Instruction.NOP:
                _nop();
                break;
            case Instruction.INVOKE:
                _invoke(instruction.getArg1(), instruction.getArg2(), instruction.getArg3());
                break;
            case Instruction.RETURN:
                _return();
                break;
            default:
                throw new Error("Unknown opcode in instruction " + PC + ": " + instruction);
        }
    }

    private void _load(int k, int d) {
        stack[TOP + 1] = stack[spp(d, PP, FP) + k]; 
        TOP = TOP + 1;
        PC = PC + 1;
    }

    private void _store(int k, int d) {
        stack[spp(d, PP, FP) + k] = stack[TOP];
        TOP = TOP - 1;
        PC = PC + 1;
    }

    private void _return() {
        int result = stack[TOP];
        TOP = PP;
        PP = stack[FP];
        PC = stack[FP + 4];
        FP = stack[FP + 1];
        stack[TOP] = result;
    }

    private void _invoke(int n, int p, int d) {
        stack[TOP + 1] = PP;
        stack[TOP + 2] = FP;
        stack[TOP + 3] = spp(d, PP, FP);
        stack[TOP + 4] = sfp(d, PP, FP);
        stack[TOP + 5] = PC + 1;
        PP = TOP - n + 1;
        FP = TOP + 1;
        TOP = TOP + 5;
        PC = p;
    }
    
    /** Determine the d-th previous static parameter pointer.
     */
    private int spp(int d, int PP, int FP) {
        if (d == 0) {
            return PP;
        } else {
            return spp(d - 1, stack[FP + 2], stack[FP + 3]);
        }
    }
    /** Determine the d-th previous static frame pointer.
     */
    private int sfp(int d, int PP, int FP) {
        if (d == 0) {
            return FP;
        } else {
            return sfp(d - 1, stack[FP + 2], stack[FP + 3]);
        }
    }

    private void _ifzero(int p) {
        if (stack[TOP] == 0) {
            PC = p;
        } else {
            PC = PC + 1;            
        }
        TOP = TOP - 1;
    }

    private void _goto(int p) {
        PC = p;
    }

    private void _nop() {
        PC = PC + 1;
    }

    private void _neq() {
        if (stack[TOP - 1] != stack[TOP]) {
            stack[TOP - 1] = 1;
        } else {
            stack[TOP - 1] = 0;
        }
        TOP = TOP - 1;
        PC = PC + 1;
    }

    private void _eq() {
        if (stack[TOP - 1] == stack[TOP]) {
            stack[TOP - 1] = 1;
        } else {
            stack[TOP - 1] = 0;
        }
        TOP = TOP - 1;
        PC = PC + 1;
    }

    private void _gt() {
        if (stack[TOP - 1] > stack[TOP]) {
            stack[TOP - 1] = 1;
        } else {
            stack[TOP - 1] = 0;
        }
        TOP = TOP - 1;
        PC = PC + 1;
    }
    
    private void _lt() {
        if (stack[TOP - 1] < stack[TOP]) {
            stack[TOP - 1] = 1;
        } else {
            stack[TOP - 1] = 0;
        }
        TOP = TOP - 1;
        PC = PC + 1;
    }
    
    private void _div() {
        stack[TOP - 1] = stack[TOP - 1] / stack[TOP];
        TOP = TOP - 1;
        PC = PC + 1;
    }

    private void _mul() {
        stack[TOP - 1] = stack[TOP - 1] * stack[TOP];
        TOP = TOP - 1;
        PC = PC + 1;
    }

    private void _sub() {
        stack[TOP - 1] = stack[TOP - 1] - stack[TOP];
        TOP = TOP - 1;
        PC = PC + 1;
    }

    private void _add() {
        stack[TOP - 1] = stack[TOP - 1] + stack[TOP];
        TOP = TOP - 1;
        PC = PC + 1;
    }

    private void _halt() {
        PC = -1;
    }

    private void _const(int k) {
        stack[TOP + 1] = k;
        TOP = TOP + 1;
        PC = PC + 1;
    }
    
    
    private void debug() {
        if (!debug) {
            return;
        }
        System.out.print(
          String.format("PC: %d | PP: %d | FP: %d | TOP: %d | Stack: ", PC, PP, FP, TOP)
        );
        for (int i = 0; i <= TOP; i++) {
            System.out.print(stack[i] + " ");
        }
        System.out.println();
        if (PC >= 0) {
            System.out.println(String.format("%s", text[PC]));
        }
    }
    
    public void setTop(int top) {
        this.TOP = top;
    }
    
    public int getStackAt(int position) {
        if (position <= TOP) {
            return stack[position]; // may throw an index-out-of-bounce error
        } else {
            throw new Error("Accessing unused stack cells.");
        }
    }

}
