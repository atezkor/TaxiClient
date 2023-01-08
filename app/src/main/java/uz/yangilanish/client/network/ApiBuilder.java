package uz.yangilanish.client.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiBuilder {

    public static Retrofit buildRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(ApiConstants.API_URL);

        /* Sinf obyektini GSON oraqli json o'tkazish uchun */
        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create();
        builder.addConverterFactory(GsonConverterFactory.create(gson));

        /* Interceptors - qabul qilish. Javob */
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Content-Type", "application/json;charset=utf-8")
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request); // Response
                }).build();

         // builder.client(client); // Vaqtni chegaralash uchun

        return builder.build();
    }

    public static ClientApi api() {
        Retrofit retrofit = buildRetrofit();
        return retrofit.create(ClientApi.class);
    }
}
