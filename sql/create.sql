CREATE DATABASE one;
CREATE TABLE `table_user` (
  `user_name` varchar(20) NOT NULL,
  `user_password` char(6) NOT NULL,
  `freeze_time` char(19) DEFAULT NULL,
  `card` char(14) NOT NULL,
  `money` decimal(8,3) NOT NULL,
  `sex` varchar(10) NOT NULL,
  `age` int(3) NOT NULL,
  `user_id` varchar(18) NOT NULL,
  `error_times` int(1) NOT NULL,
  PRIMARY KEY (`card`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `table_record` (
  `record_number` char(14) NOT NULL,
  `card` char(14) NOT NULL,
  `money` decimal(8,3) NOT NULL,
  `time` char(19) NOT NULL,
  PRIMARY KEY (`record_number`),
  KEY `card` (`card`),
  CONSTRAINT `table_record_ibfk_1` FOREIGN KEY (`card`) REFERENCES `table_user` (`card`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8