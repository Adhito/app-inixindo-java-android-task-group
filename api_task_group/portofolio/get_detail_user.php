<?php 
	//Mendapatkan Nilai Dari Variable ID Pegawai yang ingin ditampilkan
	$user = $_GET['user'] ?? null;

	//Import File Koneksi Database
	require_once('../koneksi.php');
	
	//Membuat SQL Query
	$sql = "SELECT a.id_user, a.username, a.password, 
    b.id_detail_user, b.sid, b.nama, b.email, b.balance,
	IFNULL((SELECT SUM(c.total_beli) FROM `beli` c WHERE c.id_detail_user = b.id_detail_user),0) total
    FROM user a
    JOIN detail_user b ON a.id_user = b.id_user    
    WHERE username = '$user'";
	
	//Mendapatkan Hasil
	$r = mysqli_query($con,$sql);
	
	//Membuat Array Kosong 
	$result = array();
	
	while($row = mysqli_fetch_array($r)){
		
		//Memasukkan Nama dan ID kedalam Array Kosong yang telah dibuat 
		array_push($result,array(
			"id_user"=>$row['id_user'],
			"username"=>$row['username'],
			"password"=>$row['password'],
            "id_detail_user"=>$row['id_detail_user'],
			"sid"=>$row['sid'],
			"nama"=>$row['nama'],
            "email"=>$row['email'],
			"balance"=>$row['balance'],
			"total"=>$row['total']
		));
	}
	
	//Menampilkan Array dalam Format JSON
	echo json_encode(array('result'=>$result));
	
	mysqli_close($con);
?>