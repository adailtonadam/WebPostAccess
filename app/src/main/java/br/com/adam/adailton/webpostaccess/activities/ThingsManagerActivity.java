package br.com.adam.adailton.webpostaccess.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import br.com.adam.adailton.webpostaccess.R;
import br.com.adam.adailton.webpostaccess.volley.WebAccessController;
import br.com.adam.adailton.webpostaccess.webUpload.*;




public class ThingsManagerActivity extends Activity implements
        Response.Listener<String>,
        Response.ErrorListener {

    String currentEditId = null;
    boolean imageAltered = false;
    boolean imageUploaded = false;
    EditText name;
    EditText type;
    ProgressDialog pDialog = null;
    boolean deleting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_things_manager);
        currentEditId = getIntent().getStringExtra("thing_id");

        name = (EditText) findViewById(R.id.activity_things_manager_name);
        type = (EditText) findViewById(R.id.activity_things_manager_type);

        String editingText;
        if (currentEditId != null) {
            editingText = getResources().getString(R.string.activity_things_manager_editing) + " " + currentEditId;

            if(getIntent().getStringExtra("thing_name") != null){
                name.setText(getIntent().getStringExtra("thing_name"));
            }
            if(getIntent().getStringExtra("thing_type") != null){
                type.setText(getIntent().getStringExtra("thing_type"));
            }

        } else {
            editingText = getResources().getString(R.string.activity_things_manager_adding);
            findViewById(R.id.activity_things_manager_button_remove).setVisibility(View.GONE);
        }
        ((TextView) findViewById(R.id.activity_things_manager_editing)).setText(editingText);
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

    public void onButtonRemoveClick(View v) {
        String tag_json_obj = "json_delete";
        String url;
        if (currentEditId == null) {
          return;
        }
        deleting =  true;
        url = MainActivity.baseUrl + "things/delete_thing.php";
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getResources().getString(R.string.activity_things_manager_msg_loading));
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, url, this, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", currentEditId);
                return params;
            }

        };

        // Adding request to request queue
        WebAccessController.getInstance().addToRequestQueue(req, tag_json_obj);
    }

    public void onButtonSaveClick(View v) {

        boolean canSave = true;
        if (type.getText().toString().trim().isEmpty()) {
            type.setVisibility(View.INVISIBLE);
            type.setBackgroundColor(Color.YELLOW);
            type.setVisibility(View.VISIBLE);
            canSave = false;
        }
        if (name.getText().toString().trim().isEmpty()) {
            name.setVisibility(View.INVISIBLE);
            name.setBackgroundColor(Color.YELLOW);
            name.setVisibility(View.VISIBLE);
            canSave = false;
        }
        if (!canSave) {
            return;
        }
        save(name.getText().toString().trim(), type.getText().toString().trim());
    }

    @Override
    public void onResponse(String response) {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
        if (response.compareTo("Ok") == 0) {

            String message;
            if(deleting){
               message =  getResources().getString(R.string.activity_things_manager_msg_deleted_ok);
            } else if(currentEditId != null){
                message = getResources().getString(R.string.activity_things_manager_msg_edited_ok);
            } else {
                message =  getResources().getString(R.string.activity_things_manager_msg_inserted_ok);
            }
            if(imageAltered){
                if(!imageUploaded) {
                    if(currentEditId != null) {
                        uploadImage(currentEditId);
                        return;
                    } else{

                        //retornar o id criado junto com o ok
                       // uploadImage(currentEditId);
                        //return;
                    }


                }
            }
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
            WebAccessController.getInstance().getRequestQueue().getCache().invalidate(MainActivity.baseUrl + ThingsListActivity.url, true);
            finish();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getResources().getString(R.string.activity_things_manager_msg_operation_error));
            if(deleting){
                alertDialog.setMessage(getResources().getString(R.string.activity_things_manager_msg_deleted_nok));
            } else if(currentEditId != null){
                alertDialog.setMessage(getResources().getString(R.string.activity_things_manager_msg_edited_nok));
            } else {
                alertDialog.setMessage(getResources().getString(R.string.activity_things_manager_msg_inserted_nok));
            }
            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                    getResources().getString(R.string.activity_things_manager_button_error_ok),
                    new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //VolleyLog.d(TAG, "Error: " + error.getMessage());
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getResources().getString(R.string.activity_things_manager_msg_http_error));
        alertDialog.setMessage(getResources().getString(R.string.activity_things_manager_msg_http_error1));

        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                getResources().getString(R.string.activity_things_manager_button_error_ok),
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }


    private void save(final String name, final String type) {
        String tag_json_obj = "json_insert";
        String url;
        if (currentEditId == null) {
            url = MainActivity.baseUrl + "things/insert_thing.php";
        } else {
            url = MainActivity.baseUrl + "things/update_thing.php";
        }

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getResources().getString(R.string.activity_things_manager_msg_loading));
        pDialog.show();

        StringRequest req = new StringRequest(Request.Method.POST, url, this, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nome", name);
                params.put("tipo", type);
                if (currentEditId != null) {
                    params.put("id", currentEditId);
                }
                return params;
            }

        };

        WebAccessController.getInstance().addToRequestQueue(req, tag_json_obj);
    }

    private void uploadImage(String id) {
        Bitmap bp;
        ImageView image = (ImageView) findViewById(R.id.activity_things_manager_imageView);
        bp =  ((BitmapDrawable)image.getDrawable()).getBitmap();
        String url;
        url = MainActivity.baseUrl + "things/insert_thing_image.php";
        ImageUploadTask imageUploadTask =  new ImageUploadTask(this,url,bp,id);
        imageUploadTask.execute();

        /*
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getResources().getString(R.string.activity_things_manager_msg_loading));
        pDialog.show();

        StringRequest req = new   ImageRequest(Request.Method.POST, url, this, this) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
            //    params.put("image", type);
                if (currentEditId != null) {
                    params.put("id", currentEditId);
                }
                return params;
            }

        };

        WebAccessController.getInstance().addToRequestQueue(req, tag_json_obj);
        */
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageAltered = true;
        Bitmap bp = (Bitmap) data.getExtras().get("data");

        ImageView img =(ImageView)findViewById(R.id.activity_things_manager_imageView);
        img.setImageBitmap(bp);
    }

    public void onButtonAddImageClick(View v) {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);
    }

}
