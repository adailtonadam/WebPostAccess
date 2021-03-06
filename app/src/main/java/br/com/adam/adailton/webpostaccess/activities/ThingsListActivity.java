package br.com.adam.adailton.webpostaccess.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import br.com.adam.adailton.webpostaccess.R;
import br.com.adam.adailton.webpostaccess.adapters.ThingsListAdapter;
import br.com.adam.adailton.webpostaccess.pojo.Thing;
import br.com.adam.adailton.webpostaccess.volley.WebAccessController;

public class ThingsListActivity extends Activity implements
        Response.Listener<JSONArray>,
        Response.ErrorListener  {


    static final String url = "things/getall_things.php";

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
    }

    private void setAdapter(){

        ListView list = (ListView)findViewById(R.id.thigsListView);

        if (things != null && things.size() != 0) {
            findViewById(R.id.adapter_textview_semdados).setVisibility(View.GONE);
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
        createList(response.toString());
    }


    void createList(String value){
        Gson gson = new Gson();
        things = Arrays.asList(gson.fromJson(value, Thing[].class));
        if(pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
        setAdapter();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //VolleyLog.d(TAG, "Error: " + error.getMessage());
        if(pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Http error");
        alertDialog.setMessage("Houve um erro ao carregar dados do servidor http");
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                getResources().getString(R.string.activity_things_manager_button_error_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
      //  alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }


    public void getList() {
        String tag_json_arry = "json_array_req";


        Cache cache = WebAccessController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(MainActivity.baseUrl + url);
        if(entry != null){
            try {
                String data = new String(entry.data, "UTF-8");
                createList(data);
               if(!entry.refreshNeeded()) {
                   return;
               }
            } catch (UnsupportedEncodingException e) {
            }
        }
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getResources().getString(R.string.activity_things_list_msg_loading));
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(MainActivity.baseUrl +url, this, this);
        req.setShouldCache(true);
        // Adding request to request queue
        WebAccessController.getInstance().addToRequestQueue(req, tag_json_arry);
    }

}
