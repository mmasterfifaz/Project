package com.maxelyz.utils;

/**
 * Created by apichatt on 21/7/2017.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class VoiceFileRestful2 {

    String urlString;

    public VoiceFileRestful2(String urlString) {
        this.urlString = urlString;
    }

    // http://localhost:8080/RESTfulExample/json/product/post
    public String post(String inputJsonString) {
        String ret = "";

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
//            HttpPost postRequest = new HttpPost("http://192.168.51.102:10001/api/ConvertVoice/convert");
//            HttpPost postRequest = new HttpPost(JSFUtil.getApplication().getConvertVoiceRestful());
            HttpPost postRequest = new HttpPost(urlString);

            String tmp = inputJsonString;

//            StringEntity input = new StringEntity(tmp);
            StringEntity input = new StringEntity(tmp, "UTF-8");
            input.setContentType("application/json; charset=utf-8");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);

//			if (response.getStatusLine().getStatusCode() != 201) {
//				throw new RuntimeException("Failed : HTTP error code : "
//						+ response.getStatusLine().getStatusCode());
//			}

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (response.getEntity().getContent())));

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

            httpClient.getConnectionManager().shutdown();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static void main(String[] args) {
        VoiceFileRestful2 v = new VoiceFileRestful2("http://192.168.51.102:10001/api/ConvertVoice/convert");
//        VoiceFileRestful2 v = new VoiceFileRestful2("http://172.18.10.10:9101/api/ConvertVoice/convert");
        String tmp = "[ {\"folderName\":\"fwd_central\\\\FWD_QC\\\\\",\"trackId\":424095,\"fileName\":\"C6000059 กิตติ ธีรภร1\"}, {\"folderName\":\"fwd_central\\\\FWD_QC\\\\\",\"trackId\":424144,\"fileName\":\"C6000059 กิตติ ธีรภร2\"}, {\"folderName\":\"fwd_central\\\\FWD_QC\\\\\",\"trackId\":424147,\"fileName\":\"C6000059 กิตติ ธีรภรRe1\"}]";
//        String tmp = "[ {\"folderName\":\"fwd_central\\\\FWD_QC\\\\111\\\\\",\"trackId\":424095,\"fileName\":\"C6000059 กิตติ ธีรภร1\"}]";
        System.out.println(tmp);
        String ret = v.post(tmp);
        System.out.println(ret);
    }

}


