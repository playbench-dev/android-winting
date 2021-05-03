package com.playbench.winting.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.playbench.winting.Utils.ServerManagement.AsyncResponse;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.playbench.winting.Utils.Util.NetWorkCheckAndPingCheck;
import static org.apache.http.conn.ssl.SSLSocketFactory.SSL;

public class NetworkUtils {

    public static String mRequestResult                 = "";
    public static String ERROR_CD                       = "Error_Cd";
    public static String ERROR_NM                       = "Error_Nm";
    public static String RESOURCES                      = "Resources";
    public static String REQUEST_SUCCESS                = "0000";

    public static String REGISTER                       = "REGISTER";                   //회원가입
    public static String OVERLAP                        = "OVERLAP";                    //중복체크
    public static String LOGIN                          = "LOGIN";                      //로그인
    public static String LOGOUT                         = "LOGOUT";                     //로그아웃
    public static String WITHDRAWAL                     = "WITHDRAWAL";                 //회원탈퇴
    public static String ID_SEARCH                      = "ID_SEARCH";                  //아이디 찾기
    public static String PW_SEARCH                      = "PW_SEARCH";                  //비밀번호 찾기
    public static String PW_CHANGE                      = "PW_CHANGE";                  //비밀번호 변경
    public static String COMPANY_SEARCH                 = "COMPANY_SEARCH";             //업체검색
    public static String ORDER_LIST                     = "ORDER_LIST";                 //견적 리스트
    public static String ORDER_DETAIL                   = "ORDER_DETAIL";               //견적 상세
    public static String ESTIMATE_INSERT                = "ESTIMATE_INSERT";            //견적산출
    public static String FILM_LIST                      = "FILM_LIST";                  //필름리스트
    public static String FILM_INSERT                    = "FILM_INSERT";                //필름 등록
    public static String FILM_EDIT                      = "FILM_EDIT";                  //필름 수정
    public static String FILM_DELETE                    = "FILM_DELETE";                //필름 삭제
    public static String ANTECEDENTS_LIST               = "ANTECEDENTS_LIST";           //이력 리스트
    public static String ANTECEDENTS_DETAIL             = "ANTECEDENTS_DETAIL";         //이력 상세
    public static String ANTECEDENTS_INSERT             = "ANTECEDENTS_INSERT";         //이력 저장
    public static String ANTECEDENTS_DONE               = "ANTECEDENTS_DONE";           //이력 완료
    public static String EDIT_USER                      = "EDIT_USER";                  //정보수정
    public static String ALARM_INFO                     = "ALARM_INFO";                 //알람정보
    public static String ALARM_SETTING                  = "ALARM_SETTING";              //알람수정
    public static String ESTIMATE_IMAGE_CALL            = "ESTIMATE_IMAGE_CALL";        //이미지 호출
    public static String ESTIMATE_IMAGE_DELETE          = "ESTIMATE_IMAGE_DELETE";      //이미지 삭제
    public static String ESTIMATE_DETAIL                = "ESTIMATE_DETAIL";            //견적 산출 상세


    public static class NetworkCall extends AsyncTask<String, String, String> {

        public AsyncResponse delegate = null;
        String code;
        String mUrl;
        Context context;
        private String TAG;

        public NetworkCall(Context context,AsyncResponse delegate,String TAG ,String code) {
            this.code = code;
            this.context = context;
            this.TAG = TAG;
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //memo 네트워크 체크 후 진행, 미연결시 요청종료
            if (!NetWorkCheckAndPingCheck(context)){
                this.onCancelled();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            FormBody.Builder body = new FormBody.Builder();

            if (code.equals(REGISTER)){
                mUrl = Server.Register();
                body.add("user_id",strings[0])
                        .add("user_pw",strings[1])
                        .add("company_no",strings[2])
                        .add("user_name",strings[3])
                        .add("phone_num",strings[4])
                        .add("region",strings[5])
                        .add("token",strings[6])
                        .add("use_yn","0");
            }else if (code.equals(OVERLAP)){
                mUrl = Server.Overlap();
                body.add("user_id",strings[0]);
            }else if (code.equals(LOGIN)){
                mUrl = Server.Login();
                body.add("user_id",strings[0])
                        .add("user_pw",strings[1])
                        .add("token",strings[2]);
            }else if (code.equals(ID_SEARCH)){
                mUrl = Server.IdSearch();
                body.add("user_name",strings[0])
                        .add("phone_num",strings[1]);
            }else if (code.equals(PW_SEARCH)){
                mUrl = Server.PwSearch();
                body.add("user_id",strings[0])
                        .add("phone_num",strings[1]);
            }else if (code.equals(WITHDRAWAL)){
                mUrl = Server.Withdrawal();
                body.add("user_no",strings[0]);
            }else if (code.equals(COMPANY_SEARCH)){
                mUrl = Server.CompanySearch();
                body.add("search_text",strings[0])
                        .add("page_num",strings[1])
                        .add("page_showCnt",strings[2]);
            }else if (code.equals(ORDER_LIST)){
                mUrl = Server.OrderList();
                body.add("user_no",strings[0])
                        .add("page_num",strings[1])
                        .add("page_showCnt",strings[2])
                .add("region",strings[3]);
                Log.i(TAG,"region : " + strings[3]);
            }else if (code.equals(ORDER_DETAIL)){
                mUrl = Server.OrderDetail();
                body.add("order_no",strings[0]);
            }else if (code.equals(ESTIMATE_INSERT)){
                mUrl = Server.EstimateInsert();
                body.add("user_no",strings[0])
                        .add("order_no",strings[1])
                        .add("estimate_info",strings[2]);
            }else if (code.equals(FILM_LIST)){
                mUrl = Server.FilmList();
                body.add("user_no",strings[0])
                        .add("page_num",strings[1])
                        .add("page_showCnt",strings[2]);
            }else if (code.equals(FILM_INSERT)){
                mUrl = Server.FilmInsert();
                body.add("user_no",strings[0])
                        .add("type",strings[1])
                .add("brand",strings[2])
                .add("product",strings[3])
                .add("vlt",strings[4])
                .add("uvr",strings[5])
                .add("irr",strings[6])
                .add("tser",strings[7])
                .add("as_date",strings[8]);
            }else if (code.equals(FILM_EDIT)){
                mUrl = Server.FilmEdit();
                body.add("film_no",strings[0])
                        .add("type",strings[1])
                        .add("brand",strings[2])
                        .add("product",strings[3])
                        .add("vlt",strings[4])
                        .add("uvr",strings[5])
                        .add("irr",strings[6])
                        .add("tser",strings[7])
                        .add("as_date",strings[8]);
            }else if (code.equals(FILM_DELETE)){
                mUrl = Server.FilmDelete();
                body.add("film_no",strings[0]);
            }else if (code.equals(ANTECEDENTS_LIST)){
                mUrl = Server.AntecedentsList();
                body.add("user_no",strings[0])
                        .add("page_num",strings[1])
                        .add("page_showCnt",strings[2]);
            }else if (code.equals(ANTECEDENTS_DETAIL)){
                mUrl = Server.AntecedentsDetail();
                body.add("estimate_no",strings[0]);
            }else if (code.equals(ANTECEDENTS_INSERT)){
                mUrl = Server.AntecedentsInsert();
                body.add("user_id",strings[0]);
                body.add("estimate_no",strings[1]);
                if (strings[2].equals("0000-00-00")){
                    strings[2] = "";
                }else{
                    body.add("start_date",strings[2]);
                }
                if (strings[3].equals("0000-00-00")){
                    strings[3] = "";
                }else{
                    body.add("end_date",strings[3]);
                }

            }else if (code.equals(ANTECEDENTS_DONE)){
                mUrl = Server.AntecedentsDone();
                body.add("estimate_no",strings[0]);
            }else if (code.equals(EDIT_USER)){
                mUrl = Server.EditUser();
                body.add("user_no",strings[0])
                        .add("user_pw",strings[1])
                        .add("region",strings[2]);
            }else if (code.equals(ALARM_INFO)){
                mUrl = Server.AlarmInfo();
                body.add("user_no",strings[0]);
            }else if (code.equals(ALARM_SETTING)){
                mUrl = Server.AlarmSetting();
                body.add("user_no",strings[0])
                        .add("new",strings[1])
                        .add("progress_state",strings[2]);
            }else if (code.equals(ESTIMATE_IMAGE_CALL)){
                mUrl = Server.EstimateImageCall();
                body.add("order_no",strings[0]);
            }else if (code.equals(ESTIMATE_IMAGE_DELETE)){
                mUrl = Server.EstimateImageDelete();
                body.add("orderNo",strings[0])
                .add("fileSaveType",strings[1])
                .add("filePath",strings[2]);
            }else if (code.equals(ESTIMATE_DETAIL)){
                mUrl = Server.EstimateDetail();
                body.add("estimate_no",strings[0]);
            }

            OkHttpClient client = new OkHttpClient();

            try {
                mRequestResult = POST(client, mUrl, body.build());
                Log.d(TAG,"NetworkCall : " + mRequestResult);
                return mRequestResult;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null){
                if (NetWorkCheckAndPingCheck(context)){
                    delegate.ProcessFinish(code,s);
                }else{
                    this.onCancelled();
                }
            }else{
                Toast.makeText(context, "서버와 통신이 불안전합니다. 잠시후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String POST(OkHttpClient client, String url, RequestBody body) throws IOException {

        final SSLContext sslContext;
        javax.net.ssl.SSLSocketFactory sslSocketFactory = null;
        try {
            sslContext = SSLContext.getInstance(SSL);
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.sslSocketFactory(sslSocketFactory,(X509TrustManager) trustAllCerts[0]);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client = builder.build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    final static TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                               String authType) throws
                        CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                               String authType) throws
                        CertificateException {
                }
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }
    };

}
