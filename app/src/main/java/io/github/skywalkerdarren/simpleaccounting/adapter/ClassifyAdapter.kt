package io.github.skywalkerdarren.simpleaccounting.adapter

import android.animation.Animator
import android.content.Context
import android.view.animation.AccelerateDecelerateInterpolator
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.adapter.diff.TypeStatsDiff
import io.github.skywalkerdarren.simpleaccounting.base.BaseDataBindingAdapter
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemClassifyBinding
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository.Companion.getInstance
import io.github.skywalkerdarren.simpleaccounting.model.datasource.TypeDataSource.LoadTypeCallBack
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors
import java.math.BigDecimal

/**
 * 分类页面的统计数据适配器
 *
 * @author darren
 * @date 2018/3/25
 */
class ClassifyAdapter(context: Context) : BaseDataBindingAdapter<TypeStats, ItemClassifyBinding>(R.layout.item_classify, null) {
    private var mSum = BigDecimal.ZERO
    private val mRepository: AppRepository? = getInstance(AppExecutors(), context)
    fun setNewList(data: List<TypeStats>?) {
        setNewDiffData(TypeStatsDiff(data))
        mSum = BigDecimal.ZERO
        if (data == null) {
            return
        }
        for ((_, balance) in data) {
            mSum = mSum.add(balance)
        }
    }

    override fun startAnim(anim: Animator, index: Int) {
        anim.duration = 300
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.start()
    }

    override fun convert(binding: ItemClassifyBinding, item: TypeStats) {
        binding.stats = item
        binding.sum = mSum
        mRepository?.getType(item.typeId, object : LoadTypeCallBack {
            override fun onTypeLoaded(type: Type?) {
                binding.type = type
            }
        })
    }

}