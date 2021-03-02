package com.example.cs2410_hogan_matthew_assn6.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.cs2410_hogan_matthew_assn6.R;


public class CircleDisplay extends AppCompatImageView {
    Paint paint = new Paint();
    String letter;
    int width = 40;
    public CircleDisplay(Context context, String letter) {
        super(context);
        float density = getResources().getDisplayMetrics().density;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int)(width*density) , (int)(width*density));
        this.letter = letter.toUpperCase();
        setLayoutParams(params);
    }

    @Override
    public void onDraw(Canvas canvas) {
        int halfWidth = width / 2;
        paint.setColor(getResources().getColor(R.color.purple_500, null));
        float density = getResources().getDisplayMetrics().density;
        canvas.drawCircle(halfWidth * density, halfWidth * density, halfWidth * density, paint);
        paint.setColor(Color.WHITE);
        paint.setTextSize(24*density);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(letter, (halfWidth * density), (halfWidth * density) + halfWidth, paint);
    }

}
