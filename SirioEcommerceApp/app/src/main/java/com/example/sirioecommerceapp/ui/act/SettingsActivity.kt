package com.example.sirioecommerceapp.ui.act

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.sirioecommerceapp.R
import com.example.sirioecommerceapp.firestore.FirestoreClass
import com.example.sirioecommerceapp.models.User
import com.example.sirioecommerceapp.utils.Constants
import com.example.sirioecommerceapp.utils.LoaderGlide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BasicActivity(),  View.OnClickListener {
    private lateinit var mUserDetails: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.hide()
        btn_logout.setOnClickListener(this@SettingsActivity)
        ll_address.setOnClickListener(this@SettingsActivity)
        btn_back.setOnClickListener(this@SettingsActivity)

    }
    override fun onResume() {
        super.onResume()

        getUserDetails()
    }
    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {


                R.id.ll_address -> {
                    val intent = Intent(this@SettingsActivity, AddressListActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_logout -> {

                    FirebaseAuth.getInstance().signOut()

                    val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                R.id.btn_back ->{
                    onBackPressed()
                }
            }
        }
    }

    private fun getUserDetails() {

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getUserDetails(this@SettingsActivity)
    }
    fun userDetailsSuccess(user: User) {
        hideProgressDialog()
        LoaderGlide(this@SettingsActivity).loadUserPicture(user.image, iv_user_photo)

        tv_name.text = "${user.firstName} ${user.lastName}"
        tv_gender.text = user.gender
        tv_email.text = user.email
        tv_mobile_number.text = "${user.mobile}"
    }

}