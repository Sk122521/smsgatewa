package com.api.smsgateway.retrofit;

import com.api.smsgateway.model.ChecksentRequest;
import com.api.smsgateway.model.MessageResponse;
import com.api.smsgateway.model.ReceiveRequest;
import com.api.smsgateway.model.ReceiveResponse;
import com.api.smsgateway.model.RequestModel;
import com.api.smsgateway.model.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("v1/message/{id}")
    Call<MessageResponse> getMessages(@Path("id") String id);

    @POST("v1/message/{id}")
    Call<MessageResponse> checksentstatus(@Body ChecksentRequest request,@Path("id") String id);


    @POST("/login")
    Call<ResponseModel> performLogin(@Body RequestModel request);

    @POST("/sync/sms")
    Call<Void> submitMessage(@Body ReceiveRequest request);

    @GET("/sync/sms")
    Call<Void> checkHeartbeat();

    @GET("/about-us")
    Call<String> aboutUs();


}
