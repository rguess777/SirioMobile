package com.example.sirioecommerceapp.ui.act

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sirioecommerceapp.R
import com.example.sirioecommerceapp.firestore.FirestoreClass
import com.example.sirioecommerceapp.models.User
import com.example.sirioecommerceapp.utils.Constants
import com.example.sirioecommerceapp.utils.LoaderGlide
import kotlinx.android.synthetic.main.activity_profil.*
import java.io.IOException

class ProfilActivity : BasicActivity(), View.OnClickListener {

    private lateinit var mUserDetails: User
    private var mSelectedImageFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)



        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        if (mUserDetails.profileCompleted == 0) {
            tv_title.text = resources.getString(R.string.complete_profile)
            et_first_name.isEnabled = false
            et_first_name.setText(mUserDetails.firstName)

            et_last_name.isEnabled = false
            et_last_name.setText(mUserDetails.lastName)

            et_email_p.isEnabled = false
            et_email_p.setText(mUserDetails.email)
        } else {
            setupActionBar()
            tv_title.text = resources.getString(R.string.edit_profile)
            LoaderGlide(this@ProfilActivity).loadUserPicture(mUserDetails.image, iv_user_photo)

            et_first_name.setText(mUserDetails.firstName)
            et_last_name.setText(mUserDetails.lastName)

            et_email_p.isEnabled = false
            et_email_p.setText(mUserDetails.email)

            if (mUserDetails.mobile != 0L) {
                et_mobile_number.setText(mUserDetails.mobile.toString())
            }
            if (mUserDetails.gender == Constants.MALE) {
                rb_male.isChecked = true
            } else {
                rb_female.isChecked = true
            }
        }


        iv_user_photo.setOnClickListener(this@ProfilActivity)
        btn_submit.setOnClickListener(this@ProfilActivity)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.btn_submit -> {


                    if (validateUserProfileDetails()) {
                        showProgressDialog(resources.getString(R.string.please_wait))
                        if (mSelectedImageFileUri != null) {

                            FirestoreClass().uploadImageToCloudStorage(
                                this@ProfilActivity,
                                mSelectedImageFileUri,
                                Constants.PRODUCT_IMAGE
                            )
                        } else {
                            updateUserProfileDetails()
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Constants.showImageChooser(this@ProfilActivity)
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        mSelectedImageFileUri = data.data!!


                        LoaderGlide(this@ProfilActivity).loadUserPicture(
                            mSelectedImageFileUri!!,
                            iv_user_photo
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@ProfilActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    private fun setupActionBar() {


        setSupportActionBar(toolbar_profil_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_profil_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {

            TextUtils.isEmpty(et_mobile_number.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun updateUserProfileDetails() {

        val userHashMap = HashMap<String, Any>()

        val mobileNumber = et_mobile_number.text.toString().trim { it <= ' ' }

        val firstName = et_first_name.text.toString().trim { it <= ' ' }
        if (firstName != mUserDetails.firstName) {
            userHashMap[Constants.FIRST_NAME] = firstName
        }
        val lastName = et_last_name.text.toString().trim { it <= ' ' }
        if (lastName != mUserDetails.lastName) {
            userHashMap[Constants.LAST_NAME] = lastName
        }

        val gender = if (rb_male.isChecked) {
            Constants.MALE
        } else {
            Constants.FEMALE
        }


        if (mobileNumber.isNotEmpty() && mobileNumber != mUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        if (gender.isNotEmpty() && gender != mUserDetails.gender) {
            userHashMap[Constants.GENDER] = gender
        }

        if (mUserDetails.profileCompleted == 0) {
            userHashMap[Constants.COMPLETE_PROFILE] = 1
        }

        FirestoreClass().updateUserProfileData(
            this@ProfilActivity,
            userHashMap
        )
    }

    fun userProfileUpdateSuccess() {

        hideProgressDialog()

        Toast.makeText(
            this@ProfilActivity,
            resources.getString(R.string.profile_update_success),
            Toast.LENGTH_SHORT
        ).show()
        startActivity(Intent(this@ProfilActivity, DashboardActivity::class.java))
        finish()
    }

}
