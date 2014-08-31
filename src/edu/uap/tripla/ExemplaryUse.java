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
                    "  var one = 1\n" +
                    "  factorial(number) {" +
                    "    print(number) ; \n" +
                    "    if (number == 0) then\n" +
                    "      one\n" +
                    "    else\n" +
                    "      number * factorial(number - 1)" + 
                    "  }\n" +
                    "in\n" +
                    "  factorial(5)";
            int result = Tripla.run(sourcecode, false);
            System.out.println(String.format("Result: %d", result));
        }

}
