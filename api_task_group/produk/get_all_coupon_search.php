<?php 

	$id_detail_user = $_GET['id_detail_user'];
	$search_txt = $_GET['search_txt'];
        
        //Import File Koneksi Database
	require_once('../koneksi.php');
	
	//Membuat SQL Query
	$sql = "SELECT seri_produk, yield*harga_unit*jumlah_unit/100 as kupon, tgl_kupon
			from kupon k join beli b 
			on b.id_beli = k.id_beli
			join produk p
			on p.id_produk = b.id_produk
			where b.id_detail_user=$id_detail_user AND p.seri_produk LIKE '$search_txt'
			order by 3;";
	
	//Mendapatkan Hasil
	$r = mysqli_query($con,$sql);
	
	//Membuat Array Kosong 
	$result = array();
	
	while($row = mysqli_fetch_array($r)){
		
		//Memasukkan Nama dan ID kedalam Array Kosong yang telah dibuat 
		array_push($result,array(
			"seri_produk"=>$row['seri_produk'],
			"kupon"=>$row['kupon'], 
			"tgl_kupon"=>$row['tgl_kupon']

		));
	}
	
	//Menampilkan Array dalam Format JSON
	echo json_encode(array('result'=>$result));
	
	mysqli_close($con);
?>