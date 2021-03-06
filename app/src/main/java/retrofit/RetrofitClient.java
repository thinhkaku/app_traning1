package retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    public static Retrofit getLient(String base){
        if (retrofit==null){
            retrofit=new Retrofit.Builder().baseUrl(base).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
