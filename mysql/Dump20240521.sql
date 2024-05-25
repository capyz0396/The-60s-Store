-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: the_60s_store
-- ------------------------------------------------------
-- Server version	8.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `access_history`
--

DROP TABLE IF EXISTS `access_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `access_history` (
  `accessid` int NOT NULL AUTO_INCREMENT,
  `customerid` int DEFAULT NULL,
  `access_date` datetime DEFAULT NULL,
  PRIMARY KEY (`accessid`),
  KEY `customerid` (`customerid`),
  CONSTRAINT `access_history_ibfk_1` FOREIGN KEY (`customerid`) REFERENCES `customers` (`customerid`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `access_history`
--

LOCK TABLES `access_history` WRITE;
/*!40000 ALTER TABLE `access_history` DISABLE KEYS */;
INSERT INTO `access_history` VALUES (1,1,'2024-05-21 09:20:48'),(2,1,'2024-05-21 09:21:05'),(3,1,'2024-05-21 09:21:54'),(4,2,'2024-05-21 09:22:06'),(5,2,'2024-05-21 09:22:19'),(6,2,'2024-05-21 09:22:28'),(7,2,'2024-05-21 09:23:31'),(8,2,'2024-05-21 09:29:04'),(9,2,'2024-05-21 09:31:57'),(10,2,'2024-05-21 09:35:53'),(11,2,'2024-05-21 09:41:53'),(12,2,'2024-05-21 09:42:15'),(13,2,'2024-05-21 09:42:24'),(14,2,'2024-05-21 09:46:37'),(15,2,'2024-05-21 09:47:33'),(16,2,'2024-05-21 09:50:09'),(17,2,'2024-05-21 09:52:31'),(18,2,'2024-05-21 09:55:25'),(19,2,'2024-05-21 09:58:12'),(20,2,'2024-05-21 10:00:08'),(21,2,'2024-05-21 10:02:29'),(22,2,'2024-05-21 10:02:39'),(23,2,'2024-05-21 10:13:16');
/*!40000 ALTER TABLE `access_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_levels`
--

DROP TABLE IF EXISTS `customer_levels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_levels` (
  `levelid` int NOT NULL AUTO_INCREMENT,
  `level_name` varchar(50) NOT NULL,
  `min_points` int NOT NULL,
  `max_points` int DEFAULT NULL,
  PRIMARY KEY (`levelid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_levels`
--

LOCK TABLES `customer_levels` WRITE;
/*!40000 ALTER TABLE `customer_levels` DISABLE KEYS */;
INSERT INTO `customer_levels` VALUES (1,'Bronze',0,100),(2,'Silver',101,300),(3,'Gold',301,500),(4,'Diamond',501,1000);
/*!40000 ALTER TABLE `customer_levels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `customerid` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `date_of_birth` datetime(6) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `registration_date` datetime DEFAULT NULL,
  `access_count` int DEFAULT '0',
  `confirmation_status` tinyint(1) DEFAULT '0',
  `lock_status` tinyint(1) DEFAULT '0',
  `roleid` int DEFAULT '3',
  `loyalty_point` int DEFAULT '0',
  `levelid` int DEFAULT NULL,
  PRIMARY KEY (`customerid`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username` (`username`),
  KEY `roleid` (`roleid`),
  KEY `levelid` (`levelid`),
  CONSTRAINT `customers_ibfk_1` FOREIGN KEY (`roleid`) REFERENCES `roles` (`roleid`),
  CONSTRAINT `customers_ibfk_2` FOREIGN KEY (`levelid`) REFERENCES `customer_levels` (`levelid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,'Hoàng Long','Lê','1996-03-12 00:00:00.000000','longlh12031996@gmail.com','Otamachi, Isesaki, Japan','administrator','$2a$10$kLtKp3RuaFvXd3R7ECcN.OgsjDxHTESYenqVbXuhKhoVG77ut3wRG','2024-03-09 08:44:31',3,1,0,1,0,1),(2,'Thụy Xuân Hồng','Phạm','1995-09-05 00:00:00.000000','phamthuyxuanhong@gmail.com','Osaka, Nhật Bản','shopowner','$2a$10$PoM2mIsc2jYD/VpcxnWgWexOTPFRnOJo7f/.YPs8gMnaYnzVj1BzG','2024-03-11 21:07:59',20,1,0,2,0,1);
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_details`
--

DROP TABLE IF EXISTS `invoice_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice_details` (
  `invoice_detail_id` int NOT NULL AUTO_INCREMENT,
  `invoiceid` int DEFAULT NULL,
  `productid` int DEFAULT NULL,
  `quantity` int NOT NULL,
  `subtotal` decimal(38,2) DEFAULT NULL,
  PRIMARY KEY (`invoice_detail_id`),
  KEY `invoiceid` (`invoiceid`),
  KEY `productid` (`productid`),
  CONSTRAINT `invoice_details_ibfk_1` FOREIGN KEY (`invoiceid`) REFERENCES `invoices` (`invoiceid`),
  CONSTRAINT `invoice_details_ibfk_2` FOREIGN KEY (`productid`) REFERENCES `products` (`productid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_details`
--

LOCK TABLES `invoice_details` WRITE;
/*!40000 ALTER TABLE `invoice_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoices`
--

DROP TABLE IF EXISTS `invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoices` (
  `invoiceid` int NOT NULL AUTO_INCREMENT,
  `customerid` int DEFAULT NULL,
  `invoice_date` datetime(6) DEFAULT NULL,
  `shipping_address` varchar(255) DEFAULT NULL,
  `total_amount` decimal(38,2) DEFAULT NULL,
  `invoice_status` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`invoiceid`),
  KEY `customerid` (`customerid`),
  CONSTRAINT `invoices_ibfk_1` FOREIGN KEY (`customerid`) REFERENCES `customers` (`customerid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoices`
--

LOCK TABLES `invoices` WRITE;
/*!40000 ALTER TABLE `invoices` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_prices`
--

DROP TABLE IF EXISTS `product_prices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_prices` (
  `priceid` int NOT NULL AUTO_INCREMENT,
  `productid` int DEFAULT NULL,
  `price` int DEFAULT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  `end_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`priceid`),
  KEY `productid` (`productid`),
  CONSTRAINT `product_prices_ibfk_1` FOREIGN KEY (`productid`) REFERENCES `products` (`productid`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_prices`
--

LOCK TABLES `product_prices` WRITE;
/*!40000 ALTER TABLE `product_prices` DISABLE KEYS */;
INSERT INTO `product_prices` VALUES (1,1,168000,'2024-03-09 08:48:42.901623',NULL),(2,2,168000,'2024-03-09 08:50:42.972932',NULL),(3,3,215000,'2024-03-09 08:54:39.034804',NULL),(4,4,215000,'2024-03-09 08:56:03.774269',NULL),(5,5,246000,'2024-03-09 09:00:46.828479',NULL),(6,6,246000,'2024-03-09 09:02:34.090486',NULL),(7,7,196000,'2024-03-09 09:08:22.442827',NULL),(8,8,196000,'2024-03-09 09:09:24.128899',NULL),(9,9,196000,'2024-03-09 09:11:33.132779',NULL),(10,10,250000,'2024-05-21 09:29:03.940462',NULL),(11,11,300000,'2024-05-21 09:31:57.240153',NULL),(12,12,700000,'2024-05-21 09:35:52.753230',NULL),(13,13,300000,'2024-05-21 09:46:36.565660',NULL),(14,14,200000,'2024-05-21 09:50:09.398519',NULL),(15,15,200000,'2024-05-21 09:52:30.792841',NULL), (16,16,320000,'2024-03-09 08:48:42.901623',NULL);
/*!40000 ALTER TABLE `product_prices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `productid` int NOT NULL AUTO_INCREMENT,
  `product_name_en` varchar(255) NOT NULL,
  `product_name_vi` varchar(255) NOT NULL,
  `origin_en` varchar(255) DEFAULT NULL,
  `origin_vi` varchar(255) DEFAULT NULL,
  `product_type_en` varchar(255) DEFAULT NULL,
  `product_type_vi` varchar(255) DEFAULT NULL,
  `description_en` text,
  `description_vi` text,
  `img_url` varchar(255) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`productid`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES 
(1,'Gray Bigen - 855 Dark Brown','Bigen Xám - 855 Nâu Đỏ Thẫm','Japan','Nhật Bản','Hair Dye','Thuốc nhuộm tóc','Gray Bigen is one of the hair dye products with the best ability to cover gray hair at a reasonable price with a special formula, providing absolute color coverage, helping women have shiny, silver-free hair.\r\n\r\nWith ingredients extracted from 6 herbal nutrients, Bigen gives hair a soft appearance, preventing dryness and breakage. Achieve your desired hair color in just 10 minutes, quick and simple.','Bigen Xám là một trong những sản phẩm nhuộm tóc với khả năng phủ bạc tốt nhất với giá thành hợp lý công thức đặc biệt, cho độ bao phủ màu tuyệt đối, giúp phái đẹp sở hữu mái tóc bóng mượt, không còn sợi bạc.\r\n\r\nThành phần chứa chiết xuất từ 6 dưỡng chất thảo dược, Bigen mang lại cho tóc vẻ mềm mại, ngăn ngừa tình trạng khô xơ, gãy rụng. Màu tóc như ý chỉ trong 10 phút, nhanh gọn và thao tác đơn giản.','https://product.hstatic.net/1000348839/product/product_bhcc_855_0b70475f0735485db0501862a228df68_master.png',0),
(2,'Gray Bigen 875 - Bronze Brown','Bigen Xám 875 - Nâu Đồng','Japan','Nhật','Hair Dye','Thuốc nhuộm tóc','Gray Bigen - a special formula for Gray Coverage helps cover gray hair completely, giving women silver-free hair in just 10 minutes.\r\n\r\nThe ingredients contain 6 natural herbs that nourish the hair during coloring, preventing dryness and breakage, leaving the hair smooth and silky. In addition, Gray Bigen comes equipped with all the necessary coloring tools, such as a coloring tray, coloring comb, gloves, etc., making it convenient and cost-effective for women to dye or touch up gray hair at home.','Bigen Xám - công thức đặc biệt cho Phủ Bạc giúp phủ bạc tuyệt đối cho chị em sở hữu mái tóc không còn sợi bạc chỉ trong 10 phút\r\n\r\nThành phần chứa 06 thảo dược thiên nhiên  giúp dưỡng tóc ngay khi nhuộm, ngăn ngừa tình trạng khô xơ, gãy rụng, cho tóc luôn suôn mượt. Ngoài ra, Bigen Xám trang bị đầy đủ dụng cụ trợ nhuộm như: Khay nhuộm, lược nhuộm, bao tay,...giúp chị em nhuộm hoặc dặm chân tóc bạc tại nhà tiện lợi và tiết kiệm.','https://product.hstatic.net/1000348839/product/product_bhcc_875_422cf2b8e99446bf9c250dd403db4d32_master.png',0),
(3,'Bigen Speedy No. 2 - Hazelnut Brown with Golden Highlights','Bigen Speedy Số 2 - Nâu Hạt Dẻ Ánh Vàng','Japan','Nhật Bản','Hair Dye','Thuốc nhuộm tóc','Bigen Speedy is the number 1 solution for gray coverage, featuring a specialized formula that ensures perfect gray coverage from roots to ends. Enriched with extracts from 6 types of natural herbs, including Gleditsia, Sicklepod, Hop, Polygonum, Chamomile, and Peppermint, it nourishes the hair during the coloring process. This ensures smooth and silky hair after coloring, eliminating the need for additional hair care while maintaining softness, strength, and promoting healthy, shiny hair to prevent breakage.\r\n\r\nBigen Speedy No. 2 - Hazelnut Brown with Golden Highlights is suitable for women who prefer bright, fashionable hair colors.','Bigen Speedy giải pháp SỐ 1 cho phủ bạc - Với công thức chuyên biệt, giúp phủ bạc hoàn hảo từ chân tóc đến ngọn cùng chiết xuất 6 loại thảo dược thiên nhiên giúp DƯỠNG TÓC NGAY KHI NHUỘM TÓC: Thì Là, Tầm Gửi, Hoa Bia, Cỏ Thi, Hoa Cúc, Bạc Hà cho tóc suôn mượt sau khi nhuộm, không cần dưỡng tóc nhưng vẫn mềm mượt, chắc khoẻ, nuôi dưỡng tóc khoẻ, mượt, tránh gãy rụng.\r\n\r\nBigen Speedy Số 2 - Nâu Hạt Dẻ Ánh Vàng phù hợp chị em có sở thích màu tóc sáng màu, thời trang.','https://product.hstatic.net/1000348839/product/product_bscc_2_47e0f2c3775f40fd84531ec44ca8379f_master.png',0),
(4,'Bigen Speedy No. 8 - Natural Black','Bigen Speedy Số 8 - Đen Tự Nhiên','Japan','Nhật Bản','Hair Dye','Thuốc nhuộm tóc','Bigen Speedy is the number 1 solution for gray coverage, featuring a specialized formula that ensures perfect gray coverage from roots to ends. Enriched with extracts from 6 types of natural herbs, including Gleditsia, Sicklepod, Hop, Polygonum, Chamomile, and Peppermint, it nourishes the hair during the coloring process. This ensures smooth and silky hair after coloring, eliminating the need for additional hair care while maintaining softness, strength, and promoting healthy, shiny hair to prevent breakage.\r\n\r\nBigen Speedy No. 8 - Natural Black is suitable for women who desire naturally black hair.','Bigen Speedy giải pháp SỐ 1 cho phủ bạc - Với công thức chuyên biệt, giúp phủ bạc hoàn hảo từ chân tóc đến ngọn cùng chiết xuất 6 loại thảo dược thiên nhiên giúp DƯỠNG TÓC NGAY KHI NHUỘM TÓC: Thì Là, Tầm Gửi, Hoa Bia, Cỏ Thi, Hoa Cúc, Bạc Hà cho tóc suôn mượt sau khi nhuộm, không cần dưỡng tóc nhưng vẫn mềm mượt, chắc khoẻ, nuôi dưỡng tóc khoẻ, mượt, tránh gãy rụng.\r\n\r\nBigen Speedy Số 8 - Đen Tự Nhiên phù hợp chị em cần mái tóc đen tự nhiên ','https://product.hstatic.net/1000348839/product/product_bscc_8_6fc3f28fa4694b59a67331d3f6c61c8e_master.png',0),
(5,'Bigen Japan 2 - Light Brown','Bigen Nhật 2 - Nâu Sáng','Japan','Nhật Bản','Hair Dye','Thuốc nhuộm tóc','Bigen Japan provides perfect gray coverage with a specialized formula designed for gray hair, helping to cover grays from root to tip. Combined with 6 types of natural herbs: hop, Gleditsia, peppermint, Polygonum, chamomile, and Sicklepod, it nurtures the hair during and after coloring, preventing dryness and breakage for smooth and healthy hair.\r\n\r\nWith the accompanying coloring tools, you can achieve your desired hair color in just 10 minutes, making it quick, convenient, and simple to use at home.\r\n\r\nBigen Japan 7 - Black with Brown Highlights is suitable for those who love the traditional East Asian black-brown hair color.','Bigen Nhật phủ bạc hoàn hảo với công thức chuyên biệt dành cho tóc bạc, giúp phủ bạc từ gốc đến ngọn. Kết hợp cùng 06 loại thảo dược thiên nhiên: hoa bia, thì là, bạc hà, cỏ thi, hoa cúc, tầm gửi giúp nuôi dưỡng tóc ngay và sau khi nhuộm, ngăn ngừa tình trạng khô xơ, gãy rụng cho tóc luôn suôn mượt & chắc khoẻ. \r\n\r\nVới bộ dụng cụ nhuộm đi kèm cho bạn màu tóc như ý chỉ trong 10 phút, nhanh gọn và thao tác đơn giản tại nhà.\r\n\r\nBigen Nhật 7 - Đen Ánh Nâu phù hợp với chị em yêu thích mái tóc đen nâu truyền thống Á Đông. ','https://product.hstatic.net/1000348839/product/2-3_ce504bcdd7ec4f71a34425d40b960505_master.jpg',0),
(6,'Bigen Japan 7 - Black with Brown Highlights','Bigen Nhật 7 - Đen Ánh Nâu','Japan','Nhật Bản','Hair Dye','Thuốc nhuộm tóc','Bigen Japan provides perfect gray coverage with a specialized formula designed for gray hair, helping to cover grays from root to tip. Combined with 6 types of natural herbs: hop, Gleditsia, peppermint, Polygonum, chamomile, and Sicklepod, it nurtures the hair during and after coloring, preventing dryness and breakage for smooth and healthy hair.\r\n\r\nWith the accompanying coloring tools, you can achieve your desired hair color in just 10 minutes, making it quick, convenient, and simple to use at home.\r\n\r\nBigen Japan 7 - Black with Brown Highlights is suitable for those who love the traditional East Asian black-brown hair color.','Bigen Nhật phủ bạc hoàn hảo với công thức chuyên biệt dành cho tóc bạc, giúp phủ bạc từ gốc đến ngọn. Kết hợp cùng 06 loại thảo dược thiên nhiên: hoa bia, thì là, bạc hà, cỏ thi, hoa cúc, tầm gửi giúp nuôi dưỡng tóc ngay và sau khi nhuộm, ngăn ngừa tình trạng khô xơ, gãy rụng cho tóc luôn suôn mượt & chắc khoẻ. \r\n\r\nVới bộ dụng cụ nhuộm đi kèm cho bạn màu tóc như ý chỉ trong 10 phút, nhanh gọn và thao tác đơn giản tại nhà.\r\n\r\nBigen Nhật 7 - Đen Ánh Nâu phù hợp với chị em yêu thích mái tóc đen nâu truyền thống Á Đông. ','https://product.hstatic.net/1000348839/product/7-3_7c1ffd8252e24237b0e3a004e3c0beb1_master.jpg',0),
(7,'Bigen Silk Touch 5B - Chocolate Brown','Bigen Silk Touch 5B - Nâu Sôcôla','Japan','Nhật Bản','Hair Dye','Thuốc nhuộm tóc','Bigen Silk Touch is suitable for women who love fashionable hair colors. With a special formula that provides perfect gray coverage, along with herbal extracts such as olive oil, ginseng, silk protein, and vitamin E, it not only helps you dye your hair evenly but also deeply nourishes each strand to retain moisture, nurture, and protect your hair, leaving it smooth and especially shiny like silk after coloring.\r\n\r\nBigen Silk Touch 5B - Chocolate Brown offers a sophisticated chocolate brown shade for women who desire elegant and beautiful hair.','Bigen Silk Touch phù hợp cho chị em yêu thích màu thời trang. Với công thức đặc biệt giúp phủ bạc hoàn hảo, cùng chiết xuất thảo dược: dầu oliu, nhân sâm, silk protein và vitamin E không chỉ giúp bạn nhuộm mái tóc đều màu, mà còn cung cấp chất dinh dưỡng thấm sâu vào từng sợi tóc để giữ ẩm, nuôi dưỡng và bảo vệ tóc luôn suôn mềm, đặc biệt là bóng mượt như tơ lụa sau khi nhuộm.\r\n\r\nBigen Silk Touch 5B – Nâu Sô-cô-la thời thượng cho chị em mái tóc đẹp sang trọng.','https://product.hstatic.net/1000348839/product/product_bst_5b_91a2b39ba98947119e00331f30274fa7_master.png',0),
(8,'Rohto V11','Rohto V11','Japan','Nhật Bản','Eyes Drops','Thuốc nhỏ mắt','Rohto V11 is a Japanese eye drop containing 11 active ingredients to relieve eye strain and blurriness caused by prolonged computer or phone use. It also helps reduce itchiness, redness due to allergies, blepharitis, and external factors like dust and sweat. With a gentle formula, Rohto V11 is suitable for daily use to protect and maintain healthy eyes.','Rohto V11 là thuốc nhỏ mắt của Nhật Bản, chứa 11 hoạt chất giúp cải thiện tình trạng mỏi mắt, mờ mắt do làm việc nhiều trên máy tính, điện thoại. Sản phẩm còn hỗ trợ giảm ngứa, đỏ mắt do dị ứng, viêm mí mắt và các tác nhân bên ngoài như bụi, mồ hôi. Với công thức dịu nhẹ, Rohto V11 phù hợp sử dụng hàng ngày để bảo vệ và duy trì đôi mắt khỏe mạnh.','https://jp.rohto.com/-/media/com/v11/eyedrop/img_100545_02.jpg?la=ja-jp&rev=21eff56e88844a7c9b098c2c5bad3361',0),
(9,'Rohto V Active','Rohto V Active','Japan','Nhật Bản','Eyes Drops','Thuốc nhỏ mắt','Rohto V Active is a premium eye drop from Japan, containing active ingredients to relieve eye strain and blurriness caused by prolonged computer and phone use. It also helps improve dry eyes, itchy eyes, and other discomforts. With a unique formula, Rohto V Active provides a refreshing cooling sensation, keeping your eyes healthy and bright.','Rohto V Active là thuốc nhỏ mắt cao cấp từ Nhật Bản, chứa các thành phần hoạt tính giúp giảm mỏi mắt, mờ mắt do làm việc nhiều trên máy tính và điện thoại. Sản phẩm còn hỗ trợ cải thiện tình trạng khô mắt, ngứa mắt và các triệu chứng khó chịu khác. Với công thức độc đáo, Rohto V Active mang lại cảm giác mát lạnh sảng khoái, giúp đôi mắt luôn khỏe mạnh và tươi sáng.','https://jp.rohto.com/-/media/com/v-active/eyedrop/img_135974_02.jpg?la=ja-jp&rev=4607d65af0c249cfbafd8f5d5bbaa7a5',0),
(10,'V Rohto Premium Eye Supplement Tablets 80 tablets','Viên uống bổ mắt cao cấp V Rohto Premium 80 viên','Japan','Nhật Bản','Functional Food','Thực phẩm chức năng','V Rohto Premium Eye Supplement Tablets are a comprehensive solution for tired eyes and declining vision. With a special formula containing 7 active ingredients at maximum levels, the product helps improve eye strain, blurriness, and eye pain caused by overwork or prolonged exposure to blue light from digital screens. Additionally, V Rohto Premium supports blood circulation, protects the retina, and prevents eye diseases.','Viên uống bổ mắt V Rohto Premium là giải pháp toàn diện cho đôi mắt mệt mỏi và suy giảm thị lực. Với công thức đặc biệt chứa 7 thành phần hoạt tính tối đa, sản phẩm giúp cải thiện tình trạng mỏi mắt, mờ mắt, đau nhức mắt do làm việc quá sức hoặc tiếp xúc nhiều với ánh sáng xanh từ màn hình điện tử. Ngoài ra, V Rohto Premium còn hỗ trợ tăng cường tuần hoàn máu, bảo vệ võng mạc và ngăn ngừa các bệnh lý về mắt.','https://item-shopping.c.yimg.jp/i/n/osharecafe_6046371',0),
(11,'DHC DHA 30 Day Supply (120 capsules)','DHC DHA 30 ngày (120 viên)','Japan','Nhật Bản','Functional Food','Thực phẩm chức năng','DHC DHA is a dietary supplement containing DHA extracted from pure fish oil, providing essential nutrients for the brain and eyes. DHA helps enhance memory, concentration, and supports brain development in children while maintaining brain health in adults. The product also contains vitamin E, which helps protect cells from free radical damage. With 120 capsules, DHC DHA provides enough DHA for 30 days of use, making it convenient and easy to incorporate into your daily diet.','DHC DHA là viên uống bổ sung DHA chiết xuất từ dầu cá tinh khiết, cung cấp nguồn dưỡng chất thiết yếu cho não bộ và mắt. DHA giúp tăng cường trí nhớ, khả năng tập trung, đồng thời hỗ trợ phát triển trí não ở trẻ nhỏ và duy trì sức khỏe não bộ ở người lớn. Sản phẩm còn chứa vitamin E, giúp bảo vệ tế bào khỏi tác hại của gốc tự do. Với 120 viên, DHC DHA cung cấp đủ lượng DHA cần thiết cho 30 ngày sử dụng, tiện lợi và dễ dàng bổ sung vào chế độ dinh dưỡng hàng ngày.','https://shop.r10s.jp/acelinx/cabinet/compass1657774083.jpg',0),
(12,'DHC Vitamin C (Hard Capsules) 90-Day Supply (180 capsules)','DHC Vitamin C (Viên nang cứng) 90 ngày (180 viên)','Japan','Nhật Bản','Functional Food','Thực phẩm chức năng','DHC Vitamin C is a dietary supplement providing 1000mg of vitamin C per day in hard capsule form, helping to boost the immune system, fight oxidation, and promote skin beauty. The product also contains vitamin B2 to enhance vitamin C absorption. With 180 capsules, DHC Vitamin C is enough for 90 days, making it convenient and easy to incorporate into your daily diet.','DHC Vitamin C là viên uống bổ sung vitamin C dạng viên nang cứng, cung cấp 1000mg vitamin C mỗi ngày, giúp tăng cường hệ miễn dịch, chống oxy hóa và làm đẹp da. Sản phẩm còn chứa vitamin B2 hỗ trợ hấp thu vitamin C tốt hơn. Với 180 viên, DHC Vitamin C đủ dùng trong 90 ngày, tiện lợi và dễ dàng bổ sung vào chế độ dinh dưỡng hàng ngày.','https://item-shopping.c.yimg.jp/i/n/mokomoko-shop_nk3-0002410',0),
(13,'DHC Multivitamin 90-Day Supply (90 tablets)','DHC Multivitamin 90 ngày (90 viên)','Japan','Nhật Bản','Functional Food','Thực phẩm chức năng','DHC Multivitamin is a dietary supplement providing 11 essential vitamins, helping to compensate for vitamin deficiencies in your daily diet. The product supports a stronger immune system, maintains healthy skin, improves vision, and enhances nerve function. With a balanced formula and appropriate vitamin content, DHC Multivitamin helps you maintain overall health and energy for a busy lifestyle.','DHC Multivitamin là viên uống bổ sung 11 loại vitamin thiết yếu, giúp bù đắp lượng vitamin thiếu hụt trong chế độ ăn uống hàng ngày. Sản phẩm hỗ trợ tăng cường sức đề kháng, duy trì làn da khỏe mạnh, cải thiện thị lực và chức năng thần kinh. Với công thức cân đối và hàm lượng vitamin phù hợp, DHC Multivitamin giúp bạn duy trì sức khỏe tổng thể và năng lượng cho cuộc sống bận rộn.','https://item-shopping.c.yimg.jp/i/n/macaron-store_s10394',0),
(14,'DHC Multimineral 90-Day Supply (270 tablets)','DHC Multimineral 90 ngày (270 viên)','Japan','Nhật Bản','Functional Food','Thực phẩm chức năng','DHC Multimineral is a dietary supplement providing 10 essential minerals, supporting the maintenance of healthy bones, teeth, hair, nails, and other important physiological functions. The product contains minerals such as calcium, magnesium, iron, zinc, copper, selenium, chromium, manganese, iodine, and molybdenum, helping to compensate for mineral deficiencies in your daily diet. DHC Multimineral is especially suitable for people with unbalanced diets, vegetarians, the elderly, and pregnant or breastfeeding women.','DHC Multimineral là viên uống bổ sung 10 loại khoáng chất thiết yếu, hỗ trợ duy trì sức khỏe xương, răng, tóc, móng và các chức năng sinh lý quan trọng khác. Sản phẩm chứa các khoáng chất như canxi, magie, sắt, kẽm, đồng, selen, crom, mangan, iốt và molypden, giúp bù đắp lượng khoáng chất thiếu hụt trong chế độ ăn uống hàng ngày. DHC Multimineral đặc biệt phù hợp với những người ăn uống không cân đối, người ăn chay, người lớn tuổi và phụ nữ mang thai hoặc cho con bú.','https://item-shopping.c.yimg.jp/i/n/first-value_4511413403518-3_1',0),
(15,'Rohto Cool 40α 12mL x 10','Rohto Cool 40α 12mL x 10','Japan','Nhật Bản','Eyes Drops','Thuốc nhỏ mắt','Rohto Cool 40α is an eye drop containing 4 essential vitamins, helping to soothe and refresh tired eyes caused by prolonged computer use, exposure to blue light, and other irritants. The product provides an instant cooling sensation while improving blurry vision, dry eyes, and itchy eyes. With a compact design, Rohto Cool 40α is convenient to carry around and use whenever you need it.','Rohto Cool 40α là thuốc nhỏ mắt chứa 4 loại vitamin thiết yếu, giúp làm dịu và phục hồi đôi mắt mệt mỏi do làm việc nhiều trên máy tính, tiếp xúc với ánh sáng xanh và các tác nhân gây kích ứng khác. Sản phẩm mang lại cảm giác mát lạnh sảng khoái tức thì, đồng thời hỗ trợ cải thiện tình trạng mờ mắt, khô mắt và ngứa mắt. Với thiết kế nhỏ gọn, Rohto Cool 40α tiện lợi mang theo bên mình và sử dụng bất cứ khi nào bạn cần.','https://images.eyedrops.biz/l-m/cool-40-alpha-japanese-eye-v-81544024.jpg',0),
(16,'V Rohto Premium 15mL','V Rohto Premium 15mL','Japan','Nhật Bản','Eyes Drops','Thuốc nhỏ mắt','V Rohto Premium is a premium eye drop containing 12 active ingredients, helping to restore and protect tired, dry eyes caused by prolonged computer use, exposure to blue light, and other irritants. The product relieves eye strain and blurriness while providing essential moisture and nutrients for the eyes. With a gentle, non-irritating formula, V Rohto Premium is suitable for daily use to maintain healthy and clear eyes.','V Rohto Premium là thuốc nhỏ mắt cao cấp chứa 12 thành phần hoạt tính, giúp phục hồi và bảo vệ đôi mắt mệt mỏi, khô rát do làm việc nhiều trên máy tính, tiếp xúc với ánh sáng xanh và các tác nhân gây kích ứng khác. Sản phẩm giúp giảm mỏi mắt, mờ mắt, đồng thời cung cấp độ ẩm và dưỡng chất cần thiết cho mắt. Với công thức dịu nhẹ, không gây cay mắt, V Rohto Premium phù hợp sử dụng hàng ngày để duy trì đôi mắt khỏe mạnh và sáng rõ.','https://store-thumber.nicho.co.jp/?storagekey=%2Fspree%2Fproducts%2F9122%2Foriginal%2Fproduct_4987241174454_0001.jpg',0);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `roleid` int NOT NULL AUTO_INCREMENT,
  `rolename` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`roleid`),
  UNIQUE KEY `rolename` (`rolename`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ADMIN'),(2,'OWNER'),(3,'USER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tokens`
--

DROP TABLE IF EXISTS `tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tokens` (
  `tokenid` int NOT NULL AUTO_INCREMENT,
  `customerid` int DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `expiry_date` datetime DEFAULT NULL,
  `is_token_confirmed` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`tokenid`),
  KEY `customerid` (`customerid`),
  CONSTRAINT `tokens_ibfk_1` FOREIGN KEY (`customerid`) REFERENCES `customers` (`customerid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tokens`
--

LOCK TABLES `tokens` WRITE;
/*!40000 ALTER TABLE `tokens` DISABLE KEYS */;
INSERT INTO `tokens` VALUES (1,1,'039a74f3-acd1-48c0-9fe9-2076ab1aa5b1','2024-03-10 08:44:31',1),(2,2,'862f5647-8e86-4a6a-8e80-0e12ebbe5619','2024-03-12 21:07:59',1);
/*!40000 ALTER TABLE `tokens` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-21 10:17:43
