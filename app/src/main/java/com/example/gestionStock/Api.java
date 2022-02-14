package com.example.gestionStock;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {
    @GET("AllCategorie")
    Call<List<Categorie>> getCategories();
    @GET("getByCategorie/{id}")
    Call<List<Article>> getArticles(@Path("id") int ido);
    @POST("save")
    Call<Article> AjouterArticle(@Body Article art);
}