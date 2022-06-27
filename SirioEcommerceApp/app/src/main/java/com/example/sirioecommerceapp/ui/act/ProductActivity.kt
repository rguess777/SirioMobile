package com.example.sirioecommerceapp.ui.act

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.sirioecommerceapp.R
import com.example.sirioecommerceapp.firestore.FirestoreClass
import com.example.sirioecommerceapp.models.Cart
import com.example.sirioecommerceapp.models.Product
import com.example.sirioecommerceapp.utils.Constants
import com.example.sirioecommerceapp.utils.LoaderGlide
import kotlinx.android.synthetic.main.activity_product.*

class ProductActivity : BasicActivity(), View.OnClickListener {


    private var mProductId: String = ""
    private lateinit var mProductDetails: Product
    private var mProductOwnerId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        supportActionBar?.hide()
        btn_add_to_cart.setOnClickListener(this)
        btn_go_to_cart.setOnClickListener(this)
        btn_back.setOnClickListener(this)

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId =
                intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }
        var productOwnerId: String = ""

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            productOwnerId =
                intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }
        if (FirestoreClass().getCurrentUserID() == productOwnerId) {
            btn_add_to_cart.visibility = View.GONE
            btn_go_to_cart.visibility = View.GONE
        } else {
            btn_add_to_cart.visibility = View.VISIBLE
        }
        getProductDetails()
    }

    private fun getProductDetails() {

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductDetails(this@ProductActivity, mProductId)
    }
    fun productDetailsSuccess(product: Product) {
        mProductDetails = product

        LoaderGlide(this@ProductActivity).loadProductPicture(
            product.image,
            iv_product_detail_image
        )

        tv_product_details_title.text = product.title
        tv_product_details_price.text = "${product.price}€"
        tv_product_details_description.text = product.description
        tv_product_details_available_quantity.text = product.stock_quantity
        if (product.stock_quantity.toInt() == 0) {

            hideProgressDialog()

            btn_add_to_cart.visibility = View.GONE

            val tv_product_details_stock_quantity = findViewById<TextView>(R.id.tv_product_details_quantity)
            tv_product_details_stock_quantity.text =
                resources.getString(R.string.out_of_stock)

            tv_product_details_stock_quantity.setTextColor(
                ContextCompat.getColor(
                    this@ProductActivity,
                    R.color.red
                )
            )
        } else {

            if (FirestoreClass().getCurrentUserID() == product.user_id) {
                hideProgressDialog()
            } else {
                FirestoreClass().checkIfItemExistInCart(this@ProductActivity, mProductId)
            }
        }
    }
    private fun addToCart() {

        val addToCart = Cart(
            FirestoreClass().getCurrentUserID(),
            mProductId,
            mProductOwnerId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().addCartItems(this@ProductActivity, addToCart)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.btn_add_to_cart -> {
                    addToCart()
                }
                R.id.btn_go_to_cart->{
                    startActivity(Intent(this@ProductActivity, CartListActivity::class.java))
                }
                R.id.btn_back -> {
                    onBackPressed()
                }
            }
        }
    }
    fun addToCartSuccess() {
        hideProgressDialog()

        Toast.makeText(
            this@ProductActivity,
            resources.getString(R.string.message_item_added_to_cart),
            Toast.LENGTH_SHORT
        ).show()
        btn_add_to_cart.visibility = View.GONE
        btn_go_to_cart.visibility = View.VISIBLE
    }
    fun productExistsInCart() {

        hideProgressDialog()
        btn_add_to_cart.visibility = View.GONE
        btn_go_to_cart.visibility = View.VISIBLE
    }

}