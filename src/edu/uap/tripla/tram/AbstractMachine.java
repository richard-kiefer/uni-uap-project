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
    
    private static final int stackSize = 512;
    
    /** The set of instructions to be executed. */
    private Instruction[] text;
    private StackElement[] stack = new StackElement[stackSize];
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

        return stack[TOP].value; // may throw an array-out-of-bounce error
    }
    
    private void reset() {
        for (int i = 0; i < stack.length; i++) {
            stack[i] = new StackElement();
        }
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
                _invoke(instruction.getArg1(),
                        instruction.getArg2(),
                        instruction.getArg3());
                break;
            case Instruction.RETURN:
                _return();
                break;
            case Instruction.LAZY:
                _lazy(instruction.getArg1(), instruction.getArg2());
                break;
            case Instruction.LAZYRETURN:
                _lazyreturn();
                break;
            default:
                throw new Error(String.format(
                                    "Unknown opcode in instruction %d: %s",
                                    PC,
                                    instruction)
                               );
        }
    }

    private void _load(int k, int d) {
        int spp = spp(d, PP, FP);
        switch (stack[spp + k].tag) {
        case I:
            stack[TOP + 1].assign(stack[spp + k]);
            TOP = TOP + 1;
            PC = PC + 1;
            break;
        case C:
            stack[TOP + 1].assign(PC + 1, StackElement.Tag.P);
            stack[TOP + 2].assign(spp + k, StackElement.Tag.I);
            stack[TOP + 3].assign(FP, StackElement.Tag.P);
            stack[TOP + 4].assign(PP, StackElement.Tag.P);
            TOP = TOP + 4;
            PC = stack[stack[spp + k].value].value;
            FP = stack[stack[spp + k].value + 1].value;
            PP = stack[stack[spp + k].value + 2].value;
            break;
        default:
            throw new Error(String.format(
                                "Unexpected tag for stack element %d: %s",
                                spp + k,
                                stack[spp + k])
                           );    
        }
    }

    private void _store(int k, int d) {
        stack[spp(d, PP, FP) + k].assign(stack[TOP]);
        TOP = TOP - 1;
        PC = PC + 1;
    }
    
    private void _lazy(int k, int p) {
        stack[PP + k].assign(TOP + 1, StackElement.Tag.C);
        stack[TOP + 1].assign(p,  StackElement.Tag.P); 
        stack[TOP + 2].assign(FP, StackElement.Tag.I); 
        stack[TOP + 3].assign(PP, StackElement.Tag.I);
        TOP = TOP + 3;
        PC = PC + 1;
    }

    private void _lazyreturn() {
        PC = stack[TOP - 4].value;
        stack[stack[TOP - 3].value].assign(stack[TOP]);
        FP = stack[TOP - 2].value;
        PP = stack[TOP - 1].value;
        stack[TOP - 4].assign(stack[TOP]);
        TOP = TOP - 4;
    }


    private void _return() {
        StackElement result = stack[TOP];
        TOP = PP;
        PP = stack[FP].value;
        PC = stack[FP + 4].value;
        FP = stack[FP + 1].value;
        stack[TOP].assign(result);
    }

    private void _invoke(int n, int p, int d) {
        // built-in functions are identified by
        // invoke-instructions with negative labels.
        if (p < 0) {
            int[] parameters = new int[n];
            for (int i = 1; i <= n; i++) {
                parameters[i-1] = stack[TOP - n + i].value;
            }
            int result = BuiltInFunction.execute(p, parameters);
            TOP = TOP - n + 1;
            stack[TOP].value = result; 
            PC = PC + 1;
        }
        else {
            stack[TOP + 1].assign(PP, StackElement.Tag.I);
            stack[TOP + 2].assign(FP, StackElement.Tag.I);
            stack[TOP + 3].assign(spp(d, PP, FP), StackElement.Tag.I);
            stack[TOP + 4].assign(sfp(d, PP, FP), StackElement.Tag.I);
            stack[TOP + 5].assign(PC + 1, StackElement.Tag.P);
            PP = TOP - n + 1;
            FP = TOP + 1;
            TOP = TOP + 5;
            PC = p;
        }
    }
    
    
    /** Determine the d-th previous static parameter pointer.
     */
    private int spp(int d, int PP, int FP) {
        if (d == 0) {
            return PP;
        } else {
            return spp(d - 1, stack[FP + 2].value, stack[FP + 3].value);
        }
    }
    /** Determine the d-th previous static frame pointer.
     */
    private int sfp(int d, int PP, int FP) {
        if (d == 0) {
            return FP;
        } else {
            return sfp(d - 1, stack[FP + 2].value, stack[FP + 3].value);
        }
    }

    private void _ifzero(int p) {
        if (stack[TOP].value == 0) {
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
        checkInteger();
        if (stack[TOP - 1].value != stack[TOP].value) {
            stack[TOP - 1].value = 1;
        } else {
            stack[TOP - 1].value = 0;
        }
        TOP = TOP - 1;
        PC = PC + 1;
    }

    private void _eq() {
        checkInteger();
        if (stack[TOP - 1].value == stack[TOP].value) {
            stack[TOP - 1].value = 1;
        } else {
            stack[TOP - 1].value = 0;
        }
        TOP = TOP - 1;
        PC = PC + 1;
    }

    private void _gt() {
        checkInteger();
        if (stack[TOP - 1].value > stack[TOP].value) {
            stack[TOP - 1].value = 1;
        } else {
            stack[TOP - 1].value = 0;
        }
        TOP = TOP - 1;
        PC = PC + 1;
    }
    
    private void _lt() {
        checkInteger();
        if (stack[TOP - 1].value < stack[TOP].value) {
            stack[TOP - 1].value = 1;
        } else {
            stack[TOP - 1].value = 0;
        }
        TOP = TOP - 1;
        PC = PC + 1;
    }
    
    private void _div() {
        checkInteger();
        stack[TOP - 1].value = stack[TOP - 1].value / stack[TOP].value;
        TOP = TOP - 1;
        PC = PC + 1;
    }

    private void _mul() {
        checkInteger();
        stack[TOP - 1].value = stack[TOP - 1].value * stack[TOP].value;
        TOP = TOP - 1;
        PC = PC + 1;
    }

    private void _sub() {
        checkInteger();
        stack[TOP - 1].value = stack[TOP - 1].value - stack[TOP].value;
        TOP = TOP - 1;
        PC = PC + 1;
    }

    private void _add() {
        checkInteger();
        stack[TOP - 1].value = stack[TOP - 1].value + stack[TOP].value;
        TOP = TOP - 1;
        PC = PC + 1;
    }

    /**
     * Throws an error if the two topmost stack elements are not tagged
     * "Integer".
     */
    private void checkInteger() {
        if (stack[TOP - 1].tag != StackElement.Tag.I
         || stack[TOP].tag != StackElement.Tag.I) {
            throw new Error("The two topmost stack elements are not tagged as integer (I).");
        }
    }

    private void _halt() {
        PC = -1;
    }

    private void _const(int k) {
        stack[TOP + 1].assign(k, StackElement.Tag.I);
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
            return stack[position].value; // may throw an index-out-of-bounce error
        } else {
            throw new Error("Accessing unused stack cells.");
        }
    }

}
