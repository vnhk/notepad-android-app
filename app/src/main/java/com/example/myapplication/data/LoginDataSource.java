package com.example.myapplication.data;

import com.example.myapplication.data.model.LoggedInUser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            URL url = new URL("https://noter-word-app.herokuapp.com/login");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            byte[] out = ("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}")
                    .getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            con.setFixedLengthStreamingMode(length);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            final InputStream[] inputStream = {null};
            Thread thread = new Thread(() -> {
                try {
                    con.connect();
                    try (OutputStream os = con.getOutputStream()) {
                        os.write(out);
                    }
                    inputStream[0] = con.getInputStream();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            thread.start();

            thread.join();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream[0]));
            String response = reader.readLine();

            JSONObject jsonObject = new JSONObject(response);
            Object token = jsonObject.get("token");

            LoggedInUser loggedUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username, (String) token);
            return new Result.Success<>(loggedUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}