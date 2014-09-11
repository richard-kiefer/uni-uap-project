package edu.uap.tripla.tram.tests;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.uap.tripla.tram.AbstractMachine;
import edu.uap.tripla.tram.Instruction;

public class TestBuiltInFunctions {


    AbstractMachine tram;
    @Before
    public void setUp() throws Exception {
        tram = new AbstractMachine();
        tram.debug = false;
    }
    

    // For testing System.out output,
    // thanks to http://stackoverflow.com/a/1119559/1242922
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }
    @After
    public void cleanUpStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    
    
    @Test
    public void testPrint() {
        tram.load(new Instruction[]{
                new Instruction(Instruction.CONST, 42),
                new Instruction(Instruction.INVOKE, 1, -1, 0),
                new Instruction(Instruction.CONST, 23),
                new Instruction(Instruction.INVOKE, 1, -1, 0),
                new Instruction(Instruction.HALT)
        });
        int result = tram.run();
        assertEquals("42\n23\n", outContent.toString());
        assertEquals(23, result);
    }

}
