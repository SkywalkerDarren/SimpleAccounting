package io.github.skywalkerdarren.simpleaccounting.adapter

import android.animation.Animator
import android.animation.AnimatorInflater
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.animation.BaseAnimation
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.adapter.diff.DefaultDiff
import io.github.skywalkerdarren.simpleaccounting.base.BaseDataBindingAdapter
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemTypeBinding
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type
import io.github.skywalkerdarren.simpleaccounting.view_model.BillEditViewModel

/**
 * 类型适配器
 *
 * @author darren
 * @date 2018/3/8
 */
class TypeAdapter(private val viewModel: BillEditViewModel, private val typeIv: ImageView)
    : BaseDataBindingAdapter<Type, ItemTypeBinding>(R.layout.item_type, null) {
    @JvmField
    var isOpen = false

    override fun convert(holder: BaseDataBindingHolder<ItemTypeBinding>, item: Type) {
        holder.dataBinding?.typeItem?.alpha = 0f
        holder.dataBinding?.type = item
        holder.dataBinding?.circle?.setOnClickListener { click(item) }
    }

    private fun click(item: Type) {
        isOpen = true
        viewModel.type.value = item
        val appear = AnimatorInflater.loadAnimator(context, R.animator.type_appear)
        appear.setTarget(typeIv)
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
        setDiffCallback(DefaultDiff())
        adapterAnimation = object : BaseAnimation {
            override fun animators(view: View): Array<Animator> {
                if (isOpen) {
                    view.alpha = 1f
                    return arrayOf()
                }
                val animator = AnimatorInflater.loadAnimator(context,
                        R.animator.type_item_appear)
                animator.setTarget(view)
                return arrayOf(animator)
            }
        }
    }
}