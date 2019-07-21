package com.leverages.careerticketcafe.controllers

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leverages.careerticketcafe.R
import kotlinx.android.synthetic.main.activity_menu_list.*

class MenuListActivity : AppCompatActivity() , MenuListViewHolder.ItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_list)

        val texts: Array<TextView?> = arrayOfNulls(2)

        val hoges = listOf("hoge hoge piyo","てすとだよー","三つ目だよ")

        menuListRecyclerView.adapter = MenuListAdapter(this, this, hoges)
        menuListRecyclerView.layoutManager = GridLayoutManager(this, 3,RecyclerView.VERTICAL, false)
    }

    override fun onItemClick(view: View, position: Int) {
//        Toast.makeText(applicationContext, "position $position was tapped", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MenuDetailActivity::class.java)
        startActivity(intent)
    }
}

class MenuListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    val itemTextView: TextView = view.findViewById(R.id.menu_name)
    val itemImageView: ImageView = view.findViewById(R.id.thumbnail)

    init {
        // layoutの初期設定するときはココ
    }
}

class MenuListAdapter
    (private val context: Context,
     private val itemClickListener: MenuListViewHolder.ItemClickListener,
     private val itemList:List<String>)
    : RecyclerView.Adapter<MenuListViewHolder>()
{

    private var mRecyclerView : RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mRecyclerView = null

    }

    override fun onBindViewHolder(holder: MenuListViewHolder, position: Int) {
        holder.let {
            it.itemTextView.text = itemList.get(position)
            it.itemImageView.setImageResource(R.mipmap.ic_launcher)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {

        val layoutInflater = LayoutInflater.from(context)
        val mView = layoutInflater.inflate(R.layout.menu_list_item, parent, false)

        mView.setOnClickListener { view ->
            mRecyclerView?.let {
                itemClickListener.onItemClick(view, it.getChildAdapterPosition(view))
            }
        }

        return MenuListViewHolder(mView)
    }

}