package com.example.contactsprovider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.example.contactsprovider.adapter.ContactAdapter;
import com.example.contactsprovider.model.ContactItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ContactAdapter.OnItemClickListener{

    public static final String EXTRA_NAME = "contactName";
    public static final String EXTRA_CONTACT_ID = "contactID";
    private RecyclerView recyclerView;
    private List<ContactItem> contactList;
    private ContactItem contactItem;
    ContactAdapter contactAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchContacts();
        displayContactName();
    }

    private void fetchContacts() {
        contactList = new ArrayList<>();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.RawContacts.CONTACT_ID,};

        String sortOrder =
                ContactsContract.Contacts.Entity.RAW_CONTACT_ID +
                        " ASC";

        Cursor cursor = getContentResolver().query(uri,
                projection,
                null,
                null,
                sortOrder);

        try {
            while (cursor != null && cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));

                contactItem = new ContactItem(name, id);
                contactList.add(contactItem);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    private void displayContactName() {
        recyclerView = findViewById(R.id.rvContacts);
        contactAdapter = new ContactAdapter(contactList, this);
        recyclerView.setAdapter(contactAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter.setOnItemClickListener(MainActivity.this);

    }


    @Override
    public void onItemClick(int position) {

        Intent detailIntent = new Intent(this, DetailedActivity.class);
        ContactItem clickedItem = contactList.get(position);
        detailIntent.putExtra(EXTRA_NAME, clickedItem.getContactItemName());
        detailIntent.putExtra(EXTRA_CONTACT_ID, clickedItem.getContactId());

        startActivity(detailIntent);


    }
}
