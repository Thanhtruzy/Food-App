package com.name.foodapp.retrofit;

import com.name.foodapp.model.OrderDetailModel;
import com.name.foodapp.model.CategoryModel;
import com.name.foodapp.model.Login;
import com.name.foodapp.model.MealDetailModel;
import com.name.foodapp.model.MealModel;
import com.name.foodapp.model.MessModel;
import com.name.foodapp.model.OrderDetail;
import com.name.foodapp.model.OrderHistory;
import com.name.foodapp.model.Signup;
import com.name.foodapp.model.Meals;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface FoodAppApi {
    @GET("category.php")
    Call<CategoryModel> getCategory();

    @POST("meal.php")
    @FormUrlEncoded
    Call<MealModel> getMeals(@Field("idcate") int idcate);

    @FormUrlEncoded
    @POST("mealdetail.php")
    Call<MealDetailModel> getMealsDetail(@Field("id") int id);

    @POST("cart.php")
    @FormUrlEncoded
    Call<MessModel> postCart(
            @Field("iduser") int id,
            @Field("amount") int amount,
            @Field("total") double total,
            @Field("detail") String detail
    );

    @FormUrlEncoded
    @POST("search_meal.php")
    Call<List<Meals>> searchMeals(@Field("query") String query);

    @FormUrlEncoded
    @POST("signup.php")
    Call<Signup> signUp(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<Login> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("order_history.php")
    Call<List<OrderHistory>> getOrderHistory(
            @Field("iduser") int iduser
    );


    @FormUrlEncoded
    @POST("order_detail.php")
    Call<OrderDetailModel> getOrderDetail(@Field("idorder") int idorder);

}
