package id.nicholasp.projectgroup;

public class Configuration {

    // API URL untuk GET, GET_DETAIL, ADD, UPDATE dan DELETE
    public static final String ip_adress = "192.168.190.102";

    // API Format
    public static final String API_1    = String.format("http://%s/api_task_group/$NAMA_KELOMPOKNYA/$NAMA_FILE_PHP.php", ip_adress);
    public static final String API_2    = String.format("http://%s/api_task_group/$NAMA_KELOMPOKNYA/$NAMA_FILE_PHP.php", ip_adress);
    public static final String API_3    = String.format("http://%s/api_task_group/$NAMA_KELOMPOKNYA/$NAMA_FILE_PHP.php", ip_adress);

    // API Produk
    public static final String GET_ALL_PRODUCT              = String.format("http://%s/api_task_group/produk/get_all_produk.php", ip_adress);
    public static final String GET_DETAIL_PRODUCT           = String.format("http://%s/api_task_group/produk/get_detail_produk.php?id_produk=", ip_adress);
    public static final String URL_ADD_PRODUCT              = String.format("http://%s/api_task_group/produk/add_produk.php", ip_adress);
    public static final String URL_GET_ALL_COUPON           = String.format("http://%s/api_task_group/produk/get_all_coupon.php?id_detail_user=", ip_adress);
    public static final String URL_GET_ALL_PRODUCT_SEARCH   = String.format("http://%s/api_task_group/produk/get_all_produk_search.php?search_txt=", ip_adress);
    public static final String URL_GET_ALL_COUPON_SEARCH    = String.format("http://%s/api_task_group/produk/get_all_coupon_search.php", ip_adress);

    // API TAG
    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_JSON_ID_PRODUCT = "id_produk";
    public static final String PGW_ID = "emp_id";

}
