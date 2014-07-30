package br.com.adam.adailton.webpostaccess;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import br.com.adam.adailton.webpostaccess.R;

public class ThingsList extends Activity {

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


    public void getList(){
       // Telefone[] telefones = null;
        Gson gson = new Gson();
     /*   HttpResponse response = null;
        HttpGet getMethod = new HttpGet("http://www.cherobin.com.br/android/rest/listarTelefones.php");
        try {
            HttpClient httpClient = new DefaultHttpClient();
            response = httpClient.execute(getMethod);
            String result = EntityUtils.toString(response.getEntity());
            Log.e("response", result);
            telefones = gson.fromJson(result, Telefone[].class);

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Não foi possível baixar os telefones!", Toast.LENGTH_LONG).show();
            return false;
        }
        */
    }

}
