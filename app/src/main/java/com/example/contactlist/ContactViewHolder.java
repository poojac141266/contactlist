package com.example.contactlist;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
public class ContactViewHolder extends RecyclerView.ViewHolder {
    public TextView contactName, contactPhone;
    public ImageView ivPhoto;

    public ContactViewHolder(View view) {
        super(view);
        contactName = view.findViewById(R.id.tvName);
        contactPhone = view.findViewById(R.id.tvPhone);
        ivPhoto = view.findViewById(R.id.ivPhoto);
    }
}