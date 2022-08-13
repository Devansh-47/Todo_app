package com.example.todo_app_roomdb

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Color.parseColor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.database.getStringOrNull
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app_roomdb.rvadapter.obj.arrs
import com.example.todo_app_roomdb.rvadapter.obj.count
import com.example.todo_app_roomdb.rvadapter.obj.sizeofcolor
import kotlinx.android.synthetic.main.activity_new_task.view.*
import kotlinx.android.synthetic.main.todo_layouut.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class rvadapter(val list:ArrayList<todomodel>,var context:Context):RecyclerView.Adapter<rvadapter.viewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): rvadapter.viewholder {
       return viewholder(LayoutInflater.from(parent.context).inflate(R.layout.todo_layouut,parent,false))
    }

    @SuppressLint("Range")
    override fun onBindViewHolder(holder: rvadapter.viewholder, position: Int) {
      //  holder.bind(list.get(holder.adapterPosition))
        holder.itemView.fl.setBackgroundColor(parseColor(arrs[(++count)% sizeofcolor]))
        holder.itemView.todo_layout_task_date.text=list.get(holder.adapterPosition).date
        holder.itemView.todo_layout_task_time.text=list.get(holder.adapterPosition).time
        holder.itemView.todo_layout_task_title.text=list.get(holder.adapterPosition).title
        holder.itemView.todo_layout_task_descp.text=list.get(holder.adapterPosition).description
        holder.itemView.statusicon.setImageResource(R.drawable.ic_baseline_access_time_filled_24)
        val db=DBHelper(context,null)
        val cursor=db.getdatastatus(list.get(holder.adapterPosition).title)
        if (cursor != null) {
            cursor.moveToFirst()
        }
        if (cursor != null) {
            if(cursor.getString(cursor.getColumnIndex("isFinished"))!= "-1" ){
                holder.itemView.statusicon.setImageResource(R.drawable.ic_baseline_check_box_24)
                Log.d("LKL",""+list.get(holder.adapterPosition).title+"  isfinish=="+cursor.getString(cursor.getColumnIndex("isFinished")))
            }
        }

        holder.itemView.invisiblebtn.setOnClickListener{
            holder.itemView.statusicon.setImageResource(R.drawable.ic_baseline_check_box_24)
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }
    class viewholder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bind(obj:todomodel){
//            val milis:Long=obj.time.toLong()
//            val hrs=(milis/1000)/3600;
//            val min=((milis/1000)%3600)/60


        }
    }

    object obj {
        var count=0
        val arrs= arrayOf("#ABCC0000","#ABCC003A","#ABCC8800","#AB77CC00","#AB00CC85","#AB00C5CC","#AB005CCC","#AB6600CC","#ABB800CC")
        val sizeofcolor=arrs.size
    }
}