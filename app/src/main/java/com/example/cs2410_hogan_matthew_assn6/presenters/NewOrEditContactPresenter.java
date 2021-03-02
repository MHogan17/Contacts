package com.example.cs2410_hogan_matthew_assn6.presenters;

import com.example.cs2410_hogan_matthew_assn6.NewOrEditContactActivity;
import com.example.cs2410_hogan_matthew_assn6.database.AppDatabase;
import com.example.cs2410_hogan_matthew_assn6.models.Contact;

public class NewOrEditContactPresenter {
    public static final int DEFAULT_CONTACT_ID = -1;
    Contact contact;
    MVPView view;
    AppDatabase database;
    public interface MVPView extends BaseMVPView{
        void goBack(Contact contact);
        void goToPhotos();
        void displayPicture(String pictureUri);
        void populateTextFields(Contact contact);
        void goToCamera();
        void displayFirstError();
        void displayNumberError();
        void displayEmailError();
    }

    public NewOrEditContactPresenter(NewOrEditContactActivity view, int id){
        this.view = view;
        this.database = view.getContextDatabase();
        if (id != DEFAULT_CONTACT_ID) {
            // load the post from the database
            new Thread(() -> {
                contact = database.getContactDao().findById(id);
                view.populateTextFields(contact);
            }).start();
        }
    }

    public void saveContact(int id, String first, String last, String number, String email, String pictureUri) {
        boolean hasError = false;
        if (first.length() == 0) {
            view.displayFirstError();
            hasError = true;
        }
        if (number.length() == 0) {
            view.displayNumberError();
            hasError = true;
        }
        if (email.length() != 0 && !email.contains("@")){
            view.displayEmailError();
            hasError = true;
        }
        if (hasError) {
            return;
        }
        // insertion into the database
        new Thread(() -> {
            if (id < 0) {
                Contact newContact = new Contact();
                newContact.first = first;
                newContact.last = last;
                newContact.number = number;
                newContact.email = email;
                newContact.pictureUri = pictureUri;
                newContact.id = (int) database.getContactDao().insert(newContact);
                view.goBack(newContact);
            } else{
                Contact contact = database.getContactDao().findById(id);
                contact.first = first;
                contact.last = last;
                contact.number = number;
                contact.email = email;
                contact.pictureUri = pictureUri;
                database.getContactDao().update(contact);
                view.goBack(contact);
            }
        }).start();
    }

    public void handleSelectPictureButtonPressed(){
        view.goToPhotos();
    }
    public void handleTakePicturePressed(){ view.goToCamera(); }

    public void handlePictureSelected(String pictureUri) {
        view.displayPicture(pictureUri);
    }
    public void handleCancelPressed() {
        view.goBack(null);
    }
    public void handleEditContact(int id) {
        new Thread(() -> {
            Contact contact = database.getContactDao().findById(id);
            view.populateTextFields(contact);
        });
    }

}