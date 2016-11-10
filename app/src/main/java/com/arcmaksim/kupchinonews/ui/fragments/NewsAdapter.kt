package com.arcmaksim.kupchinonews.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.arcmaksim.kupchinonews.NewsItem
import com.arcmaksim.kupchinonews.R
import com.arcmaksim.kupchinonews.commons.tint
import kotlinx.android.synthetic.main.item_news.view.*
import java.util.*

class NewsAdapter(val mNews: ArrayList<NewsItem>, val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        @JvmStatic private val TAG: String = NewsAdapter::class.java.simpleName
    }

    val mExpandableLayoutsStates: BooleanArray by lazy {
        BooleanArray(mNews.size, { i -> if(i == 0) true else false })
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) = (holder as ViewHolder).bindView(position)

    override fun getItemViewType(position: Int) = 0

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_news, parent, false))

    override fun getItemCount() = mNews.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), PopupMenu.OnMenuItemClickListener {

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when(item?.itemId) {
                R.id.item_toggle_content_layout -> toggleExpandableContent(adapterPosition, itemView.expandableContent)
                R.id.item_go_to_site -> {
                    val viewIntent = Intent("android.intent.action.VIEW", Uri.parse(mNews[adapterPosition].mLink))
                    mContext.startActivity(viewIntent)
                    return true
                }
            }
            return false
        }

        fun bindView(position: Int) {
            itemView.newsTitleView.text = mNews[position].mTitle
            itemView.newsDateView.text = mNews[position].mPubDate
            itemView.newsHeader.setOnClickListener { toggleExpandableContent(position, itemView.expandableContent) }

            if(mNews[position].mImage != null) {
                itemView.newsImageView.setImageBitmap(mNews[position].mImage)
            }
            itemView.newsDescriptionView.text = mNews[position].mDescription
            itemView.expandableContent.visibility = if (mExpandableLayoutsStates[position]) View.VISIBLE else View.GONE

            itemView.newsPopupMenuButton.drawable.tint(mContext, R.color.material_color_grey_600)
            itemView.newsPopupMenuButton.setOnClickListener {
                val popupMenu = PopupMenu(mContext, it)
                popupMenu.setOnMenuItemClickListener(this)
                popupMenu.inflate(R.menu.popup_news)
                if(mExpandableLayoutsStates[position]) popupMenu.menu.getItem(0).setTitle(R.string.hide_content_layout_popup)
                popupMenu.show()
            }
        }
    }

    private fun toggleExpandableContent(position: Int, itemView: View) {
        mExpandableLayoutsStates[position] = !mExpandableLayoutsStates[position]
        itemView.visibility = if(mExpandableLayoutsStates[position]) View.VISIBLE else View.GONE
    }

}