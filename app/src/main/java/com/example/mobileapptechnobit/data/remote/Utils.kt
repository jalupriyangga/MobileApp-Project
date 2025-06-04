package com.example.mobileapptechnobit.data.remote

fun formatCurrency(amount: Int?): String {
    return if (amount == null) "Rp0"
    else "Rp" + String.format("%,d", amount).replace(',', '.')
}