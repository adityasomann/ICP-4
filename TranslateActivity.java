package tutorial.cs5551.com.translateapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TranslateActivity extends AppCompatActivity {

    String API_URL = "https://api.fullcontact.com/v2/person.json?";
    String API_KEY = "b29103a702edd6a";
    String sourceText;
    TextView outputTextView;
    Context mContext;
    String tocountrycode;
    String tocountry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        outputTextView = (TextView) findViewById(R.id.txt_Result);
    }
    public void logout(View v) {
        Intent redirect = new Intent(TranslateActivity.this, LoginActivity.class);
        startActivity(redirect);
    }

    public void spinner1(Spinner spinner1)
    {
        Spinner langSpinner = (Spinner) findViewById(R.id.lang_spinner);
        ArrayAdapter<CharSequence> lang_adapter = ArrayAdapter.createFromResource(this, R.array.fromlangs, android.R.layout.simple_spinner_item);
        lang_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Toast.makeText(this,""+ lang_adapter, Toast.LENGTH_SHORT).show();
        langSpinner.setAdapter(lang_adapter);
        String fromcountry = spinner1.getSelectedItem().toString();

    }

    public void spinner2(Spinner spinner2)
    {
        Spinner tospinner = (Spinner) findViewById(R.id.to_spinner);
        ArrayAdapter<CharSequence> to_adapter = ArrayAdapter.createFromResource(this, R.array.tolangs, android.R.layout.simple_spinner_item);
        to_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tospinner.setAdapter(to_adapter);
        tocountry = tospinner.getSelectedItem().toString();
        //tocountrycode = tocountry[1];

    }

    public void translateText(View v) {
        TextView sourceTextView = (TextView) findViewById(R.id.txt_Email);

        sourceText = sourceTextView.getText().toString();
        Spinner fromspinner = (Spinner) findViewById(R.id.lang_spinner);
        Spinner tospinner = (Spinner) findViewById(R.id.to_spinner);
        String fromcountry = fromspinner.getSelectedItem().toString().split(" ")[1];
        tocountry = tospinner.getSelectedItem().toString().split(" ")[1];
        String getURL = "https://translate.yandex.net/api/v1.5/tr.json/translate?" +
                "key=trnsl.1.1.20180207T171044Z.4fc89a37fd9055ee." +
                "32c820cbf0180beb18ff949be29c2db72a730a6e&text=" + sourceText +"&" +
                "lang=" + fromcountry + "-" + tocountry + "&[format=plain]&[options=1]&[callback=set]";//The API service URL
        final String response1 = "";
        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder()
                    .url(getURL)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println(e.getMessage());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final JSONObject jsonResult;
                    final String result = response.body().string();
                    try {
                        jsonResult = new JSONObject(result);
                        JSONArray convertedTextArray = jsonResult.getJSONArray("text");
                        final String convertedText = convertedTextArray.get(0).toString();
                        Log.d("okHttp", jsonResult.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                outputTextView.setText(convertedText);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (Exception ex) {
            outputTextView.setText(ex.getMessage());

        }

    }
}
