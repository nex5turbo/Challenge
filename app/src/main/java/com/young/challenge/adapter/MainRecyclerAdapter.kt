package com.young.challenge.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.young.challenge.ui.ListDetailActivity
import com.young.challenge.R
import com.young.challenge.databinding.MainRecyclerItemBinding
import com.young.challenge.room.entity.ChallengeList
import com.young.challenge.ui.MainViewModel
import com.young.challenge.utils.Constants.APP_NAME
import com.young.challenge.utils.DateUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.File

@Suppress("DEPRECATION")
class MainRecyclerAdapter(val viewModel: MainViewModel): RecyclerView.Adapter<MainRecyclerAdapter.MyViewHolder>() {
    private var items = listOf<ChallengeList>()

    private val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + APP_NAME
    private lateinit var binding: MainRecyclerItemBinding
    inner class MyViewHolder(val binding: MainRecyclerItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = MainRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val b = holder.binding
        val context = holder.itemView.context
        b.data = items[position]

        val isAlarmOn = items[position].isAlarmOn
        val isHideOn = items[position].isHideOn
        val startDate = items[position].startDate
        val endDate = items[position].endDate
        val kind = items[position].kind
        val dDays = DateUtil.dateDifference(startDate, endDate, kind)

        if (kind == 1 && dDays == 0) {
            b.dateTextView.text = "D-DAY"
        } else if (kind == 1 && dDays < 0) {
            b.dateTextView.text = "챌린지 종료"
        } else {
            b.dateTextView.text = "D-${dDays + 1}"
        }

        b.alarmTextView.text = if (isAlarmOn) context.getString(R.string.alarmOnMessage) else context.getString(R.string.alarmOffMessage)
        b.hideTextView.text = if (isHideOn) context.getString(R.string.hideOnMessage) else context.getString(R.string.hideOffMessage)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ListDetailActivity::class.java)
            intent.putExtra("data", items[position])
            context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            CoroutineScope(IO).launch {
                setDirectoryEmpty(items[position].challengeName, items[position].isHideOn)
                viewModel.deleteList(items[position].challengeName)
            }
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int  = items.size

    fun setData(data: List<ChallengeList>) {
        items = data
        notifyDataSetChanged()
    }

    private fun setDirectoryEmpty(name: String, isHideOn: Boolean) {
        val hideName = if (isHideOn) ".$name" else name
        val filePath = path + File.separator + hideName
        val folder = File(filePath)
        val childFileList = folder.listFiles()

        if (folder.exists()) {
            childFileList?.forEach { childFile ->
                childFile.delete()
            }
            folder.delete()
        }
    }
}