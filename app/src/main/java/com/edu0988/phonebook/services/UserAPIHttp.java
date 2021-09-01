package com.edu0988.phonebook.services;

import android.util.Log;

import com.edu0988.phonebook.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class UserAPIHttp implements UserAPI {

    private static final String HOST = "http://0988.vozhzhaev.ru/";
    private static final String host1 = "http://q90313c1.beget.tech/handlerAddUser.php";
    private static final String ADD = "handlerAddUser.php";
    private static final String GET = "handlerGetUser.php";
    private static final String GET_ALL = "handlerGetUsers.php";
    private static final String UPDATE = "handlerUpdateUser.php";
    private static final String DELETE = "handlerDeleteUser.php";
    private static final String PARAM_UUID = "?uuid=%s";
    private static final String PARAM_FULL = "?name=%s&lastname=%s&phone=%s&uuid=%s";

    @Override
    public boolean add(User user) {
        try {
            URL url = new URL(HOST + ADD + getUrlEncodedParamsFull(user));
            connect(url);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User get(String uuid) {
        try {
            URL url = new URL(HOST + GET + getUrlEncodedParamsUUID(uuid));

            String json = connect(url);
            JSONObject jsonObject = new JSONObject(json);
            User user = new User(jsonObject.getString("uuid"));
            user.setName(jsonObject.getString("username"));
            user.setLastname(jsonObject.getString("lastname"));
            user.setPhone(jsonObject.getString("phone"));
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<User> getAll() {

        Log.d("MY_TAG", "GetALL " + Thread.currentThread().toString());

        List<User> userList = new ArrayList<>();
        try {
            URL url = new URL(HOST + GET_ALL);

            String json = connect(url);
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                User user = new User(jsonObject.getString("uuid"));
                user.setName(jsonObject.getString("username"));
                user.setLastname(jsonObject.getString("lastname"));
                user.setPhone(jsonObject.getString("phone"));
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public boolean update(User updateUser) {

        Log.d("MY_TAG", "UpdateUser " + Thread.currentThread().toString());

        User user = get(updateUser.getUuid());

        if (user == null) {
            return add(updateUser);
        } else {

            try {
                URL url = new URL(HOST + UPDATE + getUrlEncodedParamsFull(updateUser));
                connect(url);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean delete(String uuid) {

        Log.d("MY_TAG", "DeleteUser " + Thread.currentThread().toString());

        User user = get(uuid);

        if (user != null) {
            try {
                URL url = new URL(HOST + DELETE + getUrlEncodedParamsUUID(user.getUuid()));
                connect(url);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static String connect(URL url) {
        String response = "";
        try {
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.addRequestProperty("User-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36");
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            response = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static String getUrlEncodedParamsFull(User user) throws UnsupportedEncodingException {
        return String.format(PARAM_FULL,
                URLEncoder.encode(user.getName(), "UTF-8"),
                URLEncoder.encode(user.getLastname(), "UTF-8"),
                URLEncoder.encode(user.getPhone(), "UTF-8"),
                URLEncoder.encode(user.getUuid(), "UTF-8")
        );
    }

    private static String getUrlEncodedParamsUUID(String uuid) throws UnsupportedEncodingException {
        return String.format(PARAM_UUID, URLEncoder.encode(uuid, "UTF-8"));
    }

}
