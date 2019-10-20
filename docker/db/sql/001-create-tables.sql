---- drop ----
DROP TABLE IF EXISTS `todos`;

DROP TABLE IF EXISTS `users`;

---- create ----
create table IF not exists `users`
(
 `id`               BIGINT UNSIGNED AUTO_INCREMENT,
 `name`             VARCHAR(20) NOT NULL,
 `email`            VARCHAR(100) NOT NULL,
 `password`         VARCHAR(64)  NOT NULL,
 `salt`             VARCHAR(30)  NOT NULL,
 `created_at`       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 `updated_at`       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

create table IF not exists `todos`
(
 `id`               INT UNSIGNED unsigned AUTO_INCREMENT,
 `user_id`          BIGINT UNSIGNED,
 `title`            VARCHAR(100),
 `content`          TEXT,
 `dead_line_at`     TIMESTAMP NOT NULL,
 `completed_at`     TIMESTAMP NULL DEFAULT NULL,
 `created_at`       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 `updated_at`       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY user_id_index(`user_id`) REFERENCES users(`id`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

