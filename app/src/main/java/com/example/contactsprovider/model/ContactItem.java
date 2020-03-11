package com.example.contactsprovider.model;

public class ContactItem {
    private String contactItemName;
    private String contactId;

    public ContactItem(String contactItemName, String contactId) {
        this.contactItemName = contactItemName;
        this.contactId = contactId;
    }

    public String getContactItemName() {
        return contactItemName;
    }

    public String getContactId() {
        return contactId;
    }
}
