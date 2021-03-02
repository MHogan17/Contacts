package com.example.cs2410_hogan_matthew_assn6;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Gravity;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.example.cs2410_hogan_matthew_assn6.components.ImageSelector;
import com.example.cs2410_hogan_matthew_assn6.components.MaterialInput;
import com.example.cs2410_hogan_matthew_assn6.models.Contact;
import com.example.cs2410_hogan_matthew_assn6.presenters.NewOrEditContactPresenter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.Date;

public class NewOrEditContactActivity extends BaseActivity implements NewOrEditContactPresenter.MVPView {
    NewOrEditContactPresenter presenter;
    private final int PICK_IMAGE = 1;
    private final int TAKE_PICTURE = 2;
    ImageSelector selector;
    MaterialInput firstView;
    MaterialInput lastView;
    MaterialInput numberView;
    MaterialInput emailView;
    LinearLayout mainLayout;
    String currentFilePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        int contactId = getIntent().getIntExtra("id", NewOrEditContactPresenter.DEFAULT_CONTACT_ID);

        selector = new ImageSelector(this);
        selector.setOnClickListener((view) -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Choose Image")
                    .setItems(new CharSequence[]{"From Camera", "From Photos"}, (menuItem, i) -> {
                        if (i == 0) {
                            presenter.handleTakePicturePressed();
                        } else {
                            presenter.handleSelectPictureButtonPressed();
                        }
                    }).show();
        });
        mainLayout.addView(selector);

        firstView = new MaterialInput(this, "First Name");
        lastView = new MaterialInput(this, "Last Name");
        numberView = new MaterialInput(this, "Number");
        numberView.setInputType(InputType.TYPE_CLASS_PHONE);
        emailView = new MaterialInput(this, "Email");
        emailView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);


        presenter = new NewOrEditContactPresenter(this, contactId);


        mainLayout.addView(firstView);
        mainLayout.addView(lastView);
        mainLayout.addView(numberView);
        mainLayout.addView(emailView);

        MaterialButton saveButton = new MaterialButton(this);
        saveButton.setText("Save");
        saveButton.setOnClickListener((view) -> {
            firstView.setErrorEnabled(false);
            numberView.setErrorEnabled(false);
            emailView.setErrorEnabled(false);

            presenter.saveContact(
                    contactId,
                    firstView.getText().toString(),
                    lastView.getText().toString(),
                    numberView.getText().toString(),
                    emailView.getText().toString(),
                    selector.getImageUri()
            );
        });

        MaterialButton cancelButton = new MaterialButton(this, null, R.attr.borderlessButtonStyle);
        cancelButton.setText("Cancel");
        cancelButton.setOnClickListener((view) -> {
            presenter.handleCancelPressed();
        });

        LinearLayout buttonsLayout = new LinearLayout(this);
        buttonsLayout.setGravity(Gravity.RIGHT);
        buttonsLayout.setPadding(48, 0, 48, 0);
        buttonsLayout.addView(cancelButton);
        buttonsLayout.addView(saveButton);

        mainLayout.addView(buttonsLayout);

        setContentView(mainLayout);

        if (contactId != NewOrEditContactPresenter.DEFAULT_CONTACT_ID) {
            presenter.handleEditContact(contactId);
        }
    }

    @Override
    public void goBack(Contact contact) {
        if (contact == null) {
            setResult(Activity.RESULT_CANCELED, null);
        } else {
            Intent intent = new Intent();
            intent.putExtra("result", contact);
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

    @Override
    public void goToPhotos() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void displayPicture(String pictureUri) {
        selector.setImageUri(pictureUri);
    }

    @Override
    public void populateTextFields(Contact contact) {
        runOnUiThread(() ->{
            firstView.setText(contact.first);
            lastView.setText(contact.last);
            emailView.setText(contact.email);
            numberView.setText(contact.number);
            selector.setImageUri(contact.pictureUri);
        });
    }

    @Override
    public void goToCamera() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "JPEG_" + timeStamp + ".jpg";

        File imageFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
        currentFilePath = imageFile.getAbsolutePath();

        Uri imageUri = FileProvider.getUriForFile(
                this,
                "com.example.contact.provider",
                imageFile
        );

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @Override
    public void displayFirstError() {
        firstView.setErrorEnabled(true);
        firstView.setError("First name cannot be blank");
        Snackbar.make(mainLayout, "First name cannot be blank", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void displayNumberError() {
        numberView.setErrorEnabled(true);
        numberView.setError("Number cannot be blank");
        Snackbar.make(mainLayout, "Number cannot be blank", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void displayEmailError() {
        emailView.setErrorEnabled(true);
        emailView.setError("Email must contain '@'");
        Snackbar.make(mainLayout, "Email must contain '@'", Snackbar.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri pictureUri = data.getData();
            presenter.handlePictureSelected(pictureUri.toString());
        }
        if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            presenter.handlePictureSelected(currentFilePath);
        }
    }
}
