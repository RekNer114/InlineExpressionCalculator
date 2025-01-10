package com.shpp.p2p.cs.vmachohan.assignment10;

import java.util.ArrayList;
import java.util.HashMap;

public class Assignment10Part1 {

    public static void main(String[] args) {
        args = new String[]{"-(-(-a))+6/0", "a=2", "b=2"};
        //args = new String[]{"-a*2", "a=b", "b=c" , "c=1"};

        if (args.length == 0) {
            System.out.println("No expression!");
            return;
        }

        // read expression from the first parameter and remove spaces for easier
        // processing
        String expression = args[0].replaceAll("\\s+", "");

        //separated operands and operation
        ArrayList<String> tokens = FormulaParser.checkAndTokenizeExpression(expression);
        System.out.println(tokens);

        try {
            double result;
            // collect other parameters in hashmap if they are present
            if (args.length > 1) {
                HashMap<String, String> vars = FormulaParser.collectArguments(args);

                tokens = FormulaParser.injectUnknowns(vars, tokens);
                System.out.println(tokens);

                boolean hasUnknown = FormulaParser.hasUnknown(tokens);
                System.out.println(hasUnknown);

                result = getResult(hasUnknown, tokens);
            } else {
                boolean hasUnknown = FormulaParser.hasUnknown(tokens);
                result = getResult(hasUnknown, tokens);
            }

            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     *
     * @param hasUnknown show if there is  any unknown variable in the tokens list
     * @param tokens tokens of the expression
     * @return result of the math expression
     * @throws Exception
     */
    static double getResult(boolean hasUnknown, ArrayList<String> tokens) throws Exception {
        FormulaCalculator calc = new FormulaCalculator();
        double res;
        if (hasUnknown) {
            throw new Exception("\"can't calculate 'cause present unknown vars\"");
        } else {
            res = calc.calculate(tokens);
            System.out.println("Resul: " + res);
        }
        return res;
    }


}
