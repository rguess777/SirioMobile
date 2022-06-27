package com.example.sirioecommerceapp.ui.fragment

import android.app.Dialog
import androidx.fragment.app.Fragment
import com.example.sirioecommerceapp.R
import kotlinx.android.synthetic.main.dialog_progress.*

open class BasicFragment : Fragment() {


    private lateinit var mProgressDialog: Dialog

    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(requireActivity())

        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.progress_text.text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()
    }


    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

}