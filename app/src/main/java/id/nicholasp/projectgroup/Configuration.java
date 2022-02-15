package id.nicholasp.projectgroup;

public class Configuration {

    // API URL untuk GET, GET_DETAIL, ADD, UPDATE dan DELETE
    public static final String ip_adress = "192.168.5.93";

    // Format API
    public static final String API_1    = String.format("http://%s/api_task_group/$NAMA_KELOMPOKNYA/$NAMA_FILE_PHP.php", ip_adress);
    public static final String API_2    = String.format("http://%s/api_task_group/$NAMA_KELOMPOKNYA/$NAMA_FILE_PHP.php", ip_adress);
    public static final String API_3    = String.format("http://%s/api_task_group/$NAMA_KELOMPOKNYA/$NAMA_FILE_PHP.php", ip_adress);

}
