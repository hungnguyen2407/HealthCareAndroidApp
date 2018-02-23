package com.healthcare;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by hungnguyen on 19/06/2017.
 */

public class Connection {
    //Gia tri dia chi cua may chu web service
//    private static String host = "http://10.0.2.2:8080/HealthCare/rest";
    private static String host = "http://healthcare21617.azurewebsites.net/rest";

    public static JSONObject getWorkSchedule(String doctorID) {
        JSONObject workScheduleJSON = null;

        try {
            HttpURLConnection urlConnection = null;
            URL url = new URL(host + "/doctor/schedule/" + doctorID);
            Log.v("id",doctorID);
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
            HttpPost httpPost = new HttpPost(host + "/doctor/register");
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("userName", doctorJSON.getString("userName")));
            list.add(new BasicNameValuePair("password", doctorJSON.getString("password")));
            list.add(new BasicNameValuePair("name", doctorJSON.getString("name")));
            list.add(new BasicNameValuePair("specialty", doctorJSON.getString("specialty")));
            list.add(new BasicNameValuePair("degree", doctorJSON.getString("degree")));
            list.add(new BasicNameValuePair("experience", doctorJSON.getString("experience")));
            list.add(new BasicNameValuePair("email", doctorJSON.getString("email")));
            list.add(new BasicNameValuePair("doctorAddress", doctorJSON.getString("doctorAddress")));
            list.add(new BasicNameValuePair("phone", doctorJSON.getString("phone")));
            list.add(new BasicNameValuePair("passport", doctorJSON.getString("passport")));
            list.add(new BasicNameValuePair("birthDate", doctorJSON.getString("birthDate")));
            httpPost.setEntity(new UrlEncodedFormEntity(list, "utf-8"));

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
            HttpPost httpPost = new HttpPost(host + "/doctor/update/info/" + doctorJSON.getString("idDoctor"));
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("passwords", doctorJSON.getString("password")));
            list.add(new BasicNameValuePair("name", doctorJSON.getString("nameDoctor")));
            list.add(new BasicNameValuePair("specialty", doctorJSON.getString("nameSpecialty")));
//            list.add(new BasicNameValuePair("degree", doctorJSON.getString("degree")));
            list.add(new BasicNameValuePair("experience", doctorJSON.getString("experience")));
//            list.add(new BasicNameValuePair("email", doctorJSON.getString("email")));
//            list.add(new BasicNameValuePair("birthDate", doctorJSON.getString("birthDate")));
//            list.add(new BasicNameValuePair("doctorAddress", doctorJSON.getString("doctorAddress")));
//            list.add(new BasicNameValuePair("phone", doctorJSON.getString("phone")));
//            list.add(new BasicNameValuePair("passport", doctorJSON.getString("passport")));
            Log.v("list", list.toString());
            httpPost.setEntity(new UrlEncodedFormEntity(list, "utf-8"));

            HttpResponse httpResponse = httpClient.execute(httpPost);

            s = readResponse(httpResponse);

            System.out.println(s);
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }


    public static boolean resetPass(String email) {
        String s = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPut httpPost = new HttpPut(host + "/doctor/forgetpassword");
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("email", email));
            httpPost.setEntity(new UrlEncodedFormEntity(list, "utf-8"));
            System.out.println("List: " + list.toString());
            HttpResponse httpResponse = httpClient.execute(httpPost);
            s = readResponse(httpResponse);
            System.out.println(s);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Complete");
        return !s.equals("");
    }

    public static boolean sendMessages(String doctorID, String userID, String content) {
        try {
            String s = "";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(host + "/messages/conversation/doctor/");
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("idDoctor", doctorID));
            list.add(new BasicNameValuePair("idUser", userID));
            list.add(new BasicNameValuePair("content", content));

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


    public static JSONObject getClinicList() {
        JSONObject clinicListJSON = null;

        try {
            HttpURLConnection urlConnection = null;
            URL url = new URL(host + "/clinic/all");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStreamReader inputStreamReader = new InputStreamReader((InputStream) urlConnection.getContent());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line, response = "";
            do {
                line = bufferedReader.readLine();
                if (line != null) response += line;
            } while (line != null);
            clinicListJSON = new JSONObject(response);
            urlConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return clinicListJSON;
    }

    public static String registWorkSchedule(String idDoctor, String scheduleList) {
        String s = "", scheduleListJSON = "{\"scheduleList\":[" + scheduleList + "]}";
        //TODO: Chua duoc
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(host + "/schedules/registry/list");
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("idDoctor", idDoctor));
            list.add(new BasicNameValuePair("scheduleList", scheduleListJSON));
            Log.v("workScheduleList", scheduleListJSON);
            httpPost.setEntity(new UrlEncodedFormEntity(list, "utf-8"));
            HttpResponse httpResponse = httpClient.execute(httpPost);

        } catch (Exception e) {
            System.out.println(e);
        }

        return s;
    }
}
