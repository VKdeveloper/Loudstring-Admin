package com.loudstring.loudstring_admin;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login_btn = (Button) findViewById(R.id.login_btn);
        final EditText login_id = (EditText) findViewById(R.id.email_txt);
        final EditText pass_ward = (EditText) findViewById(R.id.pass_txt);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usr = login_id.getText().toString();
                String pass = pass_ward.getText().toString();
                 validateFields(usr,pass);
            }
        });
    }

    public void validateFields(String usr , String pass)
    {
        if(usr.trim().equals(""))
        {
            Toast.makeText(LoginActivity.this, "Please enter username or valid email", Toast.LENGTH_SHORT).show();
        }
        else if(usr.trim().contains("@") && usr.trim().length() < 8 )
        {
            Toast.makeText(LoginActivity.this, "Not a valid email address", Toast.LENGTH_SHORT).show();
        }
        else if(!usr.trim().contains("@") && usr.trim().length() < 5)
        {
            Toast.makeText(LoginActivity.this, "Please enter valid username or email", Toast.LENGTH_SHORT).show();
        }
        else if(pass.trim().equals(""))
        {
            Toast.makeText(LoginActivity.this, "Please enter valid password", Toast.LENGTH_SHORT).show();
        }
        else if(pass.trim().length() < 8 )
        {
            Toast.makeText(LoginActivity.this, "Not a valid password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            startASycnc(usr,pass);
        }
    }

    public void startASycnc(String usr , String pass) {
        new loginOperation().execute(usr,pass);
    }

    private class loginOperation extends AsyncTask<String, Void, String> {
        private ProgressDialog pdia;
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            API controller = new API(LoginActivity.this);
            HttpPost httppost = new HttpPost(controller.getApiUrl("user_login"));
            Log.i("URL",controller.getApiUrl("user_login"));
            try {
                //String macAddress = wInfo.getMacAddress();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("user_name", params[0]));
                nameValuePairs.add(new BasicNameValuePair("password", params[1]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                // According to the JAVA API, InputStream constructor do nothing.
                //So we can't initialize InputStream although it is not an interface
                InputStream inputStream = response.getEntity().getContent();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder stringBuilder = new StringBuilder();

                String bufferedStrChunk = null;

                while((bufferedStrChunk = bufferedReader.readLine()) != null){
                    stringBuilder.append(bufferedStrChunk);
                }

                return stringBuilder.toString();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return "null";
        }

        @Override
        protected void onPostExecute(String result) {
            pdia.dismiss();
            try {
                JSONObject obj = new JSONObject(result);
                JSONArray user_conv = obj.getJSONArray("response");
                if (user_conv.length() > 0) {

                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(LoginActivity.this);
            pdia.setMessage("Loading...");
            pdia.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
