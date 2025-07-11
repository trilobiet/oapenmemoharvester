
ALTER TABLE `oapen_library`.`title`
ADD COLUMN `year_issued` INT NULL DEFAULT NULL AFTER `series_number`, 
ADD COLUMN `date_accessioned` DATE NULL DEFAULT NULL AFTER `year_issued`,
ADD COLUMN `date_available` DATE NULL DEFAULT NULL AFTER `date_accessioned`;

