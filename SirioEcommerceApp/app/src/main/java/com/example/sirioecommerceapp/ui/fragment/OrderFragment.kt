package com.example.sirioecommerceapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sirioecommerceapp.OrderList
import com.example.sirioecommerceapp.R
import com.example.sirioecommerceapp.firestore.FirestoreClass
import com.example.sirioecommerceapp.models.Order
import kotlinx.android.synthetic.main.fragment_order.*

class OrderFragment : BasicFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_order, container, false)
        return root
    }
    override fun onResume() {
        super.onResume()

        getMyOrdersList()
    }
    private fun getMyOrdersList() {
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getMyOrdersList(this@OrderFragment)
    }
    fun populateOrdersListInUI(ordersList: ArrayList<Order>) {

        hideProgressDialog()
        if (ordersList.size > 0) {

            rv_my_order_items.visibility = View.VISIBLE
            tv_no_orders_found.visibility = View.GONE

            rv_my_order_items.layoutManager = LinearLayoutManager(activity)
            rv_my_order_items.setHasFixedSize(true)

            val myOrdersAdapter = OrderList(requireActivity(), ordersList)
            rv_my_order_items.adapter = myOrdersAdapter
        } else {
            rv_my_order_items.visibility = View.GONE
            tv_no_orders_found.visibility = View.VISIBLE
        }
    }
}