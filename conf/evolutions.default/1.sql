
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
			ON DELETE CASCADE
			ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Table `tempus`.`Student`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`Student` (
  `StudentID` INT NOT NULL,
  `CourseID` INT NULL,
  `CurrentFEHQLevelCompleted` TINYINT NULL,
  `AcademicAdvisorID` INT NULL,
  `PersonID` INT NULL,
  PRIMARY KEY (`StudentID`),
  CONSTRAINT `AcademicAdvisorFK`
    FOREIGN KEY (`AcademicAdvisorID`)
    REFERENCES `tempus`.`Staff` (`StaffID`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `StudentPersonFK`
    FOREIGN KEY (`PersonID`)
    REFERENCES `tempus`.`Person` (`PersonID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

	-- -----------------------------------------------------
-- Table `tempus`.`Building`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`Building` (
  `BuildingID` INT NOT NULL,
  `BuildingName` VARCHAR(45) NULL,
  PRIMARY KEY (`BuildingID`));

# --- !Downs

DROP TABLE `tempus`.`Person`;
DROP TABLE `tempus`.`Staff`;
DROP TABLE `tempus`.`Student`;
