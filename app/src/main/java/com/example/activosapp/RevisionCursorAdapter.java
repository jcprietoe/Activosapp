package com.example.activosapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

public class RevisionCursorAdapter extends CursorAdapter {

    public RevisionCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item_revision, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
//"_id","nombre", "areid", "est","ctrl"
        // Referencias UI.
        TextView revisionText = (TextView) view.findViewById(R.id.tv_des_revision);
        CheckBox cbSi=(CheckBox) view.findViewById(R.id.cbrevisionSi);
        CheckBox cbNo=(CheckBox) view.findViewById(R.id.cbrevisionNo);

        // Get valores.
        String desRevision = cursor.getString(cursor.getColumnIndex("des_revision"));


        // Setup.
        revisionText.setText(desRevision);


    }

}


