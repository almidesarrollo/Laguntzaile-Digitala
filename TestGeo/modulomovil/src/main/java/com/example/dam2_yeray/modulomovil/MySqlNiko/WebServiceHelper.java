package com.example.dam2_yeray.modulomovil.MySqlNiko;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.dam2_yeray.modulomovil.PostPhp.PostArray;


/**
 *
 * Auxiliar class for sending or getting web responses in JSON format
 *
 * */
public class WebServiceHelper {

    public JSONArray httpUrlConnect(PostArray sendParams, String url) {

        HttpURLConnection connection = null;

        try {

            /**we create connection here*/
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            /**we show the params for debug*/
            showParams(sendParams);

            /**we cast the params*/
            String charset = "UTF-8";
            String params = "";
            for (int i=0;i<sendParams.size();i++) {
                if (i == 0)
                    params += sendParams.getKey(i) + "=" + URLEncoder.encode(sendParams.getValue(i), charset);
                else
                    params += "&" + sendParams.getKey(i) + "=" + URLEncoder.encode(sendParams.getValue(i), charset);
            }

            Log.e("params = ", params);


            /*  AQUI SE ESPECIFICA LA LONGITUD DE BANDA DE ENVIO DE DATOS */
            connection.setFixedLengthStreamingMode(params.getBytes().length);

            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.print(params);
            out.close();

            /**here we obtain the response*/
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();

            /**we read each line of the response*/
            while ((line = br.readLine()) != null) {

                response.append(line);
                response.append('\r');
            }

            /**we close the reader*/
            br.close();

            Log.e("RESPONSE = ", response.toString());

            /**we return a jsonarray to handle the result data (empty if url didn't send data*/
            try {
                return new JSONArray(response.toString());
            } catch (Exception e) {
                return null;
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;

    }

    public static void showParams(PostArray sendParams) {
        for (int i=0;i<sendParams.size();i++) {
            Log.e("POST", "$_POST['" + sendParams.getKey(i) + "']+----> " + sendParams.getValue(i));
        }
    }
        
  }     
  