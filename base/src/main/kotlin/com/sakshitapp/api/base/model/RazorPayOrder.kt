package com.sakshitapp.api.base.model

data class RazorPayOrder(
    val razorpay_payment_id: String,
    val razorpay_order_id: String,
    val razorpay_signature: String
)
