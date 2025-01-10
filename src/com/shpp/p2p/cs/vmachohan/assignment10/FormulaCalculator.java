package com.shpp.p2p.cs.vmachohan.assignment10;

import java.util.ArrayList;
import java.util.Stack;


/**
 * Class that apply operation for expression and calculate final result
 */
public class FormulaCalculator {

    /**
     * Method if there is only expression with numbers.
     *
     * @param expression hashset with variable names and value
     * @return double result of the expression
     */
    public double calculate(ArrayList<String> expression) {
        //convert expression to Reversed Polish Notation
        expression = reversePolishNotationConverter(expression);
        //solving stack
        Stack<String> solve = new Stack<>();

        //go through each token in expression
        for (String s : expression) {
            System.out.println(solve);
            //if element isn't an operator we push it to solve stack
            if (!ArgumentsProcessor.isOperator(s)) {
                solve.push(s);
            } else {
                //if there is only 1 element left in the stack
                //and if there is minus, just negate last number
                if(solve.size()==1 && s.equals("-"))
                    solve.push(String.valueOf(
                            -Double.parseDouble(solve.pop())
                    ));

                else
                    //if there is more, apply default operation
                    solve.push(String.valueOf(
                            applyOperator(s, Double.parseDouble(solve.pop()), Double.parseDouble(solve.pop()))));


            }
        }

        //return final result
        return Double.parseDouble(solve.peek());
    }


    /**
     *
     * @param operator operator for 2 numbers
     * @param a number from expression to process
     * @param b number from expression to process
     * @return result of operation with that 2 numbers
     */
    private double applyOperator(String operator, double a, double b) {
        return switch (operator) {
            case "+" -> b + a;
            case "-" -> b - a;
            case "*" -> b * a;
            case "/" -> {
                if(a==0)
                    throw new IllegalArgumentException("can't divide by 0");
                else
                    yield b/a;
            }
            case "^" -> Math.pow(b, a);
            default -> throw new IllegalArgumentException("Invalid operator: " + operator);
        };
    }

    /**
     * Method to make calculation simpler, cause of reverse polish notation,
     * it will be easier to calculate expression in right order.
     * (
     * Source: <a href="https://en.wikipedia.org/wiki/Reverse_Polish_notation">...</a>
     * )
     *
     * @param expression arraylist which represents tokenized expression
     * @return arraylist that represents current expression in reverse polish
     *         expression
     */
    private ArrayList<String> reversePolishNotationConverter(ArrayList<String> expression) {
        // arraylist to save result
        ArrayList<String> out = new ArrayList<>();
        // stack to hold operators
        Stack<String> hold = new Stack<>();

        // go through each element of the expression
        for (String s : expression) {
            // if it's not operator and "("
            if (!ArgumentsProcessor.isOperator(s) && !s.equals("(") && !s.equals(")")) {
                // we add it to the result
                out.add(s);
                // if it's "(" we add it to the stack as a break point for situation when we'll
                // reach ')'
            } else if (s.equals("(")) {
                hold.push(s);
            } else if (s.equals(")")) {
                // while last element in stack not equals "("
                while (!hold.peek().equals( "(")) {
                    // add to the result last result
                    out.add(hold.pop());
                }
                // pop stack to remove "("
                hold.pop();
            } else {
                // and if it's operator
                // check if it's not empty and precidence of the current operator is greater
                // then others then
                while (!hold.isEmpty() && getPrecidence(hold.peek()) >= getPrecidence(s)) {
                    // add to the output
                    out.add(hold.pop());
                }
                // and push current operator
                hold.push(s);
            }
        }

        // in the end, add all operators that is in the hold to the result
        while (!hold.isEmpty()) {
            out.add(hold.pop());
        }

        return out;
    }

    /**
     * Method to get precidence of operator.
     * If predicence is higher, than it's more important, and calculates first.
     *
     *
     * @param op operator for which will be returned its precidence
     * @return precidence of the operator
     */
    private int getPrecidence(String op) {
        return switch (op) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            case "^" -> 3;
            default -> -1;
        };
    }


}
