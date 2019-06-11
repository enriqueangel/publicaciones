package co.com.ceiba.mobile.pruebadeingreso.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
import co.com.ceiba.mobile.pruebadeingreso.entity.Publication;
import co.com.ceiba.mobile.pruebadeingreso.rest.Endpoints;

public class PostActivity extends Activity {

    JSONArray publications;
    Endpoints endpoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String userId = getIntent().getStringExtra("user");

        JsonArrayRequest arrReq = new JsonArrayRequest(
                Request.Method.GET,
                endpoints.URL_BASE + endpoints.GET_POST_USER + "userId=" + userId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        publications = response;
                        loadPublications();
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
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    }

}
