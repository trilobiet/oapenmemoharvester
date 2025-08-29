
# Drop obsolete tables
DROP TABLE `oapen_library`.`export_chunk`;

# Add extra date columns
ALTER TABLE `oapen_library`.`title`
ADD COLUMN `year_issued` INT NULL DEFAULT NULL AFTER `series_number`, 
ADD COLUMN `date_accessioned` DATE NULL DEFAULT NULL AFTER `year_issued`,
ADD COLUMN `date_available` DATE NULL DEFAULT NULL AFTER `date_accessioned`;

# Increase identifier size (URIs can be > 100 chars)
ALTER TABLE `oapen_library`.`identifier` 
CHANGE COLUMN `identifier` `identifier` VARCHAR(255) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_bin' NOT NULL ;

# New table peerreview
CREATE TABLE peerreview (
    id CHAR(36) NOT NULL,
    handle_title VARCHAR(25) NOT NULL,
    title VARCHAR(255),
    anonymity VARCHAR(100),
    is_open_review BOOLEAN,
    responsibilities TEXT,
    stage VARCHAR(255),
    type VARCHAR(255),
    reviewer_type TEXT,
    comments TEXT,
    PRIMARY KEY (id),
    CONSTRAINT `FK_peerreview__handle_title` FOREIGN KEY (handle_title) REFERENCES title(handle)
);
