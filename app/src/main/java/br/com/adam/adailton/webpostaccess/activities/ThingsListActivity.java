package br.com.adam.adailton.webpostaccess.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import br.com.adam.adailton.webpostaccess.R;
import br.com.adam.adailton.webpostaccess.adapters.ThingsListAdapter;
import br.com.adam.adailton.webpostaccess.pojo.Thing;
import br.com.adam.adailton.webpostaccess.volley.WebAccessController;

public class ThingsListActivity extends Activity implements
        Response.Listener<JSONArray>,
        Response.ErrorListener  {

    List<Thing> things;
    ProgressDialog pDialog = null;

    @Override
    protected void onResume() {
        super.onResume();
        getList();
        setAdapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_things_list);
      //  getList();
    }

    private void setAdapter(){

        ListView list = (ListView)findViewById(R.id.thigsListView);

        if (things != null && things.size() != 0) {
            list.setAdapter(new ThingsListAdapter(this, things));
        }
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

    @Override
    public void onResponse(JSONArray response) {
        // Log.d(TAG, response.toString());
        Gson gson = new Gson();
        things = Arrays.asList(gson.fromJson(response.toString(), Thing[].class));
        if(pDialog != null) {
            pDialog.hide();
        }
        setAdapter();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //VolleyLog.d(TAG, "Error: " + error.getMessage());
        if(pDialog != null) {
            pDialog.hide();
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Http error");
        alertDialog.setMessage("Houve um erro ao carregar dados do servidor http");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Add your code for the button here.
            }
        });
      //  alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }


    public void getList() {


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

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(url,this,this);
        // Adding request to request queue
        WebAccessController.getInstance().addToRequestQueue(req, tag_json_arry);
    }

}
