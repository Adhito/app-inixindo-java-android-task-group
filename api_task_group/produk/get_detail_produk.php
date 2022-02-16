<?php 
	//Mendapatkan Nilai Dari Variable ID Pegawai yang ingin ditampilkan
	$id_produk = $_GET['id_produk'];

	//Import File Koneksi Database
	require_once('../koneksi.php');
	
	//Membuat SQL Query
	$sql = "SELECT * FROM produk  WHERE id_produk=$id_produk";
	
	//Mendapatkan Hasil
	$r = mysqli_query($con,$sql);
	
	//Membuat Array Kosong 
	$result = array();
	
	while($row = mysqli_fetch_array($r)){
		
		//Memasukkan Nama dan ID kedalam Array Kosong yang telah dibuat 
		array_push($result,array(
            "id_produk"=>$row['id_produk'],
			"seri_produk"=>$row['seri_produk'],
			"nama_produk"=>$row['nama_produk'],
			"nilai_unit"=>$row['nilai_unit'],
			"yield"=>$row['yield'], 
			"jatuh_tempo"=>$row['jatuh_tempo'],
			"minimum_transaksi"=>$row['minimum_transaksi'],
			"maksimum_transaksi"=>$row['maksimum_transaksi'],
			"kelipatan_transaksi"=>$row['kelipatan_transaksi'],
			"penerbit"=>$row['penerbit'],
			"jenis_kupon"=>$row['jenis_kupon'],
			"mata_uang"=>$row['mata_uang'],
			"pembayaran_kupon"=>$row['pembayaran_kupon']
		));
	}
	
	//Menampilkan Array dalam Format JSON
	echo json_encode(array('result'=>$result));
	
	mysqli_close($con);
?>