package io.github.skywalkerdarren.simpleaccounting.util.calculate

import java.math.BigDecimal
import java.util.*

/**
 * 运算符类
 * 设定各种运算符，大数或长整数的运算
 *
 * @author darren
 * @date 2018/3/10
 */
internal object Operators {
    /**
     * 存放运算符的哈希表，通过运算符字符串获取Operator运算符
     *
     * @see Operator
     */
    @JvmField
    var operatorHashMap = HashMap<String, Operator>(32)

    // 运算符初始化
    init { // 中止符
        operatorHashMap["@"] = object : Operator {
            override val ary = 0

            override val level = -1

            override val name: String? = null

            override fun calc(vararg args: BigDecimal): BigDecimal? = null
        }
        operatorHashMap["+"] = object : Operator {
            override val ary = 2

            override val level = 1

            override val name = "+"

            override fun calc(vararg args: BigDecimal) = args[0].add(args[1])
        }
        operatorHashMap["-"] = object : Operator {
            override val ary = 2

            override val level = 1

            override val name = "-"

            override fun calc(vararg args: BigDecimal) = args[0].subtract(args[1])
        }
        operatorHashMap["*"] = object : Operator {
            override val ary = 2

            override val level = 2

            override val name = "*"

            override fun calc(vararg args: BigDecimal) = args[0].multiply(args[1])
        }
        operatorHashMap["/"] = object : Operator {
            override val ary = 2

            override val level = 2

            override val name = "/"

            override fun calc(vararg args: BigDecimal) = args[0].divide(args[1], 32, BigDecimal.ROUND_HALF_UP)
        }
    }
}