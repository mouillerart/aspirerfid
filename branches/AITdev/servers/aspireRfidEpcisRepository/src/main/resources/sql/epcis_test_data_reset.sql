--Copyright © 2008-2010, Aspire 
--
-- This file contains the source code of the Accada library by ETH Zurich (www.accada.org),
-- licensed under the terms of the GNU Lesser General Public License version 2.1 in 2007
-- and modified for the needs of the Aspire project.
--
-- Aspire is free software; you can redistribute it and/or 
-- modify it under  the terms of the GNU Lesser General Public 
-- License version 2.1 as published by the Free Software Foundation (the 
-- "LGPL"). 
--
-- You should have received a copy of the GNU Lesser General Public 
-- License along with this library in the file COPYING-LGPL-2.1; if not, write to the Free Software 
-- Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301 USA. 
--
-- This software is distributed on an "AS IS" basis, WITHOUT WARRANTY 
-- OF ANY KIND, either express or implied. See the GNU Lesser General Public 
-- License for the specific language governing rights and limitations. 



/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema epcis
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ epcis;
USE epcis;

--
-- Table structure for table `epcis`.`biztransaction`
--

DROP TABLE IF EXISTS `biztransaction`;
CREATE TABLE `biztransaction` (
  `id` bigint(20) NOT NULL auto_increment,
  `bizTrans` bigint(20) NOT NULL,
  `type` bigint(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`biztransaction`
--

/*!40000 ALTER TABLE `biztransaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `biztransaction` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`event_aggregationevent`
--

DROP TABLE IF EXISTS `event_aggregationevent`;
CREATE TABLE `event_aggregationevent` (
  `id` bigint(20) NOT NULL auto_increment,
  `eventTime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `recordTime` timestamp NOT NULL default '0000-00-00 00:00:00',
  `eventTimeZoneOffset` varchar(8) NOT NULL,
  `parentID` varchar(1023) default NULL,
  `action` varchar(8) NOT NULL,
  `bizStep` bigint(20) default NULL,
  `disposition` bigint(20) default NULL,
  `readPoint` bigint(20) default NULL,
  `bizLocation` bigint(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`event_aggregationevent`
--

/*!40000 ALTER TABLE `event_aggregationevent` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_aggregationevent` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`event_aggregationevent_biztrans`
--

DROP TABLE IF EXISTS `event_aggregationevent_biztrans`;
CREATE TABLE `event_aggregationevent_biztrans` (
  `event_id` bigint(20) NOT NULL,
  `bizTrans_id` bigint(20) NOT NULL,
  `idx` int(11) NOT NULL,
  KEY `event_id` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`event_aggregationevent_biztrans`
--

/*!40000 ALTER TABLE `event_aggregationevent_biztrans` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_aggregationevent_biztrans` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`event_aggregationevent_epcs`
--

DROP TABLE IF EXISTS `event_aggregationevent_epcs`;
CREATE TABLE `event_aggregationevent_epcs` (
  `event_id` bigint(20) NOT NULL,
  `epc` varchar(1023) NOT NULL,
  `idx` int(11) NOT NULL,
  KEY `event_id` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`event_aggregationevent_epcs`
--

/*!40000 ALTER TABLE `event_aggregationevent_epcs` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_aggregationevent_epcs` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`event_aggregationevent_extensions`
--

DROP TABLE IF EXISTS `event_aggregationevent_extensions`;
CREATE TABLE `event_aggregationevent_extensions` (
  `id` bigint(20) NOT NULL auto_increment,
  `event_id` bigint(20) NOT NULL,
  `fieldname` varchar(128) NOT NULL,
  `prefix` varchar(32) NOT NULL,
  `intValue` int(11) default NULL,
  `floatValue` float default NULL,
  `dateValue` timestamp NULL default NULL,
  `strValue` varchar(1024) default NULL,
  PRIMARY KEY  (`id`),
  KEY `event_id` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`event_aggregationevent_extensions`
--

/*!40000 ALTER TABLE `event_aggregationevent_extensions` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_aggregationevent_extensions` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`event_objectevent`
--

DROP TABLE IF EXISTS `event_objectevent`;
CREATE TABLE `event_objectevent` (
  `id` bigint(20) NOT NULL auto_increment,
  `eventTime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `recordTime` timestamp NOT NULL default '0000-00-00 00:00:00',
  `eventTimeZoneOffset` varchar(8) NOT NULL,
  `action` varchar(8) NOT NULL,
  `bizStep` bigint(20) default NULL,
  `disposition` bigint(20) default NULL,
  `readPoint` bigint(20) default NULL,
  `bizLocation` bigint(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`event_objectevent`
--

/*!40000 ALTER TABLE `event_objectevent` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_objectevent` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`event_objectevent_biztrans`
--

DROP TABLE IF EXISTS `event_objectevent_biztrans`;
CREATE TABLE `event_objectevent_biztrans` (
  `event_id` bigint(20) NOT NULL,
  `bizTrans_id` bigint(20) NOT NULL,
  `idx` int(11) NOT NULL,
  KEY `event_id` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`event_objectevent_biztrans`
--

/*!40000 ALTER TABLE `event_objectevent_biztrans` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_objectevent_biztrans` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`event_objectevent_epcs`
--

DROP TABLE IF EXISTS `event_objectevent_epcs`;
CREATE TABLE `event_objectevent_epcs` (
  `event_id` bigint(20) NOT NULL,
  `epc` varchar(1023) NOT NULL,
  `idx` int(11) NOT NULL,
  KEY `event_id` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`event_objectevent_epcs`
--

/*!40000 ALTER TABLE `event_objectevent_epcs` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_objectevent_epcs` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`event_objectevent_extensions`
--

DROP TABLE IF EXISTS `event_objectevent_extensions`;
CREATE TABLE `event_objectevent_extensions` (
  `id` bigint(20) NOT NULL auto_increment,
  `event_id` bigint(20) NOT NULL,
  `fieldname` varchar(128) NOT NULL,
  `prefix` varchar(32) NOT NULL,
  `intValue` int(11) default NULL,
  `floatValue` float default NULL,
  `dateValue` timestamp NULL default NULL,
  `strValue` varchar(1024) default NULL,
  PRIMARY KEY  (`id`),
  KEY `event_id` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`event_objectevent_extensions`
--

/*!40000 ALTER TABLE `event_objectevent_extensions` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_objectevent_extensions` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`event_quantityevent`
--

DROP TABLE IF EXISTS `event_quantityevent`;
CREATE TABLE `event_quantityevent` (
  `id` bigint(20) NOT NULL auto_increment,
  `eventTime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `recordTime` timestamp NOT NULL default '0000-00-00 00:00:00',
  `eventTimeZoneOffset` varchar(8) NOT NULL,
  `epcClass` bigint(20) NOT NULL,
  `quantity` bigint(20) NOT NULL,
  `bizStep` bigint(20) default NULL,
  `disposition` bigint(20) default NULL,
  `readPoint` bigint(20) default NULL,
  `bizLocation` bigint(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`event_quantityevent`
--

/*!40000 ALTER TABLE `event_quantityevent` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_quantityevent` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`event_quantityevent_biztrans`
--

DROP TABLE IF EXISTS `event_quantityevent_biztrans`;
CREATE TABLE `event_quantityevent_biztrans` (
  `event_id` bigint(20) NOT NULL,
  `bizTrans_id` bigint(20) NOT NULL,
  `idx` int(11) NOT NULL,
  KEY `event_id` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`event_quantityevent_biztrans`
--

/*!40000 ALTER TABLE `event_quantityevent_biztrans` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_quantityevent_biztrans` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`event_quantityevent_extensions`
--

DROP TABLE IF EXISTS `event_quantityevent_extensions`;
CREATE TABLE `event_quantityevent_extensions` (
  `id` bigint(20) NOT NULL auto_increment,
  `event_id` bigint(20) NOT NULL,
  `fieldname` varchar(128) NOT NULL,
  `prefix` varchar(32) NOT NULL,
  `intValue` int(11) default NULL,
  `floatValue` float default NULL,
  `dateValue` timestamp NULL default NULL,
  `strValue` varchar(1024) default NULL,
  PRIMARY KEY  (`id`),
  KEY `event_id` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`event_quantityevent_extensions`
--

/*!40000 ALTER TABLE `event_quantityevent_extensions` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_quantityevent_extensions` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`event_transactionevent`
--

DROP TABLE IF EXISTS `event_transactionevent`;
CREATE TABLE `event_transactionevent` (
  `id` bigint(20) NOT NULL auto_increment,
  `eventTime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `recordTime` timestamp NOT NULL default '0000-00-00 00:00:00',
  `eventTimeZoneOffset` varchar(8) NOT NULL,
  `parentID` varchar(1023) default NULL,
  `action` varchar(8) NOT NULL,
  `bizStep` bigint(20) default NULL,
  `disposition` bigint(20) default NULL,
  `readPoint` bigint(20) default NULL,
  `bizLocation` bigint(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`event_transactionevent`
--

/*!40000 ALTER TABLE `event_transactionevent` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_transactionevent` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`event_transactionevent_biztrans`
--

DROP TABLE IF EXISTS `event_transactionevent_biztrans`;
CREATE TABLE `event_transactionevent_biztrans` (
  `event_id` bigint(20) NOT NULL,
  `bizTrans_id` bigint(20) NOT NULL,
  `idx` int(11) NOT NULL,
  KEY `event_id` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`event_transactionevent_biztrans`
--

/*!40000 ALTER TABLE `event_transactionevent_biztrans` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_transactionevent_biztrans` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`event_transactionevent_epcs`
--

DROP TABLE IF EXISTS `event_transactionevent_epcs`;
CREATE TABLE `event_transactionevent_epcs` (
  `event_id` bigint(20) NOT NULL,
  `epc` varchar(1023) NOT NULL,
  `idx` int(11) NOT NULL,
  KEY `event_id` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`event_transactionevent_epcs`
--

/*!40000 ALTER TABLE `event_transactionevent_epcs` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_transactionevent_epcs` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`event_transactionevent_extensions`
--

DROP TABLE IF EXISTS `event_transactionevent_extensions`;
CREATE TABLE `event_transactionevent_extensions` (
  `id` bigint(20) NOT NULL auto_increment,
  `event_id` bigint(20) NOT NULL,
  `fieldname` varchar(128) NOT NULL,
  `prefix` varchar(32) NOT NULL,
  `intValue` int(11) default NULL,
  `floatValue` float default NULL,
  `dateValue` timestamp NULL default NULL,
  `strValue` varchar(1024) default NULL,
  PRIMARY KEY  (`id`),
  KEY `event_id` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`event_transactionevent_extensions`
--

/*!40000 ALTER TABLE `event_transactionevent_extensions` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_transactionevent_extensions` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`subscription`
--

DROP TABLE IF EXISTS `subscription`;
CREATE TABLE `subscription` (
  `subscriptionid` varchar(767) NOT NULL,
  `params` blob,
  `dest` varchar(1023) default NULL,
  `sched` blob,
  `trigg` varchar(1023) default NULL,
  `initialrecordingtime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `exportifempty` tinyint(1) default NULL,
  `queryname` varchar(1023) default NULL,
  `lastexecuted` timestamp NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (`subscriptionid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`subscription`
--

/*!40000 ALTER TABLE `subscription` DISABLE KEYS */;
/*!40000 ALTER TABLE `subscription` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`voc_any`
--

DROP TABLE IF EXISTS `voc_any`;
CREATE TABLE `voc_any` (
  `id` bigint(20) NOT NULL auto_increment,
  `uri` varchar(1023) NOT NULL,
  `vtype` varchar(1023) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`voc_any`
--

/*!40000 ALTER TABLE `voc_any` DISABLE KEYS */;
/*!40000 ALTER TABLE `voc_any` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`voc_any_attr`
--

DROP TABLE IF EXISTS `voc_any_attr`;
CREATE TABLE `voc_any_attr` (
  `id` bigint(20) NOT NULL,
  `attribute` varchar(1023) NOT NULL,
  `value` varchar(1023) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`voc_any_attr`
--

/*!40000 ALTER TABLE `voc_any_attr` DISABLE KEYS */;
/*!40000 ALTER TABLE `voc_any_attr` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`voc_bizloc`
--

DROP TABLE IF EXISTS `voc_bizloc`;
CREATE TABLE `voc_bizloc` (
  `id` bigint(20) NOT NULL auto_increment,
  `uri` varchar(1023) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`voc_bizloc`
--

/*!40000 ALTER TABLE `voc_bizloc` DISABLE KEYS */;
INSERT INTO `voc_bizloc` (`id`,`uri`) VALUES 
 (3,'urn:epcglobal:fmcg:loc:greece:pireus:mainacme'),
 (4,'urn:epcglobal:fmcg:loc:greece:pireus:mainacme,urn:epcglobal:fmcg:loc:acme:warehouse1'),
 (5,'urn:epcglobal:fmcg:loc:greece:pireus:mainacme,urn:epcglobal:fmcg:loc:acme:warehouse2'),
 (7,'urn:epcglobal:fmcg:loc:greece:pireus:mainacme,urn:epcglobal:fmcg:loc:acme:warehouse3');
/*!40000 ALTER TABLE `voc_bizloc` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`voc_bizloc_attr`
--

DROP TABLE IF EXISTS `voc_bizloc_attr`;
CREATE TABLE `voc_bizloc_attr` (
  `id` bigint(20) NOT NULL,
  `attribute` varchar(1023) NOT NULL,
  `value` varchar(1023) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`voc_bizloc_attr`
--

/*!40000 ALTER TABLE `voc_bizloc_attr` DISABLE KEYS */;
INSERT INTO `voc_bizloc_attr` (`id`,`attribute`,`value`) VALUES 
 (3,'urn:epcglobal:epcis:mda:Name','Acme'),
 (3,'urn:epcglobal:epcis:mda:Address','Akadimias 3'),
 (3,'urn:epcglobal:epcis:mda:City','Pireus'),
 (3,'urn:epcglobal:epcis:mda:Country','Greece'),
 (3,'urn:epcglobal:epcis:mda:Read Point','urn:epcglobal:fmcg:loc:45632.Warehouse1DocDoor_urn:epcglobal:fmcg:loc:06141.Warehouse2DocDoor_'),
 (4,'urn:epcglobal:epcis:mda:Name','AcmeWarehouse1'),
 (4,'urn:epcglobal:epcis:mda:Read Point','urn:epcglobal:fmcg:loc:45632.Warehouse1DocDoor_'),
 (5,'urn:epcglobal:epcis:mda:Name','AcmeWarehouse2'),
 (5,'urn:epcglobal:epcis:mda:Read Point','urn:epcglobal:fmcg:loc:06141.Warehouse2DocDoor'),
 (7,'urn:epcglobal:epcis:mda:Name','AcmeWarehouse3'),
 (7,'urn:epcglobal:epcis:mda:Read Point','urn:epcglobal:fmcg:loc:rp:warehouse3docdoor_');
/*!40000 ALTER TABLE `voc_bizloc_attr` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`voc_bizstep`
--

DROP TABLE IF EXISTS `voc_bizstep`;
CREATE TABLE `voc_bizstep` (
  `id` bigint(20) NOT NULL auto_increment,
  `uri` varchar(1023) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`voc_bizstep`
--

/*!40000 ALTER TABLE `voc_bizstep` DISABLE KEYS */;
INSERT INTO `voc_bizstep` (`id`,`uri`) VALUES 
 (1,'urn:epcglobal:fmcg:bizstep:receiving'),
 (2,'urn:epcglobal:fmcg:bizstep:picking'),
 (3,'urn:epcglobal:fmcg:bizstep:shipping'),
 (4,'urn:fosstrak:demo:bizstep:fmcg:shipment'),
 (5,'urn:fosstrak:demo:bizstep:fmcg:production'),
 (6,'urn:fosstrak:demo:bizstep:fmcg:accepting'),
 (7,'urn:fosstrak:demo:bizstep:fmcg:inspecting'),
 (8,'urn:fosstrak:demo:bizstep:fmcg:storing'),
 (9,'urn:fosstrak:demo:bizstep:fmcg:packing'),
 (10,'urn:fosstrak:demo:bizstep:fmcg:loading'),
 (11,'urn:fosstrak:demo:bizstep:fmcg:commissioning'),
 (12,'urn:fosstrak:demo:bizstep:fmcg:decommissioning'),
 (13,'urn:fosstrak:demo:bizstep:fmcg:destroying');
/*!40000 ALTER TABLE `voc_bizstep` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`voc_bizstep_attr`
--

DROP TABLE IF EXISTS `voc_bizstep_attr`;
CREATE TABLE `voc_bizstep_attr` (
  `id` bigint(20) NOT NULL,
  `attribute` varchar(1023) NOT NULL,
  `value` varchar(1023) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`voc_bizstep_attr`
--

/*!40000 ALTER TABLE `voc_bizstep_attr` DISABLE KEYS */;
INSERT INTO `voc_bizstep_attr` (`id`,`attribute`,`value`) VALUES 
 (1,'urn:epcglobal:epcis:mda:Name','receiving'),
 (2,'urn:epcglobal:epcis:mda:Name','picking'),
 (3,'urn:epcglobal:epcis:mda:Name','shipping'),
 (4,'urn:epcglobal:epcis:mda:Name','shipment'),
 (5,'urn:epcglobal:epcis:mda:Name','production'),
 (6,'urn:epcglobal:epcis:mda:Name','accepting'),
 (7,'urn:epcglobal:epcis:mda:Name','inspecting'),
 (8,'urn:epcglobal:epcis:mda:Name','storing'),
 (9,'urn:epcglobal:epcis:mda:Name','packing'),
 (10,'urn:epcglobal:epcis:mda:Name','loading'),
 (11,'urn:epcglobal:epcis:mda:Name','commissioning'),
 (12,'urn:epcglobal:epcis:mda:Name','decommissioning'),
 (13,'urn:epcglobal:epcis:mda:Name','destroying');
/*!40000 ALTER TABLE `voc_bizstep_attr` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`voc_biztrans`
--

DROP TABLE IF EXISTS `voc_biztrans`;
CREATE TABLE `voc_biztrans` (
  `id` bigint(20) NOT NULL auto_increment,
  `uri` varchar(1023) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`voc_biztrans`
--

/*!40000 ALTER TABLE `voc_biztrans` DISABLE KEYS */;
INSERT INTO `voc_biztrans` (`id`,`uri`) VALUES 
 (5,'urn:epcglobal:fmcg:bti:acmesupplying'),
 (6,'urn:epcglobal:fmcg:bte:acmewarehouse1receive'),
 (7,'urn:epc:id:gid:145.12.76427'),
 (8,'urn:epcglobal:fmcg:bte:acmewarehouse2ship');
/*!40000 ALTER TABLE `voc_biztrans` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`voc_biztrans_attr`
--

DROP TABLE IF EXISTS `voc_biztrans_attr`;
CREATE TABLE `voc_biztrans_attr` (
  `id` bigint(20) NOT NULL,
  `attribute` varchar(1023) NOT NULL,
  `value` varchar(1023) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`voc_biztrans_attr`
--

/*!40000 ALTER TABLE `voc_biztrans_attr` DISABLE KEYS */;
INSERT INTO `voc_biztrans_attr` (`id`,`attribute`,`value`) VALUES 
 (5,'urn:epcglobal:epcis:mda:Name','AcmeSupplyingTrans'),
 (6,'urn:epcglobal:epcis:mda:ecreport_names','bizTransactionIDs_1234,transactionItems_1234'),
 (5,'urn:epcglobal:epcis:mda:Children','urn:epcglobal:fmcg:bte:acmewarehouse1receive,urn:epcglobal:fmcg:bte:acmewarehouse2ship'),
 (6,'urn:epcglobal:epcis:mda:event_name','Warehouse1DocDoorReceive'),
 (6,'urn:epcglobal:epcis:mda:event_type','ObjectEvent'),
 (6,'urn:epcglobal:epcis:mda:business_step','urn:epcglobal:fmcg:bizstep:receiving'),
 (6,'urn:epcglobal:epcis:mda:business_location','urn:epcglobal:fmcg:loc:acme:warehouse1'),
 (6,'urn:epcglobal:epcis:mda:disposition','urn:epcglobal:fmcg:disp:in_progress'),
 (6,'urn:epcglobal:epcis:mda:ecspec_name','ECSpecObjectEventFiltering'),
 (6,'urn:epcglobal:epcis:mda:read_point','urn:epcglobal:fmcg:loc:45632.Warehouse1DocDoor'),
 (6,'urn:epcglobal:epcis:mda:transaction_type','urn:epcglobal:fmcg:btt:receiving'),
 (6,'urn:epcglobal:epcis:mda:action','ADD'),
 (8,'urn:epcglobal:epcis:mda:ecreport_names','transactionItems_6734,bizTransactionIDs_6734');
INSERT INTO `voc_biztrans_attr` (`id`,`attribute`,`value`) VALUES 
 (8,'urn:epcglobal:epcis:mda:event_name','Warehouse2DocDoorShipping'),
 (8,'urn:epcglobal:epcis:mda:event_type','ObjectEvent'),
 (8,'urn:epcglobal:epcis:mda:business_step','urn:epcglobal:fmcg:bizstep:shipping'),
 (8,'urn:epcglobal:epcis:mda:business_location','urn:epcglobal:fmcg:loc:acme:warehouse2'),
 (8,'urn:epcglobal:epcis:mda:disposition','urn:epcglobal:fmcg:disp:in_transit'),
 (8,'urn:epcglobal:epcis:mda:ecspec_name','ECSpecShipping'),
 (8,'urn:epcglobal:epcis:mda:read_point','urn:epcglobal:fmcg:loc:06141.Warehouse2DocDoor'),
 (8,'urn:epcglobal:epcis:mda:transaction_type','urn:epcglobal:fmcg:btt:shipping'),
 (8,'urn:epcglobal:epcis:mda:action','ADD');
/*!40000 ALTER TABLE `voc_biztrans_attr` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`voc_biztranstype`
--

DROP TABLE IF EXISTS `voc_biztranstype`;
CREATE TABLE `voc_biztranstype` (
  `id` bigint(20) NOT NULL auto_increment,
  `uri` varchar(1023) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`voc_biztranstype`
--

/*!40000 ALTER TABLE `voc_biztranstype` DISABLE KEYS */;
INSERT INTO `voc_biztranstype` (`id`,`uri`) VALUES 
 (2,'urn:epcglobal:fmcg:btt:shipping'),
 (3,'urn:epcglobal:fmcg:btt:receiving');
/*!40000 ALTER TABLE `voc_biztranstype` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`voc_biztranstype_attr`
--

DROP TABLE IF EXISTS `voc_biztranstype_attr`;
CREATE TABLE `voc_biztranstype_attr` (
  `id` bigint(20) NOT NULL,
  `attribute` varchar(1023) NOT NULL,
  `value` varchar(1023) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`voc_biztranstype_attr`
--

/*!40000 ALTER TABLE `voc_biztranstype_attr` DISABLE KEYS */;
INSERT INTO `voc_biztranstype_attr` (`id`,`attribute`,`value`) VALUES 
 (2,'urn:epcglobal:epcis:mda:Name','shipping'),
 (3,'urn:epcglobal:epcis:mda:Name','receiving');
/*!40000 ALTER TABLE `voc_biztranstype_attr` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`voc_disposition`
--

DROP TABLE IF EXISTS `voc_disposition`;
CREATE TABLE `voc_disposition` (
  `id` bigint(20) NOT NULL auto_increment,
  `uri` varchar(1023) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`voc_disposition`
--

/*!40000 ALTER TABLE `voc_disposition` DISABLE KEYS */;
INSERT INTO `voc_disposition` (`id`,`uri`) VALUES 
 (1,'urn:epcglobal:fmcg:disp:active'),
 (2,'urn:epcglobal:fmcg:disp:inactive'),
 (3,'urn:epcglobal:fmcg:disp:reserved'),
 (4,'urn:epcglobal:fmcg:disp:encoded'),
 (5,'urn:epcglobal:fmcg:disp:in_transit'),
 (6,'urn:epcglobal:fmcg:disp:non_sellable'),
 (7,'urn:epcglobal:fmcg:disp:in_progress'),
 (8,'urn:epcglobal:fmcg:disp:sold');
/*!40000 ALTER TABLE `voc_disposition` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`voc_disposition_attr`
--

DROP TABLE IF EXISTS `voc_disposition_attr`;
CREATE TABLE `voc_disposition_attr` (
  `id` bigint(20) NOT NULL,
  `attribute` varchar(1023) NOT NULL,
  `value` varchar(1023) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`voc_disposition_attr`
--

/*!40000 ALTER TABLE `voc_disposition_attr` DISABLE KEYS */;
INSERT INTO `voc_disposition_attr` (`id`,`attribute`,`value`) VALUES 
 (1,'urn:epcglobal:epcis:mda:Name','active'),
 (2,'urn:epcglobal:epcis:mda:Name','inactive'),
 (3,'urn:epcglobal:epcis:mda:Name','reserved'),
 (4,'urn:epcglobal:epcis:mda:Name','encoded'),
 (5,'urn:epcglobal:epcis:mda:Name','in_transit'),
 (6,'urn:epcglobal:epcis:mda:Name','non_sellable'),
 (7,'urn:epcglobal:epcis:mda:Name','in_progress'),
 (8,'urn:epcglobal:epcis:mda:Name','sold');
/*!40000 ALTER TABLE `voc_disposition_attr` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`voc_epcclass`
--

DROP TABLE IF EXISTS `voc_epcclass`;
CREATE TABLE `voc_epcclass` (
  `id` bigint(20) NOT NULL auto_increment,
  `uri` varchar(1023) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`voc_epcclass`
--

/*!40000 ALTER TABLE `voc_epcclass` DISABLE KEYS */;
/*!40000 ALTER TABLE `voc_epcclass` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`voc_epcclass_attr`
--

DROP TABLE IF EXISTS `voc_epcclass_attr`;
CREATE TABLE `voc_epcclass_attr` (
  `id` bigint(20) NOT NULL,
  `attribute` varchar(1023) NOT NULL,
  `value` varchar(1023) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`voc_epcclass_attr`
--

/*!40000 ALTER TABLE `voc_epcclass_attr` DISABLE KEYS */;
/*!40000 ALTER TABLE `voc_epcclass_attr` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`voc_readpoint`
--

DROP TABLE IF EXISTS `voc_readpoint`;
CREATE TABLE `voc_readpoint` (
  `id` bigint(20) NOT NULL auto_increment,
  `uri` varchar(1023) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`voc_readpoint`
--

/*!40000 ALTER TABLE `voc_readpoint` DISABLE KEYS */;
INSERT INTO `voc_readpoint` (`id`,`uri`) VALUES 
 (1,'urn:epcglobal:fmcg:loc:45632.Warehouse1DocDoor'),
 (2,'urn:epcglobal:fmcg:loc:06141.Warehouse2DocDoor'),
 (3,'urn:epcglobal:fmcg:loc:rp:warehouse3docdoor');
/*!40000 ALTER TABLE `voc_readpoint` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`voc_readpoint_attr`
--

DROP TABLE IF EXISTS `voc_readpoint_attr`;
CREATE TABLE `voc_readpoint_attr` (
  `id` bigint(20) NOT NULL,
  `attribute` varchar(1023) NOT NULL,
  `value` varchar(1023) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`voc_readpoint_attr`
--

/*!40000 ALTER TABLE `voc_readpoint_attr` DISABLE KEYS */;
INSERT INTO `voc_readpoint_attr` (`id`,`attribute`,`value`) VALUES 
 (1,'urn:epcglobal:epcis:mda:Name','Warehouse1DocDoor'),
 (2,'urn:epcglobal:epcis:mda:Name','Warehouse2DocDoor'),
 (3,'urn:epcglobal:epcis:mda:Name','Warehouse3DocDoor');
/*!40000 ALTER TABLE `voc_readpoint_attr` ENABLE KEYS */;


--
-- Table structure for table `epcis`.`vocabularies`
--

DROP TABLE IF EXISTS `vocabularies`;
CREATE TABLE `vocabularies` (
  `id` bigint(20) NOT NULL auto_increment,
  `uri` varchar(1023) NOT NULL,
  `table_name` varchar(128) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `epcis`.`vocabularies`
--

/*!40000 ALTER TABLE `vocabularies` DISABLE KEYS */;
INSERT INTO `vocabularies` (`id`,`uri`,`table_name`) VALUES 
 (1,'urn:epcglobal:epcis:vtype:BusinessStep','BizStep'),
 (2,'urn:epcglobal:epcis:vtype:BusinessTransaction','BizTrans'),
 (3,'urn:epcglobal:epcis:vtype:Disposition','Disposition'),
 (4,'urn:epcglobal:epcis:vtype:ReadPoint','ReadPoint'),
 (5,'urn:epcglobal:epcis:vtype:BusinessLocation','BizLoc');
/*!40000 ALTER TABLE `vocabularies` ENABLE KEYS */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;