package space.sakibnm.okhttpbarebone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    private String baseUrlSearch;
    private String baseUrlAdd;
    private String searchName;
    private String searchType;
    private String addName;
    private String addEmail;
    private String addPhone;
    private String addType;

    private HttpUrl url;
    private OkHttpClient client;

    private TextView textViewResponse;

    private static class Keys{
        final static String name = "name";
        final static String email = "email";
        final static String phone = "phone";
        final static String type = "type";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textViewResponse = findViewById(R.id.textViewResponse);
        this.client = new OkHttpClient();
        this.baseUrlSearch = "https://www.theappsdr.com/contacts/search";
        this.baseUrlAdd = "https://www.theappsdr.com/contact/create";
        this.searchName = "Bob";
        this.searchType = "HOME";

        this.url = HttpUrl.parse(baseUrlSearch).newBuilder()
                                .addQueryParameter(Keys.name,this.searchName)
                                .addQueryParameter(Keys.type, this.searchType)
                                .build();

        Log.d(Tags.TAG, "Url: "+url);

//        searchUser(searchName, searchType);


        this.addName = "Bobby Smith";
        this.addEmail = "bobby.s@email.com";
        this.addPhone = "980-111-3333";
        this.addType = "HOME";

//        createNewUsers(addName, addEmail, addPhone, addType);
    }


    private void searchUser(String name, String type) {
        Request requestGet = new Request.Builder()
                .url(this.url)
                .build();
        client.newCall(requestGet)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e(Tags.TAG, "onFailure: "+e.getMessage());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if(response.isSuccessful()){
                            ResponseBody responseBody = response.body();
                            String body = responseBody.string();
                            Log.d(Tags.TAG, "onResponse: "+body);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textViewResponse.setText(body);
                                }
                            });

                        }else{
                            throw new IOException("Unexpected code " + response);
                        }
                    }
                });
    }

    private void createNewUsers(String addName, String addEmail, String addPhone, String addType) {
        RequestBody formBody = new FormBody.Builder()
                .add(Keys.name, addName)
                .add(Keys.email, addEmail)
                .add(Keys.type, addType)
                .add(Keys.phone, addPhone)
                .build();
        Request request = new Request.Builder()
                .url(baseUrlAdd)
                .post(formBody)
                .build();

        this.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(Tags.TAG, "onFailure: "+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();
                    Log.d(Tags.TAG, "onResponse: "+body);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewResponse.setText(body);
                        }
                    });

                }else{
                    throw new IOException("Unexpected code " + response);
                }
            }
        })
        ;
    }



}