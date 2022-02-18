<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		//Mendapatkan Nilai Dari Variable 
		$id_detail_user = $_POST['id_detail_user'];
		$nama = $_POST['nama'];
        $email = $_POST['email'];
        $no_hp = $_POST['no_hp'];
		
		//import file koneksi database 
		require_once('../koneksi.php');
		
		//Membuat SQL Query
		$sql = "UPDATE detail_user SET nama = '$nama', email = '$email', no_hp = '$no_hp' WHERE id_detail_user = '$id_detail_user'";
		
		//Meng-update Database 
		if(mysqli_query($con,$sql)){
			echo 'Berhasil Update Profile';
		}else{
			echo 'Gagal Update Profile';
		}
		
		mysqli_close($con);
	}
?>