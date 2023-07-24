package uz.yangilanish.client.data.dto.response;


import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import uz.yangilanish.client.data.dto.Response;
import uz.yangilanish.client.resources.language.Localization;


public class BadResponse extends Response {

    private static BadResponse response;

    private List<Error> errors;

    // Here we will be creating private constructor
    private BadResponse() {
    }

    public static BadResponse getInstance(@NonNull ResponseBody errorBody) {
        Gson gson = new Gson();
        try {
            response = gson.fromJson(errorBody.string(), BadResponse.class);
        } catch (IOException | JsonSyntaxException e) {
            response = new BadResponse();
            response.setMsg(Localization.getUnknownError());
            e.printStackTrace();
        }

        return response;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public static class Error {
        private String value;
        private String msg;
        private String param;
        private String location;
    }

    public static String errorBodyToMessage(Retrofit retrofit, ResponseBody responseBody) {
        BadResponse response;

        try {
            response = (BadResponse) retrofit.responseBodyConverter(BadResponse.class, new Annotation[0]).convert(responseBody);

            assert response != null;
            return response.getMsg();
        } catch (IOException e) {
            return null;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return response.getMsg();
    }
}
