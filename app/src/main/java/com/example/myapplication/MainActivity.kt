package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.activities.AddContactActivity
import com.example.myapplication.adapters.ContactAdapter
import com.example.myapplication.database.DatabaseHandler

class MainActivity : AppCompatActivity() {

    private lateinit var btnNewContact: Button
    private lateinit var recyclerView: RecyclerView
    private var adapter: ContactAdapter? = null
    private lateinit var databaseHandler: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        databaseHandler = DatabaseHandler(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initRecyclerView()
        getContacts()

        adapter?.setOnClickDeleteItem {
            deleteContact(it.phoneNumber)
        }

    }

    private fun initRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter= ContactAdapter()
        recyclerView.adapter=adapter
    }

    private fun initView(){
        btnNewContact = findViewById(R.id.btnNewContact)
        recyclerView= findViewById(R.id.recyclerView)
        btnNewContact.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    AddContactActivity::class.java
                )
            )
        }
    }

    private fun getContacts(){
        var contactList = databaseHandler.getAllContacts()
        adapter?.addItems(contactList)
    }

    private fun deleteContact(phoneNumber: String){
        var builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete this contact? $phoneNumber")
        builder.setCancelable(true)

        builder.setPositiveButton("Yes"){dialog,_->
            databaseHandler.deleteContact(phoneNumber)
            getContacts()
            dialog.dismiss()
        }

        builder.setNegativeButton("No"){dialog,_->
            dialog.dismiss()
        }

        var alert = builder.create()
        alert.show()
    }
}