<?php
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		//Mendapatkan Nilai Variable
		$id_detail_user = $_POST['id_detail_user'];
		$id_produk = $_POST['id_produk'];
		$tgl_beli = $_POST['tgl_beli'];
		$harga_unit = $_POST['harga_unit'];
		$jumlah_unit = $_POST['jumlah_unit'];
		
		//Pembuatan Syntax SQL
		$sql = "INSERT INTO beli (id_detail_user,id_produk,tgl_beli, harga_unit, jumlah_unit) VALUES ('$id_detail_user','$id_produk','$tgl_beli','$harga_unit','$jumlah_unit')";
		
		//Import File Koneksi database
		require_once('../koneksi.php');
		
		//Eksekusi Query database
		if(mysqli_query($con,$sql)){
			echo 'Berhasil Menambahkan Transaksi';
		}else{
			echo 'Gagal Menambahkan Transaksi';
		}
		
		mysqli_close($con);
	}
?>