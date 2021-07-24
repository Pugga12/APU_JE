package com.addypug.apu.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;


//Most of this code was created from https://github.com/GandyT/GandyClient-2.0/blob/master/src/main/java/GandyClient/DataManager.java
public class GetOnlineData {
    public static Logger ch_update_logger = LoggerFactory.getLogger("updatechkr");

    /**
     * Gets info about the latest update from a JSON file over the internet
     * @param databaseUrl The URL of the JSON file to fetch
     */
    public static void fetchUpdates(String databaseUrl) {
        ch_update_logger.info("Downloading from " + databaseUrl);
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(
                            X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            ch_update_logger.error(e.getMessage());
        }
        HostnameVerifier allHostsValid = (hostname, session) -> true;
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            HttpsURLConnection connection = (HttpsURLConnection) new URL(databaseUrl).openConnection();
            connection.connect();
            JsonObject updates = JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
            String release = updates.get("latest-alpha").toString();
            int build = Integer.parseInt(updates.get("build").toString());
            String download_link = updates.get("download-link").toString();
            String readme = updates.get("readme").toString();
            if (!release.equals(values.release_json)) {
                ch_update_logger.warn("Update Detected! Current Version is " + values.release_json + " but latest version is " + release + "\nDownload the new release at " + download_link);
            } else {
                ch_update_logger.info("Update Check Complete. Up To Date!");
            }
        } catch (Exception e) {
            ch_update_logger.error("Unable To Retrieve The Update File\n" + e.getMessage());
        }
    }
}

