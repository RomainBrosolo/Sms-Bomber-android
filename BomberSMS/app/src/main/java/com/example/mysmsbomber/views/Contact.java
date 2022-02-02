package com.example.mysmsbomber.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.example.mysmsbomber.R;

import java.util.ArrayList;

public class Contact extends Fragment {

    public EditText listContact;
    private static final int CONTACT_PERMISSION_CODE = 1;
    public Contact() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.contact_layout, container , false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        listContact = getView().findViewById(R.id.listContact);

        if (checkContactPermission()){
            ContentResolver contentResolver = this.getActivity().getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_ALTERNATIVE,
                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);


            while(cursor.moveToNext()) {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_ALTERNATIVE));
                @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                listContact.setText(listContact.getText().toString() + "\n\r" + name + " : " + number + "\n");
            }

            cursor.close();
        }
        else {

            requestContactPermission();
        }

    }

    private boolean checkContactPermission(){
        //check if contact permission was granted or not
        boolean result = ContextCompat.checkSelfPermission(
                getContext(),
                Manifest.permission.READ_CONTACTS) == (PackageManager.PERMISSION_GRANTED
        );

        return result;  //true if permission granted, false if not
    }

    private void requestContactPermission(){
        //permissions to request
        String[] permission = {Manifest.permission.READ_CONTACTS};

        ActivityCompat.requestPermissions(this.getActivity(), permission, CONTACT_PERMISSION_CODE);
    }

    public void readSms() {
        Cursor cursor = getActivity().getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        ArrayList<String> numbers = new ArrayList<String>();

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                String number = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                numbers.add(number);
                listContact.setText(listContact.getText().toString() + "\n\r" + number);
                //viewModel.countMessageOfNumber(number);
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }

        cursor.close();

    }



}
