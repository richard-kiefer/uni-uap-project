package edu.uap.tripla.compiler;

import java.util.LinkedList;
import java.util.List;

import edu.uap.tripla.tram.Instruction;
import edu.uap.tripla.parser.*;

/**
 * Compiler for the Trier Programming Language (TRIPLA).
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class TriplaCompiler {

    // the following variables are used as members in order to avoid
    // having them passed as arguments with every single code()-call.
    /** Holds the current nesting level. */
    static int nestingLevel;
    /** Holds the current address environment. */
    static AddressEnvironment rho;
    /** Holds one LabelProvider, necessary for jump-instructions. */
    static LabelProvider labelProvider;
    
    /**
     * Takes an abstract syntax tree and returns the
     * corresponding machine instructions.
     * 
     * @param ast The abstract syntax tree.
     * @return The program, i.e. machine instructions.
     */
    public static Instruction[] compile(AbstractSyntaxTree ast) {
        nestingLevel = 0;
        labelProvider = new LabelProvider();
        rho = new AddressEnvironment(labelProvider);

        List<Instruction> code = code(ast);
        code.add(new Instruction(Instruction.HALT));
        
        labelProvider.resolveLabels(code);
        // freeing objects
        labelProvider = null;
        rho = null;
        
        return code.toArray(new Instruction[0]);
    }
    
    /**
     * Takes an abstract syntax tree and returns the corresponding
     * machine instructions, where jump-instructions have unresolved
     * labels instead of real instruction locations.
     * 
     * Determines the given object's class at runtime, performs an
     * explicit cast and calls the specific code()-method.
     * Thereby, it allows to have all the code-generating methods
     * here in one place, separated in single methods for each
     * node-type of the abstract syntax tree.
     * 
     * @param ast AbstractSyntaxTree-object or one of its descendants.
     * @return
     */
    private static List<Instruction> code(AbstractSyntaxTree ast) {
        if (ast instanceof StatementSequence) {
            return code((StatementSequence)ast);
        }
        else if (ast instanceof Constant) {
            return code((Constant)ast);
        }
        else if (ast instanceof Operation) {
            return code((Operation)ast);
        }
        else if (ast instanceof Conditional) {
            return code((Conditional)ast);
        }
        else if (ast instanceof Program) {
            return code((Program)ast);
        }
        else if (ast instanceof FunctionDeclaration) {
            return code((FunctionDeclaration)ast);
        }        
        else if (ast instanceof VariableDeclaration) {
            return code((VariableDeclaration)ast);
        }        
        else if (ast instanceof LazyVariableDeclaration) {
            return code((LazyVariableDeclaration)ast);
        }        
        else if (ast instanceof FunctionCall) {
            return code((FunctionCall)ast);
        }
        else if (ast instanceof Identifier) {
            return code((Identifier)ast);
        }
        else if (ast instanceof Assignment) {
            return code((Assignment)ast);
        }
        else {
            throw new Error("Wopa. Unrecognized AbstractSyntaxTree-element.");
        }
    }
    
    /** @see List<Instruction> code(AbstractSyntaxTree) */
    private static List<Instruction> code(StatementSequence ss) {
        List<Instruction> r = new LinkedList<Instruction>();
        for (AbstractSyntaxTree statement: ss.getStatements()) {
            r.addAll(code(statement));
        }
        return r;
    }
    
    /** @see List<Instruction> code(AbstractSyntaxTree) */
    private static List<Instruction> code(Constant c) {
        List<Instruction> r = new LinkedList<Instruction>();
        r.add(new Instruction(Instruction.CONST, c.getValue()));
        return r;
    }

    /** @see List<Instruction> code(AbstractSyntaxTree) */
    private static List<Instruction> code(Operation o) {
        List<Instruction> r = new LinkedList<Instruction>();
        r.addAll(code(o.getOperandLeft()));
        r.addAll(code(o.getOperandRight()));
        String op = o.getOperator();
        if (op.equals("+")) {
            r.add(new Instruction(Instruction.ADD));
        }
        else if (op.equals("-")) {
            r.add(new Instruction(Instruction.SUB));            
        }
        else if (op.equals("*")) {
            r.add(new Instruction(Instruction.MUL));            
        }
        else if (op.equals("/")) {
            r.add(new Instruction(Instruction.DIV));            
        }
        else if (op.equals("==")) {
            r.add(new Instruction(Instruction.EQ));            
        }
        else if (op.equals("!=")) {
            r.add(new Instruction(Instruction.NEQ));            
        }
        else if (op.equals("<")) {
            r.add(new Instruction(Instruction.LT));            
        }
        else if (op.equals(">")) {
            r.add(new Instruction(Instruction.GT));            
        }
        else {
            throw new Error("Unrecognized operator.");
        }
        return r;
    }
    
    /** @see List<Instruction> code(AbstractSyntaxTree) */
    private static List<Instruction> code(Conditional c) {
        List<Instruction> r = new LinkedList<Instruction>();
        List<Instruction> conditional = code(c.getCondition());
        List<Instruction> consequent  = code(c.getConsequent());
        List<Instruction> alternative = code(c.getAlternative());
        Instruction nop = new Instruction(Instruction.NOP);
        int label_alt = labelProvider.getNewLabel(alternative.get(0));
        int label_nop = labelProvider.getNewLabel(nop);
        
        r.addAll(conditional);
        r.add(new Instruction(Instruction.IFZERO, label_alt));
        r.addAll(consequent);
        r.add(new Instruction(Instruction.GOTO, label_nop));        
        r.addAll(alternative);
        r.add(nop);
        
        return r;
    }
    
    /** @see List<Instruction> code(AbstractSyntaxTree) */
    private static List<Instruction> code(Program p) {
        AddressEnvironment old_rho = rho;
        rho = new AddressEnvironment(old_rho);
        rho.increaseNestingLevel();
        rho.elab_def(p);
        nestingLevel++;
        
        List<Instruction> r = new LinkedList<Instruction>();
        List<Instruction> function_decl = new LinkedList<Instruction>();
        List<Instruction> variable_decl = new LinkedList<Instruction>();
        List<Instruction> variable_init = new LinkedList<Instruction>();

        int label1 = labelProvider.getNewLabel();
        int label2 = labelProvider.getNewLabel();
        
        int numberOfVariables = 0;
        variable_decl.add(new Instruction(Instruction.NOP));
        variable_init.add(new Instruction(Instruction.NOP));
        for (AbstractSyntaxTree d: p.getDeclarations()) {
            if (d instanceof FunctionDeclaration) {
                function_decl.addAll(code(d));
            }
            else { // should be declaration of a (lazy) variable
                numberOfVariables++;
                variable_decl.addAll(code(d));
                variable_init.add(new Instruction(Instruction.CONST, 0));
            }
        }
        List<Instruction> body = code(p.getBody());
        
        r.add(new Instruction(Instruction.GOTO, label1));
        r.addAll(function_decl);
        r.addAll(variable_decl);
        r.addAll(body);
        r.add(new Instruction(Instruction.RETURN));
        r.addAll(variable_init);        
        r.add(new Instruction(Instruction.INVOKE, numberOfVariables, label2, 0));

        labelProvider.registerInstruction(label1, variable_init.get(0));
        labelProvider.registerInstruction(label2, variable_decl.get(0));

        nestingLevel--;
        rho = old_rho;
        return r;
    }
    
    /** @see List<Instruction> code(AbstractSyntaxTree) */
    private static List<Instruction> code(FunctionDeclaration fd) {
        AddressEnvironment old_rho = rho;
        rho = new AddressEnvironment(old_rho);
        rho.increaseNestingLevel();
        rho.elab_def(fd);
        nestingLevel++;
        
        List<Instruction> body = code(fd.getBody());
        body.add(new Instruction(Instruction.RETURN));
        labelProvider.registerInstruction(rho.get(fd).location, body.get(0));
        
        nestingLevel--;
        rho = old_rho;
        return body;        
    }
    
    /** @see List<Instruction> code(AbstractSyntaxTree) */
    private static List<Instruction> code(VariableDeclaration vd) {
        List<Instruction> r = code(vd.getExpression());
        r.add(new Instruction(Instruction.STORE,
                              rho.get(vd.getVariable()).location,
                              0
              ));
        return r;        
    }
    
    /** @see List<Instruction> code(AbstractSyntaxTree) */
    private static List<Instruction> code(LazyVariableDeclaration lvd) {
        int label1 = labelProvider.getNewLabel();
        int label2 = labelProvider.getNewLabel();
        
        List<Instruction> r = new LinkedList<Instruction>();
        List<Instruction> code_e = code(lvd.getExpression());
        Instruction code_lazy = new Instruction(
                                    Instruction.LAZY,
                                    rho.get(lvd.getVariable()).location,
                                    label2
                                );
        
        labelProvider.registerInstruction(label1, code_lazy);
        labelProvider.registerInstruction(label2, code_e.get(0));
        
        r.add(new Instruction(Instruction.GOTO, label1));
        r.addAll(code_e);
        r.add(new Instruction(Instruction.LAZYRETURN));
        r.add(code_lazy);
        
        return r;        
    }
    
    /** @see List<Instruction> code(AbstractSyntaxTree) */
    private static List<Instruction> code(FunctionCall fc) {
        List<Instruction> r = new LinkedList<Instruction>();
        AbstractSyntaxTree[] arguments = fc.getArguments();
        for (AbstractSyntaxTree argument: arguments) {
            r.addAll(code(argument));
        }
        Address a = rho.get(fc);
        r.add(new Instruction(
                Instruction.INVOKE,
                arguments.length,
                a.location,
                nestingLevel - a.nestingLevel
              ));
        return r;
    }
    
    /** @see List<Instruction> code(AbstractSyntaxTree) */
    private static List<Instruction> code(Identifier id) {
        List<Instruction> r = new LinkedList<Instruction>();
        Address a = rho.get(id);
        r.add(new Instruction(
                Instruction.LOAD,
                a.location,
                nestingLevel - a.nestingLevel
              ));
        return r;
    }
    
    /** @see List<Instruction> code(AbstractSyntaxTree) */
    private static List<Instruction> code(Assignment a) {
        Address var_address = rho.get(a.getVariable()); 
        List<Instruction> r;
        r = code(a.getExpression());
        r.add(new Instruction(
                Instruction.STORE,
                var_address.location,
                nestingLevel - var_address.nestingLevel
              ));
        r.add(new Instruction(
                Instruction.LOAD,
                var_address.location,
                nestingLevel - var_address.nestingLevel
              ));
        return r;
    }
}
