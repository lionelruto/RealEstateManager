package com.openclassrooms.realestatemanager.views;

import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Insert;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.Models.Estate;
import com.openclassrooms.realestatemanager.Models.ListPhotos;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.ViewModel.EstateFactory;
import com.openclassrooms.realestatemanager.ViewModel.EstateViewModel;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.hilt.module.EstatesRepository;
import com.openclassrooms.realestatemanager.repositories.EstateRepository;
import com.openclassrooms.realestatemanager.room.EstateDAO;
import com.openclassrooms.realestatemanager.room.LoginLocalDB;
import com.openclassrooms.realestatemanager.views.fragments.ModalBottomSheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TextView textViewMain;
    private TextView textViewQuantity;
    private ActivityMainBinding activityMainBinding;
    private ModalBottomSheet modalBottomSheet;

    LoginLocalDB loginLocalDB;
    private EstateViewModel viewModel;
    private EstateFactory viewModelFactory;
    //@Inject EstateRepository estatesRepository;
    private ArrayList<Estate> estates= new ArrayList<>();
    private ArrayList<String> ListInterets= new ArrayList<>();
    private Boolean edition= false;
    private Estate estateIntent;
    private int getId;
    private SharedPreferences sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        HorizontalScrollView linear= activityMainBinding.chipScroll;
        CheckBox checkSold = (MaterialCheckBox) activityMainBinding.checkboxChild1;
        CheckBox checkHold = (MaterialCheckBox) activityMainBinding.checkboxChild2;
        CheckBox checkAvailable = (MaterialCheckBox) activityMainBinding.checkboxChild3;
        checkSold.setOnCheckedChangeListener(this);
        checkAvailable.setOnCheckedChangeListener(this);
        checkHold.setOnCheckedChangeListener(this);

        //edit du sharepreference qui permet de cacher les floating action buton au retour de la precedente activitée
        sharedPreference =  getSharedPreferences("isAllFabsVisible", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putBoolean("isAllFabsVisible",true);
        editor.apply();

        activityMainBinding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                modalBottomSheet= new ModalBottomSheet();
                modalBottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");
                return true;
            }
        });
        activityMainBinding.addButton.setOnClickListener(this);
        activityMainBinding.addButtonChip.setOnClickListener(this);

        String[] randomArray = {"Item 1", "Item 2", "Item 3", "Item 4"};
        MaterialAutoCompleteTextView itemview;

        ArrayList<String> items = new ArrayList<String>(Arrays.asList(randomArray));
        ArrayAdapter<String> h = new ArrayAdapter(MainActivity.this,R.layout.list_items, items);
        itemview = findViewById(R.id.filledtype);
        itemview.setAdapter(h);
        //itemview.showDropDown();

        //pour vérifier si il y'a une opération de modification de l'estate
        estateIntent= (Estate) getIntent().getSerializableExtra("oneEstate");
        if(estateIntent!= null){
            setInfo(estateIntent);
            edition= true;

        }else{
            edition=false;
        }

    }


    @Override
    public void onClick(View v) {
        CheckBox checkSold = (MaterialCheckBox) activityMainBinding.checkboxChild1;
        CheckBox checkHold = (MaterialCheckBox) activityMainBinding.checkboxChild2;
        CheckBox checkAvailable = (MaterialCheckBox) activityMainBinding.checkboxChild3;
        switch (v.getId()){
            case R.id.addButton:

                String value="0";
                if(checkHold.isChecked()) value="1";
                if (checkAvailable.isChecked()) value="0";
                if (checkSold.isChecked()) value="2";
                getInfo(value);
                break;
            case R.id.addButtonChip:
                Toast.makeText(this, "addclick", Toast.LENGTH_LONG).show();
                TextInputLayout filled=  activityMainBinding.filledinteres;
                ChipGroup chipGroup= findViewById(R.id.chipgroup);

                String text= Objects.requireNonNull(filled.getEditText()).getText().toString().trim();
                if (text.equals("")){
                    filled.setError("Entrer un interet");
                }else {
                    filled.setErrorEnabled(false);
                    filled.getEditText().setText("");
                    addChip(text, chipGroup);
                }
        }

    }

    //add dynamically a chip to a chipgroup and add to the interet list
    public void addChip(String text, ChipGroup chipGroup){
        Chip chip =new Chip(this);
        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(this,
                null,
                0,
                R.style.Widget_MaterialComponents_Chip_Entry);
        chip.setChipDrawable(chipDrawable);
        chip.setLayoutParams(new ChipGroup.LayoutParams(ChipGroup.LayoutParams.WRAP_CONTENT, ChipGroup.LayoutParams.WRAP_CONTENT));
        chip.setChipBackgroundColorResource(R.color.colorAccent);
        chip.setText(text);
        chip.setCloseIconVisible(false);
        chip.setCheckedIconVisible(false);
        chip.setId(ViewCompat.generateViewId());
        chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                chip.setCloseIconVisible(isChecked);
            }
        });

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chip chip2 = (Chip) v;
                chipGroup.removeView(chip2);
            }
        });
        chipGroup.addView(chip);
        ListInterets.add(text);
    }

    //add estate on editex
    public void setInfo(Estate estate){
        activityMainBinding.filledSurface.getEditText().setText(estate.getSurface());
        //activityMainBinding.filledtype.getEditableText().setText();
        activityMainBinding.filleddesc.getEditText().setText(estate.getDescription());
        int index= estate.getId();
        activityMainBinding.filledrooms.getEditText().setText(estate.getNbre_piece());
        activityMainBinding.filleloc.getEditText().setText(estate.getAdress());
        activityMainBinding.filledPrice.getEditText().setText(estate.getPrice());
        activityMainBinding.filledbeth.getEditText().setText(estate.getNbre_piece());
        //activityMainBinding.filledinteres.getEditText().setText(estate.getInteret());
        activityMainBinding.filledaddr.getEditText().setText(estate.getAdress());
    }

    //get info by saving estate
    public void getInfo(String value){
        int surf = Integer.parseInt(activityMainBinding.filledSurface.getEditText().getText().toString().trim());
        String type = activityMainBinding.filledtype.getEditableText().toString();
        String desc =activityMainBinding.filleddesc.getEditText().getText().toString().trim();
        int rooms =Integer.parseInt( activityMainBinding.filledrooms.getEditText().getText().toString().trim());
        String location =activityMainBinding.filleloc.getEditText().getText().toString().trim();
        int price =Integer.parseInt( activityMainBinding.filledPrice.getEditText().getText().toString().trim());
        String beth =activityMainBinding.filledbeth.getEditText().getText().toString();
        int status= Integer.parseInt(value);
        String interets =activityMainBinding.filledinteres.getEditText().getText().toString().trim();
        String addr = activityMainBinding.filledaddr.getEditText().getText().toString().trim();
        ArrayList<ListPhotos> photosList  = new ArrayList();
        LinearLayout linear = activityMainBinding.imgcontent;

        //Loop in child images and take images to convert in string of bitmap
        for(int i= 0; i<linear.getChildCount(); i++){
            ImageView img= (ImageView) linear.getChildAt(i);
            String image= ((BitmapDrawable)img.getDrawable()).getBitmap().toString();
            photosList.add(new ListPhotos(image, "the description"));
        }
        estates.add(new Estate(
                null,
                type,
                photosList,
               price,
                surf,
                rooms,
                desc,
                addr,
                ListInterets,
                status,
                "12/12/2003",
                "12/12/2003",
                "lionel Mambingo"
        ));

        //initialisation du ViewModel
        EstatesRepository estateReposito = new EstatesRepository(LoginLocalDB.Companion.getInstance(this));
        viewModelFactory = new EstateFactory(estateReposito);
        viewModel = new ViewModelProvider(this,viewModelFactory).get(EstateViewModel.class);

        //when we come from list to edit estate
        if (edition){
             ArrayList<Estate> estateedit= new ArrayList<>();
             getId= estateIntent.getId();
            estateedit.add(new Estate(
                    getId,
                    type,
                    photosList,
                    1,
                    2,
                    3,
                    desc,
                    addr,
                    ListInterets,
                    1,
                    "12/12/2003",
                    "12/12/2003",
                    "lionel Mambingo"
            ));

            viewModel.updateEstate(estateedit.get(0));
            edition=false;

        }
        else{
            viewModel.insertEstate(estates.get(0));
        }
        //LoginLocalDB.Companion.getInstance(this).DaoAccess().insertEstate(estates.get(0));

        Intent intent = new Intent(MainActivity.this, First_Activity.class);
        intent.putExtra("estates", estates);
        startActivity(intent);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        CheckBox checkSold = (MaterialCheckBox) activityMainBinding.checkboxChild1;
        CheckBox checkHold = (MaterialCheckBox) activityMainBinding.checkboxChild2;
        CheckBox checkAvailable = (MaterialCheckBox) activityMainBinding.checkboxChild3;
        boolean checked = ((CheckBox) buttonView).isChecked();

        switch (buttonView.getId()){
            case R.id.checkbox_child_1:
                if (checked) {
                    checkHold.setEnabled(false);
                    checkAvailable.setEnabled(false);
                }
                if (!checked) {
                    checkHold.setEnabled(true);
                    checkAvailable.setEnabled(true);
                }
                break;

            case R.id.checkbox_child_2:
                if (checked) {
                    checkSold.setEnabled(false);
                    checkAvailable.setEnabled(false);
                }
                if (!checked) {
                    checkSold.setEnabled(true);
                    checkAvailable.setEnabled(true);
                }
                break;

            case R.id.checkbox_child_3:
                if (checked) {
                    checkHold.setEnabled(false);
                    checkSold.setEnabled(false);
                }
                if (!checked) {
                    checkHold.setEnabled(true);
                    checkSold.setEnabled(true);
                }

        }
    }
}


