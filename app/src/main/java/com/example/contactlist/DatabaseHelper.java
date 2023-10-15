package com.example.contactlist;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "contact.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CONTACT = "contact";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHOTO = "photo";

    private static final String CREATE_TABLE_CONTACT =
            "CREATE TABLE " + TABLE_CONTACT + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_PHONE + " TEXT, "
                    + COLUMN_EMAIL + " TEXT, "
                    + COLUMN_PHOTO + " BLOB"
                    + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_CONTACT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        onCreate(sqLiteDatabase);
    }

    public long insertContact(String name, String phone, String email, byte[] photo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHOTO, photo);

        long id = db.insert(TABLE_CONTACT, null, values);
        db.close();
        return id;
    }

    public List<ContactModel> getAllContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ContactModel> contactList = new ArrayList<ContactModel>();

        String[] columns = { COLUMN_ID, COLUMN_NAME, COLUMN_PHONE, COLUMN_EMAIL, COLUMN_PHOTO };
        Cursor cursor = db.query(TABLE_CONTACT, columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
            int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);
            int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
            int photoIndex = cursor.getColumnIndex(COLUMN_PHOTO);

            do {
                long id = cursor.getLong(idIndex);
                String name = cursor.getString(nameIndex);
                String phone = cursor.getString(phoneIndex);
                String email = cursor.getString(emailIndex);
                byte[] photo = cursor.getBlob(photoIndex);

                ContactModel contact = new ContactModel(id, name, phone, email, photo);
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return contactList;
    }
    public void updateContact(ContactModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_PHONE, contact.getPhone());
        values.put(COLUMN_EMAIL, contact.getEmail());

        db.update(TABLE_CONTACT, values, COLUMN_ID + " = ?", new String[] { String.valueOf(contact.getId()) });

        db.close();
    }

    public void deleteContact(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACT, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
        db.close();
    }
}
