package co.com.ceiba.mobile.pruebadeingreso.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.com.ceiba.mobile.pruebadeingreso.R;
import co.com.ceiba.mobile.pruebadeingreso.adapter.UserAdapter;
import co.com.ceiba.mobile.pruebadeingreso.entity.User;
import co.com.ceiba.mobile.pruebadeingreso.rest.Endpoints;

public class MainActivity extends Activity {

    JSONArray users;
    ArrayList<User> userList;
    Endpoints endpoints;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest arrReq = new JsonArrayRequest(
                Request.Method.GET,
                endpoints.URL_BASE + endpoints.GET_USERS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        users = response;
                        loadUsers();
                        Log.d("Users", "Respuesta en JSON: ");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Users", "Error Respuesta en JSON: " + error.getMessage());
                    }
                });

        requestQueue.add(arrReq);

        EditText search = findViewById(R.id.editTextSearch);

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
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void loadUsers() {
        userList = new ArrayList<>();

        try {
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                String id = user.getString("id"),
                        name = user.getString("name"),
                        email = user.getString("email"),
                        phone = user.getString("phone");

                userList.add(new User(id, name, email, phone));
            }

            RecyclerView content = findViewById(R.id.recyclerViewSearchResults);
            content.setHasFixedSize(true);

            LinearLayoutManager linear =  new LinearLayoutManager(this);
            linear.setOrientation(LinearLayoutManager.VERTICAL);

            userAdapter = new UserAdapter(userList);
            content.setAdapter(userAdapter);
            content.setLayoutManager(linear);
        } catch (JSONException e){
            e.printStackTrace();
        }
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