package com.example.contactlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ContactModel> contactList;
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private DatabaseHelper databaseHelper;

    private EditText enterName;
    private EditText enterPhone;
    private EditText enterEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterName = findViewById(R.id.enter_name);
        enterPhone = findViewById(R.id.enter_phone);
        enterEmail = findViewById(R.id.enter_email);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactList = new ArrayList<>();

        databaseHelper = new DatabaseHelper(this);

        loadContacts();
    }

    private void loadContacts() {
        contactList.addAll(databaseHelper.getAllContacts());
        contactAdapter = new ContactAdapter(this, contactList);
        recyclerView.setAdapter(contactAdapter);
    }

    public void saveContact(View view) {
        String name = enterName.getText().toString();
        String phone = enterPhone.getText().toString();
        String email = enterEmail.getText().toString();

        if(!name.isEmpty() && !phone.isEmpty() && !email.isEmpty()) {
            byte[] defaultPhoto = getByteArrayFromDrawable(R.drawable.ic_launcher_background);
            long id = databaseHelper.insertContact(name, phone, email, defaultPhoto);
            ContactModel contact = new ContactModel(id, name, phone, email, defaultPhoto);
            contactList.add(contact);

            contactAdapter.notifyDataSetChanged();

            enterName.getText().clear();
            enterPhone.getText().clear();
            enterEmail.getText().clear();
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }
    private byte[] getByteArrayFromDrawable(int drawableId) {
        Drawable drawable = getResources().getDrawable(drawableId);
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}