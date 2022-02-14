package com.example.gestionStock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleActivity extends AppCompatActivity {
    private Button retour;
    private Button ajouter;
    private TextView cat_designation;
    private TextInputEditText libelle;
    private TextInputEditText prix;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.137.1:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Api api = retrofit.create(Api.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        retour=findViewById(R.id.button2);
        ajouter=findViewById(R.id.button);
        libelle=findViewById(R.id.pro_lib);
        prix=findViewById(R.id.pro_prix);
        cat_designation=findViewById(R.id.cat_designation);
        Bundle valeurs=getIntent().getExtras();
        cat_designation.setText(valeurs.getString("cat"));
        String catId=valeurs.getString("id").toString();
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent il =new Intent(ArticleActivity.this,MainActivity.class);
                startActivity(il);
            }
        });
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creatArticle(catId);
            }
        });

    }
    private void creatArticle(String catId){
        Article art=new Article(libelle.getText().toString(),Double.parseDouble(prix.getText().toString()),Integer.parseInt(catId));
        Call<Article> call  = api.AjouterArticle(art);
        call.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                if(!response.isSuccessful()){
                    makeToast("Le code: " + response.code());
                    return;
                }
                Article artresp=response.body();
                String content="";
                content+="Cette article avec ce id :"+artresp.getId()+" est bien ajouter ";
                makeToast(content);
                libelle.setText("");
                prix.setText("");
            }


            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });
    }
    //L'affichage des messages
    Toast t;
    public void makeToast(String message){
        if(t!=null) t.cancel();
        t=Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT);
        t.show();
    }

}