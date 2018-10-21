package com.mca.joshua.webclient_hosted_fake_rest_api;

import android.content.Context;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    TextView id,fn,ln,op; ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        op=findViewById(R.id.output);

        String url="https://reqres.in/api/users?page=2";
        String output="",res="";

        LayoutInflater inflator = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout single_Student=findViewById(R.id.singleItem);

        getDataFromWeb getData=new getDataFromWeb();
        try {
            output=getData.execute(url).get();
            JSONObject jObj = new JSONObject(output);
            JSONArray data = jObj.getJSONArray("data");
            if(data != null) {
                for(int i = 0 ; i < data.length() ; i++) {

                    View view = inflator.inflate(R.layout.single, null);
                    JSONObject jobj=data.getJSONObject(i);
                    //Toast.makeText(getApplicationContext(),jobj.toString(),Toast.LENGTH_LONG).show();
                    id=view.findViewById(R.id.ID);
                    fn=view.findViewById(R.id.fname);
                    ln=view.findViewById(R.id.lname);
                    img=view.findViewById(R.id.img);
                    id.setText(jobj.getString("id"));
                    fn.setText(jobj.getString("first_name"));
                    ln.setText(jobj.getString("last_name"));
                    Picasso.get().load(jobj.getString("avatar")).into(img);
                    single_Student.addView(view);
                }

            }
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        op.setText(output);
    }


    public class getDataFromWeb extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params){
            String stringUrl = params[0];
            String result="";
            String inputLine;
            try {
                URL myUrl = new URL(stringUrl);
                HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }
    }
}
