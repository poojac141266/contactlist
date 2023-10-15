package com.example.contactlist;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.ImageView;
import android.content.Intent;
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {

    private Context context;
    private List<ContactModel> contactList;

    public ContactAdapter(Context context, List<ContactModel> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
        return new ContactHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        ContactModel contact = contactList.get(position);

        holder.name.setText(contact.getName());
        holder.phone.setText(contact.getPhone());
        holder.email.setText(contact.getEmail());
        if (contact.getPhoto() != null) {
            // Load contact photo from database
            byte[] photo = contact.getPhoto();
            Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
            holder.ivPhoto.setImageBitmap(bitmap);
        } else {
            holder.ivPhoto.setImageResource(R.drawable.ic_launcher_background);
        }
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivPhoto;
        TextView name, phone, email;

        public ContactHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            phone = itemView.findViewById(R.id.tvPhone);
            email = itemView.findViewById(R.id.tvEmail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ContactModel contact = contactList.get(position);

            Intent intent = new Intent(context, ContactActivity.class);
            intent.putExtra("ACTION_TYPE", "Update");
            intent.putExtra("CONTACT_ID", String.valueOf(contact.getId()));
            context.startActivity(intent);
        }
    }
}
