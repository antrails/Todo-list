package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class MissionAdapter(private val missionList: MutableList<Mission>,private val onItemClick:(Mission,Int)-> Unit): RecyclerView.Adapter<MissionAdapter.MissionViewHolder>()
{
    inner class MissionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textViewTitle = itemView.findViewById<TextView>(R.id.textView_title)
        val textViewContext = itemView.findViewById<TextView>(R.id.textView_context)
        val textViewDate = itemView.findViewById<TextView>(R.id.textView_date)
        val textViewTime = itemView.findViewById<TextView>(R.id.textView_time)
        val textViewLocation = itemView.findViewById<TextView>(R.id.textView_locate)

        val textViewIsDone = itemView.findViewById<TextView>(R.id.textView_isdone)

        fun bind(mission: Mission,position: Int){
            itemView.setOnClickListener {
                onItemClick(mission,position)
            }
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MissionAdapter.MissionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_mission,parent,false)
        return MissionViewHolder(view)
    }

    override fun onBindViewHolder(holder: MissionAdapter.MissionViewHolder, position: Int) {
        val mission = missionList[position]
        holder.textViewTitle.text = mission.title
        holder.textViewContext.text = mission.context
        holder.textViewDate.text = mission.date
        holder.textViewTime.text = mission.time
        holder.textViewLocation.text = "地點:${mission.location}"
        holder.textViewIsDone.text = if (mission.isDone) "任務狀態 : 已完成" else "任務狀態 :尚未完成"
        holder.bind(mission,position)
    }

    override fun getItemCount(): Int {
        return missionList.size
    }

    fun addMission(mission: Mission) {
        missionList.add(mission)
        notifyItemInserted(missionList.size-1)
    }


}


