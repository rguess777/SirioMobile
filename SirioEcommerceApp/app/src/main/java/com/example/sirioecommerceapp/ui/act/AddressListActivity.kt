package com.example.sirioecommerceapp.ui.act

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sirioecommerceapp.AddressList
import com.example.sirioecommerceapp.R
import com.example.sirioecommerceapp.firestore.FirestoreClass
import com.example.sirioecommerceapp.models.Address
import com.example.sirioecommerceapp.models.User
import com.example.sirioecommerceapp.utils.Constants
import com.example.sirioecommerceapp.utils.SwipeToDelete
import com.example.sirioecommerceapp.utils.SwipeToEdit
import kotlinx.android.synthetic.main.activity_address_list.*

class AddressListActivity : BasicActivity() {
    private var mSelectAddress: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        supportActionBar?.hide()



        if (intent.hasExtra(Constants.EXTRA_SELECT_ADDRESS)) {
            mSelectAddress =
                intent.getBooleanExtra(Constants.EXTRA_SELECT_ADDRESS, false)
        }
        if (mSelectAddress) {
            tv_title.text = resources.getString(R.string.title_select_address)
        }

        tv_add_address.setOnClickListener {
            val intentA = Intent(this@AddressListActivity, AddEditAddressActivity::class.java)
            startActivity(intentA)
        }
        btn_back.setOnClickListener {
            onBackPressed()
        }

        getAddressList()

        //startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)
    }
    @Override
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.ADD_ADDRESS_REQUEST_CODE) {

                getAddressList()
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Request Cancelled", "To add the address.")
        }
    }

    private fun getAddressList() {

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getAddressesList(this@AddressListActivity)
    }

    fun successAddressListFromFirestore(addressList: ArrayList<Address>) {

        hideProgressDialog()

        if (addressList.size > 0) {

            rv_address_list.visibility = View.VISIBLE
            tv_no_address_found.visibility = View.GONE

            rv_address_list.layoutManager = LinearLayoutManager(this@AddressListActivity)
            rv_address_list.setHasFixedSize(true)
            val addressAdapter = AddressList(this@AddressListActivity, addressList, mSelectAddress)
            rv_address_list.adapter = addressAdapter

            if (!mSelectAddress) {
                val editSwipeHandler = object : SwipeToEdit(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                        val adapter = rv_address_list.adapter as AddressList
                        adapter.notifyEditItem(
                            this@AddressListActivity,
                            viewHolder.adapterPosition
                        )
                    }
                }
                val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
                editItemTouchHelper.attachToRecyclerView(rv_address_list)


                val deleteSwipeHandler = object : SwipeToDelete(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                        // Show the progress dialog.
                        showProgressDialog(resources.getString(R.string.please_wait))

                        FirestoreClass().deleteAddress(
                            this@AddressListActivity,
                            addressList[viewHolder.adapterPosition].id
                        )
                    }
                }
                val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
                deleteItemTouchHelper.attachToRecyclerView(rv_address_list)
            }
        } else {
            rv_address_list.visibility = View.GONE
            tv_no_address_found.visibility = View.VISIBLE
        }
    }
    fun deleteAddressSuccess() {

        hideProgressDialog()

        Toast.makeText(
            this@AddressListActivity,
            resources.getString(R.string.err_your_address_deleted_successfully),
            Toast.LENGTH_SHORT
        ).show()

        getAddressList()
    }
}