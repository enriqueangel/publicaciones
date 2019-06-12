package co.com.ceiba.mobile.pruebadeingreso.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.com.ceiba.mobile.pruebadeingreso.R;
import co.com.ceiba.mobile.pruebadeingreso.adapter.UserAdapter;
import co.com.ceiba.mobile.pruebadeingreso.data.User;
import co.com.ceiba.mobile.pruebadeingreso.data.UserDbHelper;
import co.com.ceiba.mobile.pruebadeingreso.rest.Endpoints;
import dmax.dialog.SpotsDialog;

public class MainActivity extends Activity {

    AlertDialog dialog;
    ArrayList<User> userList;
    Endpoints endpoints;
    JSONArray users;
    RecyclerView content;
    UserAdapter userAdapter;
    UserDbHelper mUserDbHelper;
    LayoutInflater mInflate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText search = findViewById(R.id.editTextSearch);
        mInflate = LayoutInflater.from(this);

        content = findViewById(R.id.recyclerViewSearchResults);
        content.setHasFixedSize(true);

        LinearLayoutManager linear =  new LinearLayoutManager(this);
        linear.setOrientation(LinearLayoutManager.VERTICAL);
        content.setLayoutManager(linear);

        mUserDbHelper = new UserDbHelper(this);
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Cargando")
                .build();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<User> filterList;
                if (s.toString().isEmpty()) {
                    filterList = userList;
                } else {
                    filterList = filter(userList, s.toString());
                }

                userAdapter.setFilter(filterList);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final View emptyView = mInflate.inflate(R.layout.empty_view, null, false);

        dialog.show();
        getUsersWs();

        /*userAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (userAdapter.getItemCount() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    emptyView.setVisibility(View.GONE);
                }
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void getUsersWs() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest arrReq = new JsonArrayRequest(
                Request.Method.GET,
                endpoints.URL_BASE + endpoints.GET_USERS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        users = response;
                        loadUsers();
                        Log.d("Users", "Response JSON: ");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Users", "Error Response JSON: " + error.getMessage());
                    }
                });

        requestQueue.add(arrReq);
    }

    private void loadUsers() {
        userList = new ArrayList<>();

        Cursor usersListDb = mUserDbHelper.getAllUsers();
        int cantUserDb = usersListDb.getCount();

        try {
            if (cantUserDb == 0){
                for (int i = 0; i < users.length(); i++) {
                    JSONObject user = users.getJSONObject(i);
                    int id = user.getInt("id");
                    String name = user.getString("name"),
                            email = user.getString("email"),
                            phone = user.getString("phone");
                    User userData = new User(id, name, email, phone);
                    mUserDbHelper.createUser(userData);
                    userList.add(userData);
                }
            } else {
                while (usersListDb.moveToNext()) {
                    User userData = new User(
                            usersListDb.getInt(usersListDb.getColumnIndex("id")),
                            usersListDb.getString(usersListDb.getColumnIndex("name")),
                            usersListDb.getString(usersListDb.getColumnIndex("email")),
                            usersListDb.getString(usersListDb.getColumnIndex("phone"))
                    );
                    userList.add(userData);
                }
            }

            userAdapter = new UserAdapter(userList);
            content.setAdapter(userAdapter);
        } catch (JSONException e){
            e.printStackTrace();
        }

        dialog.dismiss();
    }

    private ArrayList<User> filter(ArrayList<User> list, String text) {
        ArrayList<User> filterList = new ArrayList<>();

        text = text.toLowerCase();
        for(User user : list) {
            if (user.getName().toLowerCase().contains(text)) {
                filterList.add(user);
            }
        }

        return filterList;
    }

}