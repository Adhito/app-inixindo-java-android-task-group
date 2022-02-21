-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 22 Feb 2022 pada 00.37
-- Versi server: 10.4.22-MariaDB
-- Versi PHP: 8.1.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `maybankobligasi`
--

DELIMITER $$
--
-- Prosedur
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `dates_between` (IN `from_date` DATETIME, IN `to_date` DATETIME)  BEGIN
    WITH RECURSIVE dates(Date) AS
    (
        SELECT from_date as Date
        UNION ALL
        SELECT DATE_ADD(Date, INTERVAL 1 day) FROM dates WHERE Date < to_date
    )
    SELECT DATE(Date) FROM dates;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Struktur dari tabel `beli`
--

CREATE TABLE `beli` (
  `id_beli` int(11) NOT NULL,
  `id_detail_user` int(11) NOT NULL,
  `id_produk` int(11) NOT NULL,
  `tgl_beli` date NOT NULL,
  `harga_unit` float NOT NULL,
  `jumlah_unit` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `beli`
--

INSERT INTO `beli` (`id_beli`, `id_detail_user`, `id_produk`, `tgl_beli`, `harga_unit`, `jumlah_unit`) VALUES
(41, 2, 1, '2022-02-18', 1000000, 40),
(42, 2, 6, '2022-02-18', 1000000, 20),
(43, 2, 1, '2022-02-18', 1000000, 20),
(44, 2, 9, '2022-02-18', 1000000, 2000),
(45, 4, 1, '2022-02-18', 1000000, 1),
(46, 7, 1, '2022-02-20', 1000000, 9),
(47, 7, 4, '2022-02-21', 1000000, 1);

-- --------------------------------------------------------

--
-- Struktur dari tabel `detail_user`
--

CREATE TABLE `detail_user` (
  `id_detail_user` int(11) NOT NULL,
  `sid` int(11) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `no_hp` varchar(13) NOT NULL,
  `balance` double(100,1) NOT NULL,
  `id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `detail_user`
--

INSERT INTO `detail_user` (`id_detail_user`, `sid`, `nama`, `email`, `no_hp`, `balance`, `id_user`) VALUES
(1, 933013, 'Chairani Tiara', 'chairani@email.com', '08112233445', 7000000.0, 4),
(2, 508382, 'Nicholas Phandinata', 'nicholasp@email.com', '08132233445', 1008113111111.0, 1),
(3, 662133, 'Adi Yusuf', 'adi@email.com', '08525544332', 10000000.0, 3),
(4, 119545, 'Farid Pridiatama', 'faridp@email.com', '08123344556', 83220011.0, 2),
(6, 98765, 'minarin', 'mina@email.com', '08123746373', 10507532.0, 6),
(7, 112233, 'Messi leo', 'messi@gmail.com', '0899221133', 40000000.0, 7);

-- --------------------------------------------------------

--
-- Struktur dari tabel `kupon`
--

CREATE TABLE `kupon` (
  `id_kupon` int(11) NOT NULL,
  `id_beli` int(11) NOT NULL,
  `tgl_kupon` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `kupon`
--

INSERT INTO `kupon` (`id_kupon`, `id_beli`, `tgl_kupon`) VALUES
(2465, 41, '2022-08-18'),
(2466, 41, '2023-02-18'),
(2467, 41, '2023-08-18'),
(2468, 41, '2024-02-18'),
(2469, 41, '2024-08-18'),
(2472, 42, '2022-08-18'),
(2473, 43, '2022-08-18'),
(2474, 43, '2023-02-18'),
(2475, 43, '2023-08-18'),
(2476, 43, '2024-02-18'),
(2477, 43, '2024-08-18'),
(2480, 44, '2022-08-18'),
(2481, 44, '2023-02-18'),
(2482, 44, '2023-08-18'),
(2483, 45, '2022-08-18'),
(2484, 45, '2023-02-18'),
(2485, 45, '2023-08-18'),
(2486, 45, '2024-02-18'),
(2487, 45, '2024-08-18'),
(2488, 46, '2022-08-20'),
(2489, 46, '2023-02-20'),
(2490, 46, '2023-08-20'),
(2491, 46, '2024-02-20'),
(2492, 46, '2024-08-20'),
(2493, 47, '2022-08-21'),
(2494, 47, '2023-02-21'),
(2495, 47, '2023-08-21'),
(2496, 47, '2024-02-21'),
(2497, 47, '2024-08-21');

-- --------------------------------------------------------

--
-- Struktur dari tabel `produk`
--

CREATE TABLE `produk` (
  `id_produk` int(11) NOT NULL,
  `seri_produk` varchar(10) NOT NULL,
  `nama_produk` varchar(50) NOT NULL,
  `nilai_unit` float NOT NULL,
  `yield` decimal(6,3) DEFAULT NULL,
  `jatuh_tempo` date NOT NULL,
  `minimum_transaksi` float NOT NULL,
  `kelipatan_transaksi` float NOT NULL,
  `penerbit` varchar(100) DEFAULT NULL,
  `jenis_kupon` varchar(20) DEFAULT NULL,
  `mata_uang` varchar(20) DEFAULT NULL,
  `pembayaran_kupon` varchar(25) DEFAULT NULL,
  `maksimum_transaksi` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `produk`
--

INSERT INTO `produk` (`id_produk`, `seri_produk`, `nama_produk`, `nilai_unit`, `yield`, `jatuh_tempo`, `minimum_transaksi`, `kelipatan_transaksi`, `penerbit`, `jenis_kupon`, `mata_uang`, `pembayaran_kupon`, `maksimum_transaksi`) VALUES
(1, 'ORI020', 'Obligasi Negara Ritel 020', 1000000, '4.950', '2024-10-15', 1000000, 1000000, 'Pemerintah Republik Indonesia', 'fixed', 'rupiah', 'Setiap 6 Bulan', 2000000000),
(2, 'ORI017', 'Obligasi Negara Ritel 017', 1000000, '6.400', '2023-07-15', 1000000, 1000000, 'Pemerintah Republik Indonesia', 'fixed', 'rupiah', 'Setiap 6 Bulan', 2000000000),
(3, 'ORI018', 'Obligasi Negara Ritel 018', 1000000, '5.700', '2023-10-15', 1000000, 1000000, 'Pemerintah Republik Indonesia', 'fixed', 'rupiah', 'Setiap 6 Bulan', 2000000000),
(4, 'ORI021', 'Obligasi Negara Ritel 021', 1000000, '4.900', '2025-02-15', 1000000, 1000000, 'Pemerintah Republik Indonesia', 'fixed', 'rupiah', 'Setiap 6 Bulan', 2000000000),
(5, 'ORI019', 'Obligasi Negara Ritel 019', 1000000, '5.570', '2024-02-15', 1000000, 1000000, 'Pemerintah Republik Indonesia', 'fixed', 'rupiah', 'Setiap 6 Bulan', 2000000000),
(6, 'SR010', 'Sukuk Negara Ritel Seri SR-010', 1000000, '5.900', '2022-12-31', 1000000, 1000000, 'Pemerintah Republik Indonesia', 'fixed', 'rupiah', 'Setiap 6 Bulan', 2000000000),
(7, 'SR011', 'Sukuk Negara Ritel Seri SR-011', 1000000, '8.050', '2022-03-10', 1000000, 1000000, 'Pemerintah Republik Indonesia', 'fixed', 'rupiah', 'Setiap 6 Bulan', 2000000000),
(8, 'SR012', 'Sukuk Negara Ritel Seri SR-012', 1000000, '6.300', '2023-03-10', 1000000, 1000000, 'Pemerintah Republik Indonesia', 'fixed', 'rupiah', 'Setiap 6 Bulan', 2000000000),
(9, 'SR013', 'Sukuk Negara Ritel Seri SR-013', 1000000, '6.050', '2023-09-10', 1000000, 1000000, 'Pemerintah Republik Indonesia', 'fixed', 'rupiah', 'Setiap 6 Bulan', 2000000000);

-- --------------------------------------------------------

--
-- Struktur dari tabel `user`
--

CREATE TABLE `user` (
  `id_user` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `user`
--

INSERT INTO `user` (`id_user`, `username`, `password`) VALUES
(1, 'nicholasp', '2a9e5708715cf08cca55805374c305ae'),
(2, 'faridp', '2a9e5708715cf08cca55805374c305ae'),
(3, 'adiyusuf', '2a9e5708715cf08cca55805374c305ae'),
(4, 'chairanit', '2a9e5708715cf08cca55805374c305ae'),
(6, 'minarin', '2a9e5708715cf08cca55805374c305ae'),
(7, 'messiun', 'c4ca4238a0b923820dcc509a6f75849b');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `beli`
--
ALTER TABLE `beli`
  ADD PRIMARY KEY (`id_beli`),
  ADD KEY `cons_id_user_beli` (`id_detail_user`),
  ADD KEY `cons_id_produk_beli` (`id_produk`);

--
-- Indeks untuk tabel `detail_user`
--
ALTER TABLE `detail_user`
  ADD PRIMARY KEY (`id_detail_user`),
  ADD KEY `cons_id_user` (`id_user`);

--
-- Indeks untuk tabel `kupon`
--
ALTER TABLE `kupon`
  ADD PRIMARY KEY (`id_kupon`),
  ADD KEY `cons_id_beli_kupon` (`id_beli`);

--
-- Indeks untuk tabel `produk`
--
ALTER TABLE `produk`
  ADD PRIMARY KEY (`id_produk`);

--
-- Indeks untuk tabel `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id_user`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `beli`
--
ALTER TABLE `beli`
  MODIFY `id_beli` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;

--
-- AUTO_INCREMENT untuk tabel `detail_user`
--
ALTER TABLE `detail_user`
  MODIFY `id_detail_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT untuk tabel `kupon`
--
ALTER TABLE `kupon`
  MODIFY `id_kupon` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2498;

--
-- AUTO_INCREMENT untuk tabel `produk`
--
ALTER TABLE `produk`
  MODIFY `id_produk` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT untuk tabel `user`
--
ALTER TABLE `user`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `beli`
--
ALTER TABLE `beli`
  ADD CONSTRAINT `cons_id_produk_beli` FOREIGN KEY (`id_produk`) REFERENCES `produk` (`id_produk`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `cons_id_user_beli` FOREIGN KEY (`id_detail_user`) REFERENCES `detail_user` (`id_detail_user`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `detail_user`
--
ALTER TABLE `detail_user`
  ADD CONSTRAINT `cons_id_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `kupon`
--
ALTER TABLE `kupon`
  ADD CONSTRAINT `cons_id_beli_kupon` FOREIGN KEY (`id_beli`) REFERENCES `beli` (`id_beli`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
