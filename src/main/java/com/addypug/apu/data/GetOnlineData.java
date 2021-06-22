package com.addypug.apu.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Most of this code was  created from https://github.com/GandyT/GandyClient-2.0/blob/master/src/main/java/GandyClient/DataManager.java
 */
public class GetOnlineData {
    public static Logger ch_update_logger = LoggerFactory.getLogger("chkupdates");
    public JsonObject jsonObject = null;
    public static void fetchUpdates(){
        String databaseUrl = values.update_server_endpoint;
        JsonParser jsonParser = new JsonParser();
        ch_update_logger.info("Reading: " + databaseUrl);
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            HttpsURLConnection connection = (HttpsURLConnection) new URL(databaseUrl).openConnection();
            connection.connect();
            JsonObject capes = jsonParser.parse(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
            String release = capes.get("latest-alpha").toString();
            if (!release.equals(values.release_json)) {
                ch_update_logger.warn("Update Detected! Current Version is " + values.release_json + " but latest version is " + release);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    }

