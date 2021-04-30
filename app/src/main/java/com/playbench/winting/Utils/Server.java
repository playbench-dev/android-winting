package com.playbench.winting.Utils;

public class Server {

    public static String serverUrl = "http://139.150.75.61:3010/";
    public static String serverUrl1 = "http://192.168.0.33:3010";

    public static String Register(){
        String str = serverUrl + "company/register";
        return str;
    }

    public static String Overlap(){
        String str = serverUrl + "company/overlap";
        return str;
    }

    public static String Login(){
        String str = serverUrl + "company/login";
        return str;
    }

    public static String Logout(){
        String str = serverUrl + "";
        return str;
    }

    public static String Withdrawal(){
        String str = serverUrl + "";
        return str;
    }

    public static String IdSearch(){
        String str = serverUrl + "";
        return str;
    }

    public static String PwSearch(){
        String str = serverUrl + "";
        return str;
    }

    public static String CompanySearch(){
        String str = serverUrl + "company/company-search";
        return str;
    }

    public static String OrderList(){
        String str = serverUrl + "company/order-list";
        return str;
    }

    public static String OrderDetail(){
        String str = serverUrl + "company/order-detail";
        return str;
    }

    public static String EstimateInsert(){
        String str = serverUrl + "company/estimate-insert";
        return str;
    }

    public static String FilmList(){
        String str = serverUrl + "company/film-list";
        return str;
    }

    public static String FilmInsert(){
        String str = serverUrl + "company/film-insert";
        return str;
    }

    public static String FilmEdit(){
        String str = serverUrl + "company/film-edit";
        return str;
    }

    public static String FilmDelete(){
        String str = serverUrl + "company/film-delete";
        return str;
    }

    public static String AntecedentsList(){
        String str = serverUrl + "company/antecedents-list";
        return str;
    }

    public static String AntecedentsDetail(){
        String str = serverUrl + "company/antecedents-detail";
        return str;
    }

    public static String AntecedentsInsert(){
        String str = serverUrl + "company/antecedents-update";
        return str;
    }

    public static String AntecedentsDone(){
        String str = serverUrl + "company/antecedents-done";
        return str;
    }

    public static String EditUser(){
        String str = serverUrl + "company/edit-user";
        return str;
    }

    public static String AlarmInfo(){
        String str = serverUrl + "company/alarm-info";
        return str;
    }

    public static String AlarmSetting(){
        String str = serverUrl + "company/alarm-setting";
        return str;
    }
}
