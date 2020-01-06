package io.github.skywalkerdarren.simpleaccounting.util.calculate;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * 大数计算工具
 *
 * @author darren
 * @date 2018/3/10
 */

public class CalculateUtil {

    private static final int LENGTH = 32;

    //    private int radix = NORMAL;
    private static Pattern patternFloat = Pattern.compile("[-+]?\\d+\\.\\d+");
    private static Pattern patternExp = Pattern.compile("[-+]?\\d+\\.\\d+[Ee]+\\++\\d+");
    private static Pattern patternInteger = Pattern.compile("[-+]?\\d+");
    private static Pattern patternHex = Pattern.compile("[-+]?[\\d[a-f][A-F]]+");

    /**
     * 获取计算结果
     *
     * @return 计算结果
     */
    public static BigDecimal getResult(Expression expression) {
        String exp = convertToExpression(expression);
        BigDecimal r;
        try {
            r = new BigDecimal(new CalculateUtil().getResult(exp));
        } catch (Exception e) {
            r = BigDecimal.ZERO;
        }
        return r;
    }

    /**
     * 动态的检查表达式合法性
     * 不完全确保正确，但可以有效的进行行为约束，空串也合法
     *
     * @param exp 未完成的表达式
     * @return true为合法
     */
    public static boolean dynamicCheckExperssion(String exp) {
        if (TextUtils.isEmpty(exp)) {
            return true;
        }
        final boolean n = false;
        final boolean y = true;
        final boolean[][] fsm = new boolean[][]{
                // num
                {y, y, y},
                // dot
                {y, n, n},
                // sign
                {y, n, n},
        };

        int j = 2, k;
        char c = exp.charAt(0);
        if (c >= '0' && c <= '9') {
            k = 0;
        } else if (c == '.') {
            k = 1;
        } else {
            k = 2;
        }

        for (int i = 1; i < exp.length(); i++) {
            j = k;
            c = exp.charAt(i);
            if (c >= '0' && c <= '9') {
                k = 0;
            } else if (c == '.') {
                k = 1;
            } else {
                k = 2;
            }

            if (!fsm[j][k]) {
                return false;
            }
        }
        return fsm[j][k];
    }

    /**
     * 转换到表达式
     *
     * @param exp 原始表达式
     * @return 表达式
     */
    private static String convertToExpression(Expression exp) {
        return exp.createExpression();
    }

    /**
     * 通过表达式获取计算结果
     *
     * @param exp 表达式构成的字符串
     * @return 表达式的结果
     */
    public String getResult(String exp) {
        if (TextUtils.isEmpty(exp)) {
            return "0";
        }
        List<String> expression = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);
            if (c == '.' || c >= '0' && c <= '9') {
                sb.append(c);
            } else {
                expression.add(sb.toString());
                sb.delete(0, sb.length());
                expression.add(c + "");
            }
        }
        expression.add(sb.toString());

        if (!checkExpression(expression)) {
            throw new IllegalArgumentException("表达式不合法");
        }

        BigDecimal answer = evaluate(expression);
        String result = answer.setScale(16, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString();
        if (result.length() < LENGTH) {
            return result;
        } else {
            return answer.setScale(16, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toEngineeringString();
        }
    }

    /**
     * 有限状态机检查表达式是否合法
     * 状态转移表：
     * 正负号, 数值, 双目运算符, 单目运算符, 左括号, 右括号
     * **0 1 2 3 4 5
     * 0   T
     * 1     T     T
     * 2   T   T T
     * 3         T
     * 4 T T   T T
     * 5     T     T
     *
     * @param exp 表达式数组
     */
    private boolean checkExpression(List<String> exp) {

        int l = 0, r = 0;
        //      neg,
        final boolean[][] fsm = new boolean[][]{
                // neg, num,    double, single, left,   right
                {false, true, false, false, false, false},
                // num
                {false, false, true, false, false, true},
                // double
                {false, true, false, true, true, false},
                // single
                {false, false, false, false, true, false},
                // left
                {true, true, false, true, true, false},
                // right
                {false, false, true, false, false, true},
        };

        // 初始化一个无效入口
        int i = 0, j = 0;
        boolean single = false;
        String start = exp.get(0);
        try {
            single = Operators.operatorHashMap.get(start).getAry() == 1;
        } catch (NullPointerException ignored) {

        }

        // 设定状态机有效入口
        if (patternFloat.matcher(start).matches() ||
                patternInteger.matcher(start).matches() ||
                patternHex.matcher(start).matches() ||
                patternExp.matcher(start).matches()) {
            j = 1;
        } else if ("-".equals(start) || "+".equals(start)) {
            j = 0;
        } else if ("(".equals(start)) {
            l = 1;
            j = 4;
        } else if (single) {
            j = 3;
        }

        for (int k = 1; k < exp.size(); k++) {
            i = j;
            if (patternFloat.matcher(exp.get(k)).matches() ||
                    patternInteger.matcher(exp.get(k)).matches() ||
                    patternHex.matcher(exp.get(k)).matches() ||
                    patternExp.matcher(exp.get(k)).matches()) {
                j = 1;
            } else if (i == 4 && ("-".equals(exp.get(k)) || "+".equals(exp.get(k)))) {
                j = 0;
            } else if ("(".equals(exp.get(k))) {
                l++;
                j = 4;
            } else if (")".equals(exp.get(k))) {
                r++;
                j = 5;
            } else if (Operators.operatorHashMap.get(exp.get(k)).getAry() == 2) {
                j = 2;
            } else if (Operators.operatorHashMap.get(exp.get(k)).getAry() == 1) {
                j = 3;
            }

            if (!fsm[i][j]) {
                return false;
            }

        }

        return fsm[i][j] && r <= l;
    }

    /**
     * 表达式处理的核心方法
     *
     * @param expression: 一个由单个数值或符号构成的表达式数组(e.g., s[] = {"3.56","+","4"})
     * @return 数值结果
     */
    private BigDecimal evaluate(List<String> expression) {
        if (expression.size() < 1) {
            throw new IllegalArgumentException("表达式为空");
        }
        Stack<BigDecimal> val = new Stack<>();
        Stack<String> flag = new Stack<>();
        // 终止符
        flag.push("@");
        for (String s : expression) {
            try {
                val.push(new BigDecimal(s));
            } catch (NumberFormatException e) {
                // 运算符
                switch (s) {
                    case ")":
                        while (!"(".equals(flag.peek())) {
                            calcExp(val, flag);
                        }
                        flag.pop();
                        break;
                    case "(":
                        flag.push(s);
                        break;
                    case "":
                        break;
                    default:
                        while (Operators.operatorHashMap.get(s).getLevel() <=
                                Operators.operatorHashMap.get(flag.peek()).getLevel()) {
                            calcExp(val, flag);
                        }
                        flag.push(s);
                        break;
                }
            }
        }
        while (!"@".equals(flag.peek())) {
            calcExp(val, flag);
        }
        if (val.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return val.pop();
    }

    /**
     * 用双栈计算表达式结果
     *
     * @param val   存储数值的栈
     * @param flag: 存储操作符的栈
     */
    private void calcExp(Stack<BigDecimal> val, Stack<String> flag) {
        BigDecimal b;
        BigDecimal a;
        BigDecimal c;
        String f = flag.pop();
        Operators.Operator op = Operators.operatorHashMap.get(f);
        switch (op.getAry()) {
            case 2:
                b = val.pop();
                try {
                    a = val.pop();
                } catch (EmptyStackException e) {
                    a = BigDecimal.ZERO;
                }
                c = op.calc(a, b);
                val.push(c);
                break;
            case 1:
                a = val.pop();
                c = op.calc(a);
                val.push(c);
                break;
            default:
                break;
        }
    }
}
