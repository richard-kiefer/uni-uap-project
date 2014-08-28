package edu.uap.tripla.tram;

/** Main program, driving the TRAM.
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 *
 */
public class Conductor {

    /**
     * @param args
     */
    public static void main(String[] args) {
        AbstractMachine tram = new AbstractMachine();
        tram.debug = true;
        tram.load(ExamplePrograms.factorial(4));
        int result = tram.run();
        System.out.println("factorial(4): " + result);
    }

}
