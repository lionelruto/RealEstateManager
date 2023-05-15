package com.openclassrooms.realestatemanager.views.fragments

import android.Manifest
import android.R.attr.data
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.checkSelfPermission
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.databinding.BottomSheetitemBinding
import com.openclassrooms.realestatemanager.views.MainActivity


class ModalBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var bot: BottomSheetitemBinding
    private lateinit var modalBottomSheet: ModalBottomSheet
    private var CAMERA_CODE = "0"
    private var imageUri: Uri? = null
    private lateinit var activityMainBinding: ActivityMainBinding
     var ImgUri: ArrayList<Uri> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.inflate(R.layout.bottom_sheetitem, container, false)
        bot= BottomSheetitemBinding.inflate(layoutInflater)
        bot.icTakephoto.setOnClickListener(this)
        bot.icGallery.setOnClickListener(this)
        return bot.root
    }


    companion object {
        const val TAG = "ModalBottomSheet"

    }

    fun requestCamerapermission(): Boolean {
        var permissionGranted = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val checkpermission =
                checkSelfPermission(activity as Context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_DENIED
            if (checkpermission) {
                // Display permission dialog
                //val permission = arrayOf(Manifest.permission.CAMERA)
                //requestPermissions(permission, CAMERA_PERMISSION_CODE)
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            } else {
                // Permission already granted
                permissionGranted = true
            }
        } else {
            permissionGranted = true
        }
        return permissionGranted
    }

    private fun openCameraInterface(id: Int) {
        requestCamerapermission()
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, R.string.take_picture)
        values.put(MediaStore.Images.Media.DESCRIPTION, R.string.take_picture_description)
        imageUri = requireActivity().contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
        if(id==R.id.ic_takephoto){
            // Create photo intent
            val intentphoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE) //take photo
            intentphoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            CAMERA_CODE="1"
            // Launch intent
            activityResultLaunch.launch(intentphoto)
        }
        else if(id==R.id.ic_gallery){
            // Create gallery intent
            val intentgall = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            intentgall.type = "image/*"//open gallery
            //intentgall.putExtra(MediaStore.EXTRA_OUTPUT)
            CAMERA_CODE="0"
            // Launch intent
            activityResultLaunch.launch(intentgall)
        }

    }

    private fun showAlert(message: String) {
        val builder = activity?.let { AlertDialog.Builder(it) }
        if (builder != null) {
            builder.setMessage(message)
        }
        builder?.setPositiveButton(R.string.ok_button_title, null)
        val dialog = builder?.create()
        if (dialog != null) {
            dialog.show()
        }
    }

    //activity when camera and gallery intents are ok
    var activityResultLaunch = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        val requestcode: String? = requireActivity()?.intent?.getStringExtra("value_Code")

        if (result.resultCode == Activity.RESULT_OK) {
            val photo = ImageView (activity as MainActivity)
            val linear=activity?.findViewById<LinearLayout>(R.id.imgcontent)

            if (CAMERA_CODE=="1") {
                photo.adjustViewBounds= true
                //photo.scaleType= "fitCenter"
                photo.layoutParams = LinearLayout.LayoutParams(250, 250) // value is in pixels
                photo.setImageURI(imageUri)
                //val img= imageUri.toString()
                Toast.makeText(requireActivity(), imageUri.toString(), LENGTH_LONG).show()
                if (linear != null) {
                    linear.addView(photo)
                    imageUri?.let { ImgUri.add(it) }
                }
            }else if(CAMERA_CODE=="0"){
                photo.adjustViewBounds= true
                //photo.scaleType= "fitCenter"
                photo.layoutParams = LinearLayout.LayoutParams(250, 250) // value is in pixels
                val intent: Intent= result.data!!
                val img: Uri= intent.data!!
                photo.setImageURI(img);
                if (linear != null) {
                    linear.addView(photo)
                    imageUri?.let { ImgUri.add(it) }

                }
            }
            //val uri: Uri = result.data as Uri

            /*else if (CAMERA_CODE=="0"){
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                if (imageUri != null) {
                    //val cursor: Cursor? = requireActivity().contentResolver?.query(imageUri!!, filePathColumn, null, null, null);

                    if (cursor != null) {
                        cursor.moveToFirst();
                        val columnIndex = cursor.getColumnIndex(filePathColumn.get(0));
                        val picturePath: String = cursor.getString(columnIndex);
                        cursor.close();
                        val picture= picturePath.toUri()
                        photo.adjustViewBounds= true
                        //photo.scaleType= "fitCenter"
                        photo.layoutParams = LinearLayout.LayoutParams(250, 250) // value is in pixels
                        photo.setImageURI(picture);

                        if (linear != null) {
                            linear.addView(photo)
                        }
                    }
                }
            }*/

            //activityMainBinding= activity.ActivityMainBinding.inflate(layoutInflater)
            //activityMainBinding.imgcontent.addView(photo)
        } else {
            showAlert("Failed to take camera picture")
        }
    }

    //request permission for camera
    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // PERMISSION GRANTED
            //openCameraInterface()

        } else {
            // PERMISSION NOT GRANTED
            showAlert("Camera permission was denied. Unable to take a picture.")
        }
    }

    override fun onClick(v: View?) {
        val id= v?.id
        if (id== R.id.ic_takephoto){
            openCameraInterface(id)
        }
        else if (id==R.id.ic_gallery){
            openCameraInterface(id)
        }
    }
}


