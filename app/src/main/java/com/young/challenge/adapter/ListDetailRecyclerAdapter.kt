package com.young.challenge.adapter

import android.content.Intent
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.young.challenge.databinding.ListDetailItemBinding
import com.young.challenge.room.entity.ChallengeItem
import com.young.challenge.room.entity.ChallengeList
import com.young.challenge.ui.DiaryActivity
import com.young.challenge.ui.ListDetailActivity
import com.young.challenge.ui.ListDetailViewModel
import com.young.challenge.utils.Constants.APP_NAME
import com.young.challenge.utils.Constants.DIARY_MODIFY_CODE
import com.young.challenge.utils.Display.deviceWidth
import java.io.File

class ListDetailRecyclerAdapter(private val listItem: ChallengeList, val viewModel: ListDetailViewModel, val activity: ListDetailActivity): RecyclerView.Adapter<ListDetailRecyclerAdapter.MyViewHolder>() {
    private var items: List<ChallengeItem> = listOf()

    lateinit var binding: ListDetailItemBinding
    inner class MyViewHolder(val binding: ListDetailItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = ListDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.data = items[position]
//        val iv = holder.binding.imageView
        val width = (deviceWidth*0.3).toInt()
//        setSize(iv.layoutParams, width-16, width-16)
        setSize(holder.itemView.layoutParams, width, width)
        val marginParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        val marginValue = 16
        when {
            position % 3 == 0 -> {
                marginParams.setMargins(marginValue,marginValue/2,0,marginValue/2)
            }
            position % 3 == 1 -> {
                marginParams.setMargins(marginValue,marginValue/2,marginValue,marginValue/2)
            }
            else -> {
                marginParams.setMargins(0,marginValue/2,marginValue,marginValue/2)
            }
        }
        holder.itemView.layoutParams = marginParams

        val dirName = if (listItem.isHideOn) ".${listItem.challengeName}" else listItem.challengeName
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath + File.separator +
                APP_NAME + File.separator + dirName + File.separator + items[position].imageName

        val multiOption = MultiTransformation(CenterCrop(), RoundedCorners(60))
        Glide.with(holder.binding.imageView.context)
            .load(path)
            .apply(RequestOptions.bitmapTransform(multiOption))
            .into(holder.binding.imageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DiaryActivity::class.java)
            intent.putExtra("path", path)
            intent.putExtra("data", items[position])
            intent.putExtra("code", DIARY_MODIFY_CODE)
            activity.startActivityForResult(intent, DIARY_MODIFY_CODE)
        }
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