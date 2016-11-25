/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.mack.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author lucas.ursolino
 */
public class Marvel {

    private static String apikey = "9338a8ea223dda78e3e09c97775145bc";
    private static String privatekey = "b52dd2d06b2cb804614e61fbe563096b9913a5ae";
    private static String urlbase = "http://gateway.marvel.com/v1/public/characters";

    public static void main(String[] args) {
        //Criação de um timestamp
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyyhhmmss");
        String ts = sdf.format(date);

        //Criação do HASH
        String hashStr = MD5(ts + privatekey + apikey);
        String uri;
        String name = "Captain%20America";
        //url de consulta
        uri = urlbase + "?nameStartsWith=" + name + "&ts=" + ts + "&apikey=" + apikey + "&hash=" + hashStr;

        try {
            CloseableHttpClient cliente = HttpClients.createDefault();

            //HttpHost proxy = new HttpHost("172.16.0.10", 3128, "http");
            //RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
            HttpGet httpget = new HttpGet(uri);
            //httpget.setConfig(config);
            HttpResponse response = cliente.execute(httpget);
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            //Header[] h = (Header[]) response.getAllHeaders();

            /*for (Header head : h) {
                System.out.println(head.getValue());
            }*/
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
                StringBuilder out = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }
                //System.out.println(out.toString());
                reader.close();
                instream.close();
                JsonParser parser = new JsonParser();

                // find character's comics
                JsonElement comicResultElement = parser.parse(out.toString());
                JsonObject comicDataWrapper = comicResultElement.getAsJsonObject();
                JsonObject data = (JsonObject) comicDataWrapper.get("data");
                JsonArray results = data.get("results").getAsJsonArray();
                
                
                System.out.println( ((JsonObject)results.get(0)).get("thumbnail"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

       

    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
