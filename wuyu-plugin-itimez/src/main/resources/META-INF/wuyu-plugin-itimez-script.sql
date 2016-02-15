--
-- index time of channel db dependency
--
CREATE TABLE `tb_itime_zone` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `channel` varchar(40) NOT NULL DEFAULT 'channel ',
  `latestIndexTime` bigint(20) NOT NULL DEFAULT '0' COMMENT 'latestIndexTime',
  `latestIndexDate` datetime DEFAULT NULL,
  `remark` varchar(40) DEFAULT 'remark',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_channel` (`channel`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;