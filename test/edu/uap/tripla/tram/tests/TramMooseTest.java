package edu.uap.tripla.tram.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.uap.tripla.tram.AbstractMachine;
import edu.uap.tripla.tram.Instruction;

/**
 * This class tests the basic instructions of the TRAM.
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 *
 */
public class TramMooseTest {

    AbstractMachine tram;

    @Before
    public void setUp() throws Exception {
        tram = new AbstractMachine();
        tram.debug = false;
    }
    
    @Test
    public void testStoreAndLoad() {
        tram.load(new Instruction[]{
                new Instruction(Instruction.CONST, 42),
                new Instruction(Instruction.CONST, 23),
                new Instruction(Instruction.STORE, 0, 0),
                new Instruction(Instruction.CONST, 142),
                new Instruction(Instruction.LOAD, 0, 0),
                new Instruction(Instruction.HALT)
        });
        assertEquals(23, tram.run());
        tram.load(new Instruction[]{
                new Instruction(Instruction.CONST, 23),
                new Instruction(Instruction.CONST, 42),
                new Instruction(Instruction.INVOKE, 2, 3, 0),
                new Instruction(Instruction.LOAD, 0, 0), 
                new Instruction(Instruction.LOAD, 1, 0),
                new Instruction(Instruction.ADD), 
                new Instruction(Instruction.HALT)
        });
        assertEquals(65, tram.run());
    }

    /**
     * Tests a bug reported by lutzr on 23rd May 2014.
     * 
     * The bug cannot be revealed by a program on this abstract
     * machine; we just have to look into the stack. Therefore,
     * this crude set of instructions is a pretty mindfuck.
     */
    @Test
    public void testInvokeWithDistanceGreaterZero() {
        tram.load(new Instruction[]{
                new Instruction(Instruction.CONST, 23),
                new Instruction(Instruction.INVOKE, 1, 3, 0),
                new Instruction(Instruction.HALT),
                new Instruction(Instruction.CONST, 42),
                new Instruction(Instruction.INVOKE, 1, 6, 0),
                new Instruction(Instruction.HALT),
                new Instruction(Instruction.CONST, 2342),
                new Instruction(Instruction.INVOKE, 1, 9, 1),
                new Instruction(Instruction.HALT),
                new Instruction(Instruction.HALT)
        });
        tram.run();
        assertEquals(1, tram.getStackAt(16));    
    }
    
    @Test
    public void testInvokeAndReturnWithDistanceZero() {
        tram.load(new Instruction[]{
                new Instruction(Instruction.CONST, 42),
                new Instruction(Instruction.INVOKE, 1, 4, 0),
                new Instruction(Instruction.CONST, 23),
                new Instruction(Instruction.HALT),
                new Instruction(Instruction.CONST, 172),
                new Instruction(Instruction.RETURN),
                new Instruction(Instruction.HALT)
        });
        assertEquals(23, tram.run());
    }

    @Test
    public void testInvoke() {
        tram.load(new Instruction[]{
                new Instruction(Instruction.CONST, 42),
                new Instruction(Instruction.INVOKE, 1, 3, 0),
                new Instruction(Instruction.HALT),
                new Instruction(Instruction.CONST, 23),
                new Instruction(Instruction.HALT)
        });
        assertEquals(23, tram.run());
    }

    @Test
    public void testIfzero() {
        tram.load(new Instruction[]{
                new Instruction(Instruction.CONST, 23),
                new Instruction(Instruction.CONST, 0),
                new Instruction(Instruction.IFZERO, 4),
                new Instruction(Instruction.CONST, 42),
                new Instruction(Instruction.HALT)
        });
        assertEquals(23, tram.run());
    }

    @Test
    public void testGoto() {
        tram.load(new Instruction[]{
                new Instruction(Instruction.GOTO, 3),
                new Instruction(Instruction.CONST, 23),
                new Instruction(Instruction.GOTO, 5),
                new Instruction(Instruction.CONST, 42),
                new Instruction(Instruction.GOTO, 1),
                new Instruction(Instruction.HALT)
        });
        assertEquals(23, tram.run());
    }

    @Test
    public void testNeq() {
        tram.load(new Instruction[]{
                new Instruction(Instruction.CONST, 23),
                new Instruction(Instruction.CONST, 42),
                new Instruction(Instruction.NEQ),
                new Instruction(Instruction.HALT)
        });
        assertEquals(1, tram.run());
    }

    @Test
    public void testEq() {
        tram.load(new Instruction[]{
                new Instruction(Instruction.CONST, 23),
                new Instruction(Instruction.CONST, 42),
                new Instruction(Instruction.EQ),
                new Instruction(Instruction.HALT)
        });
        assertEquals(0, tram.run());
    }

    @Test
    public void testGt() {
        tram.load(new Instruction[]{
                new Instruction(Instruction.CONST, 23),
                new Instruction(Instruction.CONST, 42),
                new Instruction(Instruction.GT),
                new Instruction(Instruction.HALT)
        });
        assertEquals(0, tram.run());
    }

    @Test
    public void testLt() {
        tram.load(new Instruction[]{
                new Instruction(Instruction.CONST, 23),
                new Instruction(Instruction.CONST, 42),
                new Instruction(Instruction.LT),
                new Instruction(Instruction.HALT)
        });
        assertEquals(1, tram.run());
    }

    @Test
    public void testDiv() {
        tram.load(new Instruction[]{
                new Instruction(Instruction.CONST, 84),
                new Instruction(Instruction.CONST, 2),
                new Instruction(Instruction.DIV),
                new Instruction(Instruction.HALT)
        });
        assertEquals(42, tram.run());
    }

    @Test
    public void testMul() {
        tram.load(new Instruction[]{
                new Instruction(Instruction.CONST, 23),
                new Instruction(Instruction.CONST, 2),
                new Instruction(Instruction.MUL),
                new Instruction(Instruction.HALT)
        });
        assertEquals(46, tram.run());
    }

    @Test
    public void testSub() {
        tram.load(new Instruction[]{
                new Instruction(Instruction.CONST, 65),
                new Instruction(Instruction.CONST, 23),
                new Instruction(Instruction.SUB),
                new Instruction(Instruction.HALT)
        });
        assertEquals(42, tram.run());
    }

    @Test
    public void testAdd() {
        tram.load(new Instruction[]{
                new Instruction(Instruction.CONST, 23),
                new Instruction(Instruction.CONST, 19),
                new Instruction(Instruction.ADD),
                new Instruction(Instruction.HALT)
        });
        assertEquals(42, tram.run());
    }

    @Test
    public void testConst() {
        tram.load(new Instruction[]{
                new Instruction(Instruction.CONST, 42),
                new Instruction(Instruction.HALT)
        });
        assertEquals(42, tram.run());
    }

}
