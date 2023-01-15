package com.example.myapplication.activities

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.myapplication.models.ContactModel
import com.example.myapplication.database.DatabaseHandler
import com.example.myapplication.MainActivity
import com.example.myapplication.R

class EditContactActivity : AppCompatActivity() {
    private lateinit var edEditFirstName: EditText
    private lateinit var edEditLastName: EditText
    private lateinit var spinnerEditCountry: Spinner
    private lateinit var edEditEmail: EditText
    private lateinit var edEditPhoneNumber: EditText
    private lateinit var spinnerEditGender: Spinner
    private lateinit var cbxEditIsInDB: CheckBox
    private lateinit var btnEdit: Button
    private lateinit var btnBack: Button
    private lateinit var databaseHandler: DatabaseHandler

    private var countryPosition: Int = 0
    private var genderPosition: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)
        initView()

        databaseHandler = DatabaseHandler(this)
        var intent = intent
        var firstName = intent.getStringExtra("firstName")
        var lastName = intent.getStringExtra("lastName")
        var phoneNumber = intent.getStringExtra("phoneNumber")
        var email = intent.getStringExtra("email")
        var isInDB = intent.getBooleanExtra("isInDB",false)

        edEditFirstName.setText(firstName)
        edEditLastName.setText(lastName)
        edEditPhoneNumber.setText(phoneNumber)
        edEditEmail.setText(email)
        cbxEditIsInDB.isChecked=isInDB

        btnBack.setOnClickListener{switchToMainActivity()}
        btnEdit.setOnClickListener{editContact()}

    }

    private fun initView(){
        edEditFirstName = findViewById(R.id.edEditFirstName)
        edEditLastName= findViewById(R.id.edEditLastName)
        edEditEmail = findViewById(R.id.edEditEmail)
        spinnerEditCountry = findViewById(R.id.spinnerEditCountry)
        edEditPhoneNumber = findViewById(R.id.edEditPhoneNumber)
        spinnerEditGender = findViewById(R.id.spinnerEditGender)
        cbxEditIsInDB = findViewById(R.id.cbxEditIsInDB)
        btnEdit = findViewById(R.id.btnEdit)
        btnBack = findViewById(R.id.btnBack)

        var countryAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,
            R.array.countries,android.R.layout.simple_spinner_item)
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEditCountry.adapter=countryAdapter
        countryPosition = countryAdapter.getPosition(intent.getStringExtra("country"))
        spinnerEditCountry.setSelection(countryPosition)


        var genderAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,
            R.array.genders,android.R.layout.simple_spinner_item)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEditGender.adapter=genderAdapter
        genderPosition = genderAdapter.getPosition(intent.getStringExtra("gender"))
        spinnerEditGender.setSelection(genderPosition)
    }

    private fun switchToMainActivity(){
        startActivity(
            Intent(
                this,
                MainActivity::class.java
            )
        )
    }

    private fun editContact(){
        val firstName = edEditFirstName.text.toString()
        val lastName = edEditLastName.text.toString()
        val email = edEditEmail.text.toString()
        val phoneNumber = edEditPhoneNumber.text.toString()
        val gender = spinnerEditGender.selectedItem.toString()
        val country = spinnerEditCountry.selectedItem.toString()
        val isInDB = cbxEditIsInDB.isChecked
        val countryCodes = getCountryCodes()
        var originalPhoneNumber = intent.getStringExtra("phoneNumber")

        if(firstName.isEmpty()||lastName.isEmpty()||email.isEmpty()||phoneNumber.isEmpty()||gender.isEmpty()||country.isEmpty()){
            Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show()
        }else{
            val contact = ContactModel(firstName,lastName,countryCodes[country]+phoneNumber.substring(4),country,email,gender,isInDB)
            val status = originalPhoneNumber?.let { databaseHandler.updateContact(it,contact) }
            if (status != null) {
                if(status>-1){
                    Toast.makeText(this,"Successfully updated contact",Toast.LENGTH_SHORT).show()
                    switchToMainActivity()
                }else{
                    Toast.makeText(this,"Failed to update contact",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getCountryCodes(): Map<String, String> {
        val res: Resources = resources
        val countryKeys = res.getStringArray(R.array.countries)
        val countryValues = res.getStringArray(R.array.country_codes)
        return countryKeys.zip(countryValues).toMap<String, String>()
    }
}