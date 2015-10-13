package com.example.android.sapo.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.sapo.app.R;
import com.example.android.sapo.app.datatypes.DataTienda;

import java.util.ArrayList;

/**
 * Created by Alejandro on 13-Oct-15.
 */
public class CategoriaAdapter extends ArrayAdapter<DataTienda> {

    private static class ViewHolder {
        private TextView itemView;
    }

    public CategoriaAdapter(Context context, int textViewResourceId, ArrayList<DataTienda> items) {
        super(context, textViewResourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.list_item_tiendas, parent, false);


            viewHolder.itemView = (TextView) convertView.findViewById(R.id.list_item_tienda);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DataTienda item = getItem(position);
        if (item!= null) {
            // My layout has only one TextView
            // do whatever you want with your string and long
            //viewHolder.itemView.setText(String.format("%d %s", item.getId(), item.getNombre()));
            viewHolder.itemView.setText(item.getNombre());
        }

        return convertView;
    }
}
