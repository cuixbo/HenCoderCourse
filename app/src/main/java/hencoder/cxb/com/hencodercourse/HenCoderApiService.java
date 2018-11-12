package hencoder.cxb.com.hencodercourse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HenCoderApiService {

    @GET("/author")
    Call<AuthorResp> author();

    @FormUrlEncoded
    @POST("/test/post/{id}")
    Call<HttpResp> testPost(@Path("id") int id, @Field("name") String name, @Field("nickName") String nickName);


    @FormUrlEncoded
    @POST("/login/{id}")
    void login(@Path("id") String id, @Field("pass") String pass);


    @FormUrlEncoded
    @POST("/login")
    void login2(@Field("id") String id, @Field("pass") String pass);
}
