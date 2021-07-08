-- MySQL dump 10.17  Distrib 10.3.24-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: hillfog
-- ------------------------------------------------------
-- Server version	10.3.24-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `hf_aggregation_method`
--

DROP TABLE IF EXISTS `hf_aggregation_method`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_aggregation_method` (
  `OID` char(36) NOT NULL,
  `AGGR_ID` varchar(14) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `TYPE` varchar(10) NOT NULL,
  `EXPRESSION1` varchar(4000) NOT NULL,
  `EXPRESSION2` varchar(4000) NOT NULL,
  `DESCRIPTION` varchar(500) DEFAULT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`AGGR_ID`),
  FULLTEXT KEY `IDX_1` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_aggregation_method`
--

LOCK TABLES `hf_aggregation_method` WRITE;
/*!40000 ALTER TABLE `hf_aggregation_method` DISABLE KEYS */;
INSERT INTO `hf_aggregation_method` VALUES ('07675b9f-07d3-11eb-a5e8-a7944d4e6612','MAX_001','Max','GROOVY','import org.qifu.hillfog.util.*;\n\nscore = AggregationMethod.build().max(kpi, formula, measureDatas);\n','import org.qifu.hillfog.util.*;\n\ndateRangeScores = AggregationMethod.build().maxDateRange(kpi, formula, measureDatas, frequency, dateRangeScores);\n','get max score!','admin','2020-10-06 12:54:10','admin','2020-10-14 09:25:02'),('1f2dbe0b-07d2-11eb-a5e8-31faaa9ef6bf','AVG_001','Average','GROOVY','import org.qifu.hillfog.util.*;\n\nscore = AggregationMethod.build().average(kpi, formula, measureDatas);\n','import org.qifu.hillfog.util.*;\n\ndateRangeScores = AggregationMethod.build().averageDateRange(kpi, formula, measureDatas, frequency, dateRangeScores);\n','for Average!','admin','2020-10-06 12:47:41','admin','2020-10-14 09:21:00'),('337a2a10-07d3-11eb-a5e8-6ba5d1ef74bc','MIN_001','Min','GROOVY','import org.qifu.hillfog.util.*;\n\nscore = AggregationMethod.build().min(kpi, formula, measureDatas);\n','import org.qifu.hillfog.util.*;\n\ndateRangeScores = AggregationMethod.build().minDateRange(kpi, formula, measureDatas, frequency, dateRangeScores);\n','get min score!','admin','2020-10-06 12:55:24','admin','2020-10-14 09:26:05'),('5595cffc-07d2-11eb-a5e8-9156c2b89371','AVG_002','Avg Dist (average distinct)','GROOVY','import org.qifu.hillfog.util.*;\n\nscore = AggregationMethod.build().averageDistinct(kpi, formula, measureDatas);\n','import org.qifu.hillfog.util.*;\n\ndateRangeScores = AggregationMethod.build().averageDistinctDateRange(kpi, formula, measureDatas, frequency, dateRangeScores);\n','For example, the average of (10, 10, 20) is 40/3, but the distinct average of (10, 10, 20) is 30/3.','admin','2020-10-06 12:49:12','admin','2020-10-14 09:21:47'),('5d65d2c1-07d3-11eb-a5e8-03b3da263274','SUM_001','Sum','GROOVY','import org.qifu.hillfog.util.*;\n\nscore = AggregationMethod.build().sum(kpi, formula, measureDatas);\n','import org.qifu.hillfog.util.*;\n\ndateRangeScores = AggregationMethod.build().sumDateRange(kpi, formula, measureDatas, frequency, dateRangeScores);\n','for Sum!','admin','2020-10-06 12:56:35','admin','2020-10-14 09:27:59'),('84a98cf2-07d3-11eb-a5e8-a78a4a0f6d92','SUM_002','Sum Dist (sum distinct)','GROOVY','import org.qifu.hillfog.util.*;\n\nscore = AggregationMethod.build().sumDistinct(kpi, formula, measureDatas);\n','import org.qifu.hillfog.util.*;\n\ndateRangeScores = AggregationMethod.build().sumDistinctDateRange(kpi, formula, measureDatas, frequency, dateRangeScores);\n','For example, the sum of (10,10) is 20, but the distinct sum of (10,10) is 10.','admin','2020-10-06 12:57:41','admin','2020-10-14 09:30:05'),('868fce8d-07d2-11eb-a5e8-938551191f7a','CNT_001','Count','GROOVY','import org.qifu.hillfog.util.*;\n\nscore = AggregationMethod.build().count(kpi, formula, measureDatas);\n','import org.qifu.hillfog.util.*;\n\ndateRangeScores = AggregationMethod.build().countDateRange(kpi, formula, measureDatas, frequency, dateRangeScores);\n','returns the count records of measure data. \nDoes not require the calculation formula.','admin','2020-10-06 12:50:34','admin','2020-10-14 09:22:52'),('b22b4a1e-07d2-11eb-a5e8-85cf340d68c6','CNT_002','Count Dist (count distinct)','GROOVY','import org.qifu.hillfog.util.*;\n\nscore = AggregationMethod.build().countDistinct(kpi, formula, measureDatas);\n','import org.qifu.hillfog.util.*;\n\ndateRangeScores = AggregationMethod.build().countDistinctDateRange(kpi, formula, measureDatas, frequency, dateRangeScores);\n','For example, the count of (1, 1, 2, 3) is 4, but the distinct count of (1, 1, 2, 3) is 3.','admin','2020-10-06 12:51:47','admin','2020-10-14 09:24:06');
/*!40000 ALTER TABLE `hf_aggregation_method` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_employee`
--

DROP TABLE IF EXISTS `hf_employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_employee` (
  `OID` char(36) NOT NULL,
  `ACCOUNT` varchar(24) NOT NULL,
  `EMP_ID` varchar(15) NOT NULL,
  `NAME` varchar(25) NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL DEFAULT '',
  `UPLOAD_OID` char(36) DEFAULT NULL,
  `JOB_TITLE` varchar(100) NOT NULL DEFAULT '',
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`ACCOUNT`),
  UNIQUE KEY `UK_2` (`EMP_ID`),
  FULLTEXT KEY `IDX_1` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_employee`
--

LOCK TABLES `hf_employee` WRITE;
/*!40000 ALTER TABLE `hf_employee` DISABLE KEYS */;
INSERT INTO `hf_employee` VALUES ('0','admin','EMP001','Administrator','',NULL,'','sys','2020-10-03 09:43:37',NULL,NULL),('35e73cc7-0519-11eb-b8a3-b35804e4e844','bill','EMP004','Bill chen','',NULL,'','admin','2020-10-03 01:39:00',NULL,NULL),('4e5d23ab-0519-11eb-b8a3-0762c594204e','frank','EMP002','Frank wang','',NULL,'','admin','2020-10-03 01:39:41',NULL,NULL),('640607df-0519-11eb-b8a3-5fb4ab04d7ee','cindy','EMP003','Cindy liu','',NULL,'','admin','2020-10-03 01:40:17',NULL,NULL);
/*!40000 ALTER TABLE `hf_employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_employee_hier`
--

DROP TABLE IF EXISTS `hf_employee_hier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_employee_hier` (
  `OID` char(36) NOT NULL,
  `EMP_OID` char(36) NOT NULL,
  `PARENT_OID` char(36) NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`EMP_OID`),
  KEY `IDX_1` (`PARENT_OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_employee_hier`
--

LOCK TABLES `hf_employee_hier` WRITE;
/*!40000 ALTER TABLE `hf_employee_hier` DISABLE KEYS */;
INSERT INTO `hf_employee_hier` VALUES ('0794c0f3-c661-4d25-bb07-c5ba46af4697','0','00000000-0000-0000-0000-000000000000','admin','2021-07-08 21:19:24',NULL,NULL),('52786e42-6ec2-4ac5-b903-d5999c0941b5','35e73cc7-0519-11eb-b8a3-b35804e4e844','00000000-0000-0000-0000-000000000000','admin','2021-07-08 21:19:55',NULL,NULL),('a5fcd9bc-e32f-4ce9-a7d3-b1786834c25f','640607df-0519-11eb-b8a3-5fb4ab04d7ee','00000000-0000-0000-0000-000000000000','admin','2021-07-08 21:21:30',NULL,NULL),('aeb3dced-89d6-48cf-8380-a4fb5b417bc2','4e5d23ab-0519-11eb-b8a3-0762c594204e','00000000-0000-0000-0000-000000000000','admin','2021-07-08 21:20:28',NULL,NULL);
/*!40000 ALTER TABLE `hf_employee_hier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_employee_org`
--

DROP TABLE IF EXISTS `hf_employee_org`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_employee_org` (
  `OID` char(36) NOT NULL,
  `ACCOUNT` varchar(24) NOT NULL,
  `ORG_ID` varchar(15) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`ACCOUNT`,`ORG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_employee_org`
--

LOCK TABLES `hf_employee_org` WRITE;
/*!40000 ALTER TABLE `hf_employee_org` DISABLE KEYS */;
INSERT INTO `hf_employee_org` VALUES ('35f28768-0519-11eb-b8a3-8bfef45aea64','bill','IT','admin','2020-10-03 01:39:00',NULL,NULL),('4e5ed15c-0519-11eb-b8a3-c5dbe5d18783','frank','FIN','admin','2020-10-03 01:39:41',NULL,NULL),('64074060-0519-11eb-b8a3-3d80b8b9c3e1','cindy','GA','admin','2020-10-03 01:40:17',NULL,NULL);
/*!40000 ALTER TABLE `hf_employee_org` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_formula`
--

DROP TABLE IF EXISTS `hf_formula`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_formula` (
  `OID` char(36) NOT NULL,
  `FOR_ID` varchar(14) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `TYPE` varchar(10) NOT NULL,
  `RETURN_MODE` varchar(1) NOT NULL DEFAULT 'D',
  `RETURN_VAR` varchar(50) NOT NULL DEFAULT '',
  `EXPRESSION` varchar(4000) NOT NULL,
  `DESCRIPTION` varchar(500) DEFAULT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`FOR_ID`),
  FULLTEXT KEY `IDX_1` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_formula`
--

LOCK TABLES `hf_formula` WRITE;
/*!40000 ALTER TABLE `hf_formula` DISABLE KEYS */;
INSERT INTO `hf_formula` VALUES ('6349c44a-07d1-11eb-a5e8-ef9de1a822a0','F001','percent of target','GROOVY','C','ans','import org.apache.commons.lang3.math.*;\nimport java.lang.*;\n\nif ($P{actual} > 0 && $P{target} > 0 ) {\n    ans = $P{actual} ÷ $P{target} × 100;\n    return ans;\n}\nif (\n    ($P{actual} <= 0 && $P{target} < 0 && $P{actual} >= $P{target})\n    ||\n    ($P{actual} <= 0 && $P{target} < 0 && $P{actual} < $P{target})\n    ||\n    ($P{actual} > 0 && $P{target} < 0 )\n) {\n    ans = (((($P{target} - $P{actual}) ÷ $P{target}) × 100) + 100);\n    return ans;\n}\nif ($P{actual} < 0 && $P{target} > 0 ) {\n    ans = (((($P{actual} - $P{target}) ÷ $P{target}) × 100) + 100);\n    return ans;\n}\nif ($P{actual} == 0 && $P{target} > 0 ) {\n    ans = BigDecimal.ZERO;\n    return ans;\n}\nif ($P{actual} >= 0 && $P{target} == 0 ) {\n    ans = (($P{actual} × 100) + 100);\n    return ans;\n}\nif ($P{actual} < 0 && $P{target} == 0 ) {\n    ans = (($P{actual} × 100) + 100);\n    return ans;\n}\nans = BigDecimal.ZERO;\nreturn ans;','','admin','2020-10-06 12:42:26',NULL,NULL);
/*!40000 ALTER TABLE `hf_formula` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_initiatives`
--

DROP TABLE IF EXISTS `hf_initiatives`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_initiatives` (
  `OID` char(36) NOT NULL,
  `OBJ_OID` char(36) NOT NULL,
  `CONTENT` varchar(2000) NOT NULL DEFAULT ' ',
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  KEY `IDX_1` (`OBJ_OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_initiatives`
--

LOCK TABLES `hf_initiatives` WRITE;
/*!40000 ALTER TABLE `hf_initiatives` DISABLE KEYS */;
/*!40000 ALTER TABLE `hf_initiatives` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_key_res`
--

DROP TABLE IF EXISTS `hf_key_res`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_key_res` (
  `OID` char(36) NOT NULL,
  `OBJ_OID` char(36) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `TARGET` decimal(12,2) NOT NULL DEFAULT 0.00,
  `GP_TYPE` varchar(1) NOT NULL,
  `OP_TARGET` varchar(1) NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL DEFAULT ' ',
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`OBJ_OID`,`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_key_res`
--

LOCK TABLES `hf_key_res` WRITE;
/*!40000 ALTER TABLE `hf_key_res` DISABLE KEYS */;
/*!40000 ALTER TABLE `hf_key_res` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_key_res_val`
--

DROP TABLE IF EXISTS `hf_key_res_val`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_key_res_val` (
  `OID` char(36) NOT NULL,
  `OBJ_OID` char(36) NOT NULL,
  `RES_OID` char(36) NOT NULL,
  `DATE` varchar(8) NOT NULL,
  `VALUE` decimal(12,2) NOT NULL DEFAULT 0.00,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`OBJ_OID`,`RES_OID`,`DATE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_key_res_val`
--

LOCK TABLES `hf_key_res_val` WRITE;
/*!40000 ALTER TABLE `hf_key_res_val` DISABLE KEYS */;
/*!40000 ALTER TABLE `hf_key_res_val` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_kpi`
--

DROP TABLE IF EXISTS `hf_kpi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_kpi` (
  `OID` char(36) NOT NULL,
  `ID` varchar(14) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(500) DEFAULT NULL,
  `WEIGHT` decimal(5,2) NOT NULL DEFAULT 0.00,
  `UNIT` varchar(20) NOT NULL,
  `FOR_ID` varchar(14) NOT NULL,
  `MAX` decimal(12,2) NOT NULL DEFAULT 0.00,
  `TARGET` decimal(12,2) NOT NULL DEFAULT 0.00,
  `MIN` decimal(12,2) NOT NULL DEFAULT 0.00,
  `MANAGEMENT` varchar(1) NOT NULL DEFAULT '1',
  `COMPARE_TYPE` varchar(1) NOT NULL DEFAULT '1',
  `AGGR_ID` varchar(14) NOT NULL,
  `DATA_TYPE` varchar(1) NOT NULL,
  `QUASI_RANGE` int(3) NOT NULL DEFAULT 0,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`ID`),
  KEY `IDX_1` (`NAME`,`FOR_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_kpi`
--

LOCK TABLES `hf_kpi` WRITE;
/*!40000 ALTER TABLE `hf_kpi` DISABLE KEYS */;
INSERT INTO `hf_kpi` VALUES ('ba3915ff-0dbf-11eb-9ab3-0336f20b4d50','K01','測試KPI','test',40.00,'%','F001',100.00,80.00,60.00,'1','1','AVG_001','1',0,'admin','2020-10-14 01:51:07',NULL,NULL);
/*!40000 ALTER TABLE `hf_kpi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_kpi_empl`
--

DROP TABLE IF EXISTS `hf_kpi_empl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_kpi_empl` (
  `OID` char(36) NOT NULL,
  `KPI_ID` varchar(14) NOT NULL,
  `ACCOUNT` varchar(24) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`KPI_ID`,`ACCOUNT`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_kpi_empl`
--

LOCK TABLES `hf_kpi_empl` WRITE;
/*!40000 ALTER TABLE `hf_kpi_empl` DISABLE KEYS */;
INSERT INTO `hf_kpi_empl` VALUES ('ba44fce2-0dbf-11eb-9ab3-b31beff232e4','K01','frank','admin','2020-10-14 01:51:07',NULL,NULL);
/*!40000 ALTER TABLE `hf_kpi_empl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_kpi_orga`
--

DROP TABLE IF EXISTS `hf_kpi_orga`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_kpi_orga` (
  `OID` char(36) NOT NULL,
  `KPI_ID` varchar(14) NOT NULL,
  `ORG_ID` varchar(15) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`KPI_ID`,`ORG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_kpi_orga`
--

LOCK TABLES `hf_kpi_orga` WRITE;
/*!40000 ALTER TABLE `hf_kpi_orga` DISABLE KEYS */;
INSERT INTO `hf_kpi_orga` VALUES ('ba3fa5b0-0dbf-11eb-9ab3-5fad6c7d8fdd','K01','FIN','admin','2020-10-14 01:51:07',NULL,NULL),('ba41a181-0dbf-11eb-9ab3-a3a8e161485d','K01','HR','admin','2020-10-14 01:51:07',NULL,NULL);
/*!40000 ALTER TABLE `hf_kpi_orga` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_measure_data`
--

DROP TABLE IF EXISTS `hf_measure_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_measure_data` (
  `OID` char(36) NOT NULL,
  `KPI_ID` varchar(14) NOT NULL,
  `DATE` varchar(8) NOT NULL,
  `TARGET` decimal(12,2) NOT NULL DEFAULT 0.00,
  `ACTUAL` decimal(12,2) NOT NULL DEFAULT 0.00,
  `FREQUENCY` varchar(1) NOT NULL,
  `ORG_ID` varchar(10) NOT NULL DEFAULT '*',
  `ACCOUNT` varchar(24) NOT NULL DEFAULT '*',
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`KPI_ID`,`DATE`,`FREQUENCY`,`ORG_ID`,`ACCOUNT`) USING BTREE,
  KEY `IDX_1` (`FREQUENCY`,`ORG_ID`,`ACCOUNT`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_measure_data`
--

LOCK TABLES `hf_measure_data` WRITE;
/*!40000 ALTER TABLE `hf_measure_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `hf_measure_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_obj_dept`
--

DROP TABLE IF EXISTS `hf_obj_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_obj_dept` (
  `OID` char(36) NOT NULL,
  `OBJ_OID` char(36) NOT NULL,
  `ORG_ID` varchar(15) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`OBJ_OID`,`ORG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_obj_dept`
--

LOCK TABLES `hf_obj_dept` WRITE;
/*!40000 ALTER TABLE `hf_obj_dept` DISABLE KEYS */;
/*!40000 ALTER TABLE `hf_obj_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_obj_owner`
--

DROP TABLE IF EXISTS `hf_obj_owner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_obj_owner` (
  `OID` char(36) NOT NULL,
  `OBJ_OID` char(36) NOT NULL,
  `ACCOUNT` varchar(24) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`OBJ_OID`,`ACCOUNT`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_obj_owner`
--

LOCK TABLES `hf_obj_owner` WRITE;
/*!40000 ALTER TABLE `hf_obj_owner` DISABLE KEYS */;
/*!40000 ALTER TABLE `hf_obj_owner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_objective`
--

DROP TABLE IF EXISTS `hf_objective`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_objective` (
  `OID` char(36) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `START_DATE` varchar(8) NOT NULL,
  `END_DATE` varchar(8) NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL DEFAULT ' ',
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_objective`
--

LOCK TABLES `hf_objective` WRITE;
/*!40000 ALTER TABLE `hf_objective` DISABLE KEYS */;
/*!40000 ALTER TABLE `hf_objective` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_org_dept`
--

DROP TABLE IF EXISTS `hf_org_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_org_dept` (
  `OID` char(36) NOT NULL,
  `ORG_ID` varchar(15) NOT NULL,
  `NAME` varchar(25) NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL DEFAULT '',
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`ORG_ID`),
  FULLTEXT KEY `IDX_1` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_org_dept`
--

LOCK TABLES `hf_org_dept` WRITE;
/*!40000 ALTER TABLE `hf_org_dept` DISABLE KEYS */;
INSERT INTO `hf_org_dept` VALUES ('036ae3f5-0519-11eb-b8a3-f12f3fa151f4','SD','Sales','','admin','2020-10-03 01:37:35',NULL,NULL),('7b7dadff-0518-11eb-b8a3-ef60293112ad','IT','Information technology','','admin','2020-10-03 01:33:47',NULL,NULL),('84a212a0-0518-11eb-b8a3-c738e5f57b42','FIN','Finance','','admin','2020-10-03 01:34:02',NULL,NULL),('93a41551-0518-11eb-b8a3-07ddefe26180','HR','Human resources','','admin','2020-10-03 01:34:27',NULL,NULL),('b9bd8052-0518-11eb-b8a3-b7023b66d33c','AC','Accounting','','admin','2020-10-03 01:35:31',NULL,NULL),('d1b97c43-0518-11eb-b8a3-6d8338deef0c','CEO','CEO office','','admin','2020-10-03 01:36:12',NULL,NULL),('e95cd0e4-0518-11eb-b8a3-a732cf32f327','GA','General affairs','','admin','2020-10-03 01:36:51',NULL,NULL);
/*!40000 ALTER TABLE `hf_org_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_pdca`
--

DROP TABLE IF EXISTS `hf_pdca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_pdca` (
  `OID` char(36) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `MST_TYPE` varchar(1) NOT NULL DEFAULT ' ',
  `MST_OID` char(36) NOT NULL,
  `KPI_FREQUENCY` varchar(1) NOT NULL DEFAULT ' ',
  `KPI_MEASURE_DATE1` varchar(8) NOT NULL DEFAULT '00000000',
  `KPI_MEASURE_DATE2` varchar(8) NOT NULL DEFAULT '00000000',
  `START_DATE` varchar(8) NOT NULL,
  `END_DATE` varchar(8) NOT NULL,
  `CONFIRM_UID` varchar(24) DEFAULT NULL,
  `CONFIRM_DATE` datetime DEFAULT NULL,
  `DESCRIPTION` varchar(4000) DEFAULT NULL,
  `PDCA_NUM` varchar(100) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`NAME`,`MST_TYPE`,`MST_OID`),
  KEY `IDX_1` (`START_DATE`,`END_DATE`),
  KEY `IDX_2` (`PDCA_NUM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_pdca`
--

LOCK TABLES `hf_pdca` WRITE;
/*!40000 ALTER TABLE `hf_pdca` DISABLE KEYS */;
/*!40000 ALTER TABLE `hf_pdca` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_pdca_attc`
--

DROP TABLE IF EXISTS `hf_pdca_attc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_pdca_attc` (
  `OID` char(36) NOT NULL,
  `PDCA_OID` char(36) NOT NULL,
  `UPLOAD_OID` char(36) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  KEY `IDX_1` (`PDCA_OID`,`UPLOAD_OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_pdca_attc`
--

LOCK TABLES `hf_pdca_attc` WRITE;
/*!40000 ALTER TABLE `hf_pdca_attc` DISABLE KEYS */;
/*!40000 ALTER TABLE `hf_pdca_attc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_pdca_close_req`
--

DROP TABLE IF EXISTS `hf_pdca_close_req`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_pdca_close_req` (
  `OID` char(36) NOT NULL,
  `PDCA_OID` char(36) NOT NULL,
  `DESCRIPTION` varchar(4000) NOT NULL,
  `CONFIRM_UID` varchar(24) DEFAULT NULL,
  `CONFIRM_DATE` datetime DEFAULT NULL,
  `APPLY_FLAG` varchar(1) NOT NULL DEFAULT 'A',
  `APPLY_TEXT` varchar(4000) DEFAULT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  KEY `IDX_1` (`PDCA_OID`,`APPLY_FLAG`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_pdca_close_req`
--

LOCK TABLES `hf_pdca_close_req` WRITE;
/*!40000 ALTER TABLE `hf_pdca_close_req` DISABLE KEYS */;
/*!40000 ALTER TABLE `hf_pdca_close_req` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_pdca_item`
--

DROP TABLE IF EXISTS `hf_pdca_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_pdca_item` (
  `OID` char(36) NOT NULL,
  `PDCA_OID` char(36) NOT NULL,
  `TYPE` varchar(1) NOT NULL,
  `PARENT_OID` char(36) NOT NULL DEFAULT '',
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(4000) DEFAULT NULL,
  `START_DATE` varchar(8) NOT NULL,
  `END_DATE` varchar(8) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`PDCA_OID`,`NAME`),
  KEY `IDX_1` (`TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_pdca_item`
--

LOCK TABLES `hf_pdca_item` WRITE;
/*!40000 ALTER TABLE `hf_pdca_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `hf_pdca_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_pdca_item_owner`
--

DROP TABLE IF EXISTS `hf_pdca_item_owner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_pdca_item_owner` (
  `OID` char(36) NOT NULL,
  `PDCA_OID` char(36) NOT NULL,
  `ITEM_OID` char(36) NOT NULL,
  `OWNER_UID` varchar(24) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`PDCA_OID`,`ITEM_OID`,`OWNER_UID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_pdca_item_owner`
--

LOCK TABLES `hf_pdca_item_owner` WRITE;
/*!40000 ALTER TABLE `hf_pdca_item_owner` DISABLE KEYS */;
/*!40000 ALTER TABLE `hf_pdca_item_owner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_pdca_owner`
--

DROP TABLE IF EXISTS `hf_pdca_owner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_pdca_owner` (
  `OID` char(36) NOT NULL,
  `PDCA_OID` char(36) NOT NULL,
  `OWNER_UID` varchar(24) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`PDCA_OID`,`OWNER_UID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_pdca_owner`
--

LOCK TABLES `hf_pdca_owner` WRITE;
/*!40000 ALTER TABLE `hf_pdca_owner` DISABLE KEYS */;
/*!40000 ALTER TABLE `hf_pdca_owner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_perspective`
--

DROP TABLE IF EXISTS `hf_perspective`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_perspective` (
  `OID` char(36) NOT NULL,
  `SC_OID` char(36) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `WEIGHT` decimal(5,2) NOT NULL DEFAULT 0.00,
  `TAB_NUM` varchar(5) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`SC_OID`,`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_perspective`
--

LOCK TABLES `hf_perspective` WRITE;
/*!40000 ALTER TABLE `hf_perspective` DISABLE KEYS */;
/*!40000 ALTER TABLE `hf_perspective` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_sc_color`
--

DROP TABLE IF EXISTS `hf_sc_color`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_sc_color` (
  `OID` char(36) NOT NULL,
  `SC_OID` char(36) NOT NULL,
  `TYPE` varchar(1) NOT NULL DEFAULT 'C',
  `RANGE1` int(9) NOT NULL DEFAULT 0,
  `RANGE2` int(9) NOT NULL DEFAULT 0,
  `FONT_COLOR` varchar(7) NOT NULL,
  `BG_COLOR` varchar(7) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  KEY `IDX_1` (`SC_OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_sc_color`
--

LOCK TABLES `hf_sc_color` WRITE;
/*!40000 ALTER TABLE `hf_sc_color` DISABLE KEYS */;
/*!40000 ALTER TABLE `hf_sc_color` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_scorecard`
--

DROP TABLE IF EXISTS `hf_scorecard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_scorecard` (
  `OID` char(36) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `CONTENT` varchar(8000) NOT NULL,
  `MISSION` varchar(8000) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_scorecard`
--

LOCK TABLES `hf_scorecard` WRITE;
/*!40000 ALTER TABLE `hf_scorecard` DISABLE KEYS */;
/*!40000 ALTER TABLE `hf_scorecard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_so_owner_kpis`
--

DROP TABLE IF EXISTS `hf_so_owner_kpis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_so_owner_kpis` (
  `OID` char(36) NOT NULL,
  `SO_OID` char(36) NOT NULL,
  `KPI_OID` char(36) NOT NULL,
  `CARD_WEIGHT` decimal(5,2) NOT NULL DEFAULT 0.00,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`SO_OID`,`KPI_OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_so_owner_kpis`
--

LOCK TABLES `hf_so_owner_kpis` WRITE;
/*!40000 ALTER TABLE `hf_so_owner_kpis` DISABLE KEYS */;
/*!40000 ALTER TABLE `hf_so_owner_kpis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_so_owner_okrs`
--

DROP TABLE IF EXISTS `hf_so_owner_okrs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_so_owner_okrs` (
  `OID` char(36) NOT NULL,
  `SO_OID` char(36) NOT NULL,
  `OKR_OID` char(36) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`SO_OID`,`OKR_OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_so_owner_okrs`
--

LOCK TABLES `hf_so_owner_okrs` WRITE;
/*!40000 ALTER TABLE `hf_so_owner_okrs` DISABLE KEYS */;
/*!40000 ALTER TABLE `hf_so_owner_okrs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hf_strategy_objective`
--

DROP TABLE IF EXISTS `hf_strategy_objective`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hf_strategy_objective` (
  `OID` char(36) NOT NULL,
  `PER_OID` char(36) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `WEIGHT` decimal(5,2) NOT NULL DEFAULT 0.00,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`PER_OID`,`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hf_strategy_objective`
--

LOCK TABLES `hf_strategy_objective` WRITE;
/*!40000 ALTER TABLE `hf_strategy_objective` DISABLE KEYS */;
/*!40000 ALTER TABLE `hf_strategy_objective` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_account`
--

DROP TABLE IF EXISTS `tb_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_account` (
  `OID` char(36) NOT NULL,
  `ACCOUNT` varchar(24) NOT NULL,
  `PASSWORD` varchar(255) NOT NULL,
  `ON_JOB` varchar(50) NOT NULL DEFAULT 'Y',
  `CUSERID` varchar(24) DEFAULT NULL,
  `CDATE` datetime DEFAULT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`ACCOUNT`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_account`
--

LOCK TABLES `tb_account` WRITE;
/*!40000 ALTER TABLE `tb_account` DISABLE KEYS */;
INSERT INTO `tb_account` VALUES ('0','admin','$2y$12$Q4x02Q0WKHWXAQ.NoGCs8ObX4sac890xeRnaNUxNnz/VEiHWazIp.','Y','admin','2012-11-11 10:56:23','admin','2014-04-19 11:32:04'),('35e519e6-0519-11eb-b8a3-c3b14d895892','bill','$2a$10$hoLMhZm3flf.0NT7uRpJXOLgt.etn3qQGoMiGyJ/FK.y9IfMGRdWm','Y','admin','2020-10-03 01:39:00',NULL,NULL),('4e55828a-0519-11eb-b8a3-95bcbb641f0b','frank','$2a$10$76MsPuFnP78HKk87YSw3deOUioOUFjfN1xy2GIcKJcImNNq6gyOEi','Y','admin','2020-10-03 01:39:41',NULL,NULL),('6404813e-0519-11eb-b8a3-795385a5d550','cindy','$2a$10$FQzsBlP8rAcyB8J2BbtygOHlmLf2VTKDp11do3uiwe4FrWSXzz85O','Y','admin','2020-10-03 01:40:17',NULL,NULL);
/*!40000 ALTER TABLE `tb_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_role`
--

DROP TABLE IF EXISTS `tb_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_role` (
  `OID` char(36) NOT NULL,
  `ROLE` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(50) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`ROLE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role`
--

LOCK TABLES `tb_role` WRITE;
/*!40000 ALTER TABLE `tb_role` DISABLE KEYS */;
INSERT INTO `tb_role` VALUES ('4b1796ad-0bb7-4a65-b45e-439540ba5dbd','admin','administrator role!','admin','2014-10-09 15:02:24',NULL,NULL),('58914623-46ea-4797-bbec-2dadc5d0800e','COMMON01','Common role!','admin','2017-05-09 13:31:42',NULL,NULL),('c7c69396-e5e6-48ca-b09c-9445b69e2ad5','*','all role','admin','2014-10-09 15:02:54',NULL,NULL);
/*!40000 ALTER TABLE `tb_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_role_permission`
--

DROP TABLE IF EXISTS `tb_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_role_permission` (
  `OID` char(36) NOT NULL,
  `ROLE` varchar(50) NOT NULL,
  `PERMISSION` varchar(255) NOT NULL,
  `PERM_TYPE` varchar(15) NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(50) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`ROLE`,`PERMISSION`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role_permission`
--

LOCK TABLES `tb_role_permission` WRITE;
/*!40000 ALTER TABLE `tb_role_permission` DISABLE KEYS */;
INSERT INTO `tb_role_permission` VALUES ('0036ffac-99a5-11eb-8f3e-5b7732394c1a','COMMON01','systemProgramLogicServiceImpl:SELECT','COMPOMENT','','admin','2021-04-10 10:32:31',NULL,NULL),('03d2b206-99a7-11eb-8f3e-4f1bc76659a3','COMMON01','HF_PROG003D0001Q','CONTROLLER','','admin','2021-04-10 10:46:56',NULL,NULL),('04328d62-99a4-11eb-8f3e-293a867add34','COMMON01','organizationLogicServiceImpl:SELECT','COMPOMENT','','admin','2021-04-10 10:25:28',NULL,NULL),('05969f0c-99a6-11eb-8f3e-af880ef98752','COMMON01','CORE_PROG002D0001Q','CONTROLLER','','admin','2021-04-10 10:39:50',NULL,NULL),('05ab2d8f-99a5-11eb-8f3e-fb07003207cf','COMMON01','systemProgramLogicServiceImpl:INSERT','COMPOMENT','','admin','2021-04-10 10:32:40',NULL,NULL),('08e65175-99a4-11eb-8f3e-57d5a3a9d689','COMMON01','organizationLogicServiceImpl:INSERT','COMPOMENT','','admin','2021-04-10 10:25:36',NULL,NULL),('08f42c09-99a7-11eb-8f3e-7babf31ced13','COMMON01','HF_PROG003D0001Q01D','CONTROLLER','','admin','2021-04-10 10:47:05',NULL,NULL),('0a932bef-99a6-11eb-8f3e-c138c641f210','COMMON01','CORE_PROG002D0001S01Q','CONTROLLER','','admin','2021-04-10 10:39:58',NULL,NULL),('0bcb2bd2-99a5-11eb-8f3e-9910ce7e0efd','COMMON01','systemProgramLogicServiceImpl:UPDATE','COMPOMENT','','admin','2021-04-10 10:32:51',NULL,NULL),('0d1bf002-c2a1-11eb-a8b6-6f827972fe41','COMMON01','HF_PROG001D0007Q','CONTROLLER','','admin','2021-06-01 14:17:33',NULL,NULL),('0f1fa418-99a4-11eb-8f3e-c9756adf4ad1','COMMON01','organizationLogicServiceImpl:UPDATE','COMPOMENT','','admin','2021-04-10 10:25:47',NULL,NULL),('0f44a612-99a6-11eb-8f3e-d5691621f75d','COMMON01','CORE_PROG002D0001S02Q','CONTROLLER','','admin','2021-04-10 10:40:06',NULL,NULL),('11435155-99a5-11eb-8f3e-1feb979159b5','COMMON01','systemProgramLogicServiceImpl:DELETE','COMPOMENT','','admin','2021-04-10 10:33:00',NULL,NULL),('13ed6da5-99a6-11eb-8f3e-8f9e9d0b77a7','COMMON01','CORE_PROG002D0002Q','CONTROLLER','','admin','2021-04-10 10:40:14',NULL,NULL),('142afe0b-99a4-11eb-8f3e-dfdc2770c1fa','COMMON01','organizationLogicServiceImpl:DELETE','COMPOMENT','','admin','2021-04-10 10:25:55',NULL,NULL),('18849e18-99a5-11eb-8f3e-2b722d8d2806','COMMON01','systemTemplateLogicServiceImpl:SELECT','COMPOMENT','','admin','2021-04-10 10:33:12',NULL,NULL),('19e0dda8-99a6-11eb-8f3e-2b86367824b6','COMMON01','CORE_PROG002D0003Q','CONTROLLER','','admin','2021-04-10 10:40:24',NULL,NULL),('1d6f299b-99a5-11eb-8f3e-05f219d5d7ac','COMMON01','systemTemplateLogicServiceImpl:INSERT','COMPOMENT','','admin','2021-04-10 10:33:20',NULL,NULL),('1e98847e-99a4-11eb-8f3e-e7a7a9c3b7f3','COMMON01','applicationSystemLogicServiceImpl:SELECT','COMPOMENT','','admin','2021-04-10 10:26:13',NULL,NULL),('1ff2f93b-99a6-11eb-8f3e-477ad6edc1ca','COMMON01','CORE_PROG003D','CONTROLLER','','admin','2021-04-10 10:40:34',NULL,NULL),('22809e0e-99a5-11eb-8f3e-2f7ef2b7a284','COMMON01','systemTemplateLogicServiceImpl:UPDATE','COMPOMENT','','admin','2021-04-10 10:33:29',NULL,NULL),('23b7f07e-99a6-11eb-8f3e-79b56684dbef','COMMON01','CORE_PROG003D0002A','CONTROLLER','','admin','2021-04-10 10:40:40',NULL,NULL),('243262d1-99a4-11eb-8f3e-f7234045671d','COMMON01','applicationSystemLogicServiceImpl:INSERT','COMPOMENT','','admin','2021-04-10 10:26:22',NULL,NULL),('27c49431-99a5-11eb-8f3e-f3619c677525','COMMON01','systemTemplateLogicServiceImpl:DELETE','COMPOMENT','','admin','2021-04-10 10:33:38',NULL,NULL),('284eb6b1-99a6-11eb-8f3e-6d2e10785a98','COMMON01','CORE_PROG003D0002E','CONTROLLER','','admin','2021-04-10 10:40:48',NULL,NULL),('29754784-99a4-11eb-8f3e-4906dee0eefc','COMMON01','applicationSystemLogicServiceImpl:UPDATE','COMPOMENT','','admin','2021-04-10 10:26:31',NULL,NULL),('2c7221a4-99a6-11eb-8f3e-954078a96d46','COMMON01','CORE_PROG003D0002Q','CONTROLLER','','admin','2021-04-10 10:40:55',NULL,NULL),('2e7297b7-99a4-11eb-8f3e-55151be50f17','COMMON01','applicationSystemLogicServiceImpl:DELETE','COMPOMENT','','admin','2021-04-10 10:26:39',NULL,NULL),('346b9ad7-99a6-11eb-8f3e-898e04b3ca99','COMMON01','CORE_PROG003D0003A','CONTROLLER','','admin','2021-04-10 10:41:08',NULL,NULL),('3848d50a-99a6-11eb-8f3e-95dc6b0dd16b','COMMON01','CORE_PROG003D0003E','CONTROLLER','','admin','2021-04-10 10:41:15',NULL,NULL),('3ce42f1d-99a6-11eb-8f3e-ad8fdb2146a3','COMMON01','CORE_PROG003D0003Q','CONTROLLER','','admin','2021-04-10 10:41:23',NULL,NULL),('45f42160-99a6-11eb-8f3e-bd8720958667','COMMON01','CORE_PROG003D0003S01Q','CONTROLLER','','admin','2021-04-10 10:41:38',NULL,NULL),('489253f3-c29f-11eb-9631-ef28657a7f50','COMMON01','pdcaLogicServiceImpl:SELECT','COMPOMENT','','admin','2021-06-01 14:04:53',NULL,NULL),('4ad5ac33-99a6-11eb-8f3e-39f68240cf59','COMMON01','CORE_PROG003D0003S02Q','CONTROLLER','','admin','2021-04-10 10:41:46',NULL,NULL),('4e952536-99a6-11eb-8f3e-c7c848f01ea8','COMMON01','CORE_PROG003D0006A','CONTROLLER','','admin','2021-04-10 10:41:52',NULL,NULL),('4f3ba676-c29f-11eb-9631-4b71141a9620','COMMON01','pdcaLogicServiceImpl:UPDATE','COMPOMENT','','admin','2021-06-01 14:05:04',NULL,NULL),('52e370b9-99a6-11eb-8f3e-2b56ac33adbb','COMMON01','CORE_PROG003D0006E','CONTROLLER','','admin','2021-04-10 10:41:59',NULL,NULL),('539db13c-cc52-11eb-a270-91795bb88f82','COMMON01','HF_PROG005D0001Q','CONTROLLER','','admin','2021-06-13 22:19:12',NULL,NULL),('53ccc674-99a3-11eb-8f3e-97df252f69f8','COMMON01','aggregationMethodLogicServiceImpl:SELECT','COMPOMENT','','admin','2021-04-10 10:20:32',NULL,NULL),('561f1bc9-c29f-11eb-9631-ed36c4363f83','COMMON01','pdcaLogicServiceImpl:DELETE','COMPOMENT','','admin','2021-06-01 14:05:16',NULL,NULL),('57a0d1bc-99a6-11eb-8f3e-17c844b1ee3a','COMMON01','CORE_PROG003D0006Q','CONTROLLER','','admin','2021-04-10 10:42:07',NULL,NULL),('59662f97-99a3-11eb-8f3e-1f0fb230ba30','COMMON01','aggregationMethodLogicServiceImpl:INSERT','COMPOMENT','','admin','2021-04-10 10:20:42',NULL,NULL),('59d5cb5f-cc52-11eb-a270-ddb7699e56a5','COMMON01','HF_PROG001D0008Q','CONTROLLER','','admin','2021-06-13 22:19:23',NULL,NULL),('5b94340c-c29f-11eb-9631-bf33d8dfbd9e','COMMON01','pdcaLogicServiceImpl:INSERT','COMPOMENT','','admin','2021-06-01 14:05:25',NULL,NULL),('5ca17d4f-99a6-11eb-8f3e-2b9a505472d8','COMMON01','CORE_PROG004D','CONTROLLER','','admin','2021-04-10 10:42:16',NULL,NULL),('5dff50d2-cc52-11eb-a270-db93023869fb','COMMON01','HF_PROG001D0008A','CONTROLLER','','admin','2021-06-13 22:19:30',NULL,NULL),('5fd0081a-99a3-11eb-8f3e-43f9daad25af','COMMON01','aggregationMethodLogicServiceImpl:UPDATE','COMPOMENT','','admin','2021-04-10 10:20:53',NULL,NULL),('60fd0f42-99a6-11eb-8f3e-a9318451966f','COMMON01','CORE_PROG004D0001Q','CONTROLLER','','admin','2021-04-10 10:42:23',NULL,NULL),('628240e5-cc52-11eb-a270-85ed27fc6e19','COMMON01','HF_PROG001D0008E','CONTROLLER','','admin','2021-06-13 22:19:37',NULL,NULL),('6519074d-99a3-11eb-8f3e-59ba47999682','COMMON01','aggregationMethodLogicServiceImpl:DELETE','COMPOMENT','','admin','2021-04-10 10:21:01',NULL,NULL),('65f8d8d5-99a6-11eb-8f3e-d1c5df8fbb26','COMMON01','CORE_PROG004D0002Q','CONTROLLER','','admin','2021-04-10 10:42:31',NULL,NULL),('672e15b8-cc52-11eb-a270-d9ad7392b789','COMMON01','HF_PROG001D0008D','CONTROLLER','','admin','2021-06-13 22:19:45',NULL,NULL),('6a116e58-99a6-11eb-8f3e-73eeb88bc657','COMMON01','CORE_PROG004D0003Q','CONTROLLER','','admin','2021-04-10 10:42:38',NULL,NULL),('6e4979e0-99a3-11eb-8f3e-191fd84c700c','COMMON01','employeeLogicServiceImpl:SELECT','COMPOMENT','','admin','2021-04-10 10:21:17',NULL,NULL),('6e8e1cdb-99a6-11eb-8f3e-274357eb12b4','COMMON01','CORE_PROG999D9999Q','CONTROLLER','','admin','2021-04-10 10:42:46',NULL,NULL),('7295112a-99a4-11eb-8f3e-11c991b2efdc','COMMON01','roleLogicServiceImpl:SELECT','COMPOMENT','','admin','2021-04-10 10:28:34',NULL,NULL),('74424113-99a3-11eb-8f3e-a363d265bac8','COMMON01','employeeLogicServiceImpl:INSERT','COMPOMENT','','admin','2021-04-10 10:21:27',NULL,NULL),('7616d2de-99a6-11eb-8f3e-c9797f58ac07','COMMON01','HF_PROG001D','CONTROLLER','','admin','2021-04-10 10:42:58',NULL,NULL),('76a84c6e-d8c5-11eb-8921-ff8210d52d0e','COMMON01','HF_PROG001D0008S','CONTROLLER','','admin','2021-06-29 18:33:37',NULL,NULL),('7825a0ad-99a4-11eb-8f3e-8101e5564bed','COMMON01','roleLogicServiceImpl:INSERT','COMPOMENT','','admin','2021-04-10 10:28:43',NULL,NULL),('79fa9cc1-99a6-11eb-8f3e-ed27978312a9','COMMON01','HF_PROG001D0001A','CONTROLLER','','admin','2021-04-10 10:43:05',NULL,NULL),('7c6896a6-99a3-11eb-8f3e-f39624a57be5','COMMON01','employeeLogicServiceImpl:UPDATE','COMPOMENT','','admin','2021-04-10 10:21:41',NULL,NULL),('7e3c6524-99a6-11eb-8f3e-1b851866ca94','COMMON01','HF_PROG001D0001E','CONTROLLER','','admin','2021-04-10 10:43:12',NULL,NULL),('7e557c70-99a4-11eb-8f3e-71aa8e8ee4be','COMMON01','roleLogicServiceImpl:UPDATE','COMPOMENT','','admin','2021-04-10 10:28:53',NULL,NULL),('8265caa9-99a3-11eb-8f3e-abb349fad292','COMMON01','employeeLogicServiceImpl:DELETE','COMPOMENT','','admin','2021-04-10 10:21:51',NULL,NULL),('8480092b-cc52-11eb-a270-e19b3818c727','COMMON01','scorecardLogicServiceImpl:INSERT','COMPOMENT','','admin','2021-06-13 22:20:34',NULL,NULL),('857f4993-99a4-11eb-8f3e-738bbf2e049c','COMMON01','roleLogicServiceImpl:DELETE','COMPOMENT','','admin','2021-04-10 10:29:05',NULL,NULL),('88453657-99a6-11eb-8f3e-2d34476b1b2f','COMMON01','HF_PROG001D0001Q','CONTROLLER','','admin','2021-04-10 10:43:29',NULL,NULL),('8973955e-cc52-11eb-a270-2f90e837e65c','COMMON01','scorecardLogicServiceImpl:UPDATE','COMPOMENT','','admin','2021-06-13 22:20:43',NULL,NULL),('8c77eaac-99a3-11eb-8f3e-31d9f27708eb','COMMON01','formulaLogicServiceImpl:SELECT','COMPOMENT','','admin','2021-04-10 10:22:08',NULL,NULL),('8d36518a-99a6-11eb-8f3e-690f677b702f','COMMON01','HF_PROG001D0002A','CONTROLLER','','admin','2021-04-10 10:43:37',NULL,NULL),('8f744bd1-cc52-11eb-a270-01876b9a133f','COMMON01','scorecardLogicServiceImpl:SELECT','COMPOMENT','','admin','2021-06-13 22:20:53',NULL,NULL),('8fa40736-99a4-11eb-8f3e-db13e96d0a45','COMMON01','systemBeanHelpLogicServiceImpl:SELECT','COMPOMENT','','admin','2021-04-10 10:29:22',NULL,NULL),('91761e1d-99a6-11eb-8f3e-09e8bf5778b3','COMMON01','HF_PROG001D0002E','CONTROLLER','','admin','2021-04-10 10:43:44',NULL,NULL),('92c7846f-99a3-11eb-8f3e-99ed8f0f0945','COMMON01','formulaLogicServiceImpl:INSERT','COMPOMENT','','admin','2021-04-10 10:22:18',NULL,NULL),('93b095f4-cc52-11eb-a270-b3e239519473','COMMON01','scorecardLogicServiceImpl:DELETE','COMPOMENT','','admin','2021-06-13 22:21:00',NULL,NULL),('94c35e59-99a4-11eb-8f3e-f104311b9068','COMMON01','systemBeanHelpLogicServiceImpl:INSERT','COMPOMENT','','admin','2021-04-10 10:29:31',NULL,NULL),('97e43c60-99a6-11eb-8f3e-4b3593d9fe24','COMMON01','HF_PROG001D0002Q','CONTROLLER','','admin','2021-04-10 10:43:55',NULL,NULL),('98795792-99a3-11eb-8f3e-f55e956f3761','COMMON01','formulaLogicServiceImpl:UPDATE','COMPOMENT','','admin','2021-04-10 10:22:28',NULL,NULL),('98ad4d9b-99a5-11eb-8f3e-7b0852bbfe3c','COMMON01','CORE_PROG001D','CONTROLLER','','admin','2021-04-10 10:36:47',NULL,NULL),('9a7fdfdc-99a4-11eb-8f3e-01186177269f','COMMON01','systemBeanHelpLogicServiceImpl:UPDATE','COMPOMENT','','admin','2021-04-10 10:29:41',NULL,NULL),('9cc55203-99a6-11eb-8f3e-61f930fa2a56','COMMON01','HF_PROG001D0003A','CONTROLLER','','admin','2021-04-10 10:44:03',NULL,NULL),('9d3371fe-99a5-11eb-8f3e-1b1d85bde516','COMMON01','CORE_PROG001D0001A','CONTROLLER','','admin','2021-04-10 10:36:55',NULL,NULL),('9ec1c565-99a3-11eb-8f3e-61c87945c13b','COMMON01','formulaLogicServiceImpl:DELETE','COMPOMENT','','admin','2021-04-10 10:22:38',NULL,NULL),('a02a5fff-99a4-11eb-8f3e-ffa12f948065','COMMON01','systemBeanHelpLogicServiceImpl:DELETE','COMPOMENT','','admin','2021-04-10 10:29:50',NULL,NULL),('a140ef16-99a6-11eb-8f3e-3d9d9ad05884','COMMON01','HF_PROG001D0003E','CONTROLLER','','admin','2021-04-10 10:44:11',NULL,NULL),('a1599c11-99a5-11eb-8f3e-8b73c5b32480','COMMON01','CORE_PROG001D0001E','CONTROLLER','','admin','2021-04-10 10:37:02',NULL,NULL),('a721e889-99a6-11eb-8f3e-f530110c589c','COMMON01','HF_PROG001D0003Q','CONTROLLER','','admin','2021-04-10 10:44:21',NULL,NULL),('a835be65-99a5-11eb-8f3e-d31ae5fcba89','COMMON01','CORE_PROG001D0001Q','CONTROLLER','','admin','2021-04-10 10:37:13',NULL,NULL),('aab83fb2-99a4-11eb-8f3e-d33a750b8ac8','COMMON01','systemExpressionLogicServiceImpl:SELECT','COMPOMENT','','admin','2021-04-10 10:30:08',NULL,NULL),('acdd4d78-99a5-11eb-8f3e-7187a300cf26','COMMON01','CORE_PROG001D0002A','CONTROLLER','','admin','2021-04-10 10:37:21',NULL,NULL),('ad0700ac-99a6-11eb-8f3e-c52839b6f1a5','COMMON01','HF_PROG001D0004A','CONTROLLER','','admin','2021-04-10 10:44:31',NULL,NULL),('ad532bfa-99a3-11eb-8f3e-c71604173545','COMMON01','kpiLogicServiceImpl:SELECT','COMPOMENT','','admin','2021-04-10 10:23:03',NULL,NULL),('af494db4-c29e-11eb-9631-0b6a0c681a06','COMMON01','HF_PROG004D','CONTROLLER','','admin','2021-06-01 14:00:36',NULL,NULL),('afa403b5-99a4-11eb-8f3e-0533c287a8f9','COMMON01','systemExpressionLogicServiceImpl:INSERT','COMPOMENT','','admin','2021-04-10 10:30:16',NULL,NULL),('b28b624d-99a3-11eb-8f3e-adddc646bb6f','COMMON01','kpiLogicServiceImpl:INSERT','COMPOMENT','','admin','2021-04-10 10:23:11',NULL,NULL),('b45e1f5f-99a6-11eb-8f3e-cf862716bc3f','COMMON01','HF_PROG001D0004E','CONTROLLER','','admin','2021-04-10 10:44:43',NULL,NULL),('b4ec3f98-99a4-11eb-8f3e-af4a3d2f7763','COMMON01','systemExpressionLogicServiceImpl:UPDATE','COMPOMENT','','admin','2021-04-10 10:30:25',NULL,NULL),('b58fb66c-99a5-11eb-8f3e-2d23b5b5b59a','COMMON01','CORE_PROG001D0002E','CONTROLLER','','admin','2021-04-10 10:37:35',NULL,NULL),('b5d30a47-c29e-11eb-9631-6fb9eb133e9c','COMMON01','HF_PROG004D0001Q','CONTROLLER','','admin','2021-06-01 14:00:47',NULL,NULL),('b830f852-99a6-11eb-8f3e-23013e279222','COMMON01','HF_PROG001D0004Q','CONTROLLER','','admin','2021-04-10 10:44:49',NULL,NULL),('b8d4ba80-99a3-11eb-8f3e-abfb82f2a1f5','COMMON01','kpiLogicServiceImpl:UPDATE','COMPOMENT','','admin','2021-04-10 10:23:22',NULL,NULL),('ba06717f-99a5-11eb-8f3e-8daa260c2355','COMMON01','CORE_PROG001D0002Q','CONTROLLER','','admin','2021-04-10 10:37:43',NULL,NULL),('bb62132a-c29e-11eb-9631-bf2c1a04d98d','COMMON01','HF_PROG004D0001A','CONTROLLER','','admin','2021-06-01 14:00:56',NULL,NULL),('bc187dbb-99a4-11eb-8f3e-29ee394b9f5b','COMMON01','systemExpressionLogicServiceImpl:DELETE','COMPOMENT','','admin','2021-04-10 10:30:37',NULL,NULL),('bd83bb85-99a6-11eb-8f3e-f13e9487d724','COMMON01','HF_PROG001D0005A','CONTROLLER','','admin','2021-04-10 10:44:58',NULL,NULL),('be7febb2-99a5-11eb-8f3e-b506797a1716','COMMON01','CORE_PROG001D0003Q','CONTROLLER','','admin','2021-04-10 10:37:50',NULL,NULL),('be86b4b3-99a3-11eb-8f3e-633c3fa15496','COMMON01','kpiLogicServiceImpl:DELETE','COMPOMENT','','admin','2021-04-10 10:23:32',NULL,NULL),('c1e7ff2d-c29e-11eb-9631-f933f9166b1b','COMMON01','HF_PROG004D0001E','CONTROLLER','','admin','2021-06-01 14:01:07',NULL,NULL),('c2209c38-99a6-11eb-8f3e-6b72cc42c61d','COMMON01','HF_PROG001D0005E','CONTROLLER','','admin','2021-04-10 10:45:06',NULL,NULL),('c277f5fe-99a4-11eb-8f3e-dd717854b8c2','COMMON01','systemJreportLogicServiceImpl:SELECT','COMPOMENT','','admin','2021-04-10 10:30:48',NULL,NULL),('c30a07b5-99a5-11eb-8f3e-f17c5d600c48','COMMON01','CORE_PROG001D0004A','CONTROLLER','','admin','2021-04-10 10:37:58',NULL,NULL),('c70f3161-99a4-11eb-8f3e-69374d741b90','COMMON01','systemJreportLogicServiceImpl:INSERT','COMPOMENT','','admin','2021-04-10 10:30:55',NULL,NULL),('c7f69850-c29e-11eb-9631-859d29959966','COMMON01','HF_PROG004D0001V','CONTROLLER','','admin','2021-06-01 14:01:18',NULL,NULL),('c842b338-99a5-11eb-8f3e-61b4a26febb8','COMMON01','CORE_PROG001D0004E','CONTROLLER','','admin','2021-04-10 10:38:07',NULL,NULL),('cc0889f4-99a4-11eb-8f3e-37c3ae235f0c','COMMON01','systemJreportLogicServiceImpl:UPDATE','COMPOMENT','','admin','2021-04-10 10:31:04',NULL,NULL),('cd08d908-99a3-11eb-8f3e-575a5eb77a9c','COMMON01','measureDataLogicServiceImpl:SELECT','COMPOMENT','','admin','2021-04-10 10:23:56',NULL,NULL),('cd107133-c29e-11eb-9631-931723e8d99c','COMMON01','HF_PROG004D0001D','CONTROLLER','','admin','2021-06-01 14:01:26',NULL,NULL),('cd7432cb-99a5-11eb-8f3e-7d7e4de1aaf5','COMMON01','CORE_PROG001D0004Q','CONTROLLER','','admin','2021-04-10 10:38:16',NULL,NULL),('d0f75b37-99a4-11eb-8f3e-25a092d321c2','COMMON01','systemJreportLogicServiceImpl:DELETE','COMPOMENT','','admin','2021-04-10 10:31:12',NULL,NULL),('d2ad65bb-99a3-11eb-8f3e-c19383cd6a9e','COMMON01','measureDataLogicServiceImpl:INSERT','COMPOMENT','','admin','2021-04-10 10:24:05',NULL,NULL),('d4762c9e-99a5-11eb-8f3e-232e0666be06','COMMON01','CORE_PROG001D0004S01Q','CONTROLLER','','admin','2021-04-10 10:38:27',NULL,NULL),('d693035b-cc54-11eb-93d3-77b2b88a5de8','COMMON01','HF_PROGCOMM0001Q','CONTROLLER','','admin','2021-06-13 22:37:11',NULL,NULL),('d8320e5e-99a3-11eb-8f3e-dd5a5a994661','COMMON01','measureDataLogicServiceImpl:UPDATE','COMPOMENT','','admin','2021-04-10 10:24:15',NULL,NULL),('d8d58f21-99a5-11eb-8f3e-51952dec2e4d','COMMON01','CORE_PROG001D0005A','CONTROLLER','','admin','2021-04-10 10:38:35',NULL,NULL),('d8f1499a-99a4-11eb-8f3e-01ac0120b3a5','COMMON01','systemMenuLogicServiceImpl:SELECT','COMPOMENT','','admin','2021-04-10 10:31:25',NULL,NULL),('dca1bc6b-99a6-11eb-8f3e-7d61a48a3600','COMMON01','HF_PROG001D0005M','CONTROLLER','','admin','2021-04-10 10:45:51',NULL,NULL),('dd578001-99a3-11eb-8f3e-9faba06c03f1','COMMON01','measureDataLogicServiceImpl:DELETE','COMPOMENT','','admin','2021-04-10 10:24:23',NULL,NULL),('dde13734-99a5-11eb-8f3e-6d0c06eaed31','COMMON01','CORE_PROG001D0005E','CONTROLLER','','admin','2021-04-10 10:38:43',NULL,NULL),('dfc46b3d-99a4-11eb-8f3e-4d1717bde7ec','COMMON01','systemMenuLogicServiceImpl:INSERT','COMPOMENT','','admin','2021-04-10 10:31:37',NULL,NULL),('e0cc052e-99a6-11eb-8f3e-352208b2e143','COMMON01','HF_PROG001D0005Q','CONTROLLER','','admin','2021-04-10 10:45:58',NULL,NULL),('e28f55f7-99a5-11eb-8f3e-d99b79fd0fd4','COMMON01','CORE_PROG001D0005Q','CONTROLLER','','admin','2021-04-10 10:38:51',NULL,NULL),('e6536cf1-99a6-11eb-8f3e-9d3bc646397e','COMMON01','HF_PROG001D0006A','CONTROLLER','','admin','2021-04-10 10:46:07',NULL,NULL),('e77a2f9a-99a5-11eb-8f3e-6177534d3231','COMMON01','CORE_PROG001D0005S01Q','CONTROLLER','','admin','2021-04-10 10:38:59',NULL,NULL),('e9a939b2-99a4-11eb-8f3e-3b837b299516','COMMON01','systemMenuLogicServiceImpl:UPDATE','COMPOMENT','','admin','2021-04-10 10:31:53',NULL,NULL),('ea85ff55-9b93-11eb-a168-55faea620980','COMMON01','CORE_PROGCOMM0001Q','CONTROLLER','','admin','2021-04-12 21:35:16',NULL,NULL),('ea8f68f4-99a6-11eb-8f3e-2b0ebde44e2a','COMMON01','HF_PROG001D0006E','CONTROLLER','','admin','2021-04-10 10:46:14',NULL,NULL),('eaa40a86-99a3-11eb-8f3e-23e31d1ff0be','COMMON01','okrBaseLogicServiceImpl:SELECT','COMPOMENT','','admin','2021-04-10 10:24:46',NULL,NULL),('ebf4942d-99a5-11eb-8f3e-9d2faa336ef3','COMMON01','CORE_PROG001D0005S02Q','CONTROLLER','','admin','2021-04-10 10:39:07',NULL,NULL),('ee498ac7-99a6-11eb-8f3e-27e0bc2ad3a1','COMMON01','HF_PROG001D0006M','CONTROLLER','','admin','2021-04-10 10:46:20',NULL,NULL),('ee60c888-9b93-11eb-a168-bf7247f2a6ac','COMMON01','CORE_PROGCOMM0002Q','CONTROLLER','','admin','2021-04-12 21:35:22',NULL,NULL),('ee798675-99a4-11eb-8f3e-2d4f51a99e6c','COMMON01','systemMenuLogicServiceImpl:DELETE','COMPOMENT','','admin','2021-04-10 10:32:01',NULL,NULL),('f06c24c9-99a3-11eb-8f3e-e1c39cd2fdbb','COMMON01','okrBaseLogicServiceImpl:INSERT','COMPOMENT','','admin','2021-04-10 10:24:55',NULL,NULL),('f2da0f6a-99a6-11eb-8f3e-1179a54c5746','COMMON01','HF_PROG001D0006Q','CONTROLLER','','admin','2021-04-10 10:46:28',NULL,NULL),('f30e0da0-99a5-11eb-8f3e-2d1eab48aef3','COMMON01','CORE_PROG001D0007Q','CONTROLLER','','admin','2021-04-10 10:39:19',NULL,NULL),('f4c7badc-9b93-11eb-a168-8946ce7dffba','COMMON01','CORE_PROGCOMM0004Q','CONTROLLER','','admin','2021-04-12 21:35:33',NULL,NULL),('f73f0d23-99a5-11eb-8f3e-57478a02a01e','COMMON01','CORE_PROG002D','CONTROLLER','','admin','2021-04-10 10:39:26',NULL,NULL),('f7411b2c-99a3-11eb-8f3e-79343bdc608b','COMMON01','okrBaseLogicServiceImpl:UPDATE','COMPOMENT','','admin','2021-04-10 10:25:07',NULL,NULL),('f7430edd-99a6-11eb-8f3e-4b387c022bd2','COMMON01','HF_PROG002D','CONTROLLER','','admin','2021-04-10 10:46:35',NULL,NULL),('f853047f-9b93-11eb-a168-2dde7b0ba705','COMMON01','CORE_PROGCOMM0003Q','CONTROLLER','','admin','2021-04-12 21:35:39',NULL,NULL),('fb550a96-99a5-11eb-8f3e-cd273544d6d6','COMMON01','CORE_PROG002D0001A','CONTROLLER','','admin','2021-04-10 10:39:33',NULL,NULL),('fb6f5370-99a6-11eb-8f3e-33dd3afe3f63','COMMON01','HF_PROG002D0001Q','CONTROLLER','','admin','2021-04-10 10:46:42',NULL,NULL),('fbeec4bf-99a3-11eb-8f3e-a3ed374f4528','COMMON01','okrBaseLogicServiceImpl:DELETE','COMPOMENT','','admin','2021-04-10 10:25:15',NULL,NULL),('ff4f4cc3-99a6-11eb-8f3e-3fc32344b648','COMMON01','HF_PROG003D','CONTROLLER','','admin','2021-04-10 10:46:49',NULL,NULL),('ffedf3a9-99a5-11eb-8f3e-8f0e1c47dac0','COMMON01','CORE_PROG002D0001E','CONTROLLER','','admin','2021-04-10 10:39:40',NULL,NULL);
/*!40000 ALTER TABLE `tb_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys`
--

DROP TABLE IF EXISTS `tb_sys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys` (
  `OID` char(36) NOT NULL,
  `SYS_ID` varchar(10) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `HOST` varchar(200) NOT NULL,
  `CONTEXT_PATH` varchar(100) NOT NULL,
  `IS_LOCAL` varchar(1) NOT NULL DEFAULT 'Y',
  `ICON` varchar(20) NOT NULL DEFAULT ' ',
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`SYS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys`
--

LOCK TABLES `tb_sys` WRITE;
/*!40000 ALTER TABLE `tb_sys` DISABLE KEYS */;
INSERT INTO `tb_sys` VALUES ('c6643182-85a5-4f91-9e73-10567ebd0dd5','CORE','Core-system','127.0.0.1:8080','core-web','Y','SYSTEM','admin','2017-04-10 20:42:00','admin','2017-04-10 20:42:00');
/*!40000 ALTER TABLE `tb_sys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_bean_help`
--

DROP TABLE IF EXISTS `tb_sys_bean_help`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_bean_help` (
  `OID` char(36) NOT NULL,
  `BEAN_ID` varchar(255) NOT NULL,
  `METHOD` varchar(100) NOT NULL,
  `SYSTEM` varchar(10) NOT NULL,
  `ENABLE_FLAG` varchar(1) NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`BEAN_ID`,`METHOD`,`SYSTEM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_bean_help`
--

LOCK TABLES `tb_sys_bean_help` WRITE;
/*!40000 ALTER TABLE `tb_sys_bean_help` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_bean_help` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_bean_help_expr`
--

DROP TABLE IF EXISTS `tb_sys_bean_help_expr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_bean_help_expr` (
  `OID` char(36) NOT NULL,
  `HELP_OID` char(36) NOT NULL,
  `EXPR_ID` varchar(20) NOT NULL,
  `EXPR_SEQ` varchar(10) NOT NULL,
  `RUN_TYPE` varchar(10) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`EXPR_ID`,`HELP_OID`,`RUN_TYPE`),
  KEY `IDX_1` (`HELP_OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_bean_help_expr`
--

LOCK TABLES `tb_sys_bean_help_expr` WRITE;
/*!40000 ALTER TABLE `tb_sys_bean_help_expr` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_bean_help_expr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_bean_help_expr_map`
--

DROP TABLE IF EXISTS `tb_sys_bean_help_expr_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_bean_help_expr_map` (
  `OID` char(36) NOT NULL,
  `HELP_EXPR_OID` char(36) NOT NULL,
  `METHOD_RESULT_FLAG` varchar(1) NOT NULL DEFAULT 'N',
  `METHOD_PARAM_CLASS` varchar(255) NOT NULL DEFAULT ' ',
  `METHOD_PARAM_INDEX` int(3) NOT NULL DEFAULT 0,
  `VAR_NAME` varchar(255) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`VAR_NAME`,`HELP_EXPR_OID`),
  KEY `IDX_1` (`HELP_EXPR_OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_bean_help_expr_map`
--

LOCK TABLES `tb_sys_bean_help_expr_map` WRITE;
/*!40000 ALTER TABLE `tb_sys_bean_help_expr_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_bean_help_expr_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_code`
--

DROP TABLE IF EXISTS `tb_sys_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_code` (
  `OID` char(36) NOT NULL,
  `CODE` varchar(25) NOT NULL,
  `TYPE` varchar(10) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `PARAM1` varchar(100) DEFAULT NULL,
  `PARAM2` varchar(100) DEFAULT NULL,
  `PARAM3` varchar(100) DEFAULT NULL,
  `PARAM4` varchar(100) DEFAULT NULL,
  `PARAM5` varchar(100) DEFAULT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_code`
--

LOCK TABLES `tb_sys_code` WRITE;
/*!40000 ALTER TABLE `tb_sys_code` DISABLE KEYS */;
INSERT INTO `tb_sys_code` VALUES ('2d9c84e4-a956-42ac-96cb-1f6292d182a9','CNF_CONF002','CNF','enable mail sender!','Y',NULL,NULL,NULL,NULL,'admin','2014-12-25 09:09:57','admin','2020-09-14 04:36:34'),('4df770a6-6a9c-4d25-bdcd-1dee819d2ba6','CNF_CONF001','CNF','default mail from account!','root@localhost',NULL,NULL,NULL,NULL,'admin','2014-12-24 21:51:16','admin','2020-09-14 04:36:34'),('a5f7ee37-f33f-48a6-b448-92ccb8cdf96a','CNF_CONF003','CNF','first load javascript','addTab(\'CORE_PROG999D9999Q\', null); addTab(\'HF_PROG001D0007Q\', null);',NULL,NULL,NULL,NULL,'admin','2014-12-25 09:09:57',NULL,NULL),('caf00ba5-fe63-4dc4-a1a3-32527f6629b2','CMM_CONF001','CMM','Common role for default user!','COMMON01',NULL,NULL,NULL,NULL,'admin','2017-05-09 12:29:00',NULL,NULL);
/*!40000 ALTER TABLE `tb_sys_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_event_log`
--

DROP TABLE IF EXISTS `tb_sys_event_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_event_log` (
  `OID` char(36) NOT NULL,
  `USER` varchar(24) NOT NULL,
  `SYS_ID` varchar(10) NOT NULL,
  `EXECUTE_EVENT` varchar(255) NOT NULL,
  `IS_PERMIT` varchar(1) NOT NULL DEFAULT 'N',
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  KEY `IDX_1` (`USER`),
  KEY `IDX_2` (`CDATE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_event_log`
--

LOCK TABLES `tb_sys_event_log` WRITE;
/*!40000 ALTER TABLE `tb_sys_event_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_event_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_expr_job`
--

DROP TABLE IF EXISTS `tb_sys_expr_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_expr_job` (
  `OID` char(36) NOT NULL,
  `SYSTEM` varchar(10) NOT NULL,
  `ID` varchar(20) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `ACTIVE` varchar(1) NOT NULL DEFAULT 'Y',
  `DESCRIPTION` varchar(500) DEFAULT NULL,
  `RUN_STATUS` varchar(1) NOT NULL DEFAULT 'Y',
  `CHECK_FAULT` varchar(1) NOT NULL DEFAULT 'N',
  `EXPR_ID` varchar(20) NOT NULL,
  `RUN_DAY_OF_WEEK` varchar(1) NOT NULL,
  `RUN_HOUR` varchar(2) NOT NULL,
  `RUN_MINUTE` varchar(2) NOT NULL,
  `CONTACT_MODE` varchar(1) NOT NULL DEFAULT '0',
  `CONTACT` varchar(500) DEFAULT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`ID`),
  KEY `IDX_1` (`SYSTEM`,`ACTIVE`,`EXPR_ID`,`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_expr_job`
--

LOCK TABLES `tb_sys_expr_job` WRITE;
/*!40000 ALTER TABLE `tb_sys_expr_job` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_expr_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_expr_job_log`
--

DROP TABLE IF EXISTS `tb_sys_expr_job_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_expr_job_log` (
  `OID` char(36) NOT NULL,
  `ID` varchar(20) NOT NULL,
  `LOG_STATUS` varchar(1) NOT NULL DEFAULT 'N',
  `BEGIN_DATETIME` datetime NOT NULL,
  `END_DATETIME` datetime NOT NULL,
  `FAULT_MSG` varchar(2000) DEFAULT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  KEY `IDX_1` (`ID`,`LOG_STATUS`,`BEGIN_DATETIME`),
  KEY `IDX_2` (`CDATE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_expr_job_log`
--

LOCK TABLES `tb_sys_expr_job_log` WRITE;
/*!40000 ALTER TABLE `tb_sys_expr_job_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_expr_job_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_expression`
--

DROP TABLE IF EXISTS `tb_sys_expression`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_expression` (
  `OID` char(36) NOT NULL,
  `EXPR_ID` varchar(20) NOT NULL,
  `TYPE` varchar(10) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `CONTENT` varchar(8000) NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`EXPR_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_expression`
--

LOCK TABLES `tb_sys_expression` WRITE;
/*!40000 ALTER TABLE `tb_sys_expression` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_expression` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_icon`
--

DROP TABLE IF EXISTS `tb_sys_icon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_icon` (
  `OID` char(36) NOT NULL,
  `ICON_ID` varchar(20) NOT NULL,
  `FILE_NAME` varchar(200) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`ICON_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_icon`
--

LOCK TABLES `tb_sys_icon` WRITE;
/*!40000 ALTER TABLE `tb_sys_icon` DISABLE KEYS */;
INSERT INTO `tb_sys_icon` VALUES ('00a11813-0fbd-481f-bab8-11bdf0df1a7e','HELP_ABOUT','help-about.png','admin','2014-09-29 00:00:00',NULL,NULL),('01a5ebc8-b79a-4960-8dac-c6543c7fa56b','REFRESH','view-refresh.png','admin','2014-09-29 00:00:00',NULL,NULL),('035d1d3b-7223-448b-a7e8-660d47c5c1a2','STOCK_HOME','stock_home.png','admin','2014-09-29 00:00:00',NULL,NULL),('140a343b-f276-49c4-9bce-69b6c1b81736','GO_LEFT','stock_left.png','admin','2014-09-29 00:00:00',NULL,NULL),('3ee47be9-dc2d-4c7f-842b-e8f807b96e4b','PDCA','pdca.png','admin','2016-04-07 11:53:39',NULL,NULL),('3f70f89b-6ef9-4112-916f-e9994519557c','SIGNATURE','text_signature.png','admin','2015-02-14 10:17:50',NULL,NULL),('4471df15-4346-4c58-9f19-572ceeca4d9d','IMPORTANT','important.png','admin','2014-09-29 00:00:00',NULL,NULL),('45cd5523-c08e-45bd-9461-67064b41052a','GWENVIEW','gwenview.png','admin','2014-09-29 00:00:00',NULL,NULL),('4b84ee11-4e89-45b0-aca2-04db58ccdfd2','DIAGRAM','x-dia-diagram.png','admin','2014-10-22 12:15:02',NULL,NULL),('4c664a5c-69cd-4e4e-8d55-050423d4e0f1','PROPERTIES','document-properties.png','admin','2014-09-29 00:00:00',NULL,NULL),('4f875ea6-6d30-4e72-ba91-56b07890325d','CHART_AREA','area_chart.png','admin','2015-01-17 12:05:49',NULL,NULL),('547c4780-4c26-4552-b5dd-f41ed3fbf6f1','REMOVE','list-remove.png','admin','2014-09-29 00:00:00',NULL,NULL),('5a5279f9-f2ab-471a-a83d-8e5e8019de90','LOCK','lock.png','admin','2014-09-29 00:00:00',NULL,NULL),('5df6aac2-03b9-45e5-b4f0-b6b549813e7d','GIMP','gimp.png','admin','2014-12-02 10:11:46',NULL,NULL),('65b10fb0-9140-4490-9d15-94148be067a8','FOLDER','folder_home.png','admin','2014-09-29 00:00:00',NULL,NULL),('7492a872-520d-48ef-bfa3-518502d48d3b','CALENDAR','xfcalendar.png','admin','2014-09-29 00:00:00',NULL,NULL),('77c3c562-b85c-4a72-b8ac-c8bc94f58df3','EXCEL','excel.png','admin','2014-09-29 00:00:00',NULL,NULL),('793989f2-4818-49d6-ab2b-44bc9ee75b43','CHART_PIE','chart-pie.png','admin','2015-01-15 22:52:29',NULL,NULL),('80d813f6-0c91-4e0b-95ef-a17bcc02e8ce','GO_FIRST','stock_first.png','admin','2014-09-29 00:00:00',NULL,NULL),('81100942-0cda-43c1-84f9-034d39ac8c58','PERSON','stock_person.png','admin','2014-09-29 00:00:00',NULL,NULL),('81959e3c-7205-4fff-8b2b-6bad5770e8c1','IMPORT','document-import.png','admin','2014-09-29 00:00:00',NULL,NULL),('92f6e3b4-e757-414f-a512-9eb53d7d7b90','INTER_ARCHIVE','internet-archive.png','admin','2014-09-29 00:00:00',NULL,NULL),('966b2e28-2168-4172-ac2d-31b429336c1c','CHART_LINE','charts-line-chart-icon.png','admin','2015-01-16 14:05:52',NULL,NULL),('9eac6e4e-3796-4e0a-b9c4-fa337d01517b','APPLICATION_PDF','application-pdf.png','admin','2014-10-30 14:26:56',NULL,NULL),('9f3b3020-b76c-4af0-ad69-7a15c4e5d022','SHARED','emblem-shared.png','admin','2014-09-29 00:00:00',NULL,NULL),('9fb99e36-6ee9-4e1c-9629-8336ede133da','PEOPLE','emblem-people.png','admin','2014-09-29 00:00:00',NULL,NULL),('a788062a-22da-4734-b63d-087f2aee32b8','FULL_SCREEN','fullscreen.png','admin','2015-08-10 21:31:30',NULL,NULL),('a9fd2f30-4960-42fd-a175-a462c6f281fb','EXPORT','document-export.png','admin','2014-09-29 00:00:00',NULL,NULL),('ad1d0d5e-111d-4020-ade4-6bcf167fed0e','WWW','www.png','admin','2014-10-22 12:14:29',NULL,NULL),('b4f3acb8-72bc-49c1-a7a5-50106181facf','SYSTEM','system-run.png','admin','2014-09-29 00:00:00',NULL,NULL),('b5d46f00-8146-4e0e-8812-6e5b56843e5b','TWITTER','twitter.png','admin','2014-12-18 19:51:48',NULL,NULL),('bbff137e-7be8-4e8f-a1d2-db41444345d3','TEXT_SOURCE','text-x-source.png','admin','2014-10-23 10:08:48',NULL,NULL),('c739a407-82d2-4e2e-be95-3df87e280bfd','COMPUTER','computer.png','admin','2014-09-29 00:00:00',NULL,NULL),('cb219bbc-db12-4765-8922-ffd76773d907','STAR','star.png','admin','2014-09-29 00:00:00',NULL,NULL),('d1727475-258b-44f6-8f4a-f36cff81fb85','GO_LAST','stock_last.png','admin','2014-09-29 00:00:00',NULL,NULL),('d1fb350e-e2f6-439a-9934-65e1b26ada3e','GO_RIGHT','stock_right.png','admin','2014-09-29 00:00:00',NULL,NULL),('d24d1ac9-bdb3-40ef-95ef-9845b47b0182','G_APP_INSTALL','gnome-app-install.png','admin','2014-09-29 00:00:00',NULL,NULL),('e5d8eec7-2063-4806-b401-5d415dbd6c25','TEMPLATE','libreoffice-template.png','admin','2014-10-21 13:40:26',NULL,NULL),('ed735416-7d0e-4df0-aa1c-7787bbc5953e','VIEW_LIST','view-list-icons.png','admin','2014-11-28 08:54:06',NULL,NULL),('f05b7819-b9ee-4409-8d2f-b9e067280acd','USERS','system-users.png','admin','2014-09-29 00:00:00',NULL,NULL),('feddb360-0b6f-4ee2-8206-b961bcb2a76d','CHART_BAR','chart-graph-2d-1.png','admin','2015-01-16 13:27:00',NULL,NULL);
/*!40000 ALTER TABLE `tb_sys_icon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_jreport`
--

DROP TABLE IF EXISTS `tb_sys_jreport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_jreport` (
  `OID` char(36) NOT NULL,
  `REPORT_ID` varchar(50) NOT NULL,
  `FILE` varchar(100) NOT NULL,
  `IS_COMPILE` varchar(50) NOT NULL DEFAULT 'N',
  `CONTENT` mediumblob NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`REPORT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_jreport`
--

LOCK TABLES `tb_sys_jreport` WRITE;
/*!40000 ALTER TABLE `tb_sys_jreport` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_jreport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_jreport_param`
--

DROP TABLE IF EXISTS `tb_sys_jreport_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_jreport_param` (
  `OID` char(36) NOT NULL,
  `REPORT_ID` varchar(50) NOT NULL,
  `URL_PARAM` varchar(100) NOT NULL,
  `RPT_PARAM` varchar(100) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`REPORT_ID`,`RPT_PARAM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_jreport_param`
--

LOCK TABLES `tb_sys_jreport_param` WRITE;
/*!40000 ALTER TABLE `tb_sys_jreport_param` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_jreport_param` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_login_log`
--

DROP TABLE IF EXISTS `tb_sys_login_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_login_log` (
  `OID` char(36) NOT NULL,
  `USER` varchar(24) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  KEY `IDX_1` (`USER`),
  KEY `IDX_2` (`CDATE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_login_log`
--

LOCK TABLES `tb_sys_login_log` WRITE;
/*!40000 ALTER TABLE `tb_sys_login_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_login_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_mail_helper`
--

DROP TABLE IF EXISTS `tb_sys_mail_helper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_mail_helper` (
  `OID` char(36) NOT NULL,
  `MAIL_ID` varchar(17) NOT NULL,
  `SUBJECT` varchar(200) NOT NULL,
  `TEXT` blob DEFAULT NULL,
  `MAIL_FROM` varchar(100) NOT NULL,
  `MAIL_TO` varchar(100) NOT NULL,
  `MAIL_CC` varchar(1000) DEFAULT NULL,
  `MAIL_BCC` varchar(1000) DEFAULT NULL,
  `SUCCESS_FLAG` varchar(1) NOT NULL DEFAULT 'N',
  `SUCCESS_TIME` datetime DEFAULT NULL,
  `RETAIN_FLAG` varchar(1) NOT NULL DEFAULT 'N',
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`MAIL_ID`),
  KEY `IDX_1` (`MAIL_ID`),
  KEY `IDX_2` (`SUCCESS_FLAG`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_mail_helper`
--

LOCK TABLES `tb_sys_mail_helper` WRITE;
/*!40000 ALTER TABLE `tb_sys_mail_helper` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_mail_helper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_menu`
--

DROP TABLE IF EXISTS `tb_sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_menu` (
  `OID` char(36) NOT NULL,
  `PROG_ID` varchar(50) NOT NULL,
  `PARENT_OID` char(36) NOT NULL,
  `ENABLE_FLAG` varchar(1) NOT NULL DEFAULT 'Y',
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`PROG_ID`,`PARENT_OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_menu`
--

LOCK TABLES `tb_sys_menu` WRITE;
/*!40000 ALTER TABLE `tb_sys_menu` DISABLE KEYS */;
INSERT INTO `tb_sys_menu` VALUES ('00e82e3c-0ea9-11eb-ba89-e94db9e8394f','HF_PROG002D','00000000-0000-0000-0000-000000000000','Y','admin','2020-10-15 05:40:59',NULL,NULL),('00eb3b7d-0ea9-11eb-ba89-b761450c2f0a','HF_PROG002D0001Q','00e82e3c-0ea9-11eb-ba89-e94db9e8394f','Y','admin','2020-10-15 05:40:59',NULL,NULL),('0fd3a30d-d000-4091-a6dd-8079b495977f','CORE_PROG003D0004Q','2eecc8a6-d234-4052-877b-bbab59839123','Y','admin','2017-06-03 08:42:14',NULL,NULL),('24720067-24c3-4527-a993-46710f3de43f','CORE_PROG001D0003Q','7ea68636-c93a-4669-ac42-dafc3770d20d','Y','admin','2017-06-05 09:55:00',NULL,NULL),('254a1a62-a47a-4aab-9c92-1f0fbafa6554','CORE_PROG003D0005Q','2eecc8a6-d234-4052-877b-bbab59839123','Y','admin','2017-06-03 08:42:14',NULL,NULL),('2b629a25-6998-4c77-ab09-4027c27208a6','CORE_PROG001D0006Q','7ea68636-c93a-4669-ac42-dafc3770d20d','Y','admin','2017-06-05 09:55:00',NULL,NULL),('2eecc8a6-d234-4052-877b-bbab59839123','CORE_PROG003D','00000000-0000-0000-0000-000000000000','Y','admin','2017-05-23 08:57:41',NULL,NULL),('392cce75-a34c-4f8d-a16f-4ed63b2d2495','CORE_PROG004D0001Q','5e055f61-bfc5-402c-93b4-f241dc17b00b','Y','admin','2017-06-03 14:23:22',NULL,NULL),('3997e92f-c5a3-11eb-a584-cd74ab7010c7','HF_PROG001D0001Q','4dff5215-03cf-4d2b-bf36-e5084828ccb9','Y','admin','2021-06-05 10:10:40',NULL,NULL),('399996e0-c5a3-11eb-a584-4b6acd0f31ab','HF_PROG001D0002Q','4dff5215-03cf-4d2b-bf36-e5084828ccb9','Y','admin','2021-06-05 10:10:40',NULL,NULL),('399aa851-c5a3-11eb-a584-c5c479ea43b9','HF_PROG001D0003Q','4dff5215-03cf-4d2b-bf36-e5084828ccb9','Y','admin','2021-06-05 10:10:40',NULL,NULL),('399c07e2-c5a3-11eb-a584-f99d5cd0314f','HF_PROG001D0004Q','4dff5215-03cf-4d2b-bf36-e5084828ccb9','Y','admin','2021-06-05 10:10:40',NULL,NULL),('399cf243-c5a3-11eb-a584-8da1ad87bfda','HF_PROG001D0005Q','4dff5215-03cf-4d2b-bf36-e5084828ccb9','Y','admin','2021-06-05 10:10:40',NULL,NULL),('399ddca4-c5a3-11eb-a584-1d490df4c24e','HF_PROG001D0006Q','4dff5215-03cf-4d2b-bf36-e5084828ccb9','Y','admin','2021-06-05 10:10:40',NULL,NULL),('399f1525-c5a3-11eb-a584-f399619c576e','HF_PROG001D0007Q','4dff5215-03cf-4d2b-bf36-e5084828ccb9','Y','admin','2021-06-05 10:10:40',NULL,NULL),('39a04da6-c5a3-11eb-a584-ef9d4f78da65','HF_PROG001D0008Q','4dff5215-03cf-4d2b-bf36-e5084828ccb9','Y','admin','2021-06-05 10:10:40',NULL,NULL),('4429a2af-c00c-4202-9642-3525029a5438','CORE_PROG001D0004Q','7ea68636-c93a-4669-ac42-dafc3770d20d','Y','admin','2017-06-05 09:55:00',NULL,NULL),('4589612b-077e-4b11-b192-01ca621391ae','CORE_PROG003D0002Q','2eecc8a6-d234-4052-877b-bbab59839123','Y','admin','2017-06-03 08:42:13',NULL,NULL),('4a8ba269-cd8d-490d-a818-2fb6b1dd31d0','CORE_PROG001D0007Q','7ea68636-c93a-4669-ac42-dafc3770d20d','Y','admin','2017-06-05 09:55:00',NULL,NULL),('4bd4d202-5feb-495b-8c8c-ec6b7f5b8041','CORE_PROG002D0002Q','79e1cf24-2522-4cdf-abcc-6455b47d545b','Y','admin','2017-05-10 14:20:12',NULL,NULL),('4dff5215-03cf-4d2b-bf36-e5084828ccb9','HF_PROG001D','00000000-0000-0000-0000-000000000000','Y','admin','2020-09-23 07:32:25',NULL,NULL),('5e055f61-bfc5-402c-93b4-f241dc17b00b','CORE_PROG004D','00000000-0000-0000-0000-000000000000','Y','admin','2017-06-03 14:23:17',NULL,NULL),('6c7ee268-14ad-408c-ac64-e6790d9f33e3','CORE_PROG004D0003Q','5e055f61-bfc5-402c-93b4-f241dc17b00b','Y','admin','2017-06-03 14:23:22',NULL,NULL),('6ea3d56f-4dd4-494d-a48b-975fd570f807','CORE_PROG003D0003Q','2eecc8a6-d234-4052-877b-bbab59839123','Y','admin','2017-06-03 08:42:13',NULL,NULL),('79e1cf24-2522-4cdf-abcc-6455b47d545b','CORE_PROG002D','00000000-0000-0000-0000-000000000000','Y','admin','2017-05-08 21:32:59',NULL,NULL),('7a8ae266-b79e-11eb-b5e3-b18180593361','HF_PROG004D','00000000-0000-0000-0000-000000000000','Y','admin','2021-05-18 14:01:25',NULL,NULL),('7ea68636-c93a-4669-ac42-dafc3770d20d','CORE_PROG001D','00000000-0000-0000-0000-000000000000','Y','admin','2017-04-20 11:24:53',NULL,NULL),('95826121-fffa-4e08-b276-44285d6668fc','CORE_PROG003D0006Q','2eecc8a6-d234-4052-877b-bbab59839123','Y','admin','2017-06-03 08:42:14',NULL,NULL),('9972c249-2985-49ac-9b8b-f6c25c65fd4e','CORE_PROG002D0003Q','79e1cf24-2522-4cdf-abcc-6455b47d545b','Y','admin','2017-05-10 14:20:12',NULL,NULL),('a3a690f4-b79e-11eb-b5e3-8768228c883c','HF_PROG004D0001Q','7a8ae266-b79e-11eb-b5e3-b18180593361','Y','admin','2021-05-18 14:02:34',NULL,NULL),('ab061944-9352-4d7f-af50-a330646f0502','CORE_PROG003D0001Q','2eecc8a6-d234-4052-877b-bbab59839123','Y','admin','2017-06-03 08:42:13',NULL,NULL),('bfda3dfe-5857-4637-bf37-0d1ae00f32ce','CORE_PROG001D0002Q','7ea68636-c93a-4669-ac42-dafc3770d20d','Y','admin','2017-06-05 09:55:00',NULL,NULL),('c5349a26-6d6e-4d94-b817-82be6d14d5ed','CORE_PROG002D0001Q','79e1cf24-2522-4cdf-abcc-6455b47d545b','Y','admin','2017-05-10 14:20:12',NULL,NULL),('da6114c4-9846-11eb-bd46-85afc7e6330c','HF_PROG003D','00000000-0000-0000-0000-000000000000','Y','admin','2021-04-08 16:46:04',NULL,NULL),('da627455-9846-11eb-bd46-938361148453','HF_PROG003D0001Q','da6114c4-9846-11eb-bd46-85afc7e6330c','Y','admin','2021-04-08 16:46:04',NULL,NULL),('efb79ce7-360e-4a73-9c52-ee5f4bc8f4e8','CORE_PROG001D0001Q','7ea68636-c93a-4669-ac42-dafc3770d20d','Y','admin','2017-06-05 09:54:59',NULL,NULL),('f21319e4-c83b-486f-857e-44a1b2baa7b1','CORE_PROG004D0002Q','5e055f61-bfc5-402c-93b4-f241dc17b00b','Y','admin','2017-06-03 14:23:22',NULL,NULL),('f7e5820f-c59e-11eb-8d6a-57ad246b0f8c','HF_PROG005D','00000000-0000-0000-0000-000000000000','Y','admin','2021-06-05 09:40:11',NULL,NULL),('f9b3b263-c59e-11eb-8d6a-1b0110f5647c','HF_PROG005D0001Q','f7e5820f-c59e-11eb-8d6a-57ad246b0f8c','Y','admin','2021-06-05 09:40:14',NULL,NULL);
/*!40000 ALTER TABLE `tb_sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_menu_role`
--

DROP TABLE IF EXISTS `tb_sys_menu_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_menu_role` (
  `OID` char(36) NOT NULL,
  `PROG_ID` varchar(50) NOT NULL,
  `ROLE` varchar(50) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`PROG_ID`,`ROLE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_menu_role`
--

LOCK TABLES `tb_sys_menu_role` WRITE;
/*!40000 ALTER TABLE `tb_sys_menu_role` DISABLE KEYS */;
INSERT INTO `tb_sys_menu_role` VALUES ('1bc27900-99a7-11eb-8f3e-131740d7ee1a','HF_PROG001D','COMMON01','admin','2021-04-10 10:47:36',NULL,NULL),('2320c3a4-99a7-11eb-8f3e-a191fa73e402','HF_PROG001D0001Q','COMMON01','admin','2021-04-10 10:47:49',NULL,NULL),('23fc3940-c1ac-11eb-bb27-ddead56a82e7','HF_PROG001D0007Q','COMMON01','admin','2021-05-31 09:04:24',NULL,NULL),('2957cc58-99a7-11eb-8f3e-75160c4ba3ec','HF_PROG001D0002Q','COMMON01','admin','2021-04-10 10:47:59',NULL,NULL),('2e0d170c-99a7-11eb-8f3e-7deffc82c602','HF_PROG001D0003Q','COMMON01','admin','2021-04-10 10:48:07',NULL,NULL),('32eec8f0-99a7-11eb-8f3e-574202465df5','HF_PROG001D0004Q','COMMON01','admin','2021-04-10 10:48:15',NULL,NULL),('3a4d1394-99a7-11eb-8f3e-15ca7c575547','HF_PROG001D0005Q','COMMON01','admin','2021-04-10 10:48:28',NULL,NULL),('3dedb9f8-99a7-11eb-8f3e-6562f4c27029','HF_PROG001D0006Q','COMMON01','admin','2021-04-10 10:48:34',NULL,NULL),('42700dcc-99a7-11eb-8f3e-35606c57b4d3','HF_PROG002D','COMMON01','admin','2021-04-10 10:48:41',NULL,NULL),('45d57ff0-99a7-11eb-8f3e-077d369d418a','HF_PROG002D0001Q','COMMON01','admin','2021-04-10 10:48:47',NULL,NULL),('489c6824-99a7-11eb-8f3e-671235b73be1','HF_PROG003D','COMMON01','admin','2021-04-10 10:48:52',NULL,NULL),('4af4d718-99a7-11eb-8f3e-d1c3a0fb9f06','HF_PROG003D0001Q','COMMON01','admin','2021-04-10 10:48:56',NULL,NULL),('6b11c2f4-c59f-11eb-8d6a-597ccee7a03c','HF_PROG001D0008Q','COMMON01','admin','2021-06-05 09:43:25',NULL,NULL),('76b669da-c59f-11eb-8d6a-f7cb1720003f','HF_PROG005D','COMMON01','admin','2021-06-05 09:43:44',NULL,NULL),('7947c31e-c59f-11eb-8d6a-43d038df47cc','HF_PROG005D0001Q','COMMON01','admin','2021-06-05 09:43:49',NULL,NULL),('ebeaa0b1-b79e-11eb-b5e3-01fca9dfc294','HF_PROG004D','COMMON01','admin','2021-05-18 14:04:35',NULL,NULL),('ee8d8625-b79e-11eb-b5e3-9d90842e736b','HF_PROG004D0001Q','COMMON01','admin','2021-05-18 14:04:39',NULL,NULL);
/*!40000 ALTER TABLE `tb_sys_menu_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_prog`
--

DROP TABLE IF EXISTS `tb_sys_prog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_prog` (
  `OID` char(36) NOT NULL,
  `PROG_ID` varchar(50) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `URL` varchar(255) NOT NULL,
  `EDIT_MODE` varchar(1) NOT NULL DEFAULT 'N',
  `IS_DIALOG` varchar(1) NOT NULL DEFAULT 'N',
  `DIALOG_W` int(4) NOT NULL DEFAULT 0,
  `DIALOG_H` int(4) NOT NULL DEFAULT 0,
  `PROG_SYSTEM` varchar(10) NOT NULL,
  `ITEM_TYPE` varchar(10) NOT NULL,
  `ICON` varchar(20) NOT NULL,
  `FONT_ICON_CLASS_ID` varchar(100) NOT NULL DEFAULT 'circle-o',
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`PROG_ID`),
  KEY `IDX_1` (`PROG_SYSTEM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_prog`
--

LOCK TABLES `tb_sys_prog` WRITE;
/*!40000 ALTER TABLE `tb_sys_prog` DISABLE KEYS */;
INSERT INTO `tb_sys_prog` VALUES ('005463fd-a1f4-4e9f-89a7-bd86aedfcbdf','CORE_PROG003D0002E','ZC02 - Expression (Edit)','sysExpressionEditPage','Y','N',0,0,'CORE','ITEM','TEXT_SOURCE','code','admin','2017-05-24 11:38:45','admin','2021-01-27 03:50:24'),('011fa9ae-c1ac-11eb-bb27-137ac9a3c0e9','HF_PROG001D0007Q','BA07 - Personal board','hfPersonalDashboardPage','N','N',0,0,'CORE','ITEM','HELP_ABOUT','pie-chart','admin','2021-05-31 09:03:26',NULL,NULL),('0b39ae53-b79d-11eb-b5e3-1dbac1d9beb2','HF_PROG004D0001E','BD01 - PDCA (Edit)','hfPdcaEditPage','Y','N',0,0,'CORE','ITEM','HELP_ABOUT','recycle','admin','2021-05-18 13:51:09',NULL,NULL),('0bebeb7d-c59d-11eb-8d6a-c9ea79d95751','HF_PROG005D','BE. Scorecard','','N','N',0,0,'CORE','FOLDER','HELP_ABOUT','cube','admin','2021-06-05 09:26:26','admin','2021-06-05 09:26:59'),('1154dcf1-98ec-11eb-906a-37a443ddc0d7','HF_PROG003D0001Q01D','BC01 - OKR Report (detail)','hfOkrReportDetailPage','Y','N',0,0,'CORE','ITEM','HELP_ABOUT','paper-plane','admin','2021-04-09 12:28:43',NULL,NULL),('133801b1-02d1-11eb-9af6-8dacfb641865','HF_PROG001D0003A','BA03 - Formula (Create)','hfFormulaCreatePage','N','N',0,0,'CORE','ITEM','TEXT_SOURCE','calculator','admin','2020-09-30 03:57:35','admin','2021-01-27 05:21:13'),('159db567-057d-11eb-ac20-af8aa39116f3','HF_PROG001D0005M','BA05 - KPI Measure data','hfMeasureDataPage','Y','N',0,0,'CORE','ITEM','STAR','book','admin','2020-10-03 13:33:55','admin','2021-01-27 05:27:46'),('186b1fb1-749f-4b6f-97d1-6b7fb8115345','CORE_PROG001D0004E','ZA04 - Template (Edit)','sysTemplateEditPage','Y','N',0,0,'CORE','ITEM','TEMPLATE','file-code-o','admin','2017-05-12 10:40:10','admin','2021-01-27 03:41:40'),('1a070914-7b72-47e9-8908-f1a022f3148f','HF_PROG001D0001E','BA01 - Organization(Edit)','hfOrgDeptEditPage','Y','N',0,0,'CORE','ITEM','DIAGRAM','home','admin','2020-09-23 07:01:13','admin','2021-01-27 05:18:42'),('1b11c7eb-6133-48fb-87f0-dfbd098ce914','CORE_PROG001D0001E','ZA01 - System site (Edit)','sysSiteEditPage','Y','N',0,0,'CORE','ITEM','COMPUTER','server','admin','2014-10-02 00:00:00','admin','2021-01-27 03:38:15'),('1bf85525-b79d-11eb-b5e3-c370ad054b9f','HF_PROG004D0001V','BD01 - PDCA (Detail)','hfPdcaViewDetailPage','Y','N',0,0,'CORE','ITEM','HELP_ABOUT','recycle','admin','2021-05-18 13:51:37',NULL,NULL),('1e1e737e-0fbe-46a5-90d5-e59ea4a3c292','HF_PROG001D0001Q','BA01 - Organization','hfOrgDeptPage','N','N',0,0,'CORE','ITEM','DIAGRAM','home','admin','2020-09-23 06:58:31','admin','2021-01-27 05:18:55'),('1e393fe3-8bbc-482c-aa23-bbb22a1dbafb','CORE_PROG001D0005A','ZA05 - Report (Create)','sysReportCreatePage','N','N',0,0,'CORE','ITEM','APPLICATION_PDF','file-pdf-o','admin','2017-05-18 09:55:46','admin','2021-01-27 03:42:41'),('22560527-90fb-4e5a-a89b-353d2aa1d433','CORE_PROG001D0005E','ZA05 - Report (Edit)','sysReportEditPage','Y','N',0,0,'CORE','ITEM','APPLICATION_PDF','file-pdf-o','admin','2017-05-18 09:56:27','admin','2021-01-27 03:42:49'),('23aeeb27-e4ed-498b-a233-526d58d12e88','CORE_PROG003D0006E','ZC06 - Expression Job (Edit)','sysExpressionJobEditPage','Y','N',0,0,'CORE','ITEM','SYSTEM','wrench','admin','2017-06-03 08:41:33','admin','2021-01-27 03:54:17'),('24e71a8b-9846-11eb-bd46-ad19c048b141','HF_PROG003D','BC. OKR','','N','N',0,0,'CORE','FOLDER','HELP_ABOUT','star','admin','2021-04-08 16:40:59',NULL,NULL),('2f4d0852-02d1-11eb-9af6-ddd9fe9a0743','HF_PROG001D0003E','BA03 - Formula (Edit)','hfFormulaEditPage','Y','N',0,0,'CORE','ITEM','TEXT_SOURCE','calculator','admin','2020-09-30 03:58:22','admin','2021-01-27 05:20:58'),('30bb5861-451c-49ef-a382-0180703333ae','HF_PROG001D0002Q','BA02 - Employee','hfEmployeePage','N','N',0,0,'CORE','ITEM','PERSON','users','admin','2020-09-26 02:56:44','admin','2021-01-27 05:19:53'),('3630ee1b-6169-452f-821f-5c015dfb84d5','CORE_PROG001D','ZA. Config','','N','N',0,0,'CORE','FOLDER','PROPERTIES','gear','admin','2014-10-02 00:00:00','admin','2021-01-27 03:37:25'),('3862b6d0-0551-45d8-8dd1-cd988a5e8e50','CORE_PROG004D0002Q','ZD02 - Login log','sysLoginLogPage','N','N',0,0,'CORE','ITEM','PROPERTIES','search','admin','2017-06-03 14:22:29','admin','2021-01-27 08:34:09'),('41fa29d8-3a53-4fbd-b2b1-cdbfd0729767','CORE_PROG001D0004Q','ZA04 - Template','sysTemplatePage','N','N',0,0,'CORE','ITEM','TEMPLATE','file-code-o','admin','2017-05-12 10:36:41','admin','2021-01-27 03:41:49'),('48e41d4f-959f-11eb-b675-69460beb3f77','HF_PROG001D0006M','BA06 - OKR Base (Masure data)','hfOkrBaseEnterMasureDataPage','Y','N',0,0,'CORE','ITEM','HELP_ABOUT','paper-plane','admin','2021-04-05 07:41:32','admin','2021-06-05 10:09:47'),('5599c3a9-f971-432a-a8e7-807d960ef196','CORE_PROG003D0003E','ZC03 - Bean support (Edit)','sysBeanSupportEditPage','Y','N',0,0,'CORE','ITEM','SYSTEM','support','admin','2017-05-25 10:39:32','admin','2021-01-27 03:51:57'),('5876c1e9-c59c-11eb-8d6a-9f1941c43ff9','HF_PROG001D0008Q','BA08 - Scorecard','hfScorecardPage','N','N',0,0,'CORE','ITEM','HELP_ABOUT','cubes','admin','2021-06-05 09:21:25',NULL,NULL),('5e082c7c-1730-4176-89c6-93e235707deb','CORE_PROG002D0001A','ZB01 - Role (Create)','roleCreatePage','N','N',0,0,'CORE','ITEM','PEOPLE','user-secret','admin','2017-05-09 11:15:50','admin','2021-01-27 03:46:36'),('61aea7ff-7a42-4a92-9a0b-4a0dfe60858b','CORE_PROG004D0001Q','ZD01 - Event log','sysEventLogPage','N','N',0,0,'CORE','ITEM','PROPERTIES','search','admin','2017-06-03 14:22:07','admin','2021-01-27 08:33:58'),('63e1a096-d8c5-11eb-8921-bb2d01df1365','HF_PROG001D0008S','Scorecard color settings','hfScorecardColorPage','Y','N',0,0,'CORE','ITEM','HELP_ABOUT','paint-brush','admin','2021-06-29 18:33:06',NULL,NULL),('6a442973-0e0c-4a7a-d546-464f4ff5f7a9','CORE_PROG001D0003Q','ZA03 - Menu settings','menuSettingsPage','N','N',0,0,'CORE','ITEM','FOLDER','sitemap','admin','2014-10-02 00:00:00','admin','2021-01-27 03:40:28'),('6b210525-8975-4fb5-954c-fe349f66d3fe','CORE_PROG002D0001S01Q','ZB01 - Role (permission)','rolePermissionPage','Y','N',0,0,'CORE','ITEM','IMPORTANT','user-secret','admin','2017-05-09 14:32:47','admin','2021-01-27 03:47:01'),('6b481707-31ff-422b-a45c-38724d83772c','CORE_PROG999D9999Q','About','about.html','N','N',0,0,'CORE','ITEM','HELP_ABOUT','info-circle','admin','2017-06-05 11:02:04','admin','2021-01-27 08:29:10'),('6d4842f1-b79e-11eb-b5e3-a16f410cd7d2','HF_PROG004D','BD. PDCA','','N','N',0,0,'CORE','FOLDER','HELP_ABOUT','retweet','admin','2021-05-18 14:01:03','admin','2021-05-18 14:03:35'),('6db21ae4-03d8-11eb-81a4-654fcf3923d4','HF_PROG001D0004E','BA04 - Aggregation method (Edit)','hfAggregationMethodEditPage','Y','N',0,0,'CORE','ITEM','TEXT_SOURCE','code','admin','2020-10-01 11:22:45','admin','2021-01-27 05:23:24'),('6fc93cc1-959f-11eb-b675-d34390f2ebad','HF_PROG001D0006E','BA06 - OKR Base (Edit)','hfOkrBaseEditPage','Y','N',0,0,'CORE','ITEM','HELP_ABOUT','paper-plane','admin','2021-04-05 07:42:37','admin','2021-06-05 10:09:38'),('72e6e0d1-1818-47d3-99f9-5134fb211b79','CORE_PROG002D','ZB. Role authority','','N','N',0,0,'CORE','FOLDER','SHARED','user-circle-o','admin','2017-05-08 21:27:52','admin','2021-01-27 03:46:03'),('7746f746-961f-44c2-9b66-fa43c0f49838','CORE_PROG001D0004S01Q','ZA04 - Template (Parameter)','sysTemplateParam','Y','N',0,0,'CORE','ITEM','TEMPLATE','file-code-o','admin','2017-05-12 10:42:04','admin','2021-01-27 03:41:58'),('7984332c-7963-4a02-9888-75a55b5af9f9','CORE_PROG003D0003S02Q','ZC03 - Bean support (Parameter)','sysBeanSupportExpressionParamPage','Y','N',0,0,'CORE','ITEM','SYSTEM','support','admin','2017-05-25 10:42:08','admin','2021-01-27 03:52:36'),('7c1e2c5e-1d62-432a-8cbb-42f550694b4b','CORE_PROG003D0006Q','ZC06 - Expression Job','sysExpressionJobPage','N','N',0,0,'CORE','ITEM','SYSTEM','wrench','admin','2017-06-03 08:40:57','admin','2021-01-27 03:54:08'),('7d9ddc45-3eab-4f61-8c0a-d5505c0cc748','CORE_PROG001D0004A','ZA04 - Template (Create)','sysTemplateCreatePage','N','N',0,0,'CORE','ITEM','TEMPLATE','file-code-o','admin','2017-05-12 10:39:20','admin','2021-01-27 03:41:31'),('8499957e-6da9-4160-c2ec-dfb7dbc202fe','CORE_PROG001D0002E','ZA02 - Program (Edit)','sysProgramEditPage','Y','N',0,0,'CORE','ITEM','G_APP_INSTALL','window-maximize','admin','2014-10-02 00:00:00','admin','2021-01-27 03:39:29'),('8bfff6de-c59c-11eb-8d6a-0f69eb8558bb','HF_PROG001D0008A','BA08 - Scorecard (Create)','hfScorecardCreatePage','N','N',0,0,'CORE','ITEM','HELP_ABOUT','cubes','admin','2021-06-05 09:22:51',NULL,NULL),('91a2dad4-95d2-45c3-9fda-ae0d69ad6f22','CORE_PROG003D0006A','ZC06 - Expression Job (Create)','sysExpressionJobCreatePage','N','N',0,0,'CORE','ITEM','SYSTEM','wrench','admin','2017-06-03 08:41:15','admin','2021-01-27 03:54:24'),('a14db172-0ea8-11eb-ba89-c5e2992711a5','HF_PROG002D','BB. KPI','','N','N',0,0,'CORE','FOLDER','STAR','dashboard','admin','2020-10-15 05:38:18','admin','2021-01-27 05:28:36'),('ac5bcfd0-4abd-11e4-916c-0800200c9a66','CORE_PROG001D0001A','ZA01 - System site (Create)','sysSiteCreatePage','N','N',0,0,'CORE','ITEM','COMPUTER','server','admin','2014-10-02 00:00:00','admin','2021-01-27 03:38:05'),('b32515ff-8d0f-11eb-8e8a-9f40aff77a7b','HF_PROG001D0006Q','BA06 - OKR Base','hfOkrBasePage','N','N',0,0,'CORE','ITEM','HELP_ABOUT','paper-plane','admin','2021-03-25 10:13:33','admin','2021-06-05 10:09:55'),('b34b95d7-8635-471e-9a34-3aee1c28a254','HF_PROG001D0002E','BA02 - Employee (Edit)','hfEmployeeEditPage','Y','N',0,0,'CORE','ITEM','PERSON','users','admin','2020-09-26 02:58:30','admin','2021-01-27 05:19:44'),('b39159ad-0707-4515-b78d-e3fc72c53974','CORE_PROG002D0001E','ZB01 - Role (Edit)','roleEditPage','Y','N',0,0,'CORE','ITEM','PEOPLE','user-secret','admin','2017-05-09 12:11:53','admin','2021-01-27 03:46:44'),('b3f7654a-9b35-4b9d-8cb7-bc2557869a3b','CORE_PROG004D0003Q','ZD03 - Expression Job log','sysExpressionJobLogPage','N','N',0,0,'CORE','ITEM','PROPERTIES','search','admin','2017-06-03 14:22:58','admin','2021-01-27 08:34:18'),('b5e32db5-0493-11eb-8a44-29d090a2d74b','HF_PROG001D0005Q','BA05 - KPI Base','hfKpiBasePage','N','N',0,0,'CORE','ITEM','STAR','book','admin','2020-10-02 09:43:22','admin','2021-01-27 05:27:59'),('b6b89559-6864-46ab-9ca9-0992dcf238f1','CORE_PROG001D0001Q','ZA01 - System site','sysSitePage','N','N',0,0,'CORE','ITEM','COMPUTER','server','admin','2014-10-02 00:00:00','admin','2021-01-27 03:38:27'),('b978f706-4c5f-40f8-83b1-395492f141d4','CORE_PROG002D0001Q','ZB01 - Role','rolePage','N','N',0,0,'CORE','ITEM','PEOPLE','user-secret','admin','2017-05-08 21:32:50','admin','2021-01-27 03:46:52'),('bc97b0d4-516a-4f26-a79a-88ecc65bfe50','CORE_PROG003D0003Q','ZC03 - Bean support','sysBeanSupportPage','N','N',0,0,'CORE','ITEM','SYSTEM','support','admin','2017-05-25 10:38:42','admin','2021-01-27 03:52:09'),('bffce168-6b55-47ed-9fa3-d37666a1e9c7','CORE_PROG003D0003A','ZC03 - Bean support (Create)','sysBeanSupportCreatePage','N','N',0,0,'CORE','ITEM','SYSTEM','support','admin','2017-05-25 10:39:07','admin','2021-01-27 03:51:46'),('c83c0d03-912e-41b4-b7ed-ff9ec19fa360','HF_PROG001D0001A','BA01 - Organization(Create)','hfOrgDeptCreatePage','N','N',0,0,'CORE','ITEM','DIAGRAM','home','admin','2020-09-23 06:59:45','admin','2021-01-27 05:18:28'),('c96ebde8-7044-4b05-a155-68a0c2605619','CORE_PROG002D0003Q','ZB03 - Role for menu','menuRolePage','N','N',0,0,'CORE','ITEM','FOLDER','sitemap','admin','2017-05-08 21:37:01','admin','2021-01-27 03:48:59'),('ca077b24-c59c-11eb-8d6a-65f93e97a83b','HF_PROG001D0008E','BA08 - Scorecard (Edit)','hfScorecardEditPage','Y','N',0,0,'CORE','ITEM','HELP_ABOUT','cubes','admin','2021-06-05 09:24:35','admin','2021-06-05 09:25:41'),('cdc2a0f6-0493-11eb-8a44-b3636b1f1261','HF_PROG001D0005E','BA05 - KPI Base (Edit)','hfKpiBaseEditPage','Y','N',0,0,'CORE','ITEM','STAR','book','admin','2020-10-02 09:44:02','admin','2021-01-27 05:27:36'),('d2e46caf-9846-11eb-bd46-772b1a3504df','HF_PROG003D0001Q','BC01 - OKR Report','hfOkrReportPage','N','N',0,0,'CORE','ITEM','HELP_ABOUT','paper-plane','admin','2021-04-08 16:45:51',NULL,NULL),('d5de9cc2-03d7-11eb-81a4-09d861070cfa','HF_PROG001D0004Q','BA04 - Aggregation method','hfAggregationMethodPage','N','N',0,0,'CORE','ITEM','TEXT_SOURCE','code','admin','2020-10-01 11:18:30','admin','2021-01-27 05:23:34'),('d8efb01f-b79c-11eb-b5e3-672e405921a7','HF_PROG004D0001Q','BD01 - PDCA (Query)','hfPdcaPage','N','N',0,0,'CORE','ITEM','HELP_ABOUT','recycle','admin','2021-05-18 13:49:44','admin','2021-05-18 14:04:14'),('d9fb1f84-0ea8-11eb-ba89-93a4e2faf1ad','HF_PROG002D0001Q','BB01 - KPI Report','hfKpiReportPage','N','N',0,0,'CORE','ITEM','STAR','line-chart','admin','2020-10-15 05:39:53','admin','2021-01-27 05:29:58'),('da0e70df-29b1-4a2e-890c-eab723c86ed6','CORE_PROG002D0001S02Q','ZB01 - Role (copy)','roleCopyPage','Y','Y',650,600,'CORE','ITEM','PEOPLE','user-secret','admin','2017-05-09 20:56:50','admin','2021-01-27 03:47:20'),('da7d969a-5efb-4e84-9eab-4fdae236f28c','CORE_PROG002D0002Q','ZB02 - User role','userRolePage','N','N',0,0,'CORE','ITEM','PERSON','drivers-license-o','admin','2017-05-08 21:34:39','admin','2021-01-27 03:48:19'),('dda67b1d-e3a2-4534-835a-c62d9e8421f3','CORE_PROG001D0005S01Q','ZA05 - Report (Parameter)','sysReportParamPage','Y','N',0,0,'CORE','ITEM','APPLICATION_PDF','file-pdf-o','admin','2017-05-18 09:57:26','admin','2021-01-27 03:43:05'),('df0edf80-92bd-11eb-9709-838bc98e4a9a','HF_PROG001D0006A','BA06 - OKR Base (Add objective)','hfOkrBaseAddPage','N','N',0,0,'CORE','ITEM','HELP_ABOUT','paper-plane','admin','2021-04-01 15:42:55','admin','2021-06-05 10:09:27'),('e32b9329-bb38-46d7-8552-2307bac77724','CORE_PROG001D0002A','ZA02 - Program (Create)','sysProgramCreatePage','N','N',0,0,'CORE','ITEM','G_APP_INSTALL','window-maximize','admin','2014-10-02 00:00:00','admin','2021-01-27 03:39:39'),('e42f4e11-ddf2-49f5-9267-7cfed6ab1d02','CORE_PROG001D0005S02Q','ZA05 - Report (Preview)','sysReportPreviewPage','Y','Y',600,600,'CORE','ITEM','APPLICATION_PDF','file-pdf-o','admin','2017-05-18 10:00:04','admin','2021-01-27 03:43:17'),('e4e691f8-2cd8-444d-b412-ccdbbec029a1','CORE_PROG003D','ZC. Service','','N','N',0,0,'CORE','FOLDER','DIAGRAM','handshake-o','admin','2017-05-23 08:53:46','admin','2021-01-27 03:49:35'),('e81cd837-0493-11eb-8a44-ed83f24dbf55','HF_PROG001D0005A','BA05 - KPI Base (Create)','hfKpiBaseCreatePage','N','N',0,0,'CORE','ITEM','STAR','book','admin','2020-10-02 09:44:46','admin','2021-01-27 05:27:22'),('e86dbb1b-6870-4827-8039-72f5e15fa4f2','CORE_PROG004D','ZD. Log','','N','N',0,0,'CORE','FOLDER','PROPERTIES','eye','admin','2017-06-03 14:21:03','admin','2021-01-27 03:55:20'),('ea08454d-bd3a-4ff4-8d08-fe5d5deb5118','HF_PROG001D0002A','BA02 - Employee (Create)','hfEmployeeCreatePage','N','N',0,0,'CORE','ITEM','PERSON','users','admin','2020-09-26 02:57:39','admin','2021-01-27 05:19:33'),('eb6e199f-c853-4fbf-acf3-0c9c77ba9953','CORE_PROG001D0002Q','ZA02 - Program','sysProgramPage','N','N',0,0,'CORE','ITEM','G_APP_INSTALL','window-maximize','admin','2014-10-02 00:00:00','admin','2021-01-27 03:39:20'),('eb786ffd-c7d1-4631-aed2-4d9d7368eb13','CORE_PROG001D0005Q','ZA05 - Report','sysReportPage','N','N',0,0,'CORE','ITEM','APPLICATION_PDF','file-pdf-o','admin','2017-05-18 09:54:35','admin','2021-01-27 03:42:58'),('ecc5c29a-c59e-11eb-8d6a-7ba2b091a0c1','HF_PROG005D0001Q','BE01 - Scorecard Report','hfScorecardReportPage','N','N',0,0,'CORE','ITEM','HELP_ABOUT','cubes','admin','2021-06-05 09:39:53',NULL,NULL),('edf4ab8c-df86-4ef0-91f6-c2ddcc6e8f7d','CORE_PROG003D0002A','ZC02 - Expression (Create)','sysExpressionCreatePage','N','N',0,0,'CORE','ITEM','TEXT_SOURCE','code','admin','2017-05-24 11:38:27','admin','2021-01-27 03:50:13'),('ef6963fe-171e-4ebe-ae24-aef7388fccce','CORE_PROG001D0007Q','ZA07 - Setting','sysSettingPage','N','N',0,0,'CORE','ITEM','PROPERTIES','edit','admin','2017-06-05 09:54:48','admin','2021-01-27 03:44:47'),('efde7f03-03d7-11eb-81a4-f152dc6201be','HF_PROG001D0004A','BA04 - Aggregation method (Create)','hfAggregationMethodCreatePage','N','N',0,0,'CORE','ITEM','TEXT_SOURCE','code','admin','2020-10-01 11:19:14','admin','2021-01-27 05:23:09'),('f2e5c690-02d0-11eb-9af6-3bccc88422a1','HF_PROG001D0003Q','BA03 - Formula','hfFormulaPage','N','N',0,0,'CORE','ITEM','TEXT_SOURCE','calculator','admin','2020-09-30 03:56:41','admin','2021-01-27 05:20:50'),('f80aa3c1-b79c-11eb-b5e3-1964e870f276','HF_PROG004D0001A','BD01 - PDCA (Create)','hfPdcaCreatePage','Y','N',0,0,'CORE','ITEM','HELP_ABOUT','recycle','admin','2021-05-18 13:50:36',NULL,NULL),('fa1e4078-638f-41e6-82d4-77ef92e76374','CORE_PROG003D0002Q','ZC02 - Expression','sysExpressionPage','N','N',0,0,'CORE','ITEM','TEXT_SOURCE','code','admin','2017-05-24 11:38:04','admin','2021-01-27 03:50:34'),('fd55cb27-30d6-4d48-9ec9-26de2ba087ac','HF_PROG001D','BA. Basic','','N','N',0,0,'CORE','FOLDER','PROPERTIES','newspaper-o','admin','2020-09-23 06:53:15','admin','2021-06-05 10:07:11'),('fee6533d-da1c-444b-aec9-6d4f81fa051d','CORE_PROG003D0003S01Q','ZC03 - Bean support (Expression)','sysBeanSupportExpressionPage','Y','N',0,0,'CORE','ITEM','SYSTEM','support','admin','2017-05-25 10:41:46','admin','2021-01-27 03:52:22');
/*!40000 ALTER TABLE `tb_sys_prog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_qfield_log`
--

DROP TABLE IF EXISTS `tb_sys_qfield_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_qfield_log` (
  `OID` char(36) NOT NULL,
  `SYSTEM` varchar(10) NOT NULL,
  `PROG_ID` varchar(50) NOT NULL,
  `METHOD_NAME` varchar(255) NOT NULL,
  `FIELD_NAME` varchar(255) NOT NULL,
  `FIELD_VALUE` varchar(500) DEFAULT NULL,
  `QUERY_USER_ID` varchar(24) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  KEY `IDX_1` (`SYSTEM`,`PROG_ID`),
  KEY `IDX_2` (`QUERY_USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_qfield_log`
--

LOCK TABLES `tb_sys_qfield_log` WRITE;
/*!40000 ALTER TABLE `tb_sys_qfield_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_qfield_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_template`
--

DROP TABLE IF EXISTS `tb_sys_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_template` (
  `OID` char(36) NOT NULL,
  `TEMPLATE_ID` varchar(10) NOT NULL,
  `TITLE` varchar(200) NOT NULL,
  `MESSAGE` varchar(4000) NOT NULL,
  `DESCRIPTION` varchar(200) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`TEMPLATE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_template`
--

LOCK TABLES `tb_sys_template` WRITE;
/*!40000 ALTER TABLE `tb_sys_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_template_param`
--

DROP TABLE IF EXISTS `tb_sys_template_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_template_param` (
  `OID` char(36) NOT NULL,
  `TEMPLATE_ID` varchar(10) NOT NULL,
  `IS_TITLE` varchar(1) NOT NULL DEFAULT 'N',
  `TEMPLATE_VAR` varchar(100) NOT NULL,
  `OBJECT_VAR` varchar(100) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`TEMPLATE_ID`,`TEMPLATE_VAR`,`IS_TITLE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_template_param`
--

LOCK TABLES `tb_sys_template_param` WRITE;
/*!40000 ALTER TABLE `tb_sys_template_param` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_template_param` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_upload`
--

DROP TABLE IF EXISTS `tb_sys_upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_upload` (
  `OID` char(36) NOT NULL,
  `SYSTEM` varchar(10) NOT NULL,
  `SUB_DIR` varchar(4) NOT NULL,
  `TYPE` varchar(10) NOT NULL,
  `FILE_NAME` varchar(50) NOT NULL,
  `SHOW_NAME` varchar(255) NOT NULL,
  `IS_FILE` varchar(1) NOT NULL DEFAULT 'Y',
  `CONTENT` mediumblob DEFAULT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  KEY `IDX_1` (`SYSTEM`,`TYPE`,`SUB_DIR`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_upload`
--

LOCK TABLES `tb_sys_upload` WRITE;
/*!40000 ALTER TABLE `tb_sys_upload` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_upload` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_usess`
--

DROP TABLE IF EXISTS `tb_sys_usess`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_usess` (
  `OID` char(36) NOT NULL,
  `SESSION_ID` varchar(64) NOT NULL,
  `ACCOUNT` varchar(24) NOT NULL,
  `CURRENT_ID` varchar(36) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`,`SESSION_ID`),
  UNIQUE KEY `UK_1` (`ACCOUNT`,`SESSION_ID`),
  KEY `IDX_1` (`CURRENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_usess`
--

LOCK TABLES `tb_sys_usess` WRITE;
/*!40000 ALTER TABLE `tb_sys_usess` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_usess` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_role`
--

DROP TABLE IF EXISTS `tb_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user_role` (
  `OID` char(36) NOT NULL,
  `ROLE` varchar(50) NOT NULL,
  `ACCOUNT` varchar(24) NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(50) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`ROLE`,`ACCOUNT`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_role`
--

LOCK TABLES `tb_user_role` WRITE;
/*!40000 ALTER TABLE `tb_user_role` DISABLE KEYS */;
INSERT INTO `tb_user_role` VALUES ('1c62cf70-ca6b-4243-8aa9-49b555024c45','COMMON01','steven','','admin','2017-05-10 14:19:58',NULL,NULL),('36054c19-0519-11eb-b8a3-a57f733ab5b5','COMMON01','bill','','admin','2020-10-03 01:39:00',NULL,NULL),('4e6253cd-0519-11eb-b8a3-97213ada0f8c','COMMON01','frank','','admin','2020-10-03 01:39:41',NULL,NULL),('640b10f1-0519-11eb-b8a3-bd311f0b2cb2','COMMON01','cindy','','admin','2020-10-03 01:40:17',NULL,NULL),('9243c7de-43b1-46ef-ac4b-2620697f319e','admin','admin','Administrator','admin','2014-09-23 00:00:00',NULL,NULL),('bd7bf78c-d84b-4524-8273-273f883d30b5','COMMON01','tester','','admin','2017-05-10 11:01:50',NULL,NULL),('da0c0462-4bf7-417b-99da-fc2e378a5ccc','COMMON01','tiffany','','admin','2017-05-10 11:01:43',NULL,NULL);
/*!40000 ALTER TABLE `tb_user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-07-08 21:23:41
