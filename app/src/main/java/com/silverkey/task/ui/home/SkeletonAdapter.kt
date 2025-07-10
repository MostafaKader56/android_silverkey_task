package com.silverkey.task.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.silverkey.task.databinding.ItemArticleSkeletonBinding

class SkeletonAdapter(
    private val placeholderCount: Int = 6
) : RecyclerView.Adapter<SkeletonAdapter.SkeletonVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkeletonVH {
        val view = ItemArticleSkeletonBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SkeletonVH(view)
    }

    override fun onBindViewHolder(holder: SkeletonVH, position: Int) {}

    override fun getItemCount(): Int = placeholderCount

    class SkeletonVH(itemView: ItemArticleSkeletonBinding) : RecyclerView.ViewHolder(itemView.root)
}
