<?php
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		//Mendapatkan Nilai Variable
		$username = $_POST['username'];
		$password = $_POST['password'];
        $sid = $_POST['sid'];
		$nama  = $_POST['nama'];
        $email = $_POST['email'];
		
		//Pembuatan Syntax SQL
		$sql = "INSERT INTO user (username,password) VALUES ('$username','$password')";
        $sql2 = "INSERT INTO detail_user (sid,nama,email,balance,id_user) VALUES ('$sid','$nama','$email','0', (SELECT MAX(id_user) FROM user))";
        // $sql = "INSERT INTO detail_user (sid,nama,id_user) VALUES ('$sid','$nama','1')";
		
		//Import File Koneksi database
		require_once('../koneksi.php');
		
		//Eksekusi Query database
		if(mysqli_query($con,$sql)){
			echo 'Berhasil Menambahkan User';
		}else{
			echo 'Gagal Menambahkan User';
		}
		if(mysqli_query($con,$sql2)){
			echo 'Berhasil Menambahkan reg';
		}else{
			echo 'Gagal Menambahkan reg';
		}
		mysqli_close($con);
	}
?>