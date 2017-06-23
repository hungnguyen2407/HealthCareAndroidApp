package com.healthcareandroidapp;

import android.os.AsyncTask;
import android.util.Log;

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
import java.util.Date;

import entity.Clinic;
import entity.Doctor;
import entity.Specialty;

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


    public static JSONObject register(String userName, String password, String name, String email, String clinicName, String clinicAddress, String speciality, String degree, String experience, String doctorAddress, String phone, String passport) {

        JSONObject doctorJSON = null;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/mm/yyyy");
        Date date = new Date();
        Doctor doctor = new Doctor(new Clinic(clinicAddress, clinicName), new Specialty(speciality), userName, name, password, phone, passport, degree, Integer.parseInt(experience), doctorAddress, date, false);
        try {
            doctorJSON = new JSONObject(doctor.toJson());
            HttpURLConnection urlConnection = null;
            URL url = new URL(host + "/doctor/register/"+ doctor.toJson());

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return doctorJSON;
    }
}
