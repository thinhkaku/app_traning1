package aero.pilotlog.interfaces;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import aero.pilotlog.common.ApiConstant;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MccCallback<T> implements Callback<T> {
    private final String STATUS = "status";
    protected RequestListener<T> listener;


    public MccCallback(RequestListener<T> listener) {
        this.listener = listener;
    }

    public void onFailure(int erorCode) {
        this.listener.onFailure(erorCode);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        this.listener.onRetrofitFailure(retrofitError);
    }

    @Override
    public void success(T t, Response response) {
        switch (response.getStatus()) {
            case 200:
                this.listener.onSuccess(t);
                break;
            case 201:
                this.listener.onSuccess(t);
                break;
            default:
                this.listener.onFailure(response.getStatus());
                break;
        }
        /*int status;
        try {
            status = Integer.parseInt(jelem.getAsJsonObject().get(STATUS).toString());
            switch (status){
                case ApiConstant.SUCCESS:
                    this.listener.onSuccess(t);
                    break;
                case ApiConstant.ERROR_USER_DOESNOT_EXIST:
                    this.listener.onFailure(ApiConstant.ERROR_USER_DOESNOT_EXIST);
                    break;
                case ApiConstant.ERROR_WRONG_PASSWORD:
                    this.listener.onFailure(ApiConstant.ERROR_WRONG_PASSWORD);
                    break;
                default:
                    this.listener.onFailure(status);
                    break;
            }

        } catch (Exception e){
            this.listener.onFailure(ApiConstant.ERROR_UNKNOWN);
        }*/
    }

    public interface RequestListener<T> {

        void onSuccess(T response);

        void onRetrofitFailure(RetrofitError error);

        void onFailure(int errorCode);
    }

}
