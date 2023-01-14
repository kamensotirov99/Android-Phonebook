package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

open class DatabaseHandler(context:Context) : SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 8
        private const val DATABASE_NAME = "contacts.db"
        private const val TBL_CONTACTS = "tbl_contacts"
        private const val FIRST_NAME = "first_name"
        private const val LAST_NAME = "last_name"
        private const val COUNTRY = "country"
        private const val EMAIL = "email"
        private const val PHONE_NUMBER = "phone_number"
        private const val GENDER = "gender"
        private const val IS_IN_DB = "is_in_db"
    }





    override fun onCreate(p0: SQLiteDatabase?) {
        val createTblContacts = ("CREATE TABLE " + TBL_CONTACTS + "("
                + PHONE_NUMBER + " TEXT PRIMARY KEY, "
                + FIRST_NAME + " TEXT, " + LAST_NAME +" TEXT," + COUNTRY + " TEXT," + EMAIL + " TEXT,"
                + GENDER + " TEXT," + IS_IN_DB + " BOOL)")
        p0?.execSQL(createTblContacts)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $TBL_CONTACTS")
        onCreate(p0)
    }

    fun insertContact (contact: ContactModel):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(FIRST_NAME,contact.firstName)
        contentValues.put(LAST_NAME,contact.lastName)
        contentValues.put(COUNTRY,contact.country)
        contentValues.put(EMAIL,contact.email)
        contentValues.put(PHONE_NUMBER,contact.phoneNumber)
        contentValues.put(GENDER,contact.gender)
        contentValues.put(IS_IN_DB,contact.isInDB)

        val success = db.insert(TBL_CONTACTS,null,contentValues)
        db.close()
        return success
    }

    fun updateContact (originalPhoneNumber:String,contact: ContactModel):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(FIRST_NAME,contact.firstName)
        contentValues.put(LAST_NAME,contact.lastName)
        contentValues.put(COUNTRY,contact.country)
        contentValues.put(EMAIL,contact.email)
        contentValues.put(PHONE_NUMBER,contact.phoneNumber)
        contentValues.put(GENDER,contact.gender)
        contentValues.put(IS_IN_DB,contact.isInDB)

        val success = db.update(TBL_CONTACTS,contentValues,"phone_number='$originalPhoneNumber'",null)
        db.close()
        return success
    }

    fun getAllContacts():ArrayList<ContactModel>{
        val contactList:ArrayList<ContactModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_CONTACTS"
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery,null)
        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        if (cursor.moveToFirst()){
            do {
                var phoneNumber = cursor.getString(0)
               var firstName = cursor.getString(1)
               var lastName = cursor.getString(2)
               var country = cursor.getString(3)
               var email = cursor.getString(4)

               var gender = cursor.getString(5)
               var isInDB = cursor.getInt(6)==1

                val contact = ContactModel(
                    firstName = firstName, lastName =lastName,
                    country = country, email =email,
                    phoneNumber = phoneNumber, gender = gender, isInDB =isInDB)
                contactList.add(contact)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return contactList
    }

    fun deleteContact(phoneNumber: String): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(PHONE_NUMBER,phoneNumber)
        val success = db.delete(TBL_CONTACTS, "phone_number='$phoneNumber'",null)
        db.close()
        return success
    }
}