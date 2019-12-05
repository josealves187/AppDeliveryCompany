package br.com.jose.alves.freedeliverycliente.network;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class ApiClient  {

    private final static String URL_BASE = "https://viacep.com.br/ws/";
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = new OkHttpClient()
            .newBuilder().build();



    public static <T> T create(Class<T> serviceClass) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient).baseUrl(URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(serviceClass);
    }
}
