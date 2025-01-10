package com.shpp.p2p.cs.vmachohan.assignment10;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that contains methods to process input data
 * (tokenize expression, collect parameters, inject parameters into expression)
 */
public class ArgumentsProcessor {
    /**
     * Method to check if expression was written in the right way, and if so, return
     * arraylist with every token from current string
     *
     * @param expression string that contains expression from the user
     * @return ArrayList<String> with each token of the input string
     */
    public static boolean isExpressionCorrect(String expression) {
        //case when expression is empty
        if (expression == null || expression.isEmpty()) {
            System.out.println("No formula present!");
            return false;
        }

        //checks if first operator minus(if operator)
        //if it's any other operator than '-', it's wrong notation
        if(expression.charAt(0)!='-' && isOperator(String.valueOf(expression.charAt(0)))){
            System.out.println("Wrong operator at the start!");
            return false;
        }

        int openBracesCount = 0;

        //check all characters in expression
        for(int i = 0; i<expression.length(); i++){
            char c = expression.charAt(i);

            //if current char is open braces
            if(c == '('){
                //add braces counter
                openBracesCount++;
                //if character before this was a number or variable
                if (i > 0 && (Character.isLetterOrDigit(expression.charAt(i - 1)))) {
                    //return false 'cause wrong notation
                    return false;
                }
            }
            //if closing braces
            if(c == ')') {
                //remove 1 from openBracesCount, 'cause 1 opened brace was closed
                openBracesCount--;
                //is next char is number or variable
                if (i < expression.length() - 1 && (Character.isLetterOrDigit(expression.charAt(i + 1)))) {
                    //return false, 'cause wrong notation
                    return false;
                }
            }
            //if there is negative value of open braces, then we have one not needed closing brace
            if(openBracesCount <0)
                return false;

            //if it's just empty braces - wrong notation
            if (i > 0 && expression.charAt(i - 1) == '(' && expression.charAt(i + 1) == ')') {
                return false;
            }

            //if current char is operator
            if(isOperator(String.valueOf(expression.charAt(i)))){
               if(!isCorrectOperatorsNotation(i, expression)){
                   return false;
               }
            }
        }
        //return separated arraylist
        return openBracesCount == 0;
    }

    /**
     *
     * @param i index of current char
     * @param expression char array that represents expression
     * @return true if operators is written without mistakes
     */
    private static boolean isCorrectOperatorsNotation(int i, String expression) {
        //check if previous character is operator
        boolean lastWasOperator = isOperator(String.valueOf(expression.charAt(i-1)));

        return !lastWasOperator || i!=expression.length()-1;
    }

    /**
     * Divide string into tokens and save them in arraylist
     * @param expression string that represents expression
     * @return ArrayList<String> with each token of the input string
     */
    public static ArrayList<String> separateValues(String expression) {
        //final arraylist that'll be returned
        ArrayList<String> res = new ArrayList<>();
        //builder to save complicated numbers (floats, doubles)
        StringBuilder temp = new StringBuilder();

        char[] expressionCharArray = expression.toCharArray();

        //go through each char of the expression string
        for (int i = 0; i<expression.length(); i++) {
            char c = expression.charAt(i);

            //if it's letter or digit(in other words, not operator)
            if (Character.isLetterOrDigit(c) || c == '.') {
                //append it to the string builder
                temp.append(c);
            }else {
                //if it's other sign, like operators
                if (!temp.isEmpty() ||  c==')'){
                    //add temp to the arraylist result
                    if(!temp.isEmpty())
                        res.add(temp.toString());
                    //and reset temp string builder;
                    temp.setLength(0);
                }
                if(((i == 0 || expressionCharArray[i-1]=='(')) &&  c == '-'){
                    temp.append(c);
                }else {
                    //and add current element
                    res.add(String.valueOf(c));
                }
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
    public static HashMap<String, String> collectArguments(String[] args) {
        // hashmap to stack variables from the parameters
        HashMap<String, String> vars = new HashMap<>();

        // process other parameters if present
        // start from 1, 'cause args[0[] is expresion
        for (int i = 1; i < args.length; i++) {
            // split name of the variable, and it's value
            //used replaceAll instead of trim(), cause here was some problems
            String parameter = args[i].replaceAll("\\s", "");
            String[] partsOfParameter = parameter.split("=");

            if (partsOfParameter.length != 2) {
                System.out.println("Wrong parameter format!");
                return null;
            }

            vars.put(partsOfParameter[0], partsOfParameter[1]);

        }
        return vars;
    }

    /**
     *
     * @param tokens ArrayList<String> with tokenized expression
     * @return true if there is any unknown variable
     */
    public static boolean hasUnknown(ArrayList<String> tokens) {
        //using stream api, to find any element that contains letter
        return tokens
                .stream()
                .anyMatch(s -> (s.matches("-?[a-zA-Z]+")));
    }

    /**
     * Method to remove all unknown variables from expression with their value from parameters
     * (if present)
     *
     * @param vars HashMap<String, Double> with all unknown variables
     * @param tokens ArrayList<String> that represents tokenized expression
     * @return arraylist that represents expression, with changed variables to numbers.
     */
    public static ArrayList<String> injectUnknowns(HashMap<String, String> vars, ArrayList<String> tokens) {
        //go through keys
        for(String k : vars.keySet()){
            //replace character in each string of the list which equals to this key with variable value.
            //if string contains '-' at the start, replace value of the variable with it's negative value.
            tokens.replaceAll(s-> s.contains(k) ?
                    (s.charAt(0) =='-') ?  String.valueOf(-Double.parseDouble(vars.get(k)))
                            :s.replaceAll(k, vars.get(k)): s);
        }
        return tokens;
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
