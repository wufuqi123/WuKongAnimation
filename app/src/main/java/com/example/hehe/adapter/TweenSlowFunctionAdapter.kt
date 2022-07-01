package com.example.hehe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.hehe.R
import com.example.hehe.uikt.TweenSlowFunctionActivityKt
import com.wukonganimation.tween.TweenManager

open class TweenSlowFunctionAdapter : RecyclerView.Adapter<TweenSlowFunctionAdapter.NormalHolder> {


    private val mActivity: TweenSlowFunctionActivityKt
    private val mDatas: MutableList<TweenSlowFunctionData>


    constructor(activity: TweenSlowFunctionActivityKt, datas: MutableList<TweenSlowFunctionData>) {
        mActivity = activity
        mDatas = datas
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NormalHolder {
        return TweenSlowFunctionAdapter.NormalHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.item_mian, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NormalHolder, position: Int) {
        holder.mBtn.text = mDatas[position].text
        holder.mBtn.setOnClickListener {
            mActivity.mTween?.remove()
            mActivity.mRunView.x = mActivity.x
            mActivity.mRunView.y = mActivity.y
            mActivity.mTween = TweenManager.builder(mActivity.mRunView)
                .from(mutableMapOf("x" to mActivity.x, "y" to mActivity.y))
                .to(mutableMapOf("x" to mActivity.x + 200, "y" to mActivity.y + 200))
                .time(5000)
                .easing(mDatas[position].easing)
                .start()
        }
    }

    override fun getItemCount() = mDatas.size


    data class TweenSlowFunctionData(
        val text: String,
        val easing: (t: Double) -> Double
    )

    class NormalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mBtn: AppCompatButton

        init {
            mBtn = itemView.findViewById(R.id.item_btn)
        }
    }
}