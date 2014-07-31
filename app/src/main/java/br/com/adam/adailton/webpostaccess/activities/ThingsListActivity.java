package br.com.adam.adailton.webpostaccess.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

import br.com.adam.adailton.webpostaccess.R;
import br.com.adam.adailton.webpostaccess.models.Thing;
import br.com.adam.adailton.webpostaccess.volley.WebAccessController;

public class ThingsListActivity extends Activity {

    List<Thing> things;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_things_list);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.things_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void getList() {

        final Gson gson = new Gson();

      /*  HttpResponse response = null;
        HttpGet getMethod = new HttpGet("http://adailtonadamdev.ddns.net/things/getall_things.php");
        try {
            HttpClient httpClient = new DefaultHttpClient();
            response = httpClient.execute(getMethod);
            String result = EntityUtils.toString(response.getEntity());
            things = Arrays.asList(gson.fromJson(result, Thing[].class));


        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Não foi possível baixar os telefones!", Toast.LENGTH_LONG).show();
            return null ;
        }

*/
        String tag_json_arry = "json_array_req";
        String url = "http://adailtonadamdev.ddns.net/things/getall_things.php";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Log.d(TAG, response.toString());
                        things = Arrays.asList(gson.fromJson(response.toString(), Thing[].class));
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        }
        );

        // Adding request to request queue
        WebAccessController.getInstance().addToRequestQueue(req, tag_json_arry);


    }

}
