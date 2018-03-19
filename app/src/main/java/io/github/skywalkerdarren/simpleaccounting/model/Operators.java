package io.github.skywalkerdarren.simpleaccounting.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.EmptyStackException;
import java.util.HashMap;

/**
 * 运算符类
 * 设定各种运算符，大数或长整数的运算
 *
 * @author darren
 * @date 2018/3/10
 */
public class Operators {
    private final static MathContext MC = new MathContext(64, RoundingMode.HALF_UP);

    /**
     * 存放运算符的哈希表，通过运算符字符串获取Operator运算符
     *
     * @see Operator
     */
    static HashMap<String, Operator> operatorHashMap = new HashMap<>(32);

    // 运算符初始化
    static {
        // 中止符
        operatorHashMap.put("@", new Operator() {
            @Override
            public int getAry() {
                return 0;
            }

            @Override
            public int getLevel() {
                return -1;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public BigDecimal calc(BigDecimal... args) {
                return null;
            }
        });

        operatorHashMap.put("+", new Operator() {
            @Override
            public int getAry() {
                return 2;
            }

            @Override
            public int getLevel() {
                return 1;
            }

            @Override
            public String getName() {
                return "+";
            }

            @Override
            public BigDecimal calc(BigDecimal... args) {
                return args[0].add(args[1]);
            }
        });

        operatorHashMap.put("-", new Operator() {
            @Override
            public int getAry() {
                return 2;
            }

            @Override
            public int getLevel() {
                return 1;
            }

            @Override
            public String getName() {
                return "-";
            }

            @Override
            public BigDecimal calc(BigDecimal... args) {
                return args[0].subtract(args[1]);
            }
        });

        operatorHashMap.put("*", new Operator() {
            @Override
            public int getAry() {
                return 2;
            }

            @Override
            public int getLevel() {
                return 2;
            }

            @Override
            public String getName() {
                return "*";
            }

            @Override
            public BigDecimal calc(BigDecimal... args) {
                return args[0].multiply(args[1]);
            }
        });

        operatorHashMap.put("/", new Operator() {
            @Override
            public int getAry() {
                return 2;
            }

            @Override
            public int getLevel() {
                return 2;
            }

            @Override
            public String getName() {
                return "/";
            }

            @Override
            public BigDecimal calc(BigDecimal... args) {
                return args[0].divide(args[1], 32, BigDecimal.ROUND_HALF_UP);
            }
        });


    }

    /**
     * 运算符抽象类
     * 定义运算符目数，优先级，名称，大数运算方法，整数运算方法
     */
    public interface Operator {
        /**
         * 获得运算符目数
         *
         * @return 运算符目数
         */
        int getAry();

        /**
         * 获得优先级
         *
         * @return 运算符优先级
         */
        int getLevel();

        /**
         * 获得运算符名称
         *
         * @return 运算符名称
         */
        String getName();

        /**
         * 运算符大数运算方法
         *
         * @param args 运算参数
         * @return 运算结果
         * @throws EmptyStackException 无法计算
         */
        BigDecimal calc(BigDecimal... args) throws EmptyStackException;

    }
}


