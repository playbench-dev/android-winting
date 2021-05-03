package com.playbench.winting.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Util {

    public static Uri       PICK_URI;
    public static String    FILE_PATH;
    public static String    USER_TOKEN;

    //유저정보
    public static String    USER_ID = "user_id";
    public static String    USER_PW = "user_pw";
    public static String    USER_NO = "user_no";
    public static String    COMPANY_NO = "company_no";
    public static String    COMPANY_NAME = "company_name";
    public static String    USER_NAME = "user_name";
    public static String    PHONE_NUM = "phone_num";
    public static String    REGION = "region";
    public static String    LOGIN_FLAG = "login_flag";

    public static String[]  mCodeList = new String[]{"11", "26", "27", "28", "29", "30", "31", "36", "41", "42", "43", "44", "45", "46", "47", "48", "50"};
    public static String[]  mNameList = new String[]{"서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시", "대전광역시", "울산광역시", "세종특별자치시", "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주특별자치도"};

    public static String POST(OkHttpClient client, String url, RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String JsonIsNullCheck(JSONObject object, String value) {
        try {
            if (object.isNull(value)) {
                return "";
            } else {
                return object.getString(value);
            }
        } catch (JSONException e) {

        }
        return null;
    }

    public static int JsonIntIsNullCheck(JSONObject object, String value) {
        try {
            if (object.isNull(value)) {
                return 0;
            } else {
                return object.getInt(value);
            }
        } catch (JSONException e) {

        }
        return 0;
    }

    public static boolean NetWorkCheckAndPingCheck(Context context){
        boolean check = false;
        if (getNetworkState(context) != null && getNetworkState(context).isConnected()){
            check = true;
        }else{
            Toast.makeText(context, "네트워크 연결이 끊어졌습니다.", Toast.LENGTH_SHORT).show();
        }
        return check;
    }

    public static NetworkInfo getNetworkState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo;
    }

    public static String GetFormatDEC(String number) {
        DecimalFormat dec = new DecimalFormat("##,###,###");
        if (!number.trim().equals("")) {
            number = dec.format(Long.valueOf(number));
        }
        return number;
    }

    public static String DueDate(String dueDate){
        String str = "";
        if (dueDate.equals("1")){
            str = "미정";
        }else if (dueDate.equals("2")){
            str = "1주 이내";
        }else if (dueDate.equals("3")){
            str = "1개월 이내";
        }else if (dueDate.equals("4")){
            str = "2개월 이내";
        }else if (dueDate.equals("5")){
            str = "2개월 이후";
        }else if (dueDate.equals("99")){
            str = "기타";
        }
        return str;
    }

    public static String EstimateProgress(String dueDate){
        String str = "";
        if (dueDate.equals("1")){
            str = "진헹중";
        }else if (dueDate.equals("2")){
            str = "진행중";
        }else if (dueDate.equals("3")){
            str = "시공완료";
        }else if (dueDate.equals("4")){
            str = "결제완료";
        }else {
            str = "취소";
        }
        return str;
    }

    public static String FormType(String form){
        String str = "";
        if (form.equals("1")){
            str = "아파트";
        }else if (form.equals("2")){
            str = "빌라/주택";
        }else if (form.equals("3")){
            str = "사무실";
        }else if (form.equals("4")){
            str = "공장";
        }else if (form.equals("5")){
            str = "창고";
        }else if (form.equals("99")){
            str = "기타";
        }
        return str;
    }
}
