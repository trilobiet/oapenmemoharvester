
# Add extra funder columns
ALTER TABLE `doab_library`.`funder`
ADD COLUMN `doi` TEXT NULL DEFAULT NULL; 


# reverse relation peerreview - title
ALTER TABLE `doab_library`.`peerreview` 
DROP FOREIGN KEY `FK_peerreview__handle_title`;

ALTER TABLE `doab_library`.`peerreview` 
DROP COLUMN `handle_title`,
DROP INDEX `handle_title` ;

ALTER TABLE `doab_library`.`peerreview` 
CHANGE COLUMN `title` `title` TEXT 
  CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_bin' NULL DEFAULT NULL ;

ALTER TABLE `doab_library`.`title` 
ADD COLUMN `id_peerreview` VARCHAR(36) NULL DEFAULT NULL AFTER `year_issued`;

ALTER TABLE `doab_library`.`title` 
ADD CONSTRAINT `Fk_title__id_peerreview`
  FOREIGN KEY (`id_peerreview`)
  REFERENCES `doab_library`.`peerreview`(`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;




