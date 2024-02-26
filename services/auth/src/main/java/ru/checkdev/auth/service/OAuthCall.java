package ru.checkdev.auth.service;

import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Map;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class OAuthCall {
    static {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }

    public String doGet(Principal user, String url) throws Exception {
        return this.call(user, "GET", url, null);
    }

    public String doPost(Principal user, String url, String data) throws Exception {
        return this.call(user, "POST", url, data);
    }

    private String call(Principal user, String method, String url, String data) throws Exception {
        String access = user != null ? ((Map<String, String>) ((Map<String, Object>) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails())
                .get("details")).get("tokenValue") : null;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(method);
        con.addRequestProperty("User-Agent", "Mozilla/5.0");
        con.addRequestProperty("Authorization", "Bearer " + access);
        con.setDoOutput(true);
        con.setInstanceFollowRedirects(false);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("charset", "utf-8");
        con.setUseCaches(false);
        if (data != null) {
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(data.getBytes(StandardCharsets.UTF_8));
            }
        }
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            in.lines().forEach(response::append);
        }
        return response.toString();
    }
}

