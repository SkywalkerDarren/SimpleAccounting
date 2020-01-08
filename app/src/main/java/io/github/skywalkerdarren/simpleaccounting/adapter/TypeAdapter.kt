package io.github.skywalkerdarren.simpleaccounting.adapter

import android.animation.Animator
import android.animation.AnimatorInflater
import android.view.View
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.base.BaseDataBindingAdapter
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentBillEditBinding
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemTypeBinding
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type

/**
 * 类型适配器
 *
 * @author darren
 * @date 2018/3/8
 */
class TypeAdapter(private val mBinding: FragmentBillEditBinding)
    : BaseDataBindingAdapter<Type, ItemTypeBinding>(R.layout.item_type, null) {
    @JvmField
    var isOpen = false

    override fun convert(binding: ItemTypeBinding, item: Type) {
        binding.typeItem.alpha = 0f
        binding.type = item
        binding.circle.setOnClickListener { click(item) }
    }

    private fun click(item: Type) {
        isOpen = true
        mBinding.edit?.setType(item)
        val appear = AnimatorInflater.loadAnimator(mContext, R.animator.type_appear)
        appear.setTarget(mBinding.typeImageView)
        appear.start()
    }

    override fun startAnim(anim: Animator, index: Int) { // 前20个项目需要动画延迟
        val num = 20
        if (index < num) {
            val col = 4
            val delay = index / col + index % col
            anim.startDelay = delay * 75.toLong()
        }
        super.startAnim(anim, index)
    }

    init {
        openLoadAnimation { view: View ->
            if (isOpen) {
                view.alpha = 1f
                return@openLoadAnimation arrayOf<Animator>()
            }
            val animator = AnimatorInflater.loadAnimator(mContext,
                    R.animator.type_item_appear)
            animator.setTarget(view)
            arrayOf(animator)
        }
    }
}