package com.example.mysmsbomber.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Toast;

import com.example.mysmsbomber.R;


public class SMS extends Fragment {

    private EditText txtContact, txtMessage, numberMessage, txtLatence;
    private Button btnContact, btnEnvoi;

    private static final int CONTACT_PERMISSION_CODE = 1;
    private static final int SMS_PERMISSION_CODE = 1;
    private static final int CONTACT_PICK_CODE = 2;
    public SMS() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sms_layout, container , false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        txtContact = getView().findViewById(R.id.txtContact);
        txtLatence = getView().findViewById(R.id.txtLatence);
        txtMessage = getView().findViewById(R.id.txtMessage);
        numberMessage = getView().findViewById(R.id.numberMessage);
        btnContact = getView().findViewById(R.id.btnContact);
        btnEnvoi = getView().findViewById(R.id.btnEnvoi);

        //handle click, to pick contact
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //first we need to check read contact permission
                if (checkContactPermission()){
                    //permission granted, pick contact
                    pickContactIntent();
                }
                else {
                    //permission not granted, request
                    requestContactPermission();
                }
            }
        });

        btnEnvoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSmsPermission()){
                    for (int i=0; i<Integer.parseInt(String.valueOf(numberMessage.getText())); i++) {
                        SmsManager.getDefault().sendTextMessage(txtContact.getText().toString(),null,txtMessage.getText().toString(), null, null);
                        try {
                            Thread.sleep(Integer.parseInt(String.valueOf(txtLatence.getText())));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(getContext(), "SMS envoyés !", Toast.LENGTH_SHORT).show();
                }
                else {
                    requestSmsPermission();
                }

            }
        });
    }

    private boolean checkContactPermission(){
        //check if contact permission was granted or not
        boolean result = ContextCompat.checkSelfPermission(
                getContext(),
                Manifest.permission.READ_CONTACTS) == (PackageManager.PERMISSION_GRANTED
        );

        return result;  //true if permission granted, false if not
    }

    private boolean checkSmsPermission(){
        //check if contact permission was granted or not
        boolean result = ContextCompat.checkSelfPermission(
                getContext(),
                Manifest.permission.SEND_SMS) == (PackageManager.PERMISSION_GRANTED
        );

        return result;  //true if permission granted, false if not
    }

    private void requestContactPermission(){
        //permissions to request
        String[] permission = {Manifest.permission.READ_CONTACTS};

        ActivityCompat.requestPermissions(this.getActivity(), permission, CONTACT_PERMISSION_CODE);
    }

    private void requestSmsPermission(){
        //permissions to request
        String[] permission = {Manifest.permission.SEND_SMS};

        ActivityCompat.requestPermissions(this.getActivity(), permission, SMS_PERMISSION_CODE);
    }

    private void pickContactIntent(){
        //intent to pick contact
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, CONTACT_PICK_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //handle permission request result
        if (requestCode == CONTACT_PERMISSION_CODE){
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //permission granted, can pick contact now
                pickContactIntent();
            }
            else {
                //permission denied
                Toast.makeText(getContext(), "Permission denied...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //handle intent results
        if (resultCode == getActivity().RESULT_OK){
            //calls when user click a contact from list

            if (requestCode == CONTACT_PICK_CODE){
                txtContact.setText("");

                Cursor cursor1, cursor2;

                //get data from intent
                Uri uri = data.getData();

                cursor1 = getActivity().getContentResolver().query(uri, null, null, null, null);

                if (cursor1.moveToFirst()){
                    //get contact details
                    @SuppressLint("Range") String contactId = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
                    @SuppressLint("Range") String idResults = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    int idResultHold = Integer.parseInt(idResults);


                    if (idResultHold == 1){
                        cursor2 = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+contactId,
                                null,
                                null
                        );
                        //a contact may have multiple phone numbers
                        while (cursor2.moveToNext()){
                            //get phone number
                            @SuppressLint("Range") String contactNumber = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            //set details

                            txtContact.setText(contactNumber);
                        }
                        cursor2.close();
                    }
                    cursor1.close();
                }
            }

        }
        else {
            //calls when user click back button | don't pick contact
        }
    }
}
