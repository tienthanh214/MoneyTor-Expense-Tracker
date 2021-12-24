package com.hcmus.group14.moneytor.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.hcmus.group14.moneytor.data.model.Relate;

import java.util.ArrayList;

public class ContactUtils {
    public static final int REQUEST_READ_CONTACTS = 79;

    static private void requestPermission(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                android.Manifest.permission.READ_CONTACTS)) {
            Toast.makeText(activity.getApplicationContext(),
                    "Contacts list permission granted",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
    }

    @SuppressLint("Range")
    public static ArrayList<Relate> getAllContacts(Activity activity, @NonNull Context context) {
        requestPermission(activity);

        ArrayList<Relate> nameList = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                @SuppressLint("Range")
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                @SuppressLint("Range")
                String name =
                        cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                Relate relate = new Relate(name, "N/A");

                Cursor phoneCursor = context.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{id},
                        null);

                if (phoneCursor.moveToNext()) {
                    String phoneNumber =
                            phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    relate.setTel(phoneNumber);
                }

                if (!relate.getTel().equals("N/A")) {
                    nameList.add(relate);
                }

                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);

                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }

                    pCur.close();
                }
            }
        }

        if (cur != null) {
            cur.close();
        }

        return nameList;
    }
}
