package com.example.activosapp;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
//import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
//import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
//import com.herprogramacion.lawyersapp.R;
//import com.herprogramacion.lawyersapp.data.LawyersContract.LawyerEntry;

/**
 * Adaptador de abogados
 */
public class ActivosCursorAdapter extends CursorAdapter {
    public ActivosCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item_activo, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
//"_id","nombre", "areid", "est","ctrl"
        // Referencias UI.
        TextView nameText = (TextView) view.findViewById(R.id.tv_nombre_activo);
        TextView areaText = (TextView) view.findViewById(R.id.tv_area_activo);
        TextView estadoText = (TextView) view.findViewById(R.id.tv_estado_activo);
        TextView variableControlText = (TextView) view.findViewById(R.id.tv_variable_control_activo);
        final ImageView avatarImage = (ImageView) view.findViewById(R.id.iv_avatar);

        // Get valores.
        String name = cursor.getString(cursor.getColumnIndex("nombre"));
        String area = cursor.getString(cursor.getColumnIndex("areid"));
        String estado = cursor.getString(cursor.getColumnIndex("est"));
        switch (estado){
            case "A":
                estado="Estado: Activo";
                break;
            case "I":
                estado="Estado: Inactivo";
                break;
        }
        String variableControl = cursor.getString(cursor.getColumnIndex("ctrl"));
//        String avatarUri = cursor.getString(cursor.getColumnIndex("imagen_activo"));

        // Setup.
        nameText.setText(name);
        areaText.setText("Area: "+area);
        estadoText.setText(estado);
        variableControlText.setText("Variable: "+variableControl);
        Glide
                .with(context)
                .load(R.drawable.ic_activo_circle)
                .asBitmap()
                .error(R.drawable.ic_activo_circle)
                .centerCrop()
                .into(new BitmapImageViewTarget(avatarImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable
                                = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        drawable.setCircular(true);
                        avatarImage.setImageDrawable(drawable);
                    }
                });

    }

}
