package com.shellever.pixierobot.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.shellever.pixierobot.bean.ChatMessage;
import com.shellever.pixierobot.bean.CommonException;
import com.shellever.pixierobot.bean.Result;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by linuxfor on 8/13/2016.
 */
public class HttpUtils {
    private static final String TAG = "HttpUtils";
    private static final String API_URL = "http://www.tuling123.com/openapi/api";
    private static final String API_KEY = "5d8ada742e9d4632b13259211867f5cb";
    private static boolean isOpenDebug = true;     //default: false

    //默认使用POST来发送请求
    public static ChatMessage sendMessage(String msg){
        return sendMessage(msg, "POST");
    }

    //发送一个消息，得到返回的消息
    public static ChatMessage sendMessage(String msg, String reqMode) {
        ChatMessage mChatMessage = new ChatMessage();
        String res;
        if("GET".equals(reqMode)) {
            res = doGet(msg);        //Get
            if(isOpenDebug){
                Log.d(TAG, "Using GET request mode");
            }
        }else/* if("POST".equals(reqMode))*/{
            res = doPost(msg);       //Post
            if(isOpenDebug){
                Log.d(TAG, "Using POST request mode");
            }
        }
        Gson gson = new Gson();
        Result result = gson.fromJson(res, Result.class);

        //异常码应该为：400001/400002/400004/400007，官方给的文档有误
        if (result.getCode() > 400000 || result.getText() == null || result.getText().trim().equals("")) {
            mChatMessage.setMsg("ErrorCode");
        } else {
            mChatMessage.setMsg(result.getText());
        }
        mChatMessage.setDate(new Date());
        mChatMessage.setType(ChatMessage.Type.INPUT);

        return mChatMessage;
    }

    //Post请求
    public static String doPost(String msg){
        String req = "key=" + API_KEY + "&info=" + msg;
        URL url;
        HttpURLConnection conn = null;
        InputStream in = null;
        OutputStream out = null;
        ByteArrayOutputStream baos = null;
        try {
            url = new URL(API_URL);     //实例化URL对象
            conn = (HttpURLConnection) url.openConnection();    //使用URL打开一个连接
            conn.setConnectTimeout(5 * 1000);   //设置连接主机超时为5000ms
            conn.setReadTimeout(5 * 1000);      //设置从主机读取数据超时为5000ms
            conn.setRequestMethod("POST");      //设置请求的方法为"POST"，默认为"GET"
            conn.setDoOutput(true);             //设置向HttpURLConnection输出，默认是false，即允许上传 //POST请求的参数要放在http正文内，故需要设为true
            conn.setDoInput(true);              //设置从HttpURLConnection读入，默认是true，即允许下载
            conn.setUseCaches(false);           //POST请求不能使用缓存，故为false
            out = conn.getOutputStream();
            out.write(req.getBytes("utf-8"));
            out.close();
            int responseCode = conn.getResponseCode();  //获取响应状态码
            if(isOpenDebug){
                Log.d(TAG, "POST ResponseCode = " + responseCode);
            }
            if (responseCode == HttpURLConnection.HTTP_OK) { //请求成功200
                in = conn.getInputStream();         //
                int len;
                byte[] buffer = new byte[128];
                baos = new ByteArrayOutputStream();

                while ((len = in.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                baos.flush();
                return baos.toString();
            } else {
                throw new CommonException("Failed to connect the server!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException("Failed to connect the server!");
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
                if(baos != null){
                    baos.close();
                }
                if(conn != null){
                    conn.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Get请求，获得返回数据
    public static String doGet(String msg) {
        URL url;
        HttpURLConnection conn = null;
        ByteArrayOutputStream out = null;
        InputStream in = null;

        try {
            url = new URL(setParameters(msg));
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);   //连接主机超时(ms)
            conn.setReadTimeout(5 * 1000);      //从主机中读取数据超时 (ms)
            conn.setRequestMethod("GET");       //
            int responseCode = conn.getResponseCode();  //获取响应状态码
            if(isOpenDebug){
                Log.d(TAG, "GET ResponseCode = " + responseCode);
            }
            if (responseCode == HttpURLConnection.HTTP_OK) { //请求成功200
                in = conn.getInputStream();         //
                int len;
                byte[] buffer = new byte[128];
                out = new ByteArrayOutputStream();

                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
                return out.toString();
            } else {
                throw new CommonException("Failed to connect the server!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException("Failed to connect the server!");
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if(conn != null){
                    conn.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //拼接URL
    private static String setParameters(String msg) {
        try {
            msg = URLEncoder.encode(msg, "UTF-8");  //使用UTF-8来编码消息内容
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return API_URL + "?key=" + API_KEY + "&info=" + msg;
    }
}

/*
//log
08-15 00:00:03.195 22981-24094/com.shellever.pixierobot D/HttpUtils: Using POST request mode
*/