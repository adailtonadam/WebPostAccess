package br.com.adam.adailton.webpostaccess.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.adam.adailton.webpostaccess.R;
import br.com.adam.adailton.webpostaccess.activities.ThingsManagerActivity;
import br.com.adam.adailton.webpostaccess.pojo.Thing;

/**
 * Created by ad036950 on 30/07/2014.
 */
public class ThingsListAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private List<Thing> objectList;
    private Activity activity;


    public ThingsListAdapter(Activity activity,List<Thing> objectList) {
        super();
        inflater = LayoutInflater.from(activity);
        this.objectList = objectList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return objectList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        view = inflater.inflate(R.layout.adapter_things_list, parent, false);

        TextView name = (TextView) view.findViewById(R.id.adapter_textview_name);
        TextView type = (TextView) view.findViewById(R.id.adapter_textview_type);
        TextView date = (TextView) view.findViewById(R.id.adapter_textview_date);
        TextView id = (TextView) view.findViewById(R.id.adapter_textview_semdados);
        name.setText(objectList.get(position).getNome());
        type.setText(objectList.get(position).getTipo());
        date.setText(objectList.get(position).getData());
        id.setText(String.valueOf(objectList.get(position).getId()));

        view.findViewById(R.id.adapter_things_list_layout).setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ThingsManagerActivity.class);
                intent.putExtra("thing_id",String.valueOf(objectList.get(position).getId()));
                activity.startActivity(intent);
            }
        });

        return view;
    }

}
