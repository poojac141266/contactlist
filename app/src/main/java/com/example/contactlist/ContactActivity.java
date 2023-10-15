package com.example.contactlist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
public class ContactActivity extends AppCompatActivity {

    private EditText enterName;
    private EditText enterPhone;
    private EditText enterEmail;
    private Button updateButton;
    private Button deleteButton;
    private Button capturePhotoButton;  // New button for capturing photos
    private ImageView contactPhotoImageView;  // To display the captured photo
    private ContactModel contact;
    private DatabaseHelper databaseHelper;
    private static final int CAMERA_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        enterName = findViewById(R.id.enter_name);
        enterPhone = findViewById(R.id.enter_phone);
        enterEmail = findViewById(R.id.enter_email);
        updateButton = findViewById(R.id.update_button);
        deleteButton = findViewById(R.id.delete_button);

        contact = (ContactModel) getIntent().getSerializableExtra("CONTACT");

        enterName.setText(contact.getName());
        enterPhone.setText(contact.getPhone());
        enterEmail.setText(contact.getEmail());

        databaseHelper = new DatabaseHelper(this);
        capturePhotoButton = findViewById(R.id.capture_photo_button);
        contactPhotoImageView = findViewById(R.id.contact_photo_imageview);

        capturePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the camera for capturing a photo
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = enterName.getText().toString();
                String phone = enterPhone.getText().toString();
                String email = enterEmail.getText().toString();

                if(!name.isEmpty() && !phone.isEmpty() && !email.isEmpty()) {
                    contact.setName(name);
                    contact.setPhone(phone);
                    contact.setEmail(email);

                    databaseHelper.updateContact(contact);

                    Intent intent = new Intent(ContactActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ContactActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.deleteContact(contact.getId());

                Intent intent = new Intent(ContactActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Handle the captured photo
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = (Bitmap) extras.get("data");
                if (photo != null) {
                    // Set the captured photo to the ImageView
                    contactPhotoImageView.setImageBitmap(photo);
                }
            }
        }
    }
}