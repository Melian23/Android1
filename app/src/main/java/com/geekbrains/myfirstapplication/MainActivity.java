package com.geekbrains.myfirstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText field;
    private Button main_btn;
    private TextView result_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        field = findViewById(R.id.field);
        main_btn = findViewById(R.id.main_btn);
        result_info = findViewById(R.id.result_info);

        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (field.getText().toString().trim().equals(""))
                    Toast.makeText(MainActivity.this, R.string.no_input, Toast.LENGTH_LONG).show();
                else {
                    String city = field.getText().toString();
                    String key = "b358595a782218044ebed157dcda99ad";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric&lang=ru\n";

                    new GetUrl().execute(url);
                }
            }
        });

    }

private class GetUrl extends AsyncTask <String, String, String>{

        protected void preExecute (){
            super.onPreExecute();
            result_info.setText("Ожидайте...");
        }
    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL (strings[0]);
            connection = (HttpURLConnection)url.openConnection ();
            connection.connect();

            InputStream stream = connection.getErrorStream();
            reader = new BufferedReader (new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null)
                buffer.append(line).append("\n");

            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null)
                connection.disconnect();
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        return null;
        }

        @Override
    protected void onPostExecute (String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                result_info.setText("Температура: " + jsonObject.getJSONObject("main").getDouble("temp"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


    }
}
}