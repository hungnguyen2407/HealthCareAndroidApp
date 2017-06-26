package com.healthcareandroidapp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by hungnguyen on 19/06/2017.
 */

public class Connection {
    //Gia tri dia chi cua may chu web service
    //TODO: Cho dia chi may chu that
//    private static String host = "http://10.0.2.2:8080/HealthCare/rest";
    private static String host = "http://healthcare21617.azurewebsites.net/rest";

    public static JSONObject getWorkSchedule(String doctorID) {
        JSONObject workScheduleJSON = null;

        try {
            HttpURLConnection urlConnection = null;
            URL url = new URL(host + "/doctor/schedule/" + doctorID);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStreamReader inputStreamReader = new InputStreamReader((InputStream) urlConnection.getContent());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line, response = "";
            do {
                line = bufferedReader.readLine();
                if (line != null) response += line;
            } while (line != null);
            workScheduleJSON = new JSONObject(response);
            urlConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return workScheduleJSON;
    }


    public static JSONObject login(String userName, String password) {
        JSONObject doctorJSON = null;

        try {
            HttpURLConnection urlConnection = null;
            URL url = new URL(host + "/doctor/login/" + userName + "/" + password);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader((InputStream) urlConnection.getContent());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line, response = "";
            do {
                line = bufferedReader.readLine();
                if (line != null) response += line;
            } while (line != null);

            doctorJSON = new JSONObject(response);

            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return doctorJSON;
    }


    public static boolean register(String doctorInfo) {

        JSONObject doctorJSON = null;

        try {
            doctorJSON = new JSONObject(doctorInfo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(host + "/doctor/register/");
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("userName", doctorJSON.getString("username")));
            list.add(new BasicNameValuePair("password", doctorJSON.getString("password")));
            list.add(new BasicNameValuePair("name", doctorJSON.getString("nameDoctor")));
            list.add(new BasicNameValuePair("specialty", doctorJSON.getString("nameSpecialty")));
            list.add(new BasicNameValuePair("degree", doctorJSON.getString("degree")));
            list.add(new BasicNameValuePair("experience", doctorJSON.getString("experience")));
            list.add(new BasicNameValuePair("email", doctorJSON.getString("email")));
            list.add(new BasicNameValuePair("doctorAddress", doctorJSON.getString("doctorAddress")));
            list.add(new BasicNameValuePair("phone", doctorJSON.getString("phone")));
            list.add(new BasicNameValuePair("passport", doctorJSON.getString("passport")));
            httpPost.setEntity(new UrlEncodedFormEntity(list, "utf-8"));
            System.out.println("List: " + list.toString());
            HttpResponse httpResponse = httpClient.execute(httpPost);

            s = readResponse(httpResponse);
            System.out.println(s);
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }

    public static String readResponse(HttpResponse res) {
        InputStream is = null;
        String return_text = "";
        try {
            is = res.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuffer sb = new StringBuffer();
            do {
                line = bufferedReader.readLine();
                if (line != null) return_text += line;
            } while (line != null);
            return_text = sb.toString();
        } catch (Exception e) {

        }
        return return_text;

    }

    public static boolean changeInfo(String doctorInfo) {
        JSONObject doctorJSON = null;

        try {
            doctorJSON = new JSONObject(doctorInfo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(host + "/doctor/register/");
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("id", doctorJSON.getString("idDoctor")));
            list.add(new BasicNameValuePair("password", doctorJSON.getString("password")));
            list.add(new BasicNameValuePair("name", doctorJSON.getString("nameDoctor")));
            list.add(new BasicNameValuePair("specialty", doctorJSON.getString("nameSpecialty")));
            list.add(new BasicNameValuePair("degree", doctorJSON.getString("degree")));
            list.add(new BasicNameValuePair("experience", doctorJSON.getString("experience")));
            list.add(new BasicNameValuePair("email", doctorJSON.getString("email")));
            list.add(new BasicNameValuePair("doctorAddress", doctorJSON.getString("doctorAddress")));
            list.add(new BasicNameValuePair("phone", doctorJSON.getString("phone")));
            list.add(new BasicNameValuePair("passport", doctorJSON.getString("passport")));
            httpPost.setEntity(new UrlEncodedFormEntity(list, "utf-8"));
            System.out.println("List: " + list.toString());
            HttpResponse httpResponse = httpClient.execute(httpPost);

            s = readResponse(httpResponse);
            System.out.println(s);
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }


    public static boolean resetPass(String email) {
        String line, response = "";
        try {
            HttpURLConnection urlConnection = null;
            URL url = new URL(host + "/doctor/forgetpassword/" + email);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader((InputStream) urlConnection.getContent());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            do {
                line = bufferedReader.readLine();
                if (line != null) response += line;
            } while (line != null);

            Log.v("result", response);

            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return !response.equals("");
    }
}
