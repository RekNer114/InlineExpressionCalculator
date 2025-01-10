package com.shpp.p2p.cs.vmachohan.assignment10;

import java.util.ArrayList;
import java.util.HashMap;

public class Assignment10Part1 {

    public static void main(String[] args) {
        args = new String[]{"2^3-a+a", "a=2"};
        if (args.length == 0) {
            System.out.println("No expression!");
            return;
        }

        // read expression from the first parameter and remove spaces for easier
        // processing
        String expression = args[0].replaceAll("\\s+", "");
        ArrayList<String> tokens;
        if(ArgumentsProcessor.isExpressionCorrect(expression)) {
            //separated operands and operation
            tokens= ArgumentsProcessor.separateValues(expression);
            System.out.println(tokens);
        }else{
            return;
        }

        try {
            double result;
            // collect other parameters in hashmap if they are present
            if (args.length > 1) {
                HashMap<String, String> vars = ArgumentsProcessor.collectArguments(args);

                tokens = ArgumentsProcessor.injectUnknowns(vars, tokens);
                System.out.println(tokens);

                boolean hasUnknown = ArgumentsProcessor.hasUnknown(tokens);
                System.out.println(hasUnknown);

                result = getResult(hasUnknown, tokens);
            } else {
                boolean hasUnknown = ArgumentsProcessor.hasUnknown(tokens);
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
            System.out.println("Result: " + res);
        }
        return res;
    }


}
