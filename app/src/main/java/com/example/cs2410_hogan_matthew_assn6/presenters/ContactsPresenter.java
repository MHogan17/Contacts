package com.example.cs2410_hogan_matthew_assn6.presenters;

import com.example.cs2410_hogan_matthew_assn6.database.AppDatabase;
import com.example.cs2410_hogan_matthew_assn6.models.Contact;

import java.util.ArrayList;

public class ContactsPresenter {
    private MVPView view;
    private ArrayList<Contact> contacts = new ArrayList<>();
    private AppDatabase database;
    public interface MVPView extends BaseMVPView{
        public void renderContact(Contact contact);
        public void goToNewContactPage();
        public void goToContactPage(int id);
        public void removeContact(Contact contact);
        public void updateContactUI(Contact contact);
    }

    public ContactsPresenter(MVPView view){
        this.view = view;
        this.database = view.getContextDatabase();
        loadContacts();
    }

    public void loadContacts(){
        new Thread(() -> {
            contacts = (ArrayList<Contact>)database.getContactDao().getAll();
            contacts.forEach(contact -> {
                view.renderContact(contact);
            });
        }).start();
    }

    public void handleContactPress(int id){
        new Thread(() ->{
            view.goToContactPage(id);
        }).start();
    }

    public void handleCreateNewContactPress(){
        new Thread(() -> {
            view.goToNewContactPage();
        }).start();
    }

    public void onNewContactCreated(Contact contact) {
        if (contact != null) {
            contacts.add(contact);
            view.renderContact(contact);
        }
    }

    public void onContactDeleted(Contact contact){
        for (int i = 0; i < contacts.size(); i ++) {
            if (contacts.get(i).id == contact.id) {
                contacts.remove(i);
                break;
            }
        }
        view.removeContact(contact);
    }

    public void onContactUpdated(Contact contact){
        for (int i = 0; i < contacts.size(); i ++) {
            if (contacts.get(i).id == contact.id) {
                contacts.set(i, contact);
                break;
            }
        }
        view.updateContactUI(contact);
    }
}