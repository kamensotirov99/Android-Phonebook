package com.example.myapplication

data class ContactModel(
    val firstName:String = "",
    val lastName:String = "",
    val phoneNumber: String = "",
    val country:String = "",
    val email:String = "",
    val gender:String= "",
    val isInDB:Boolean = false){
}
