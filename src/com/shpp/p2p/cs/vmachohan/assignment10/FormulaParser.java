package com.shpp.p2p.cs.vmachohan.assignment10;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that contains useful functions to process input data
 * (tokenize expression, collect parameters, inject parameters into expression)
 */
public class FormulaParser {
    /**
     * Method to check if expression was written in the right way, and if so, return
     * arraylist with every token from current string
     *
     * @param expression string that contains expression from the user
     * @return ArrayList<String> with each token of the input string
     */
    public static ArrayList<String> checkAndTokenizeExpression(String expression) {
        //case when expression is empty
        if (expression == null || expression.isEmpty()) {
            System.out.println("No formula present!");
            return new ArrayList<>();
        }

        //case if expression has wrong symbols
        if (!expression.matches("[a-zA-Z0-9+\\-*/^(). ]+")) {
            System.out.println("Miss input");
            return new ArrayList<>();
        }

        //return separated arraylist
        return separateValues(expression);
    }

    /**
     * Divide string into tokens and save them in arraylist
     * @param expresion string that represents expresion
     * @return ArrayList<String> with each token of the input string
     */
    private static ArrayList<String> separateValues(String expresion) {
        //final arraylist that'll be returned
        ArrayList<String> res = new ArrayList<>();
        //builder to save complicated numbers (floats, doubles)
        StringBuilder temp = new StringBuilder();
        char[] expresionArr = expresion.toCharArray();

        boolean lastWasOperator = false;

        //go through each char of the expresion string
        for (int i = 0; i<expresionArr.length; i++) {
            char c = expresionArr[i];

            //if it's letter or digit(in other words, not operator)
            if (Character.isLetterOrDigit(c) || c == '.') {
                //append it to the stringbuilder
                temp.append(c);
                lastWasOperator = false;
            } else if (lastWasOperator && isOperator(String.valueOf(c))) {
                System.out.println("Wrong notation!");
                return null;
            } else {
                //if it's other sign, like operators
                if (!temp.isEmpty() ||  c==')'){
                    //add temp to the arraylist result
                    res.add(temp.toString());
                    //and reset temp stringbuilder;
                    temp.setLength(0);
                }
                if(((i == 0 || expresionArr[i-1]=='(')) &&  c == '-'){
                    temp.append(c);
                    continue;
                }else {
                    //and add current element
                    res.add(String.valueOf(c));
                }

                lastWasOperator = isOperator(String.valueOf(c));
            }

        }
        //if after finishing loop we have smth in temp
        if (!temp.isEmpty()) {
            //add it to the result
            res.add(temp.toString());
        }

        return res;
    }

    /**
     * Method to collect all parameters other than first(expresion)
     * @param args input array of string with parameters
     * @return HashMap<String, Double> where key is variable name and value is a double
     */
    public static HashMap<String, Double> collectArguments(String[] args) {
        // hashmap to stack variables from the parameters
        HashMap<String, Double> vars = new HashMap<>();

        // proccess other parameters if present
        // start from 1, 'cause args[0[] is expresion
        for (int i = 1; i < args.length; i++) {
            // split name of the variable and it's value
            //used replaceAll instead of trim(), cause here was some problems
            String parameter = args[i].replaceAll("\\s", "");
            String[] partsOfParameter = parameter.split("=");

            if (partsOfParameter.length != 2) {
                System.out.println("Wrong parameter format!");
                return null;
            }

            vars.put(partsOfParameter[0], Double.parseDouble(partsOfParameter[1]));

        }
        return vars;
    }


    /**
     *
     * @param tokens ArrayList<String> with tokenized expresion
     * @return true if there is any unknown variable
     */
    public static boolean hasUnknown(ArrayList<String> tokens) {
        //using stream api, to find any element that contains letter
        return tokens
                .stream()
                .anyMatch(s -> !isOperator(s) && (s.matches("-[a-zA-Z]+") || s.matches("[a-zA-z]")));
    }

    /**
     * Method to remove all unknown variables from expression with their value from parameters
     * (if present)
     *
     * @param vars HashMap<String, Double> with all unknown variables
     * @param tokens ArrayList<String> that represents tokenized expression
     * @return arraylist that represents expression, with changed variables to numbers.
     */
    public static ArrayList<String> injectUnknowns(HashMap<String, Double> vars, ArrayList<String> tokens) {
        //result arraylist
        ArrayList<String> res =  new ArrayList<>();
        //go through each token
        for(String t : tokens){
            //if there is such key in the map with vars
            if(vars.containsKey(t)){
                //add value of that key to the result instead of the original letter
                res.add(String.valueOf(vars.get(t)));
            } else if (t.startsWith("-") && vars.containsKey(t.substring(1))) {
                double value = -vars.get(t.substring(1));
                res.add(String.valueOf(value));
            } else{
                //add old value, 'cause there no such key
                res.add(t);
            }
        }
        return res;
    }

    /**
     * Method return if token that was passed it operator, by evalueting it with
     * regex expresion that represents all allowed operators.
     *
     * @param token token from the expresion
     * @return true if it's operator
     */
    public static boolean isOperator(String token) {
        return token.matches("[+\\-*/^]");
    }
}
