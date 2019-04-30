package com.maxelyz.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class VoiceFileRestful {

    String urlString;

    public VoiceFileRestful(String urlString) {
        this.urlString = urlString;
    }

    // http://localhost:8080/RESTfulExample/json/product/post
    public String post(String inputJsonString) {
        String ret = "";
        try {

//            URL url = new URL("http://192.168.51.102:10001/api/ConvertVoice/convert");
//            URL url = new URL(JSFUtil.getApplication().getConvertVoiceRestful());
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            String input = inputJsonString;
            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

//            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
//                throw new RuntimeException("Failed : HTTP error code : "
//                        + conn.getResponseCode());
//            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

//            String output;
//            System.out.println("Output from Server .... \n");
//            while ((output = br.readLine()) != null) {
//                System.out.println(output);
//            }

            String output;
            StringBuilder stringBuilder = new StringBuilder();
            while ((output = br.readLine()) != null) {
                stringBuilder.append(output+"\n");
            }
            ret = stringBuilder.toString();
//            System.out.println(ret);

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static void main(String[] args) {
        VoiceFileRestful v = new VoiceFileRestful("http://192.168.51.102:10001/api/ConvertVoice/convert");
        String tmp = "[ {\"folderName\":\"fwd\\\\test20170721_115619\",\"trackId\":424095,\"fileName\":\"C6000059 กิตติ ธีรภร1\"}, {\"folderName\":\"fwd\\\\test20170721_115619\",\"trackId\":424144,\"fileName\":\"C6000059 กิตติ ธีรภร2\"}, {\"folderName\":\"fwd\\\\test20170721_115619\",\"trackId\":424147,\"fileName\":\"C6000059 กิตติ ธีรภรRe1\"}]";
        System.out.println(tmp);
        String ret = v.post(tmp);
        System.out.println(ret);

/*//test encode > result is error
        try {
            tmp = URLEncoder.encode(tmp,"UTF-8");
            System.out.println(tmp);
            ret = v.post(tmp);
            System.out.println(ret);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
*/
    }

}

