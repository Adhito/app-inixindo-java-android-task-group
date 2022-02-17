<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		//Mendapatkan Nilai Dari Variable 
		$id_detail_user = $_POST['id_detail_user'];
		$balance = $_POST['balance'];
		
		//import file koneksi database 
		require_once('../koneksi.php');
		
		//Membuat SQL Query
		$sql = "UPDATE detail_user SET balance = (balance + '$balance') WHERE id_detail_user = $id_detail_user";
		
		//Meng-update Database 
		if(mysqli_query($con,$sql)){
			echo 'Berhasil Top Up';
		}else{
			echo 'Gagal Top Up';
		}
		
		mysqli_close($con);
	}
?>