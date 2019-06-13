package co.com.ceiba.mobile.pruebadeingreso.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

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
import co.com.ceiba.mobile.pruebadeingreso.adapter.PublicationAdapter;
import co.com.ceiba.mobile.pruebadeingreso.data.Publication;
import co.com.ceiba.mobile.pruebadeingreso.data.UserDbHelper;
import co.com.ceiba.mobile.pruebadeingreso.rest.Endpoints;

public class PostActivity extends Activity {

    AlertDialog dialog;
    Endpoints endpoints;
    JSONArray publications;
    UserDbHelper mUserDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mUserDbHelper = new UserDbHelper(this);
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        mDialog.setMessage("Cargando...");
        dialog = mDialog.create();
        dialog.show();
        TextView message = dialog.findViewById(android.R.id.message);
        message.setGravity(Gravity.CENTER_HORIZONTAL);

        String userId = getIntent().getStringExtra("user");

        getUser(userId);
        getPublicationsWs(userId);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void getUser (String userId) {
        TextView name = findViewById(R.id.name),
                email = findViewById(R.id.email),
                phone = findViewById(R.id.phone);

        Cursor userData = mUserDbHelper.getUser(userId);
        if (userData.moveToFirst()) {
            name.setText(userData.getString(userData.getColumnIndex("name")));
            email.setText(userData.getString(userData.getColumnIndex("email")));
            phone.setText(userData.getString(userData.getColumnIndex("phone")));
        }
    }

    private void getPublicationsWs(String userId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest arrReq = new JsonArrayRequest(
                Request.Method.GET,
                endpoints.URL_BASE + endpoints.GET_POST_USER + "userId=" + userId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        publications = response;
                        loadPublications();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Publications", "Error Response JSON: " + error.getMessage());
                    }
                });

        requestQueue.add(arrReq);
    }

    private void loadPublications() {
        ArrayList<Publication> publicationList = new ArrayList<Publication>();

        try {
            for (int i = 0; i < publications.length(); i++) {
                JSONObject user = publications.getJSONObject(i);
                String title = user.getString("title"),
                        body = user.getString("body");

                publicationList.add(new Publication(title, body));
            }

            RecyclerView content = findViewById(R.id.recyclerViewPostsResults);
            content.setHasFixedSize(true);

            LinearLayoutManager linear =  new LinearLayoutManager(this);
            linear.setOrientation(LinearLayoutManager.VERTICAL);

            content.setAdapter(new PublicationAdapter(publicationList));
            content.setLayoutManager(linear);
        } catch (JSONException e){
            e.printStackTrace();
        }

        dialog.dismiss();
    }

}
