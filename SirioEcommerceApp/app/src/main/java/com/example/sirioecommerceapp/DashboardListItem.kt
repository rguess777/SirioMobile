package com.example.sirioecommerceapp

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sirioecommerceapp.models.Product
import com.example.sirioecommerceapp.utils.LoaderGlide
import kotlinx.android.synthetic.main.item_dashboard_layout.view.*
import android.annotation.SuppressLint
import android.view.ViewGroup
import android.view.LayoutInflater


open class DashboardListItem(
    private val context: Context,
    private var list: ArrayList<Product>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_dashboard_layout,
                parent,
                false
            )
        )
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            LoaderGlide(context).loadProductPicture(
                model.image,
                holder.itemView.iv_dashboard_item_image
            )
            holder.itemView.tv_dashboard_item_title.text = model.title
            holder.itemView.tv_dashboard_item_price.text = "${model.price}€"
            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    interface OnClickListener {

        fun onClick(position: Int, product: Product){

        }

    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}