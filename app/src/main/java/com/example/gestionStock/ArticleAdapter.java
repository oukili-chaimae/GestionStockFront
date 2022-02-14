package com.example.gestionStock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ArticleAdapter extends ArrayAdapter<Article> {
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    /**
     * Holds variables in a View
     */

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public ArticleAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Article> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String libelle = getItem(position).getLibelle();
        double prix = getItem(position).getPrix();
        int id = getItem(position).getId();

       // Article person = new Article(libelle,prix,0);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView Tvlibelle = (TextView) convertView.findViewById(R.id.tex1);
        TextView Tvprix = (TextView) convertView.findViewById(R.id.text2);
        TextView Tvid = (TextView) convertView.findViewById(R.id.text3);
        Tvlibelle.setText(String.valueOf(id));
        Tvprix.setText(libelle);
        Tvid.setText(String.valueOf(prix));
        return convertView;
    }
}
