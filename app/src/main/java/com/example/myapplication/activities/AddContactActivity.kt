package com.example.myapplication.activities

import android.app.AlertDialog
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.myapplication.models.ContactModel
import com.example.myapplication.database.DatabaseHandler
import com.example.myapplication.MainActivity
import com.example.myapplication.R

class AddContactActivity : AppCompatActivity() {
    private lateinit var spinnerCountry: Spinner
    private lateinit var edFirstName: EditText
    private lateinit var edLastName: EditText
    private lateinit var edEmail: EditText
    private lateinit var edPhoneNumber: EditText
    private lateinit var spinnerGender: Spinner
    private lateinit var cbxIsInDB: CheckBox
    private lateinit var btnAdd: Button
    private lateinit var btnBack: Button

    private lateinit var databaseHandler: DatabaseHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        initView()

        databaseHandler = DatabaseHandler(this)
        btnAdd.setOnClickListener {addContact()}
        btnBack.setOnClickListener{switchToMainActivity()}
    }

    private fun initView(){
        edFirstName = findViewById(R.id.edFirstName)
        edLastName= findViewById(R.id.edLastName)
        edEmail = findViewById(R.id.edEmail)
        spinnerCountry = findViewById(R.id.spinnerCountry)
        edPhoneNumber = findViewById(R.id.edPhoneNumber)
        spinnerGender = findViewById(R.id.spinnerGender)
        cbxIsInDB = findViewById(R.id.cbxIsInDB)
        btnAdd = findViewById(R.id.btnAdd)
        btnBack = findViewById(R.id.btnBack)

        var countryAdapter:ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,
            R.array.countries,android.R.layout.simple_spinner_item)
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountry.adapter=countryAdapter

        var genderAdapter:ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,
            R.array.genders,android.R.layout.simple_spinner_item)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGender.adapter=genderAdapter
    }

    //creates a map of country codes from both arrays
    private fun getCountryCodes(): Map<String, String> {
        val res: Resources = resources
        val countryKeys = res.getStringArray(R.array.countries)
        val countryValues = res.getStringArray(R.array.country_codes)
        return countryKeys.zip(countryValues).toMap<String, String>()
    }

    private fun addContact(){
        val firstName = edFirstName.text.toString()
        val lastName = edLastName.text.toString()
        val email = edEmail.text.toString()
        val phoneNumber = edPhoneNumber.text.toString()
        val gender = spinnerGender.selectedItem.toString()
        val country = spinnerCountry.selectedItem.toString()
        val isInDB = cbxIsInDB.isChecked
        val countryCodes = getCountryCodes()



        if(firstName.isEmpty()||lastName.isEmpty()||email.isEmpty()||phoneNumber.isEmpty()||gender.isEmpty()||country.isEmpty()){
            Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show()
        }else{


            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmation")
            builder.setMessage("Are you sure you want to create this contact?")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            builder.setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                val contact = ContactModel(firstName,lastName,countryCodes[country]+phoneNumber.substring(1),country,email,gender,isInDB)
                val status = databaseHandler.insertContact(contact)
                if(status>-1){
                    Toast.makeText(this,"Successfully added contact",Toast.LENGTH_SHORT).show()
                    switchToMainActivity()
                }else{
                    Toast.makeText(this,"Failed to save contact",Toast.LENGTH_SHORT).show()
                }
            }

            builder.setNegativeButton("No"
            ) { dialog, _ -> dialog.dismiss() }
            val alert: AlertDialog = builder.create()
            alert.show()

        }
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