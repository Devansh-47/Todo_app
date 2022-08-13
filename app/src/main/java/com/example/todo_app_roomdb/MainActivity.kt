package com.example.todo_app_roomdb

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.todo_layouut.view.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var list=ArrayList<todomodel>()
    var ad:rvadapter=rvadapter(list,this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(topAppBarmain)

        val db=DBHelper(this,null)
        val cursor=db.getdata()

        if (cursor != null) {
            while (cursor.moveToNext()){
                Log.d("AAA","cursor=="+cursor.getString(1)+"    "+cursor.getString(2)+"    "+cursor.getString(3)+"    "+cursor.getString(4)+"    "+cursor.getString(5))
                val obj=todomodel(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5))
                list.add(obj)
            }
        }

        ad=rvadapter(list,this)
        rv.adapter=ad
        rv.layoutManager=LinearLayoutManager(this)
        initswipe()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.newtask -> {
                // Handle search icon press
                startActivity(Intent(this,newTask::class.java))

                true
            }
            R.id.dunno -> {
                // Handle more item (inside overflow menu) press
                true
            }
            else -> false
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater=menuInflater
        inflater.inflate(R.menu.top_app_bar,menu)
        val manager=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchitem=menu?.findItem(R.id.search)
        val searchvieww=searchitem?.actionView as SearchView

        searchvieww.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchvieww.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchvieww.clearFocus()
                searchvieww.setQuery("",false)
                searchitem.collapseActionView()
                Toast.makeText(this@MainActivity,"queryy==${query}",Toast.LENGTH_SHORT).show()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("AAA","juguguu")

                if (newText != null) {
                    displaytodo(newText)
                }

                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    fun initswipe(){
        val simpleitemtouchcallback=object: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }


            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if(actionState==ItemTouchHelper.ACTION_STATE_SWIPE){
                     val itemview=viewHolder.itemView
                    val paint=Paint()
                    val icon:Bitmap

                    if(dX>0){
                        icon= ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_baseline_check_24)?.toBitmap()!!
                        paint.color=Color.parseColor("#388E3C")

                        c.drawRect(itemview.left.toFloat(),itemview.top.toFloat(),itemview.left.toFloat()+dX,itemview.bottom.toFloat(),paint)
                        c.drawBitmap(icon,itemview.left.toFloat()+(icon.width.toFloat())/4,
                        itemview.top.toFloat()+(itemview.bottom.toFloat()-itemview.top.toFloat()-icon.height.toFloat())/2,paint)
                    }else {
                        icon= ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_baseline_dangerous_24)?.toBitmap()!!
                        paint.color=Color.parseColor("#FFCC0000")

                        c.drawRect(itemview.right.toFloat()+dX,itemview.top.toFloat(),itemview.right.toFloat(),itemview.bottom.toFloat(),paint)
                        c.drawBitmap(icon,itemview.right.toFloat()-((icon.width.toFloat())*5)/4,
                            itemview.top.toFloat()+(itemview.bottom.toFloat()-itemview.top.toFloat()-icon.height.toFloat())/2,paint)

                    }
                    viewHolder.itemView.translationX=dX

                }else{
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }

            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val positin=viewHolder.adapterPosition
                val db=DBHelper(this@MainActivity,null)

                if(direction==ItemTouchHelper.LEFT){
                    db.deleteuser(list[positin].title)
                    list.removeAt(positin)
                    ad.notifyDataSetChanged()
                }else if(direction==ItemTouchHelper.RIGHT){
                  //  db.deleteuser(list[positin].title)
                db.updatefinishstatus(list[positin].title)
                    Log.d("AAA","check titlee=="+list[positin].title)
                    if(viewHolder.itemView.todo_layout_task_title.text.equals(list[positin].title)){
                        viewHolder.itemView.invisiblebtn.performClick()
                    }

                    ad.notifyDataSetChanged()

                }

            }

        }

        val itemTouchHelper=ItemTouchHelper(simpleitemtouchcallback)
        itemTouchHelper.attachToRecyclerView(rv)

    }

fun displaytodo(newtext:String=""){
    Log.d("AAA","=-=-intododis.."+newtext)
    list.clear()
    val db=DBHelper(this@MainActivity,null)
    val cursor: Cursor? =db.getdata()
    if (cursor != null) {
        while (cursor.moveToNext()){
            Log.d("QQQ",""+cursor.getString(1))
            if(cursor.getString(1).contains(newtext)){
                list.add(todomodel(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)))
            }
        }
        ad.notifyDataSetChanged()
    }
}

}