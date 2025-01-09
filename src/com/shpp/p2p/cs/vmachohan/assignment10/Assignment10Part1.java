package com.shpp.p2p.cs.vmachohan.assignment10;

import java.util.ArrayList;
import java.util.HashMap;

public class Assignment10Part1 {

    public static void main(String[] args) {
        args = new String[]{"-2^(-b)-9^(a)+6/0", "a=5", "b=-6"};
        args = new String[]{"-2^2 + a/(-3)", "a=9"};
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
                HashMap<String, Double> vars = FormulaParser.collectArguments(args);
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
