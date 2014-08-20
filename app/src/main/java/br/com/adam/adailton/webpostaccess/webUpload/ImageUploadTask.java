package br.com.adam.adailton.webpostaccess.webUpload;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.Image;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.entity.ContentType;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;


/**
 * Created by ad036950 on 20/08/2014.
 */
class ImageUploadTask extends AsyncTask<Void, Void, String> {
    private String webAddressToPost = "";
    private Activity activity = null;
    // private ProgressDialog dialog;
    private ProgressDialog dialog = null;
    private Bitmap bitmap;

    ImageUploadTask(Activity activity, String webAddressToPost) {
        super();
       this.activity = activity;
       this.webAddressToPost = webAddressToPost;
    }

    @Override
    protected void onPreExecute() {
        if(dialog == null){
            dialog = new ProgressDialog(activity);
        }
        dialog.setMessage("Uploading...");
        dialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(webAddressToPost);

           // MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, 100, bos);
            byte[] data = bos.toByteArray();
            String file = Base64.encodeToString(data, Base64.DEFAULT);

            StringBody fileBody = new StringBody(file,ContentType.); //image should be a String
            builder.addPart("uploaded", fileBody);
            builder.addPart("someOtherStringToSend",ContentType.DEFAULT_TEXT);
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost,localContext);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));

            String sResponse = reader.readLine();
            return sResponse;
        } catch (Exception e) {
            // something went wrong. connection with the server error
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        dialog.dismiss();
       // Toast.makeText(getApplicationContext(), "file uploaded",Toast.LENGTH_LONG).show();
    }
}
