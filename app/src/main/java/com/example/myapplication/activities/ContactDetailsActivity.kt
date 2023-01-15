package com.example.myapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.MainActivity
import com.example.myapplication.R

class ContactDetailsActivity : AppCompatActivity() {
    private lateinit var tvName: TextView
    private lateinit var tvPhoneNumber: TextView
    private lateinit var tvCountry: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvGender: TextView
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)
        initView()

        var intent = intent
        var name = intent.getStringExtra("name")
        var phoneNumber = intent.getStringExtra("phoneNumber")
        var country = intent.getStringExtra("country")
        var email = intent.getStringExtra("email")
        var gender = intent.getStringExtra("gender")

        tvName.text=name
        tvPhoneNumber.text=phoneNumber
        tvCountry.text=country
        tvEmail.text=email
        tvGender.text=gender
        btnBack.setOnClickListener{switchToMainActivity()}
    }

    private fun initView(){
        tvName=findViewById(R.id.name)
        tvPhoneNumber=findViewById(R.id.phoneNumber)
        tvCountry=findViewById(R.id.country)
        tvEmail=findViewById(R.id.email)
        tvGender=findViewById(R.id.gender)
        btnBack=findViewById(R.id.btnBackToMain)
    }

    private fun switchToMainActivity(){
        startActivity(
            Intent(
                this,
                MainActivity::class.java
            )
        )
    }
}