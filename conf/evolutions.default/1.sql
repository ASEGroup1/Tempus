# User schema

# --- !Ups

-- -----------------------------------------------------
-- Table `tempus`.`Person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`Person`
(
	`PersonID`   INT         NOT NULL,
	`FirstName`  VARCHAR(45) NULL,
	`LastName`   VARCHAR(45) NULL,
	`OtherNames` VARCHAR(45) NULL,
	PRIMARY KEY (`PersonID`)
);

-- -----------------------------------------------------
-- Table `tempus`.`Staff`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`Staff`
(
	`StaffID`  INT         NOT NULL,
	`PersonID` INT         NULL,
	`JobTitle` VARCHAR(45) NULL,
	PRIMARY KEY (`StaffID`),
	CONSTRAINT `StaffPersonFK`
		FOREIGN KEY (`PersonID`)
			REFERENCES `tempus`.`Person` (`PersonID`)
			ON DELETE NO ACTION
			ON UPDATE NO ACTION
);

# --- !Downs

drop table `tempus`.`Person`;
