package com.example.cs2410_hogan_matthew_assn6.components;


import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.cs2410_hogan_matthew_assn6.R;
import com.example.cs2410_hogan_matthew_assn6.models.Contact;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

public class ContactCard extends MaterialCardView {
    public FloatingActionButton fab;
    private Contact contact;
    ImageView imageView;
    LinearLayout header;
    MaterialButton call;
    MaterialButton text;
    MaterialButton email;
    LinearLayout mainLayout;

    public ContactCard(Context context, Contact contact) {
        super(context);
        this.contact = contact;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(48, 24, 48, 24);
        setElevation(24);
        setLayoutParams(params);
        mainLayout = new LinearLayout(context);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        header = new LinearLayout(context);
        header.setOrientation(LinearLayout.VERTICAL);
        LinearLayout body = new LinearLayout(context);
        body.setPadding(64, 32, 64, 32);
        body.setOrientation(LinearLayout.VERTICAL);
        LinearLayout footer = new LinearLayout(context);

        mainLayout.addView(header);
        mainLayout.addView(body);
        mainLayout.addView(footer);

        addView(mainLayout);

        //Header
        imageView = new AppCompatImageView(context);
        displayImage();

        //Body

        MaterialTextView nameView = new MaterialTextView(context, null, R.attr.textAppearanceHeadline6);
        nameView.setText(contact.first + " " + contact.last);
        nameView.setTextSize(30);
        body.addView(nameView);

        MaterialTextView numberView = new MaterialTextView(context);
        numberView.setText(contact.number);
        body.addView(numberView);
        numberView.setTextSize(18);

        MaterialTextView emailView = new MaterialTextView(context);
        emailView.setText(contact.email);
        emailView.setTextSize(18);
        body.addView(emailView);
        emailView.setMaxLines(3);
        emailView.setEllipsize(TextUtils.TruncateAt.END);
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentParams.setMargins(0, 12, 0, 0);
        emailView.setLayoutParams(contentParams);

        //Footer
        LinearLayout.LayoutParams footerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        footer.setGravity(Gravity.CENTER);
        footer.setLayoutParams(footerParams);
        call = new MaterialButton(context, null, R.attr.borderlessButtonStyle);
        text = new MaterialButton(context, null, R.attr.borderlessButtonStyle);
        email = new MaterialButton(context, null, R.attr.borderlessButtonStyle);
        footer.addView(call);
        footer.addView(text);
        footer.addView(email);
        call.setText("Call");
        text.setText("Text");
        email.setText("Email");

        //FAB
        fab = new FloatingActionButton(context);
        FrameLayout.LayoutParams fabParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fabParams.setMargins(0, 700, 100, 0);
        fabParams.gravity = (Gravity.END | Gravity.TOP);
        fab.setLayoutParams(fabParams);
        fab.setImageResource(R.drawable.ic_baseline_edit_24);
        addView(fab);
    }

    public void setFabOnClickListener(OnClickListener l) {
        fab.setOnClickListener(l);
    }
    public void setCallPressListener(OnClickListener l) {
        call.setOnClickListener(l);
    }
    public void setTextPressListener(OnClickListener l) {
        text.setOnClickListener(l);
    }
    public void setEmailPressListener(OnClickListener l){
        email.setOnClickListener(l);
    }
    public void displayImage(){
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 900);
        if (contact.pictureUri.equals("")) {
            imageView.setImageResource(R.drawable.ic_baseline_tag_faces_24);
            imageView.setBackgroundColor(getResources().getColor(R.color.purple_500));
        } else {
            imageView.setImageURI(Uri.parse(contact.pictureUri));
        }
        imageView.setLayoutParams(imageParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        header.addView(imageView);
    }
    public void setContact(Contact contact){
        displayImage();
    }
}

