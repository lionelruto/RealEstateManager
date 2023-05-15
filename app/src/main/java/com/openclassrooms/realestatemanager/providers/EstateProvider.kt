package com.openclassrooms.realestatemanager.providers

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.room.EstateDAO
import javax.inject.Inject


class EstateProvider: ContentProvider() {
    val AUTHORITY = "com.openclassrooms.realestatemanager.providers"
    val TABLE_NAME = Estate::class.java.simpleName
    val URI_ITEM = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
    @Inject lateinit var estateDAO: EstateDAO

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        if (context!=null){
            val index:Long = ContentUris.parseId(URI_ITEM)
            val cursor = estateDAO.getAllEstatesCursor(index)
            cursor.setNotificationUri(context!!.contentResolver,URI_ITEM)
            return cursor
        }
        throw IllegalArgumentException("Failed to query row for uri $URI_ITEM")
    }

    override fun getType(p0: Uri): String? {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }
}