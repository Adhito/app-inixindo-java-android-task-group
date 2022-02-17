<?php
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		//Mendapatkan Nilai Variable
		$id_detail_user = $_POST['id_detail_user'];
		$id_produk = $_POST['id_produk'];
		$tgl_beli = $_POST['tgl_beli'];
		$harga_unit = $_POST['harga_unit'];
		$jumlah_unit = $_POST['jumlah_unit'];
		
		//Pembuatan Syntax SQL
		$sql1 = "INSERT INTO beli (id_detail_user,id_produk,tgl_beli, harga_unit, jumlah_unit) VALUES ('$id_detail_user','$id_produk','$tgl_beli','$harga_unit','$jumlah_unit')";
		$sql2 = "Update detail_user du 
				set du.balance = (SELECT du.balance - (select jumlah_unit*harga_unit from  beli where 
								id_beli in (select max(id_beli) from beli where id_detail_user=$id_detail_user))
								FROM detail_user du 
								WHERE du.id_detail_user = $id_detail_user)
				where du.id_detail_user= $id_detail_user";

		//Import File Koneksi database
		require_once('../koneksi.php');
		
		//Eksekusi Query SQL 1 database
		if(mysqli_query($con,$sql1)){
			echo 'Berhasil Menambahkan Transaksi';
		}else{
			echo 'Gagal Menambahkan Transaksi';
		}



		//Eksekusi Query SQL 2 database
		if(mysqli_query($con,$sql2)){
			echo 'Berhasil update balance user';
		}else{
			echo 'Gagal update balance user';
		}	
		
		mysqli_close($con);
	}
?>