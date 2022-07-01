package com.example.hehe.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.hehe.R

class MainRecyclerAdapter : RecyclerView.Adapter<MainRecyclerAdapter.NormalHolder> {

    private val mContext: Context
    private val mDatas: MutableList<String>
    private val mClassMap: MutableMap<String, Class<*>>
    private val mClassKtMap: MutableMap<String, Class<*>>

    private var isKtActivity:Boolean

    constructor(
        context: Context,
        datas: MutableList<String>,
        map: MutableMap<String, Class<*>>,
        mapKt: MutableMap<String, Class<*>>,
        isKtActivity:Boolean
    ) {
        mContext = context
        mDatas = datas
        mClassMap = map
        mClassKtMap = mapKt
        this.isKtActivity = isKtActivity
    }

    fun setIsClickKtActivity(isKtActivity:Boolean){
        this.isKtActivity = isKtActivity
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NormalHolder {
        return NormalHolder(
            LayoutInflater.from(mContext).inflate(R.layout.item_mian, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NormalHolder, position: Int) {
        holder.mBtn.text = mDatas[position]
        holder.mBtn.setOnClickListener {
            val classz =
                if (isKtActivity) mClassKtMap[mDatas[position]] else mClassMap[mDatas[position]]
            if (classz != null) {
                mContext.startActivity(Intent(mContext, classz))
            }
        }

    }

    override fun getItemCount(): Int {
        return mDatas.size
    }


    class NormalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mBtn: AppCompatButton
        init {
            mBtn = itemView.findViewById(R.id.item_btn)
        }
    }
}