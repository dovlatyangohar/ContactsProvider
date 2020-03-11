package com.example.contactsprovider;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.contactsprovider.adapter.NumberAdapter;

import java.util.ArrayList;

import static com.example.contactsprovider.MainActivity.EXTRA_CONTACT_ID;
import static com.example.contactsprovider.MainActivity.EXTRA_NAME;

public class DetailedActivity extends AppCompatActivity {

    TextView tvContactName;
    private ImageView addNumber;
    ListView simpleList;
    private String updateContactName;
    private String number;
    private String currentNumber;
    private String id;
    private String contactID;
    private String addedNumber;
    ImageView makeCall;
    ImageView sendMessage;
    public int lookupKeyIndex;
    public int idIndex;
    public String currentLookupKey;
    public long currentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);


        Intent intent = getIntent();
        String contactName = intent.getStringExtra(EXTRA_NAME);
        contactID = intent.getStringExtra(EXTRA_CONTACT_ID);
        tvContactName = findViewById(R.id.contact_name);
        tvContactName.setText(contactName);

        updateContactName = tvContactName.getText().toString();

        addNumber = findViewById(R.id.add_number_img);


        fetchNumber();
        insertNumber();

        makeCall = findViewById(R.id.call_img);
        sendMessage = findViewById(R.id.message_img);

        updateName();
//        makePhoneCall();
//        sendSMS();


    }


    private void fetchNumber() {
        ArrayList<String> numberList = new ArrayList<>();

        int callImage = R.drawable.ic_call_black_24dp; //????
        int messageImage = R.drawable.ic_sms_black_24dp;


        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]
                {ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.RawContacts.CONTACT_ID
                };

        Cursor cursorDetailed = getContentResolver().query(
                uri,
                projection,
                null,
                null,
                null);

        try {
            while (cursorDetailed != null && cursorDetailed.moveToNext()) {
                number = cursorDetailed.getString(cursorDetailed.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                id = cursorDetailed.getString(cursorDetailed.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));
                if (id.equals(contactID)) {
                    currentNumber = number;
                    numberList.add(currentNumber);
                    simpleList = findViewById(R.id.number_lv);
                    NumberAdapter numberAdapter = new NumberAdapter(getApplicationContext(), numberList, callImage, messageImage);
                    simpleList.setAdapter(numberAdapter);
                }
            }

        } finally {
            if (cursorDetailed != null) {
                cursorDetailed.close();
            }
        }
    }

    private void insertNumber() {
        addNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNumberDialog();
            }
        });

    }

    private void showAddNumberDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_number_dialog);


        Button saveButton = dialog.findViewById(R.id.save_btn);
        Button cancelButton = dialog.findViewById(R.id.cancel_btn);

        final EditText newNumberEditText = dialog.findViewById(R.id.dialog_add_num);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addedNumber = newNumberEditText.getText().toString();

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetailedActivity.this, new String[]{Manifest.permission.WRITE_CONTACTS}, 0);
                } else {

                    if (!addedNumber.equals("")) {
//                        Uri selectedContactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
//                        String[] projection = new String[] {
//                                        ContactsContract.RawContacts.CONTACT_ID
//                                };
//
//                        Cursor mCursor = getContentResolver().query(
//                                selectedContactUri, ///wrong selection
//                                projection,
//                                null,
//                                null,
//                                null);
//
//                        while (mCursor != null && mCursor.moveToNext()) {
//                            lookupKeyIndex = mCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY);
//
//                            currentLookupKey = mCursor.getString(lookupKeyIndex);
//                            idIndex = mCursor.getColumnIndex(ContactsContract.Contacts._ID);
//                            currentId = mCursor.getLong(idIndex);
//                        }
//                        selectedContactUri = ContactsContract.Contacts.getLookupUri(currentId, currentLookupKey);
//
//                        Intent editIntent = new Intent(Intent.ACTION_EDIT);
//                        editIntent.setDataAndType(selectedContactUri, ContactsContract.Contacts.CONTENT_ITEM_TYPE);
//                        editIntent.putExtra("finishActivityOnSaveCompleted", true);
//
//                        startActivity(editIntent);

                        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, addedNumber);
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "something went wrong...", Toast.LENGTH_SHORT).show();
                    }
                }

                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void makePhoneCall() {

        makeCall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Calling " + currentNumber, Toast.LENGTH_SHORT).show();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + currentNumber));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });

    }

    private void sendSMS() {

//                SmsManager smgr = SmsManager.getDefault();
//                smgr.sendTextMessage(currentNumber,null,"hey",null,null);
//                Toast.makeText(getApplicationContext(), "SMS Sent Successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("vnd.android-dir/mms-sms");
        startActivity(intent);
    }

    private void updateName() {
        tvContactName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showUpdateNameDialog();
                return true;
            }
        });

    }

    private void showUpdateNameDialog() {


        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.update_name_dialog);
        dialog.setTitle("Update Name");


        Button saveButton = dialog.findViewById(R.id.save_updated_name_btn);
        Button cancelButton = dialog.findViewById(R.id.cancel_btn);

        final EditText contactNameEditText = dialog.findViewById(R.id.dialog_update_name);
        contactNameEditText.setText(updateContactName);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), updateContactName, Toast.LENGTH_SHORT).show();
                ///////////// update


                tvContactName = contactNameEditText;
                Toast.makeText(getApplicationContext(), tvContactName.getText().toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void updateNumber() {
    }

    private void showUpdateNumberdialog() {
    }


}
