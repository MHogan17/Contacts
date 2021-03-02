package com.example.cs2410_hogan_matthew_assn6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;


import com.example.cs2410_hogan_matthew_assn6.components.ContactCard;
import com.example.cs2410_hogan_matthew_assn6.components.ContactListItem;
import com.example.cs2410_hogan_matthew_assn6.models.Contact;
import com.example.cs2410_hogan_matthew_assn6.presenters.ContactsPresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ContactsActivity extends BaseActivity implements ContactsPresenter.MVPView {
    FrameLayout mainLayout;
    LinearLayout contactsLayout;
    ContactsPresenter presenter;
    ArrayList<ContactListItem> contactListItems = new ArrayList<>();
    // request codes
    private final int MODIFY_CONTACT = 0;
    private final int CREATE_NEW_CONTACT = 1;
    // results codes
    public final static int DELETED_CONTACT = 2;
    public final static int UPDATED_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ContactsPresenter(this);

        mainLayout = new FrameLayout(this);
        contactsLayout = new LinearLayout(this);
        contactsLayout.setOrientation(LinearLayout.VERTICAL);
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(contactsLayout);

        FloatingActionButton createButton = new FloatingActionButton(this);
        createButton.setOnClickListener(view -> presenter.handleCreateNewContactPress());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 48, 48);
        params.gravity = (Gravity.BOTTOM | Gravity.RIGHT);
        createButton.setLayoutParams(params);
        createButton.setImageResource(R.drawable.ic_baseline_person_add_24);

        mainLayout.addView(createButton);
        mainLayout.addView(scrollView);

        setContentView(mainLayout);
    }

    @Override
    public void renderContact(Contact contact) {
        View.OnClickListener onClickListener = view -> presenter.handleContactPress(contact.id);
        ContactListItem listItem = new ContactListItem(this, contact, onClickListener);
        listItem.setTag(contact.id);
        contactListItems.add(listItem);
        contactsLayout.addView(listItem);
    }

    @Override
    public void goToNewContactPage() {
        Intent intent = new Intent(this, NewOrEditContactActivity.class);
        startActivityForResult(intent, CREATE_NEW_CONTACT);
    }

    @Override
    public void goToContactPage(int id) {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra("contactId", id);
        startActivityForResult(intent, MODIFY_CONTACT);
    }

    @Override
    public void removeContact(Contact contact) {
        View view = contactsLayout.findViewWithTag(contact.id);
        contactsLayout.removeView(view);
    }

    @Override
    public void updateContactUI(Contact contact) {
        ContactListItem listItem = mainLayout.findViewWithTag(contact.id);
        listItem.setContact(contact);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_NEW_CONTACT && resultCode == Activity.RESULT_OK) {
            Contact contact = (Contact) data.getSerializableExtra("result");
            presenter.onNewContactCreated(contact);
        }

        if (requestCode == MODIFY_CONTACT && resultCode == DELETED_CONTACT) {
            //update the UI
            Contact deletedContact = (Contact) data.getSerializableExtra("result");
            presenter.onContactDeleted(deletedContact);
        }

        if (requestCode == MODIFY_CONTACT && resultCode == UPDATED_CONTACT) {
            //update the UI
            Contact updatedContact = (Contact)data.getSerializableExtra("result");
            presenter.onContactUpdated(updatedContact);
        }
    }
}