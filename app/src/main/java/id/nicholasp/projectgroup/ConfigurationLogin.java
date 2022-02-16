package id.nicholasp.projectgroup;

public class ConfigurationLogin {

    // API URL untuk GET, GET_DETAIL, ADD, UPDATE dan DELETE
    public static final String ip_adress = "192.168.5.93";

    // API Format Login
    public static final String URL_GET_LOGIN = String.format("http://%s/api_task_group/login/cek_login.php", ip_adress);
    public static final String URL_ADD_LOGIN = String.format("http://%s/api_task_group/login/add_login.php", ip_adress);
    public static final String URL_GET_USER = String.format("http://%s/api_task_group/login/cek_user.php?user=", ip_adress);
//    public static final String URL_ADD_LOGIN = "http://192.168.190.102/obligasi/login/add_login.php";
//    public static final String URL_GET_LOGIN = "http://192.168.190.102/obligasi/login/cek_login.php";

    // KEY Login
    public static final String KEY_LOG_USER = "username";
    public static final String KEY_LOG_PASS = "password";
    public static final String KEY_LOG_SID = "sid";
    public static final String KEY_LOG_NAMA = "nama";
    public static final String KEY_LOG_EMAIL = "email";

}
