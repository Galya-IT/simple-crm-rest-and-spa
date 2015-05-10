CREATE DATABASE  IF NOT EXISTS `crm` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `crm`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: localhost    Database: crm
-- ------------------------------------------------------
-- Server version	5.6.19-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Clients` (
  `Client_Id` int(11) NOT NULL AUTO_INCREMENT,
  `Client_Contract_Date` date DEFAULT NULL,
  `Client_Is_Deleted` bit(1) DEFAULT NULL,
  `Client_Location` varchar(50) DEFAULT NULL,
  `Client_Logo_File_Name` varchar(255) DEFAULT NULL,
  `Client_Name` varchar(50) NOT NULL,
  `Client_Notes` longtext,
  PRIMARY KEY (`Client_Id`),
  UNIQUE KEY `UK_mqo2j2mo8dh6vtj9nirp82806` (`Client_Name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Clients`
--

LOCK TABLES `Clients` WRITE;
/*!40000 ALTER TABLE `Clients` DISABLE KEYS */;
INSERT INTO `Clients` VALUES (1,'2004-08-20','\0','London','Shell Logo.jpg','Royal Dutch Shell','Royal Dutch Shell plc (LSE: RDSA, RDSB), commonly known as Shell, is an Angloâ. Dutch multinational oil and gas company headquartered in the Netherlands and incorporated in the United Kingdom.[2] Created by the merger of Royal Dutch Petroleum and UK-based Shell Transport & Trading, it is the second largest company in the world, in terms of revenue,[1] and one of the six oil and gas supermajors. Shell is also one of the world\'s most valuable companies.[3] As of January, 2013 the largest shareholder is Capital Research Global Investors with 9.85% ahead of BlackRock in second with 6.89%.[4] Shell topped the 2013 Fortune Global 500 list of the world\'s largest companies.[5] Royal Dutch Shell revenue was equal to 84% of the Netherlands\'s $555.8 billion GDP at the time.[6]'),(2,'2008-09-20','\0','Irving, Texas','250px-Exxon_Mobil_Logo.svg.png','Exxon Mobil Corporation','Exxon Mobil Corp., or ExxonMobil, is an American multinational oil and gas corporation headquartered in Irving, Texas, United States. It is a direct descendant of John D. Rockefeller\'s Standard Oil company,[3] and was formed on November 30, 1999, by the merger of Exxon and Mobil (formerly Standard Oil of New Jersey and Standard Oil of New York). It is affiliated with Imperial Oil which operates in Canada. The world\'s third largest company by revenue, ExxonMobil is also the second largest publicly traded company by market capitalization.[4][5] The company was ranked No. 5 globally in Forbes Global 2000 list in 2013.[6] ExxonMobil\'s reserves were 72 billion BOE (barrels of oil equivalent) at the end of 2007 and, at then (2007) rates of production, were expected to last more than 14 years.[7] With 37 oil refineries in 21 countries constituting a combined daily refining capacity of 6.3 million barrels (1,000,000 m3), ExxonMobil is the largest refiner in the world,[8][9] a title that was also associated with Standard Oil since its incorporation in 1870.[10]'),(3,'2006-12-20','\0','China Beijing','283px-CNPC.svg.png','China National Petroleum CorporationChina Beijing','Chinese China National Petroleum Corporation (CNfbgPC) is a Chinese state-owned oil and gas corporation and the largest integrated energy company in the People\'s Republic of China. It has its headquarters in Dongcheng District, Beijing.[3] CNPC is the parent of PetroChina, the second largest company in the world in terms of market capitalization as of June 2010.[4]'),(4,'2001-09-08','\0','Bentonville, Arkansas','New_Walmart_Logo.svg','Wal-mart','Wal-Mart Stores, Inc., branded as Walmart /4sdgfv64/, is an American multinational retail corporation that runs chains of large discount department stores and warehouse stores. Headquartered in Bentonville, Arkansas, the company was founded by Sam Walton in 1962 and incorporated on October 31, 1969. It has over 11,000 stores in 27 countries, under a total 55 different names.[7] The company operates under the Walmart name in the US and Puerto Rico. It operates in Mexico as Walmart de México y Centroamérica, in the United Kingdom as Asda, in Japan as Seiyu, and in India as Best Price. It has wholly owned operations in Argentina, Brazil, and Canada.');
/*!40000 ALTER TABLE `Clients` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-08-18 15:09:49
