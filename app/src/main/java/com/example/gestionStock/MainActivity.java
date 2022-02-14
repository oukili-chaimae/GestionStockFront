package com.example.gestionStock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity  {
    private TextView designation;
    private TextView moy;
    private TextView Nart;
    private ListView listeView;
    private Button button;
    private ArrayList<String> itemsN;
    private ArrayList Cats;
    private ArrayList CatsD;
    private ArrayList<Article> articlesList;
    private ArrayAdapter<String> adap;
    private ArticleAdapter adapterList;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.137.1:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Api api = retrofit.create(Api.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        designation = findViewById(R.id.designation);
        Nart=findViewById(R.id.Nart);
        moy = findViewById(R.id.moy);
        Spinner dropdown = findViewById(R.id.spinner1);
        Cats = new ArrayList();
        CatsD = new ArrayList();
        Cats.add("---Select---");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Cats);
        dropdown.setAdapter(adapter);
        listeView=findViewById(R.id.articleList);
        itemsN = new ArrayList<>();
        articlesList=new ArrayList<>();

        adapterList = new ArticleAdapter(this, R.layout.adapter_view_layout, articlesList);

        button=findViewById(R.id.addart);
        getCategories();
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                String select=adapterView.getItemAtPosition(i).toString();
                designation.setText(CatsD.get(i-1)+"");
                getArtByIdCat(Integer.parseInt(select));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                makeToast("Erreur\n");
            }
        });

        ///button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent il =new Intent(MainActivity.this,ArticleActivity.class);
                Bundle b=new Bundle();
                b.putString("cat",designation.getText().toString());
                il.putExtras(b);
                Bundle c=new Bundle();
                c.putString("id",dropdown.getSelectedItem().toString());
                il.putExtras(c);
                startActivity(il);
            }
        });
    }


    Toast t;
    public void makeToast(String s){
        if(t!=null) t.cancel();
        t=Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT);
        t.show();
    }

    public void getCategories(){
        Call<List<Categorie>> call = api.getCategories();
        call.enqueue(new Callback<List<Categorie>>() {
            @Override
            public void onResponse(Call<List<Categorie>> call, Response<List<Categorie>> response) {
                if (!response.isSuccessful()) {
                    makeToast("Code: " + response.code());
                    return;
                }
                List<Categorie> posts = response.body();
                for (Categorie post : posts) {
                    Cats.add(post.getId());
                    CatsD.add(post.getDesignation());
                }

            }
            @Override
            public void onFailure(Call<List<Categorie>> call, Throwable t) {
                makeToast(t.getMessage());
            }
        });
    }
    public  void getArtByIdCat(int Selected){
        Call<List<Article>> call2 = api.getArticles(Selected);
        call2.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if (!response.isSuccessful()) {
                    makeToast("Erreur");
                    return;
                }
                List<Article> articles = response.body();
                Nart.setText(""+articles.size());
                adapterList.clear();
                double somme=0.0;
                for (Article art : articles) {
                    itemsN.add(art.getId()+"\t\t\t\t\t\t\t"+art.getLibelle()+"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+art.getPrix());
                    somme=somme+art.getPrix();
                    articlesList.add(art);
                }
                moy.setText((somme/articles.size())+"");
                listeView.setAdapter(adapterList);
            }
            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                makeToast(t.getMessage());
            }
        });
    }
}