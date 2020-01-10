package io.github.skywalkerdarren.simpleaccounting.util.calculate

import java.math.BigDecimal
import java.util.*

/**
 * 运算符抽象类
 * 定义运算符目数，优先级，名称，大数运算方法，整数运算方法
 */
interface Operator {
    /**
     * 获得运算符目数
     *
     * @return 运算符目数
     */
    val ary: Int

    /**
     * 获得优先级
     *
     * @return 运算符优先级
     */
    val level: Int

    /**
     * 获得运算符名称
     *
     * @return 运算符名称
     */
    val name: String?

    /**
     * 运算符大数运算方法
     *
     * @param args 运算参数
     * @return 运算结果
     * @throws EmptyStackException 无法计算
     */
    @Throws(EmptyStackException::class)
    fun calc(vararg args: BigDecimal): BigDecimal?
}