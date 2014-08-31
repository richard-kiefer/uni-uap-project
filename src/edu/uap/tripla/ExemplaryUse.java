package edu.uap.tripla;

/**
 * Illustrates the usage of the @see Tripla class.
 * 
 * @author Richard Kiefer, s4rikief@uni-trier.de
 */
public class ExemplaryUse {

        public static void main(String argv[]) {
            String sourcecode = 
                    "let\n" +
                    "  factorial(number) {\n" +
                    "    if (number == 0) then\n" +
                    "      1\n" +
                    "    else\n" +
                    "      number * factorial(number - 1)" + 
                    "  }\n" +
                    "in\n" +
                    "  factorial(5)";
            int result = Tripla.run(sourcecode, true);
            System.out.println(String.format("Result: %d", result));
        }

}