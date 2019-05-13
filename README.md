# pushForJava
配合 pushForAndroid 编写的服务端的 java 实现代码

需要配合有数据库表使用

记录 push 的日志

DROP TABLE IF EXISTS `socket_send_record`;
CREATE TABLE `socket_send_record`  (
  `uid` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `time_create` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0)
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

记录未发送成功的消息

DROP TABLE IF EXISTS `unsend_record`;
CREATE TABLE `unsend_record`  (
  `number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `uid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `time_create` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `state` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`number`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
