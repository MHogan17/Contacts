package com.example.cs2410_hogan_matthew_assn6.components;

import android.content.Context;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.cs2410_hogan_matthew_assn6.models.Contact;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

public class ContactListItem extends MaterialCardView {
    MaterialTextView contactTextView;
    Context context;
    Contact contact;
    MaterialCardView imageView;
    int width = 40;
    float density = getResources().getDisplayMetrics().density;

    public ContactListItem(Context context, Contact contact, OnClickListener onClickListener) {
        super(context);
        this.context = context;
        this.contact = contact;
        imageView = new MaterialCardView(context);
        ViewGroup.LayoutParams iconParams = new ViewGroup.LayoutParams((int) (width * density), (int) (width * density));
        imageView.setRadius(50);
        imageView.setLayoutParams(iconParams);
        displayImage();
        addView(imageView);
        contactTextView = new MaterialTextView(context);
        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewParams.setMargins(120, 0, 0, 0);
        contactTextView.setLayoutParams(viewParams);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(10, 10, 10, 10);
        setLayoutParams(params);
        contactTextView.setText(contact.first + " " + contact.last);
        contactTextView.setTextSize(25);
        setOnClickListener(onClickListener);
        addView(contactTextView);
    }

    public void displayImage(){
        if (contact.pictureUri.equals("")) {
            CircleDisplay display = new CircleDisplay(context, contact.first.substring(0, 1));
            imageView.addView(display);

        } else {
            AppCompatImageView picture = new AppCompatImageView(context);
            picture.setScaleType(ImageView.ScaleType.CENTER_CROP);
            picture.setImageURI(Uri.parse(contact.pictureUri));
            imageView.addView(picture);
        }
    }

    public void setContact(Contact contact) {
        this.contact = contact;
        contactTextView.setText(contact.first + " " + contact.last);
        displayImage();
    }
}
