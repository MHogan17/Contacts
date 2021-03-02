package com.example.cs2410_hogan_matthew_assn6.presenters;

import android.widget.PopupMenu;

import com.example.cs2410_hogan_matthew_assn6.ContactActivity;
import com.example.cs2410_hogan_matthew_assn6.database.AppDatabase;
import com.example.cs2410_hogan_matthew_assn6.models.Contact;

public class ContactPresenter{
    MVPView view;
    AppDatabase database;
    Contact contact;
    boolean isEdited = false;

    public interface MVPView extends BaseMVPView{
        void renderContactPage(Contact contact);
        void goToEditContactPage(int id);
        void displayDeleteConfirmation();
        void goToContactsPage(Contact contact, boolean isEdited, boolean isDeleted);
        void makePhoneCall(String number);
        void sendSMS(String number);
        void sendEmail(String email);
    }

    public ContactPresenter(ContactActivity view){
        this.view = view;
        this.database = view.getContextDatabase();
    }

    public void loadContact(int id){
        new Thread(() ->{
            isEdited = false;
            contact = database.getContactDao().findById(id);
            view.renderContactPage(contact);
        }).start();
    }

    public void handleEditClick(int id){
        new Thread(() -> {
            view.goToEditContactPage(id);
        }).start();
    }

    public void handleDeleteClick() {
        view.displayDeleteConfirmation();
    }

    public void deleteContact(){
        new Thread(() -> {
            database.getContactDao().delete(contact);
            view.goToContactsPage(contact, false, true);
        }).start();
    }

    public void handleContactEdited(Contact contact){
        new Thread(() -> {
            isEdited = true;
            database.getContactDao().update(contact);
            this.contact = database.getContactDao().findById(contact.id);
            view.renderContactPage(contact);
        }).start();
    }

    public void handleCallPress(){
        view.makePhoneCall(contact.number);
    }
    public void handleTextPress(){
        view.sendSMS(contact.number);
    }
    public void handleEmailPress(){
        if (contact.email.length() != 0){
            view.sendEmail(contact.email);
        }
    }
    public void handleBackPressed(){
        view.goToContactsPage(contact, isEdited, false);
    }
}