package com.example.myapplication.adapters

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.activities.ContactDetailsActivity
import com.example.myapplication.activities.EditContactActivity
import com.example.myapplication.models.ContactModel


class ContactAdapter: RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    private var contactList: ArrayList<ContactModel> = ArrayList()
    private var onClickDeleteItem: ((ContactModel)->Unit)? = null
    private var onClickItem: ((ContactModel)->Unit)? = null

    fun addItems(items: ArrayList<ContactModel>){
        this.contactList=items
        notifyDataSetChanged()
    }

    fun setOnClickDeleteItem(callback:(ContactModel)->Unit){
        this.onClickDeleteItem = callback
    }

    fun setOnClickItem(callback: (ContactModel) -> Unit){
        this.onClickItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ContactViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_contacts,parent,false)
    )

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        if(contact.isInDB){
            holder.itemView.setBackgroundColor(Color.parseColor("#F3DDF9"))
        }
        holder.bindView(contact)
        holder.btnDelete.setOnClickListener {onClickDeleteItem?.invoke(contact)}
        holder.itemView.setOnClickListener{
            var name: String = contact.firstName + " " + contact.lastName
            var phoneNumber: String = contact.phoneNumber
            var country: String = contact.country
            var email: String = contact.email
            var gender: String = contact.gender

            val context=holder.itemView.context
            val intent = Intent( context, ContactDetailsActivity::class.java)
            intent.putExtra("name",name)
            intent.putExtra("phoneNumber",phoneNumber)
            intent.putExtra("country",country)
            intent.putExtra("email",email)
            intent.putExtra("gender",gender)
            context.startActivity(intent)
        }

        holder.btnEdit.setOnClickListener{
            var firstName: String = contact.firstName
            var lastName: String = contact.lastName
            var phoneNumber: String = contact.phoneNumber
            var country: String = contact.country
            var email: String = contact.email
            var gender: String = contact.gender
            var isInDB: Boolean = contact.isInDB

            val context=holder.itemView.context
            val intent = Intent( context, EditContactActivity::class.java)
            intent.putExtra("firstName",firstName)
            intent.putExtra("lastName",lastName)
            intent.putExtra("phoneNumber",phoneNumber)
            intent.putExtra("country",country)
            intent.putExtra("email",email)
            intent.putExtra("gender",gender)
            intent.putExtra("isInDB",isInDB)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }


    class ContactViewHolder(var view: View): RecyclerView.ViewHolder(view){
        private var firstName = view.findViewById<TextView>(R.id.tvFirstName)
        private var lastName = view.findViewById<TextView>(R.id.tvLastName)
        private var phoneNumber = view.findViewById<TextView>(R.id.tvPhoneNumber)

        var btnDelete: Button = view.findViewById(R.id.btnDelete)
        var btnEdit: Button = view.findViewById(R.id.btnEdit)

        fun bindView(contact: ContactModel){
            firstName.text=contact.firstName
            lastName.text=contact.lastName
            phoneNumber.text=contact.phoneNumber.toString()
        }
    }


}