package id.nicholasp.projectgroup;

public class ConfigurationPortofolio {
        // API URL untuk GET, GET_DETAIL, ADD, UPDATE dan DELETE
        public static final String ip_adress = "192.168.1.10";

        // API Format Login
        public static final String URL_GET_USER = String.format("http://%s/api_task_group/portofolio/get_detail_user.php?user=", ip_adress);
        public static final String URL_GET_DETAIL_PORTOFOLIO = String.format("http://%s/api_task_group/portofolio/get_detail_obigasi.php?user=", ip_adress);

        // KEY Login
        public static final String KEY_LOG_USER_ID = "id_user";
        public static final String KEY_LOG_USER = "username";
        public static final String KEY_LOG_PASS = "password";
        public static final String KEY_LOG_DETAIL_ID = "id_detail_user";
        public static final String KEY_LOG_SID = "sid";
        public static final String KEY_LOG_NAMA = "nama";
        public static final String KEY_LOG_EMAIL = "email";
        public static final String KEY_LOG_BALANCE = "balance";
        public static final String KEY_LOG_HP = "no_hp";
        public static final String KEY_LOG_TOTAL = "total";
        public static final String KEY_LOG_SERI = "seri_produk";
        public static final String KEY_LOG_NAMA_PRODUK = "nama_produk";
        public static final String KEY_LOG_HARGA_BELI = "harga_unit";

        // flag json
        public static final String TAG_JSON_ARRAY = "result";
        public static final String TAG_JSON_SERI = "seri_produk";
        public static final String TAG_JSON_PRODUK = "nama_produk";
        public static final String TAG_JSON_TOTAL = "total";
        public static final String TAG_JSON_BELI = "harga_unit";

    }

