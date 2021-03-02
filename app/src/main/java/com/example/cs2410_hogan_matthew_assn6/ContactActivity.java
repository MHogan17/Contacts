package com.example.cs2410_hogan_matthew_assn6;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.cs2410_hogan_matthew_assn6.components.ContactCard;
import com.example.cs2410_hogan_matthew_assn6.models.Contact;
import com.example.cs2410_hogan_matthew_assn6.presenters.ContactPresenter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ContactActivity extends BaseActivity implements ContactPresenter.MVPView {
    ContactPresenter presenter;
    FrameLayout mainLayout;
    private final int CALL_PERMISSION_REQUESTED = 1;
    private final int TEXT_PERMISSION_REQUESTED = 2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainLayout = new FrameLayout(this);
        presenter = new ContactPresenter(this);
        int contactId = getIntent().getIntExtra("contactId", -1);
        presenter.loadContact(contactId);
        setContentView(mainLayout);
    }

    @Override
    public void renderContactPage(Contact contact) {
        runOnUiThread(() -> {
            mainLayout.setBackgroundColor(getResources().getColor(R.color.black));
            ContactCard contactCard = new ContactCard(this, contact);
            mainLayout.addView(contactCard);
            contactCard.setFabOnClickListener((view) -> {
                PopupMenu popupMenu = new PopupMenu(this, contactCard.fab);
                popupMenu.getMenu().add("Edit");
                popupMenu.getMenu().add("Delete");
                popupMenu.setOnMenuItemClickListener((menuItem) -> {
                    if (menuItem.getTitle().toString().equals("Edit")) {
                        // handle edit
                        presenter.handleEditClick(contact.id);
                    } else {
                        // handle delete
                        presenter.handleDeleteClick();
                    }
                    return true;
                });
                popupMenu.show();
            });
            contactCard.setCallPressListener((view) -> {
                presenter.handleCallPress();
            });
            contactCard.setTextPressListener((view) -> {
                presenter.handleTextPress();
            });
            contactCard.setEmailPressListener((view) -> {
                presenter.handleEmailPress();
            });
        });
    }

    @Override
    public void goToEditContactPage(int id) {
        Intent intent = new Intent(this, NewOrEditContactActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, ContactsActivity.UPDATED_CONTACT);
    }

    @Override
    public void displayDeleteConfirmation() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Are you sure you want to delete this contact?")
                .setPositiveButton("Delete", (view, i) -> {
                    presenter.deleteContact();
                })
                .setNeutralButton("Cancel", (view, i) -> {
                    view.dismiss();
                })
                .show();
    }


    @Override
    public void goToContactsPage(Contact contact, boolean isEdited, boolean isDeleted) {
        // go back to previous page
        if (isDeleted) {
            Intent intent = new Intent();
            intent.putExtra("result", contact);
            setResult(ContactsActivity.DELETED_CONTACT, intent);
        } else if (isEdited) {
            Intent intent = new Intent();
            intent.putExtra("result", contact);
            setResult(ContactsActivity.UPDATED_CONTACT, intent);
        }
        finish();
    }

    @Override
    public void makePhoneCall(String number) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent();
            callIntent.setData(Uri.parse("tel:" + number));
            startActivity(callIntent);
        } else {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUESTED);
        }
    }


    public void sendSMS(String number) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            Intent textIntent = new Intent(Intent.ACTION_VIEW);
            textIntent.setData(Uri.parse("sms:" + number));
            startActivity(textIntent);
        } else {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, TEXT_PERMISSION_REQUESTED);
        }
    }

    @Override
    public void sendEmail(String address) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"+ address));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ContactsActivity.UPDATED_CONTACT && resultCode == RESULT_OK){
            Contact contact = (Contact) data.getSerializableExtra("result");
            presenter.handleContactEdited(contact);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION_REQUESTED) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // try to make the phone call again
                presenter.handleCallPress();
            } else {
                // display message saying that you will need to allow permissions to continue using this function.
            };
        }

        if (requestCode == TEXT_PERMISSION_REQUESTED) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // try to make the phone call again
                presenter.handleTextPress();
            } else {
                // display message saying that you will need to allow permissions to continue using this function.
            };
        }
    }

    @Override
    public void onBackPressed() {
        presenter.handleBackPressed();
    }
}
