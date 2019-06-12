package co.com.ceiba.mobile.pruebadeingreso.data;

import android.content.ContentValues;

public class User {
    private int id;
    private String name;
    private String email;
    private String phone;

    public User(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("email", email);
        values.put("phone", phone);
        return values;
    }
}
