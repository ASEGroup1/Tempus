
# --- !Ups

-- -----------------------------------------------------
-- Table `tempus`.`Person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`Person`
(
	`PersonID`   INT         NOT NULL AUTO_INCREMENT,
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
	`StaffID`  INT         NOT NULL AUTO_INCREMENT,
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
  `StudentID` INT NOT NULL AUTO_INCREMENT,
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
  `BuildingID` INT NOT NULL AUTO_INCREMENT,
  `BuildingName` VARCHAR(45) NULL,
  PRIMARY KEY (`BuildingID`));


  -- -----------------------------------------------------
-- Table `tempus`.`School`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`School` (
  `SchoolID` INT NOT NULL AUTO_INCREMENT,
  `SchoolName` VARCHAR(45) NULL,
  `MainBuildingID` INT NULL,
  PRIMARY KEY (`SchoolID`),
  CONSTRAINT `SchoolMainBuildingFK`
    FOREIGN KEY (`MainBuildingID`)
    REFERENCES `tempus`.`Building` (`BuildingID`)
    ON DELETE SET NULL
    ON UPDATE CASCADE);

	-- -----------------------------------------------------
-- Table `tempus`.`Course`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`Course` (
  `CourseID` INT NOT NULL AUTO_INCREMENT,
  `CourseName` VARCHAR(45) NULL,
  `SchoolID` INT NULL,
  PRIMARY KEY (`CourseID`),
  CONSTRAINT `CourseSchoolFK`
    FOREIGN KEY (`SchoolID`)
    REFERENCES `tempus`.`School` (`SchoolID`)
    ON DELETE SET NULL
    ON UPDATE CASCADE);

	-- -----------------------------------------------------
-- Table `tempus`.`ModuleRole`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`ModuleRole` (
  `ModuleRoleID` INT NOT NULL AUTO_INCREMENT,
  `RoleName` VARCHAR(45) NULL,
  `RoleDescription` VARCHAR(2000) NULL,
  PRIMARY KEY (`ModuleRoleID`));


-- -----------------------------------------------------
-- Table `tempus`.`ModuleTimingConstraint`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`ModuleTimingConstraint` (
  `ModuleTimingConstraintID` INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ModuleTimingConstraintID`));
  
  -- -----------------------------------------------------
-- Table `tempus`.`Module`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`Module` (
  `ModuleID` INT NOT NULL AUTO_INCREMENT,
  `ModuleCode` VARCHAR(45) NULL,
  `ModuleName` VARCHAR(45) NULL,
  `ModuleDescription` VARCHAR(2000) NULL,
  `SchoolID` INT NULL,
  `ModuleTimingConstraintID` INT NULL,
  PRIMARY KEY (`ModuleID`),
  CONSTRAINT `ModuleSchoolFK`
    FOREIGN KEY (`SchoolID`)
    REFERENCES `tempus`.`School` (`SchoolID`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `ModuleModuleTimingConstraintFK`
    FOREIGN KEY (`ModuleTimingConstraintID`)
    REFERENCES `tempus`.`ModuleTimingConstraint` (`ModuleTimingConstraintID`)
    ON DELETE SET NULL
    ON UPDATE CASCADE);

	-- -----------------------------------------------------
-- Table `tempus`.`ModuleFEHQLevel`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`ModuleFEHQLevel` (
  `ModuleFEHQLevelID` INT NOT NULL AUTO_INCREMENT,
  `ModuleID` INT NOT NULL,
  `FEHQLevel` TINYINT NOT NULL,
  PRIMARY KEY (`ModuleFEHQLevelID`),
  CONSTRAINT `ModuleFEHQLevelModuleFK`
    FOREIGN KEY (`ModuleID`)
    REFERENCES `tempus`.`Module` (`ModuleID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

	-- -----------------------------------------------------
-- Table `tempus`.`ModulePerson`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`ModulePerson` (
  `ModuleFEHQID` INT NOT NULL,
  `PersonID` INT NOT NULL,
  `ModuleRoleID` INT NOT NULL,
  `ModuleCompleted` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`ModuleFEHQID`, `PersonID`, `ModuleRoleID`),
  CONSTRAINT `ModulePersonModuleFEHQLevelFK`
    FOREIGN KEY (`ModuleFEHQID`)
    REFERENCES `tempus`.`ModuleFEHQLevel` (`ModuleFEHQLevelID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `ModulePersonPersonFK`
    FOREIGN KEY (`PersonID`)
    REFERENCES `tempus`.`Person` (`PersonID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `ModulePersonModuleRoleFK`
    FOREIGN KEY (`ModuleRoleID`)
    REFERENCES `tempus`.`ModuleRole` (`ModuleRoleID`)
    ON DELETE SET NULL
    ON UPDATE CASCADE);

	-- -----------------------------------------------------
-- Table `tempus`.`CourseRole`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`CourseRole` (
  `CourseRoleID` INT NOT NULL AUTO_INCREMENT,
  `CourseRoleName` VARCHAR(45) NOT NULL,
  `CourseRoleDescription` VARCHAR(2000) NULL,
  PRIMARY KEY (`CourseRoleID`))

  -- -----------------------------------------------------
-- Table `tempus`.`CoursePerson`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`CoursePerson` (
  `CourseID` INT NOT NULL,
  `PersonID` INT NOT NULL,
  `CourseRoleID` INT NOT NULL,
  PRIMARY KEY (`CourseID`, `PersonID`, `CourseRoleID`),
  CONSTRAINT `CoursePersonCourseFK`
    FOREIGN KEY (`CourseID`)
    REFERENCES `tempus`.`Course` (`CourseID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `CoursePersonPersonFK`
    FOREIGN KEY (`PersonID`)
    REFERENCES `tempus`.`Person` (`PersonID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `CourseRoleFK`
    FOREIGN KEY (`CourseRoleID`)
    REFERENCES `tempus`.`CourseRole` (`CourseRoleID`)
    ON DELETE SET NULL
    ON UPDATE CASCADE);

	-- -----------------------------------------------------
-- Table `tempus`.`BuildingFloor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`BuildingFloor` (
  `FloorID` INT NOT NULL AUTO_INCREMENT,
  `BuildingID` INT NULL,
  `FloorLevel` INT NULL COMMENT '0 = Ground',
  PRIMARY KEY (`FloorID`),
  CONSTRAINT `BuildingFK`
    FOREIGN KEY (`BuildingID`)
    REFERENCES `tempus`.`Building` (`BuildingID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

	-- -----------------------------------------------------
-- Table `tempus`.`RoomType`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`RoomType` (
  `RoomTypeID` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `Description` VARCHAR(2000) NULL,
  PRIMARY KEY (`RoomTypeID`));

  -- -----------------------------------------------------
-- Table `tempus`.`Room`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`Room` (
  `RoomID` INT NOT NULL AUTO_INCREMENT,
  `RoomName` INT NULL,
  `RoomTypeID` INT NULL,
  `RoomCapacity` INT NULL,
  `FloorID` INT NULL,
  PRIMARY KEY (`RoomID`),
  CONSTRAINT `RoomTypeFK`
    FOREIGN KEY (`RoomTypeID`)
    REFERENCES `tempus`.`RoomType` (`RoomTypeID`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `FloorFK`
    FOREIGN KEY (`FloorID`)
    REFERENCES `tempus`.`BuildingFloor` (`FloorID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

	-- -----------------------------------------------------
-- Table `tempus`.`ModulePrerequisite`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`ModulePrerequisite` (
  `MainModuleFEHQLevelID` INT NOT NULL,
  `PrerequisiteModuleFEHQLevelID` INT NOT NULL,
  PRIMARY KEY (`MainModuleFEHQLevelID`, `PrerequisiteModuleFEHQLevelID`),
  CONSTRAINT `ModulePrerequisiteMainModuleFK`
    FOREIGN KEY (`MainModuleFEHQLevelID`)
    REFERENCES `tempus`.`Module` (`ModuleID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `ModulePrerequisitePrerequisiteModuleFK`
    FOREIGN KEY (`PrerequisiteModuleFEHQLevelID`)
    REFERENCES `tempus`.`Module` (`ModuleID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

	-- -----------------------------------------------------
-- Table `tempus`.`ModuleTimeTable`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`ModuleTimeTable` (
  `ModuleTimeTableID` INT NOT NULL AUTO_INCREMENT,
  `TermID` INT NULL,
  `ModuleID` INT NULL,
  PRIMARY KEY (`ModuleTimeTableID`));


  -- -----------------------------------------------------
-- Table `tempus`.`AcademicYear`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`AcademicYear` (
  `AcademicYearID` INT NOT NULL AUTO_INCREMENT,
  `YearCode` VARCHAR(45) NULL,
  `StartDate` DATE NULL,
  `EndDate` DATE NULL,
  PRIMARY KEY (`AcademicYearID`));

  -- -----------------------------------------------------
-- Table `tempus`.`Term`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`Term` (
  `TermID` INT NOT NULL AUTO_INCREMENT,
  `AcademicYearID` INT NULL,
  `TermNumber` INT NULL,
  `TermStartDate` DATE NULL,
  `TermEndDate` DATE NULL,
  PRIMARY KEY (`TermID`),
  CONSTRAINT `TermAcademicYearFK`
    FOREIGN KEY (`AcademicYearID`)
    REFERENCES `tempus`.`AcademicYear` (`AcademicYearID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

	-- -----------------------------------------------------
-- Table `tempus`.`RoomPartition`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`RoomPartition` (
  `RoomPartitionID` INT NOT NULL AUTO_INCREMENT,
  `RoomID` INT NULL,
  `RoomPartitionName` VARCHAR(45) NULL,
  `RoomPartitionCapacity` INT NULL,
  `RoomTypeID` INT NULL,
  PRIMARY KEY (`RoomPartitionID`),
  CONSTRAINT `RoomPartitionRoomFK`
    FOREIGN KEY (`RoomID`)
    REFERENCES `tempus`.`Room` (`RoomID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `RoomPartitionRoomTypeFK`
    FOREIGN KEY (`RoomTypeID`)
    REFERENCES `tempus`.`RoomType` (`RoomTypeID`)
    ON DELETE SET NULL
    ON UPDATE CASCADE);

	-- -----------------------------------------------------
-- Table `tempus`.`RoomFeature`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`RoomFeature` (
  `RoomFeatureID` INT NOT NULL AUTO_INCREMENT,
  `RoomFeatureName` VARCHAR(45) NULL,
  `RoomFeatureDescription` VARCHAR(2000) NULL,
  PRIMARY KEY (`RoomFeatureID`));

  -- -----------------------------------------------------
-- Table `tempus`.`RoomPartitionFeature`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`RoomPartitionFeature` (
  `RoomPartitionID` INT NOT NULL,
  `RoomFeatureID` INT NOT NULL,
  PRIMARY KEY (`RoomPartitionID`, `RoomFeatureID`),
  CONSTRAINT `RoomPartitionFeatureRoomPartitionFK`
    FOREIGN KEY (`RoomPartitionID`)
    REFERENCES `tempus`.`RoomPartition` (`RoomPartitionID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `RoomPartitionFeatureRoomFeatureFK`
    FOREIGN KEY (`RoomFeatureID`)
    REFERENCES `tempus`.`RoomFeature` (`RoomFeatureID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

	-- -----------------------------------------------------
-- Table `tempus`.`ModuleCourseAvaiability`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tempus`.`ModuleCourseAvaiability` (
  `ModuleCourseAvaiabilityID` INT NOT NULL AUTO_INCREMENT,
  `ModuleFEHQLevelID` INT NULL,
  `CourseID` INT NULL,
  PRIMARY KEY (`ModuleCourseAvaiabilityID`),
  CONSTRAINT `ModuleCourseAvailabilityCourseFK`
    FOREIGN KEY (`CourseID`)
    REFERENCES `tempus`.`Course` (`CourseID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `ModuleCourseAvailablityModuleFEHQLevelFK`
    FOREIGN KEY (`ModuleFEHQLevelID`)
    REFERENCES `tempus`.`ModuleFEHQLevel` (`ModuleFEHQLevelID`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)

# --- !Downs

DROP TABLE `tempus`.`Person`;
DROP TABLE `tempus`.`Staff`;
DROP TABLE `tempus`.`Student`;
DROP TABLE `tempus`.`Building`;
DROP TABLE `tempus`.`School`;
DROP TABLE `tempus`.`Course`;
DROP TABLE `tempus`.`ModuleRole`;
DROP TABLE `tempus`.`ModuleTimingConstraint`;
DROP TABLE `tempus`.`Module`;
DROP TABLE `tempus`.`ModuleFEHQLevel`;
DROP TABLE `tempus`.`ModulePerson`;
DROP TABLE `tempus`.`CourseRole`;
DROP TABLE `tempus`.`CoursePerson`;
DROP TABLE `tempus`.`BuildingFloor`;
DROP TABLE `tempus`.`RoomType`;
DROP TABLE `tempus`.`Room`;
DROP TABLE `tempus`.`ModulePrerequisite`;
DROP TABLE `tempus`.`ModuleTimeTable`;
DROP TABLE `tempus`.`AcademicYear`;
DROP TABLE `tempus`.`Term`;
DROP TABLE `tempus`.`RoomPartition`;
DROP TABLE `tempus`.`RoomFeature`;
DROP TABLE `tempus`.`RoomPartitionFeature`;
DROP TABLE `tempus`.`ModuleCourseAvaiability`;





