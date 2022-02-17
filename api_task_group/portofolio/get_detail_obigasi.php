<?php 
	//Mendapatkan Nilai Dari Variable ID Pegawai yang ingin ditampilkan
	$user = $_GET['user'];

	//Import File Koneksi Database
	require_once('../koneksi.php');
	
	//Membuat SQL Query
	$sql = "SELECT du.id_detail_user, du.nama,p.seri_produk, p.nama_produk, SUM(b.jumlah_unit) total, b.harga_unit
    FROM detail_user du
    JOIN user us ON du.id_user = us.id_user
    JOIN beli b ON du.id_detail_user = b.id_detail_user
    JOIN produk p ON b.id_produk = p.id_produk
    WHERE username = '$user'
    GROUP BY p.id_produk";
	
	//Mendapatkan Hasil
	$r = mysqli_query($con,$sql);
	
	//Membuat Array Kosong 
	$result = array();
	
	while($row = mysqli_fetch_array($r)){
		
		//Memasukkan Nama dan ID kedalam Array Kosong yang telah dibuat 
		array_push($result,array(
			"id_detail_user"=>$row['id_detail_user'],
			"nama"=>$row['nama'],
			"seri_produk"=>$row['seri_produk'],
			"nama_produk"=>$row['nama_produk'],
            "total"=>$row['total'],
			"harga_unit"=>$row['harga_unit']
		));
	}
	
	//Menampilkan Array dalam Format JSON
	echo json_encode(array('result'=>$result));
	
	mysqli_close($con);
?>