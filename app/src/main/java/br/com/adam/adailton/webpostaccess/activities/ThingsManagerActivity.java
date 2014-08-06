package br.com.adam.adailton.webpostaccess.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import br.com.adam.adailton.webpostaccess.R;
import br.com.adam.adailton.webpostaccess.pojo.Thing;
import br.com.adam.adailton.webpostaccess.volley.WebAccessController;

public class ThingsManagerActivity extends Activity implements
        Response.Listener<JSONObject>,
        Response.ErrorListener {

    String currentEditId = null;

    EditText name;
    EditText type;
    ProgressDialog pDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_things_manager);
        currentEditId = getIntent().getStringExtra("thing_id");

        name = (EditText)findViewById(R.id.activity_things_manager_name);
        type = (EditText)findViewById(R.id.activity_things_manager_type);

        String editingText;
        if(currentEditId != null) {
            editingText = getResources().getString(R.string.activity_things_manager_editing) + " " + currentEditId;
        } else {
            editingText = getResources().getString(R.string.activity_things_manager_adding);
        }
        ((TextView)findViewById(R.id.activity_things_manager_editing)).setText(editingText);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.things_manager, menu);
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

    public void onButtonSaveClick(View v){

        boolean canSave = true;
        if(type.getText().toString().trim().isEmpty()){
            type.setVisibility(View.INVISIBLE);
            type.setBackgroundColor(Color.YELLOW);
            type.setVisibility(View.VISIBLE);
            canSave = false;
        }
        if(name.getText().toString().trim().isEmpty()){
            name.setVisibility(View.INVISIBLE);
            name.setBackgroundColor(Color.YELLOW);
            name.setVisibility(View.VISIBLE);
            canSave = false;
            canSave = false;
        }
        if(!canSave){
            return;
        }
        save(name.getText().toString().trim(),type.getText().toString().trim());
    }

    @Override
    public void onResponse(JSONObject response) {
        // Log.d(TAG, response.toString());
        if(pDialog != null) {
            pDialog.hide();
        }
        if(response.toString().contains("Ok")) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Sucesso");
            alertDialog.setMessage("Item atualizado com sucesso");
            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertDialog.show();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Http error");
            alertDialog.setMessage("Houve um erro ao carregar dados do servidor http");
            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,"OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            //  alertDialog.setIcon(R.drawable.icon);
            alertDialog.show();
        }

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
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,"OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        //  alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }


    private void save(final String name, final String type){
        String tag_json_obj = "json_insert";
        String url;
        if(currentEditId == null) {
            url = "http://adailtonadamdev.ddns.net/things/insert_things.php";
        } else {
            url = "http://adailtonadamdev.ddns.net/things/update_things.php";
        }

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Carregando...");
        pDialog.show();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                url, null,this, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nome", name);
                params.put("tipo", type);
                if(currentEditId != null) {
                    params.put("id", currentEditId);
                }
                return params;
            }

        };

        // Adding request to request queue
        WebAccessController.getInstance().addToRequestQueue(req, tag_json_obj);
    }

    /*
    // Tag used to cancel the request
String tag_json_obj = "json_obj_req";

String url = "http://api.androidhive.info/volley/person_object.json";

ProgressDialog pDialog = new ProgressDialog(this);
pDialog.setMessage("Loading...");
pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        pDialog.hide();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "Androidhive");
                params.put("email", "abc@androidhive.info");
                params.put("password", "password123");

                return params;
            }

        };

// Adding request to request queue
AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    * */
}
