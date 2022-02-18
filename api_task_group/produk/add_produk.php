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
		
		$sql2 ="UPDATE detail_user du 
				set du.balance = (SELECT du.balance - (select jumlah_unit*harga_unit from  beli where 
								id_beli in (select max(id_beli) from beli where id_detail_user=$id_detail_user))
								FROM detail_user du 
								WHERE du.id_detail_user = $id_detail_user)
				where du.id_detail_user=$id_detail_user";

		$sql3 ="INSERT INTO kupon(id_beli, tgl_kupon)
		WITH RECURSIVE t as (
				SELECT DATE_FORMAT((select tgl_beli from beli where id_beli in (select max(id_beli) from beli where id_detail_user=$id_detail_user)), '%Y-%m-%d') as dt
			  UNION
				SELECT DATE_ADD(t.dt, INTERVAL 6 month) FROM t WHERE DATE_ADD(t.dt, INTERVAL 6 month) between dt and (select jatuh_tempo 
							from produk p join beli b
							on p.id_produk=b.id_produk
							where id_beli in (select max(id_beli) from beli where id_detail_user=$id_detail_user))
			)
			select beli.id_beli, dt
			from t join beli join produk on beli.id_produk=produk.id_produk
			where id_beli in (select max(id_beli) from beli where id_detail_user=$id_detail_user) LIMIT 18446744073709551615 OFFSET 1;";

		$sql4 = "DELETE from kupon where tgl_kupon = CURDATE() AND id_detail_user=$id_detail_user;";

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

		//Eksekusi Query SQL 3 database
		if(mysqli_query($con,$sql3)){
			echo 'Berhasil update jadwal coupon user';
		}else{
			echo 'Gagal update jadwal coupon user';
		}	

		// //Eksekusi Query SQL 4 database
		// if(mysqli_query($con,$sql4)){
		// 	echo 'Berhasil delete jadwal coupon redundan ';
		// }else{
		// 	echo 'Gagal delete jadwal coupon redundan';
		// }	
		
		mysqli_close($con);
	}
?>