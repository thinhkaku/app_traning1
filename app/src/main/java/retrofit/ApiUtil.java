package retrofit;

public class ApiUtil {
    public static  final String BASE_URL="http://www.mcccloud.net";
    public static SoaService getService(){
        return RetrofitClient.getLient(BASE_URL).create(SoaService.class);
    }
}
