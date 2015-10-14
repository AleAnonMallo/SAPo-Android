package com.example.android.sapo.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.sapo.app.R;
import com.example.android.sapo.app.datatypes.DataCategoria;
import com.example.android.sapo.app.datatypes.DataProducto;

import java.util.ArrayList;

/**
 * Created by Alejandro on 14/10/2015.
 */
public class ProductoAdapter extends ArrayAdapter<DataProducto> {

    private static class ViewHolder {
        private TextView listProductoNombre;
        private TextView listProductoDescripcion;
    }

    public ProductoAdapter(Context context, int textViewResourceId, ArrayList<DataProducto> items) {
        super(context, textViewResourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.list_item_productos, parent, false);

            viewHolder.listProductoNombre = (TextView) convertView.findViewById(R.id.list_producto_nombre);
            viewHolder.listProductoDescripcion = (TextView) convertView.findViewById(R.id.list_producto_descripcion);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DataProducto item = getItem(position);
        if (item!= null) {
            // My layout has only one TextView
            // do whatever you want with your string and long
            //viewHolder.itemView.setText(String.format("%d %s", item.getId(), item.getNombre()));
            viewHolder.listProductoNombre.setText(item.getNombre());
            viewHolder.listProductoDescripcion.setText(item.getDescripcion().substring(0,7) + "...");
        }

        return convertView;
    }
}
