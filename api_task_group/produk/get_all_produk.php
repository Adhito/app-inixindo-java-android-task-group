<?php 
	//Import File Koneksi Database
	require_once('../koneksi.php');
	
	//Membuat SQL Query
	$sql = "SELECT * FROM produk";
	
	//Mendapatkan Hasil
	$r = mysqli_query($con,$sql);
	
	//Membuat Array Kosong 
	$result = array();
	
	while($row = mysqli_fetch_array($r)){
		
		//Memasukkan Nama dan ID kedalam Array Kosong yang telah dibuat 
		array_push($result,array(
                        "id_produk"=>$row['id_produk'],
			"seri_produk"=>$row['seri_produk'],
			"yield"=>$row['yield'], 
			"jatuh_tempo"=>$row['jatuh_tempo'],
                        "nilai_unit"=>$row['nilai_unit'] 


		));
	}
	
	//Menampilkan Array dalam Format JSON
	echo json_encode(array('result'=>$result));
	
	mysqli_close($con);
?>