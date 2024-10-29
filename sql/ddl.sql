-- MySQL dump 10.13  Distrib 5.7.40, for Win64 (x86_64)
--
-- Host: localhost    Database: fileupload
-- ------------------------------------------------------
-- Server version	5.7.40

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
-- Table structure for table `file_chunk`
--

DROP TABLE IF EXISTS `file_chunk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_chunk` (
  `id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '主键',
  `identifier` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件唯一标识',
  `real_path` varchar(700) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '分片真实的存储路径',
  `chunk_number` int(11) NOT NULL DEFAULT '0' COMMENT '分片编号',
  `expiration_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  `create_user` varchar(32) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_identifier_chunk_number_create_user` (`identifier`,`chunk_number`,`create_user`) USING BTREE COMMENT '文件唯一标识、分片编号和用户ID的唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='文件分片信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `file_info`
--

DROP TABLE IF EXISTS `file_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_info` (
  `file_id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '文件id',
  `file_transfer_name` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '转存文件名称',
  `real_path` varchar(700) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件物理路径',
  `file_size` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件实际大小',
  `file_size_desc` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件大小展示字符',
  `file_source_name` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件源名称',
  `file_preview_content_type` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件预览的响应头Content-Type的值',
  `identifier` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件唯一标识',
  `create_user_id` varchar(32) NOT NULL COMMENT '创建人',
  `create_user_name` varchar(30) COLLATE utf8mb4_bin NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `delete_flag` int(1) DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`file_id`) USING BTREE,
  KEY `file_info_identifier_IDX` (`identifier`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='物理文件信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'fileupload'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-07 13:44:50
