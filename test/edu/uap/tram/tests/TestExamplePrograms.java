package edu.uap.tram.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.uap.tram.AbstractMachine;
import edu.uap.tram.ExamplePrograms;

public class TestExamplePrograms {

    AbstractMachine tram;

    @Before
    public void setUp() throws Exception {
        tram = new AbstractMachine();
        tram.debug = false;
    }
    
    @Test
    public void testProgram1() {
        tram.load(ExamplePrograms.program1);
        tram.setTop(1); // as specified in program description
        tram.run();
        assertEquals(28, tram.getStackAt(1));
    }
    
    @Test
    public void testProgram2() {
        tram.load(ExamplePrograms.program2);
        tram.setTop(0); // as specified in program description
        assertEquals(3, tram.run());
    }
    
    @Test
    public void testProgram3() {
        tram.load(ExamplePrograms.program3);
        assertEquals(100, tram.run());
    }
    
    @Test
    public void testProgram4() {
        tram.load(ExamplePrograms.program4);
        assertEquals(4, tram.run());
    }
    
    @Test
    public void testFactorial() {
        tram.load(ExamplePrograms.factorial(4));
        assertEquals(24, tram.run());
    }
    
    @Test
    public void testFactorialOfZero() {
        tram.load(ExamplePrograms.factorial(0));
        assertEquals(1, tram.run());
    }


}
