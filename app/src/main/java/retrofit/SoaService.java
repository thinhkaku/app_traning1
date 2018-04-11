package retrofit;



import com.google.gson.stream.JsonReader;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface SoaService {
    @GET("/pilotlog/asp/pilotlog.stats.asp")
    Call<String> getKetQua(@Query("ED") String eD,
                           @Query("OS") String oS,
                           @Query("BD") String bD,
                           @Query("ID") String iD,
                           @Query("CT") String cT,
                           @Query("AI") String aI,
                           @Query("DT") String dT,
                           @Query("CS") String cS);

    @GET("/get1.php")
    Call<ArrayList<SanPham>>
    getHinh(@Query("idtungloaisp") int group);
}
