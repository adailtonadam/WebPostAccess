package br.com.adam.adailton.webpostaccess.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import br.com.adam.adailton.webpostaccess.R;


public class MainActivity extends Activity {

    //static final String baseUrl = "http://177.96.134.195/";
    static final String baseUrl = "http://adailtonadamdev.ddns.net/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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



    public void onButtonListar(View v){
        startActivity(new Intent(this,ThingsListActivity.class));
    }

    public void onButtonAdicionar(View v){
        startActivity(new Intent(this,ThingsManagerActivity.class));
    }
}

