package id.nicholasp.projectgroup;

public class ConfigurationPortofolio {
        // API URL untuk GET, GET_DETAIL, ADD, UPDATE dan DELETE
        public static final String ip_adress = "192.168.190.102";

        // API Format Login
        public static final String URL_GET_USER = String.format("http://%s/api_task_group/portofolio/get_detail_user.php?user=", ip_adress);

        // KEY Login
        public static final String KEY_LOG_USER_ID = "'id_user'";
        public static final String KEY_LOG_USER = "username";
        public static final String KEY_LOG_PASS = "password";
        public static final String KEY_LOG_DETAIL_ID = "'id_detail_user'";
        public static final String KEY_LOG_SID = "sid";
        public static final String KEY_LOG_NAMA = "nama";
        public static final String KEY_LOG_EMAIL = "email";
        public static final String KEY_LOG_BALANCE = "balance";
        public static final String KEY_LOG_TOTAL = "total";

        // flag json
        public static final String TAG_JSON_ARRAY = "result";

    }

