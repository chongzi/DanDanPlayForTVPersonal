package com.dandanplay.tv.ui.adapter

import androidx.recyclerview.widget.RecyclerView

interface OnItemClickListener {
    fun onClick(holder: RecyclerView.ViewHolder, item: Any, position: Int)
}