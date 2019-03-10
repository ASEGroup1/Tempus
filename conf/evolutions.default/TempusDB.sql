-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema tempus
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema tempus
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `tempus` DEFAULT CHARACTER SET utf8 ;
USE `tempus` ;

-- -----------------------------------------------------
-- Table `tempus`.`Person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`Person` (
  `PersonID` INT NOT NULL,
  `FirstName` VARCHAR(45) NULL,
  `LastName` VARCHAR(45) NULL,
  `OtherNames` VARCHAR(45) NULL,
  PRIMARY KEY (`PersonID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`Staff`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`Staff` (
  `StaffID` INT NOT NULL,
  `PersonID` INT NULL,
  `JobTitle` VARCHAR(45) NULL,
  PRIMARY KEY (`StaffID`),
  INDEX `PersonID_idx` (`PersonID` ASC) VISIBLE,
  CONSTRAINT `PersonFK`
    FOREIGN KEY (`PersonID`)
    REFERENCES `tempus`.`Person` (`PersonID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


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
  INDEX `StaffID_idx` (`AcademicAdvisorID` ASC) VISIBLE,
  INDEX `PersonID_idx` (`PersonID` ASC) VISIBLE,
  CONSTRAINT `AcademicAdvisorStaffFK`
    FOREIGN KEY (`AcademicAdvisorID`)
    REFERENCES `tempus`.`Staff` (`StaffID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `PersonFK2`
    FOREIGN KEY (`PersonID`)
    REFERENCES `tempus`.`Person` (`PersonID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`Building`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`Building` (
  `BuildingID` INT NOT NULL,
  `BuildingName` VARCHAR(45) NULL,
  PRIMARY KEY (`BuildingID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`School`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`School` (
  `SchoolID` INT NOT NULL,
  `SchoolName` VARCHAR(45) NULL,
  `MainBuildingID` INT NULL,
  PRIMARY KEY (`SchoolID`),
  INDEX `MainBuildingID_idx` (`MainBuildingID` ASC) VISIBLE,
  CONSTRAINT `MainBuildingFK`
    FOREIGN KEY (`MainBuildingID`)
    REFERENCES `tempus`.`Building` (`BuildingID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '	';


-- -----------------------------------------------------
-- Table `tempus`.`Course`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`Course` (
  `CourseID` INT NOT NULL,
  `CourseName` VARCHAR(45) NULL,
  `SchoolID` INT NULL,
  PRIMARY KEY (`CourseID`),
  INDEX `SchoolID_idx` (`SchoolID` ASC) VISIBLE,
  CONSTRAINT `SchoolFK`
    FOREIGN KEY (`SchoolID`)
    REFERENCES `tempus`.`School` (`SchoolID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`ModuleRole`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`ModuleRole` (
  `ModuleRoleID` INT NOT NULL,
  `RoleName` VARCHAR(45) NULL,
  `RoleDescription` VARCHAR(2000) NULL,
  PRIMARY KEY (`ModuleRoleID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`ModuleTimingConstraint`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`ModuleTimingConstraint` (
  `ModuleTimingConstraintID` INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ModuleTimingConstraintID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`Module`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`Module` (
  `ModuleID` INT NOT NULL,
  `ModuleCode` VARCHAR(45) NULL,
  `ModuleName` VARCHAR(45) NULL,
  `ModuleDescription` VARCHAR(2000) NULL,
  `SchoolID` INT NULL,
  `ModuleTimingConstraintID` INT NULL,
  PRIMARY KEY (`ModuleID`),
  INDEX `SchoolID_idx` (`SchoolID` ASC) VISIBLE,
  INDEX `ModuleTimingConstraintFK_idx` (`ModuleTimingConstraintID` ASC) VISIBLE,
  CONSTRAINT `SchoolFK2`
    FOREIGN KEY (`SchoolID`)
    REFERENCES `tempus`.`School` (`SchoolID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ModuleTimingConstraintFK`
    FOREIGN KEY (`ModuleTimingConstraintID`)
    REFERENCES `tempus`.`ModuleTimingConstraint` (`ModuleTimingConstraintID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`ModuleFEHQLevel`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`ModuleFEHQLevel` (
  `ModuleFEHQLevelID` INT NOT NULL AUTO_INCREMENT,
  `ModuleID` INT NOT NULL,
  `FEHQLevel` TINYINT NOT NULL,
  PRIMARY KEY (`ModuleFEHQLevelID`),
  CONSTRAINT `ModuleFK3`
    FOREIGN KEY (`ModuleID`)
    REFERENCES `tempus`.`Module` (`ModuleID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'This allows you to have multiple levels for a module I.E. if a module is both undergrad and masters';


-- -----------------------------------------------------
-- Table `tempus`.`ModulePerson`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`ModulePerson` (
  `ModuleFEHQID` INT NOT NULL,
  `PersonID` INT NOT NULL,
  `ModuleRoleID` INT NOT NULL,
  `ModuleCompleted` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`ModuleFEHQID`, `PersonID`, `ModuleRoleID`),
  INDEX `PersonID_idx` (`PersonID` ASC) VISIBLE,
  INDEX `ModuleRoleID_idx` (`ModuleRoleID` ASC) VISIBLE,
  CONSTRAINT `ModuleFEHQLevelFK4`
    FOREIGN KEY (`ModuleFEHQID`)
    REFERENCES `tempus`.`ModuleFEHQLevel` (`ModuleFEHQLevelID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `PersonFK3`
    FOREIGN KEY (`PersonID`)
    REFERENCES `tempus`.`Person` (`PersonID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ModuleRoleFK`
    FOREIGN KEY (`ModuleRoleID`)
    REFERENCES `tempus`.`ModuleRole` (`ModuleRoleID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`CourseRole`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`CourseRole` (
  `CourseRoleID` INT NOT NULL,
  `CourseRoleName` VARCHAR(45) NOT NULL,
  `CourseRoleDescription` VARCHAR(2000) NULL,
  PRIMARY KEY (`CourseRoleID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`CoursePerson`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`CoursePerson` (
  `CourseID` INT NOT NULL,
  `PersonID` INT NOT NULL,
  `CourseRoleID` INT NOT NULL,
  PRIMARY KEY (`CourseID`, `PersonID`, `CourseRoleID`),
  INDEX `PersonID_idx` (`PersonID` ASC) VISIBLE,
  INDEX `CourseRoleID_idx` (`CourseRoleID` ASC) VISIBLE,
  CONSTRAINT `CourseFK1`
    FOREIGN KEY (`CourseID`)
    REFERENCES `tempus`.`Course` (`CourseID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `PersonFK4`
    FOREIGN KEY (`PersonID`)
    REFERENCES `tempus`.`Person` (`PersonID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `CourseRoleFK`
    FOREIGN KEY (`CourseRoleID`)
    REFERENCES `tempus`.`CourseRole` (`CourseRoleID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`BuildingFloor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`BuildingFloor` (
  `FloorID` INT NOT NULL,
  `BuildingID` INT NULL,
  `FloorLevel` INT NULL COMMENT '0 = Ground',
  PRIMARY KEY (`FloorID`),
  INDEX `BuildingID_idx` (`BuildingID` ASC) VISIBLE,
  CONSTRAINT `BuildingFK`
    FOREIGN KEY (`BuildingID`)
    REFERENCES `tempus`.`Building` (`BuildingID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`RoomType`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`RoomType` (
  `RoomTypeID` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `Description` VARCHAR(2000) NULL,
  PRIMARY KEY (`RoomTypeID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`Room`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`Room` (
  `RoomID` INT NOT NULL,
  `RoomName` INT NULL,
  `RoomTypeID` INT NULL,
  `RoomCapacity` INT NULL,
  `FloorID` INT NULL,
  PRIMARY KEY (`RoomID`),
  INDEX `RoomType_idx` (`RoomTypeID` ASC) VISIBLE,
  INDEX `FloorFK_idx` (`FloorID` ASC) VISIBLE,
  CONSTRAINT `RoomTypeFK`
    FOREIGN KEY (`RoomTypeID`)
    REFERENCES `tempus`.`RoomType` (`RoomTypeID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FloorFK`
    FOREIGN KEY (`FloorID`)
    REFERENCES `tempus`.`BuildingFloor` (`FloorID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`ModulePrerequisite`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`ModulePrerequisite` (
  `MainModuleFEHQLevelID` INT NOT NULL,
  `PrerequisiteModuleFEHQLevelID` INT NOT NULL,
  PRIMARY KEY (`MainModuleFEHQLevelID`, `PrerequisiteModuleFEHQLevelID`),
  INDEX `PrerequisiteModuleFK_idx` (`PrerequisiteModuleFEHQLevelID` ASC) VISIBLE,
  CONSTRAINT `MainModuleFK`
    FOREIGN KEY (`MainModuleFEHQLevelID`)
    REFERENCES `tempus`.`Module` (`ModuleID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `PrerequisiteModuleFK`
    FOREIGN KEY (`PrerequisiteModuleFEHQLevelID`)
    REFERENCES `tempus`.`Module` (`ModuleID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`ModuleTimeTable`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`ModuleTimeTable` (
  `ModuleTimeTableID` INT NOT NULL AUTO_INCREMENT,
  `TermID` INT NULL,
  `ModuleID` INT NULL,
  PRIMARY KEY (`ModuleTimeTableID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`AcademicYear`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`AcademicYear` (
  `AcademicYearID` INT NOT NULL,
  `YearCode` VARCHAR(45) NULL,
  `StartDate` DATE NULL,
  `EndDate` DATE NULL,
  PRIMARY KEY (`AcademicYearID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`Term`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`Term` (
  `TermID` INT NOT NULL,
  `AcademicYearID` INT NULL,
  `TermNumber` INT NULL,
  `TermStartDate` DATE NULL,
  `TermEndDate` DATE NULL,
  PRIMARY KEY (`TermID`),
  INDEX `AcademicYearID_idx` (`AcademicYearID` ASC) VISIBLE,
  CONSTRAINT `AcademicYearID`
    FOREIGN KEY (`AcademicYearID`)
    REFERENCES `tempus`.`AcademicYear` (`AcademicYearID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`RoomPartition`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`RoomPartition` (
  `RoomPartitionID` INT NOT NULL,
  `RoomID` INT NULL,
  `RoomPartitionName` VARCHAR(45) NULL,
  `RoomPartitionCapacity` INT NULL,
  `RoomTypeID` INT NULL,
  PRIMARY KEY (`RoomPartitionID`),
  INDEX `RoomFK_idx` (`RoomID` ASC) VISIBLE,
  INDEX `RoomTypeFK_idx` (`RoomTypeID` ASC) VISIBLE,
  CONSTRAINT `RoomFK`
    FOREIGN KEY (`RoomID`)
    REFERENCES `tempus`.`Room` (`RoomID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `RoomTypeFK2`
    FOREIGN KEY (`RoomTypeID`)
    REFERENCES `tempus`.`RoomType` (`RoomTypeID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`RoomFeature`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`RoomFeature` (
  `RoomFeatureID` INT NOT NULL,
  `RoomFeatureName` VARCHAR(45) NULL,
  `RoomFeatureDescription` VARCHAR(2000) NULL,
  PRIMARY KEY (`RoomFeatureID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`RoomPartitionFeature`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`RoomPartitionFeature` (
  `RoomPartitionID` INT NOT NULL,
  `RoomFeatureID` INT NOT NULL,
  PRIMARY KEY (`RoomPartitionID`, `RoomFeatureID`),
  INDEX `RoomFeatureFK_idx` (`RoomFeatureID` ASC) VISIBLE,
  CONSTRAINT `RoomPartitionFK`
    FOREIGN KEY (`RoomPartitionID`)
    REFERENCES `tempus`.`RoomPartition` (`RoomPartitionID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `RoomFeatureFK`
    FOREIGN KEY (`RoomFeatureID`)
    REFERENCES `tempus`.`RoomFeature` (`RoomFeatureID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`ModuleCourseAvaiability`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`ModuleCourseAvaiability` (
  `ModuleCourseAvaiabilityID` INT NOT NULL,
  `ModuleFEHQLevelID` INT NULL,
  `CourseID` INT NULL,
  PRIMARY KEY (`ModuleCourseAvaiabilityID`),
  INDEX `CourseFK_idx` (`CourseID` ASC) VISIBLE,
  INDEX `ModuleFEHQLevelFK_idx` (`ModuleFEHQLevelID` ASC) VISIBLE,
  CONSTRAINT `CourseFK2`
    FOREIGN KEY (`CourseID`)
    REFERENCES `tempus`.`Course` (`CourseID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ModuleFEHQLevelFK2`
    FOREIGN KEY (`ModuleFEHQLevelID`)
    REFERENCES `tempus`.`ModuleFEHQLevel` (`ModuleFEHQLevelID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`TermPeriod`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`TermPeriod` (
  `TermPeriodID` INT NOT NULL COMMENT 'Period for repeting I.E. weeks',
  `TermID` INT NULL,
  `TermPeriodNo` INT NULL,
  `TermPeriodStart` DATE NULL,
  `TermPeriodEnd` DATE NULL,
  PRIMARY KEY (`TermPeriodID`),
  INDEX `TermFK_idx` (`TermID` ASC) VISIBLE,
  CONSTRAINT `TermFK`
    FOREIGN KEY (`TermID`)
    REFERENCES `tempus`.`Term` (`TermID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`ModuleSessionType`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`ModuleSessionType` (
  `ModuleSessionTypeID` INT NOT NULL,
  `MoudleSessionTypeName` VARCHAR(45) NULL,
  `ModuleSessionTypeDescription` VARCHAR(2000) NULL,
  `ModuleSessionTypecol` VARCHAR(45) NULL,
  PRIMARY KEY (`ModuleSessionTypeID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`ModuleSessionStructure`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`ModuleSessionStructure` (
  `ModuleSessionStructureID` INT NOT NULL,
  `ModuleFEHQLevelID` INT NULL,
  `WeekNo` INT NULL,
  `ModuleSessionTypeID` INT NULL,
  `NoSessionsRequired` INT NULL,
  `MaxSessionSize` INT NULL COMMENT 'In terms of number of students',
  PRIMARY KEY (`ModuleSessionStructureID`),
  INDEX `ModuleFEHQLevelFK_idx` (`ModuleFEHQLevelID` ASC) VISIBLE,
  INDEX `ModuleSessionTypeFK_idx` (`ModuleSessionTypeID` ASC) VISIBLE,
  CONSTRAINT `ModuleFEHQLevelFK3`
    FOREIGN KEY (`ModuleFEHQLevelID`)
    REFERENCES `tempus`.`ModuleFEHQLevel` (`ModuleFEHQLevelID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ModuleSessionTypeFK`
    FOREIGN KEY (`ModuleSessionTypeID`)
    REFERENCES `tempus`.`ModuleSessionType` (`ModuleSessionTypeID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`ModuleTermNumber`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`ModuleTermNumber` (
  `ModuleID` INT NOT NULL,
  `TermNumber` INT NOT NULL COMMENT 'Terms which the module is added to by default can contain multiple terms',
  PRIMARY KEY (`ModuleID`, `TermNumber`),
  CONSTRAINT `ModuleFK2`
    FOREIGN KEY (`ModuleID`)
    REFERENCES `tempus`.`Module` (`ModuleID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`ModuleDayTimingConstraint`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`ModuleDayTimingConstraint` (
  `ModuleTimingConstraintID` INT NOT NULL,
  `TimePeriodDayNo` INT NOT NULL,
  `DayTimingConstraintID` INT NULL,
  PRIMARY KEY (`ModuleTimingConstraintID`, `TimePeriodDayNo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`TimePeriod`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`TimePeriod` (
  `TimePeriodID` INT NOT NULL,
  `StartTime` TIME NULL,
  `EndTime` TIME NULL,
  PRIMARY KEY (`TimePeriodID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`DayTimingConstraint`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`DayTimingConstraint` (
  `ModuleDayTimingConstraintID` INT NOT NULL,
  `TimePeriodAvailabilityID` INT NULL,
  PRIMARY KEY (`ModuleDayTimingConstraintID`),
  INDEX `TimePeriodAvailabilityFK_idx` (`TimePeriodAvailabilityID` ASC) VISIBLE,
  CONSTRAINT `ModuleDayTimingConstraintFK`
    FOREIGN KEY (`ModuleDayTimingConstraintID`)
    REFERENCES `tempus`.`ModuleDayTimingConstraint` (`ModuleTimingConstraintID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `TimePeriodAvailabilityFK`
    FOREIGN KEY (`TimePeriodAvailabilityID`)
    REFERENCES `tempus`.`TimePeriodAvailability` (`TimePeriodAvailabilityID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`TimePeriodAvailability`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`TimePeriodAvailability` (
  `TimePeriodAvailabilityID` INT NOT NULL,
  `DayTimingConstraintID` INT NULL,
  `TimePeriodID` INT NULL,
  `Available` TINYINT NULL COMMENT 'Treat as Boolean',
  PRIMARY KEY (`TimePeriodAvailabilityID`),
  INDEX `DayTimingConstraintFK_idx` (`DayTimingConstraintID` ASC) VISIBLE,
  INDEX `TimePeriodFK_idx` (`TimePeriodID` ASC) VISIBLE,
  CONSTRAINT `DayTimingConstraintFK`
    FOREIGN KEY (`DayTimingConstraintID`)
    REFERENCES `tempus`.`DayTimingConstraint` (`ModuleDayTimingConstraintID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `TimePeriodFK`
    FOREIGN KEY (`TimePeriodID`)
    REFERENCES `tempus`.`TimePeriod` (`TimePeriodID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tempus`.`ModuleTimeTableSession`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`ModuleTimeTableSession` (
  `ModuleTimeTableSessionID` INT NOT NULL,
  `ModuleSessionTypeID` INT NULL,
  `TimePeriodID` INT NULL,
  `DayNo` INT NULL,
  PRIMARY KEY (`ModuleTimeTableSessionID`),
  INDEX `ModuleSessionTypeFK_idx` (`ModuleSessionTypeID` ASC) VISIBLE,
  INDEX `TimePeriodFK_idx` (`TimePeriodID` ASC) VISIBLE,
  CONSTRAINT `ModuleSessionTypeFK2`
    FOREIGN KEY (`ModuleSessionTypeID`)
    REFERENCES `tempus`.`ModuleSessionType` (`ModuleSessionTypeID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `TimePeriodFK2`
    FOREIGN KEY (`TimePeriodID`)
    REFERENCES `tempus`.`TimePeriod` (`TimePeriodID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
