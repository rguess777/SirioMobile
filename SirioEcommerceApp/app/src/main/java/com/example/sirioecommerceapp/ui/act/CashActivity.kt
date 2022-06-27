package com.example.sirioecommerceapp.ui.act

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.sirioecommerceapp.R
import com.example.sirioecommerceapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cash.*
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class CashActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash)

        supportActionBar?.hide()

        btn_payment.setOnClickListener{
            if(validateCardDetails()){
                val intent = Intent(this@CashActivity, DashboardActivity::class.java)
                startActivity(intent)
                Toast.makeText(this@CashActivity, "Your order placed successfully.", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        btn_back.setOnClickListener {
            onBackPressed()
        }
    }
    private fun validateCardDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_name_card.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_card_name), true)
                false
            }

            TextUtils.isEmpty(et_card_number.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_card_number), true)
                false
            }

            TextUtils.isEmpty(et_date_exp.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_date_exp), true)
                false
            }

            TextUtils.isEmpty(et_cvv.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_cvv), true)
                false
            }

            else -> {
                true
            }
        }
    }
}