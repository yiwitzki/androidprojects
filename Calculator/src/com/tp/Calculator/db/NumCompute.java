package com.tp.Calculator.db;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by tp on 2015/12/8.
 */
public class NumCompute {
    private Stack<String> inputStack = new Stack<>();
    private ArrayList<String> inputArr = new ArrayList<>();
    //private double result = 0;
    // ( ) * + , - . /
    private int[] priority = new int[]{-2, -2, 1, -1, 0, -1, 0, 1};

    /**
     * call Delete() when user click delete button, clear the last user's input
     */
    public void Delete()
    {
        int size = inputArr.size();
        if (size != 0)
            inputArr.remove(size - 1);
    }

    /**
     * 获得当前用户的输入
     * @return 返回用户输入的表达式
     */
    public String getInput()
    {
        if (inputArr.size() == 0)
            return "0";
        StringBuilder sb = new StringBuilder();
        for (String str : inputArr)
            sb.append(str);
        System.out.print(inputArr.size() + "\n");
        return sb.toString();
    }
    /**
     * call RecordResult when user click the number button or +, -, *, /
     *
     * @param currInput user's input,such as (1-9, +, -, *, /)
     */
    public void RecordInput(String currInput)
    {
        if (currInput == null)
            return;
        if (!isNum(currInput.charAt(0)))
            return;
        String tmp = "";
        for (int i = 0; i < currInput.length(); i++)
        {
            char cur = currInput.charAt(i);
            if (!isOperator(cur))
                tmp += cur;
            else
            {
                inputArr.add(tmp);
                inputArr.add(String.valueOf(cur));
                tmp = "";
            }
        }
        inputArr.add(tmp);
    }
    private boolean isBracket(char ch)
    {
        if (ch == '(' || ch == ')')
            return true;
        else
            return false;
    }
    private boolean isNum(char ch)
    {
        if (ch >= '0' && ch <= '9')
            return true;
        else
            return false;
    }
    public double getResult()
    {
        double result = 0;
        int size = inputArr.size();
        if (size == 0)
            return 0;
        String suffix = infixTosuffix(inputArr);
        result = suffixToResult(suffix);
        inputArr.clear();
        inputArr.add(String.valueOf(result));
        return result;
    }

    /**
     * 中缀表达式转后缀表达式
     *
     * @param infix 中缀表达式
     * @return 后缀表达式
     */
    private String infixTosuffix(ArrayList<String> infix) {
        if (infix == null)
            return null;
        StringBuilder suffix = new StringBuilder();
        // char []infixArray = infix.toCharArray();
        for (int i = 0; i < infix.size(); i++) {
            String str = infix.get(i);
            char currentOp = str.charAt(0);
            if (currentOp >= '0' && currentOp <= '9')
                suffix.append(str).append(" ");
            else if (isOperator(currentOp)) {
                if (inputStack.isEmpty())
                    inputStack.push(str);
                else {
                    char stackTop = inputStack.peek().charAt(0);
                    if (currentOp == '(')
                        inputStack.push(str);
                    else if (currentOp == ')') {
                        while (!inputStack.isEmpty() && stackTop != '(') {
                            suffix.append(stackTop).append(" ");
                            inputStack.pop();
                            if (!inputStack.isEmpty())
                                stackTop = inputStack.peek().charAt(0);
                        }
                        if (!inputStack.isEmpty() && stackTop == '(')
                            inputStack.pop();
                    } else if (priority[currentOp - '('] > priority[stackTop - '(']) {
                        inputStack.push(str);
                    } else if (priority[currentOp - '('] < priority[stackTop - '(']) {
                        while (!inputStack.isEmpty() && stackTop != '(') {
                            suffix.append(inputStack.pop()).append(" ");
                            if (!inputStack.isEmpty())
                                stackTop = inputStack.peek().charAt(0);
                        }
                        inputStack.push(str);

                    } else if (priority[currentOp - '('] == priority[stackTop - '(']) {
                        suffix.append(inputStack.pop()).append(" ");
                        inputStack.push(str);
                    }
                }
            }
        }
        while (!inputStack.isEmpty())
            suffix.append(inputStack.pop()).append(" ");
        return suffix.toString();
    }

    /**
     * 判断当前的字符是否为运算符或者括号
     *
     * @param currentOp 当前的字符
     * @return 运算符或者括号则返回true, 否则返回false
     */
    private boolean isOperator(char currentOp) {
        if (currentOp == '+' || currentOp == '-' || currentOp == '*' || currentOp == '/' || currentOp == '(' || currentOp == ')')
            return true;
        else
            return false;
    }

    /**
     * 后缀表达式求值
     *
     * @param suffix 后缀表达式字符串
     * @return
     */
    private double suffixToResult(String suffix) {
        if (suffix == null)
            return 0;
        double result = 0;
        Stack<Double> stack = new Stack<>();
        String[] suffixArray = new String[]{};
        suffixArray = suffix.split(" ");
        for (int i = 0; i < suffixArray.length; i++) {
            String str = suffixArray[i];
            char currentOp = str.charAt(0);
            if (currentOp >= '0' && currentOp <= '9') {
                stack.push(Double.valueOf(str));
            } else if (isOperator(currentOp)) {
                double op2 = stack.pop();
                double op1 = stack.pop();
                if (currentOp == '+')
                    stack.push(op1 + op2);
                else if (currentOp == '-')
                    stack.push(op1 - op2);
                else if (currentOp == '*')
                    stack.push(op1 * op2);
                else if (currentOp == '/')
                    stack.push(op1 / op2);
            }
        }
        if (!stack.isEmpty())
            result = stack.pop();
        return result;
    }

    private String[] stringToStringArray(String str) {
        if (str == null)
            return null;
        String[] result = new String[]{};
        result = str.split(" ");
        return result;
    }

    public static void main(String[] args) {
        String infix = "6*15/3";
        String infix1 = "1+2+3+4";
        String suffix = "1 2 + 3 + 4 + ";
        String suffix1 = "9 3 1 - 3 * + 10 2 / +";
        NumCompute nc = new NumCompute();
        nc.RecordInput(infix);
        /*nc.RecordInput("3");
        nc.RecordInput("-");
        nc.RecordInput("1");
        nc.RecordInput(")");
        nc.RecordInput("*");
        nc.RecordInput("3");
        nc.RecordInput("+");
        nc.RecordInput("10");
        nc.RecordInput("/");
        nc.RecordInput("2");*/
       /* System.out.print(nc.getInput() + "\n");
        nc.Delete();
        System.out.print(nc.getInput() + "\n");
        nc.Delete();
        System.out.print(nc.getInput() + "\n");*/
        double result = nc.getResult();

       /* String str = nc.infixTosuffix(stringToStringArray(infix));
        double result = nc.suffixToResult(str);*/
        System.out.print(result);

    }
}
