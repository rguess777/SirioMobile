package com.example.sirioecommerceapp.ui.act

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.example.sirioecommerceapp.R
import com.example.sirioecommerceapp.firestore.FirestoreClass
import com.example.sirioecommerceapp.models.Address
import com.example.sirioecommerceapp.utils.Constants
import kotlinx.android.synthetic.main.activity_add_edit_address.*
import android.widget.*
import kotlinx.android.synthetic.main.activity_add_edit_address.btn_back
import kotlinx.android.synthetic.main.activity_add_edit_address.tv_title
import kotlinx.android.synthetic.main.activity_address_list.*

class AddEditAddressActivity : BasicActivity() {
    private var mAddressDetails: Address? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_address)
        supportActionBar?.hide()


        if (intent.hasExtra(Constants.EXTRA_ADDRESS_DETAILS)) {
            mAddressDetails =
                intent.getParcelableExtra(Constants.EXTRA_ADDRESS_DETAILS)!!
        }
        if (mAddressDetails != null) {
            if (mAddressDetails!!.id.isNotEmpty()) {

                tv_title.text = resources.getString(R.string.title_edit_address)
                btn_submit_address.text = resources.getString(R.string.btn_update)

                et_full_name.setText(mAddressDetails?.name)
                et_phone_number.setText(mAddressDetails?.mobileNumber)
                et_address.setText(mAddressDetails?.address)
                et_zip_code.setText(mAddressDetails?.zipCode)
                et_additional_note.setText(mAddressDetails?.additionalNote)


            }
        }

        btn_submit_address.setOnClickListener {
            saveAddressToFirestore()
        }
        btn_back.setOnClickListener {
            finish()
        }
    }


    private fun validateData(): Boolean {
        return when {

            TextUtils.isEmpty(et_full_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_please_enter_full_name),
                    true
                )
                false
            }

            TextUtils.isEmpty(et_phone_number.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_please_enter_phone_number),
                    true
                )
                false
            }

            TextUtils.isEmpty(et_address.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_address), true)
                false
            }

            TextUtils.isEmpty(et_zip_code.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_zip_code), true)
                false
            }

            else -> {
                true
            }
        }
    }
    private fun saveAddressToFirestore() {
        val fullName: String = et_full_name.text.toString().trim { it <= ' ' }
        val phoneNumber: String = et_phone_number.text.toString().trim { it <= ' ' }
        val address: String = et_address.text.toString().trim { it <= ' ' }
        val zipCode: String = et_zip_code.text.toString().trim { it <= ' ' }
        val additionalNote: String = et_additional_note.text.toString().trim { it <= ' ' }

        if (validateData()) {
            showProgressDialog(resources.getString(R.string.please_wait))


            val addressModel = Address(
                FirestoreClass().getCurrentUserID(),
                fullName,
                phoneNumber,
                address,
                zipCode,
                additionalNote
            )
            if (mAddressDetails != null && mAddressDetails!!.id.isNotEmpty()) {
                FirestoreClass().updateAddress(
                    this@AddEditAddressActivity,
                    addressModel,
                    mAddressDetails!!.id
                )
            } else {
                FirestoreClass().addAddress(this@AddEditAddressActivity, addressModel)
            }
        }


    }
    fun addUpdateAddressSuccess() {

        hideProgressDialog()

        val notifySuccesMessage: String = if(mAddressDetails != null && mAddressDetails!!.id.isNotEmpty()){
            resources.getString(R.string.err_your_address_updated_successfully)
        }else{
            resources.getString(R.string.err_your_address_added_successfully)
        }

        Toast.makeText(
            this@AddEditAddressActivity,
            notifySuccesMessage,
            Toast.LENGTH_SHORT
        ).show()

        setResult(RESULT_OK)
        finish()
    }
}