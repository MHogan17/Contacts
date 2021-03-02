package com.example.cs2410_hogan_matthew_assn6.components;

import android.content.Context;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.cs2410_hogan_matthew_assn6.R;

public class ImageSelector extends FrameLayout {
    AppCompatImageView imageView;
    String imageUri;
    public ImageSelector(Context context) {
        this(context, "");
    }

    public ImageSelector(Context context, String imageUri) {
        super(context);
        this.imageUri = imageUri;
        setBackgroundColor(getResources().getColor(R.color.design_default_color_secondary_variant, null));
        imageView = new AppCompatImageView(context);
        if (imageUri.equals("")) {
            imageView.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
        } else {
            imageView.setImageURI(Uri.parse(imageUri));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 480);
        imageView.setLayoutParams(params);
        addView(imageView);
    };

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
        if (imageUri.equals("")) {
            imageView.setImageResource(R.drawable.ic_baseline_person_24);
        }
        else {
            imageView.setImageURI(Uri.parse(imageUri));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    public String getImageUri() {
        return imageUri;
    }
}