package com.example.sirioecommerceapp.ui.act

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.sirioecommerceapp.R
import com.example.sirioecommerceapp.firestore.FirestoreClass
import com.example.sirioecommerceapp.models.User
import com.example.sirioecommerceapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BasicActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()


        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        var id_register= findViewById<Button>(R.id.id_register)
        id_register.setOnClickListener{
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }


        val forgot_password = findViewById<TextView>(R.id.forgot_password)
        forgot_password.setOnClickListener(this)

        val btn_login = findViewById<TextView>(R.id.btn_login)
        btn_login.setOnClickListener(this)

        id_register.setOnClickListener(this)

    }
    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.forgot_password -> {

                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)

                }

                R.id.btn_login -> {


                    validateLoginDetails()

                }

                R.id.id_register -> {

                    val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        val et_email = findViewById<EditText>(R.id.et_email)
        val et_password = findViewById<EditText>(R.id.et_password)
        return when {
            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }
    private fun logInRegisteredUser() {
        val et_email = findViewById<EditText>(R.id.et_email)
        val et_password = findViewById<EditText>(R.id.et_password)

        if (validateLoginDetails()) {


            showProgressDialog(resources.getString(R.string.please_wait))

            val email = et_email.text.toString().trim { it <= ' ' }
            val password = et_password.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {


                        FirestoreClass().getUserDetails(this@LoginActivity)
                    } else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    fun userLoggedInSuccess(user: User) {

        hideProgressDialog()


        if (user.profileCompleted == 0) {
            val intent = Intent(this@LoginActivity, ProfilActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else {

            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        }
        finish()
    }
}