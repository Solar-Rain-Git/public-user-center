DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    username     VARCHAR(255)                       NULL COMMENT '昵称',
    userAccount  VARCHAR(255)                       NULL COMMENT '登录账号',
    avatarUrl    VARCHAR(1024)                      NULL COMMENT '头像URL',
    gender       TINYINT                            NULL COMMENT '性别 (0: 未知, 1: 男, 2: 女)',
    userPassword VARCHAR(512)                       NOT NULL COMMENT '密码',
    phone        VARCHAR(128)                       NULL COMMENT '电话',
    email        VARCHAR(512)                       NULL COMMENT '邮箱',
    userStatus   INT      DEFAULT 0                 NOT NULL COMMENT '用户状态 (0: 正常, 1: 禁用)',
    createTime   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime   DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    isDelete     TINYINT  DEFAULT 0                 NOT NULL COMMENT '是否删除 (0: 否, 1: 是)',
    userRole     TINYINT  DEFAULT 0                 NOT NULL COMMENT '用户角色 (0: 普通用户, 1: 管理员)'
) COMMENT '用户信息表';