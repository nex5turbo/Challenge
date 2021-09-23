package com.young.challenge.adapter

import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.young.challenge.databinding.ListDetailItemBinding
import com.young.challenge.room.entity.ChallengeItem
import com.young.challenge.room.entity.ChallengeList
import com.young.challenge.utils.Constants.APP_NAME
import com.young.challenge.utils.Display.deviceWidth
import java.io.File

class ListDetailRecyclerAdapter(private val listItem: ChallengeList): RecyclerView.Adapter<ListDetailRecyclerAdapter.MyViewHolder>() {
    private var items: List<ChallengeItem> = listOf()

    lateinit var binding: ListDetailItemBinding
    inner class MyViewHolder(val binding: ListDetailItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = ListDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.data = items[position]
        val width = (deviceWidth*0.33).toInt()
        setSize(holder.binding.imageView.layoutParams, width, width)

        val dirName = if (listItem.isHideOn) ".${listItem.challengeName}" else listItem.challengeName
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath + File.separator +
                APP_NAME + File.separator + dirName + File.separator + items[position].imageName

        Glide.with(holder.binding.imageView.context)
            .load(path)
            .centerCrop()
            .into(holder.binding.imageView)
    }

    override fun getItemCount(): Int = items.size

    fun setData(data: List<ChallengeItem>) {
        items = data
        notifyDataSetChanged()
    }

    private fun setSize(lp: ViewGroup.LayoutParams, width: Int, height: Int) {
        lp.width = width
        lp.height = height
    }
}