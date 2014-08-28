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
import java.nio.charset.Charset;
import java.util.logging.Logger;

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
import android.util.Log;
import android.widget.Toast;


/**
 * Created by ad036950 on 20/08/2014.
 */
public class ImageUploadTask extends AsyncTask<Void, Void, String> {
    private String webAddressToPost = "";
    private Activity activity = null;
    // private ProgressDialog dialog;
    private ProgressDialog dialog = null;
    private Bitmap bitmap;
    private String itemId;



    public ImageUploadTask(Activity activity, String webAddressToPost, Bitmap bitmap, String itemId) {
        super();
        this.activity = activity;
        this.webAddressToPost = webAddressToPost;
        this.bitmap = bitmap;
        this.itemId = itemId;
    }

    @Override
    protected void onPreExecute() {
        if (dialog == null) {
            dialog = new ProgressDialog(activity);
        }
        dialog.setMessage("Uploading...");
        dialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        String sResponse = "";
        Exception ex;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(webAddressToPost);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, 100, bos);
            byte[] data = bos.toByteArray();
            String file = Base64.encodeToString(data, Base64.DEFAULT);

            StringBody fileBody = new StringBody(file, ContentType.TEXT_PLAIN);
            builder.addPart("image", fileBody);

            StringBody itemIdField = new StringBody(itemId, ContentType.TEXT_PLAIN);

            builder.addPart("id", itemIdField);
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost, localContext);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));

             sResponse = reader.readLine();
        } catch (Exception e) {
            ex = e;
            sResponse =  e.getMessage();
        }
        return sResponse;
    }

    @Override
    protected void onPostExecute(String result) {
        dialog.dismiss();
         Toast.makeText(activity.getApplicationContext(), "file uploaded " + result, Toast.LENGTH_LONG).show();
    }
}
