-- Answer to Part 1 of the 2nd Database Assignment 2020/21
--
-- CANDIDATE NUMBER: 215758
-- Please insert your candidate number in the line above.
-- Do NOT remove ANY lines of this template.

-- In each section below put your answer in a new line 
-- BELOW the corresponding comment.
-- Use ONE SQL statement ONLY per question.
-- If you don’t answer a question just leave 
-- the corresponding space blank. 
-- Anything that does not run in SQL you MUST put in comments.

-- DO NOT REMOVE ANY LINE FROM THIS FILE.

-- START OF ASSIGNMENT CODE


-- @@01

CREATE TABLE MoSpo_HallOfFame ( 
    hoFdriverId INT(10) UNSIGNED, 
    hoFYear SMALLINT(4) ZEROFILL, 
    hoFSeries VARCHAR(9) NOT NULL, 
    hoFImage VARCHAR(200), 
    hoFWins INT DEFAULT 0, 
    hoFBestRaceName VARCHAR(30),
    hoFBestRaceDate DATE,

    PRIMARY KEY (hoFdriverId, hoFYear),
    FOREIGN KEY (hoFdriverId) REFERENCES MoSpo_Driver(driverId) ON DELETE CASCADE,
    FOREIGN KEY (hoFBestRaceName, hoFBestRaceDate) REFERENCES MoSpo_Race(raceName, raceDate) ON DELETE SET NULL,

    CONSTRAINT chk_hoFYear CHECK (hoFYear < 2155 AND hoFYear > 1901),
    CONSTRAINT chk_hoFSeries CHECK (hoFSeries in ('BritishGT', 'Formula1', 'FormulaE', 'SuperGT')),
    CONSTRAINT chk_hoFWins CHECK (hoFWins >= 0 AND hoFWins <= 99)
);
 
-- @@02

ALTER TABLE MoSpo_Driver
ADD COLUMN driverWeight FLOAT,
ADD CONSTRAINT chk_driverWeight CHECK (driverWeight >= 0.0 AND driverWeight <= 99.9);

-- @@03

UPDATE MoSpo_RacingTeam
SET teamPostcode = 'HP135PN'
WHERE teamName = 'Beechdean Motorsport';

-- @@04

DELETE FROM MoSpo_Driver 
WHERE UPPER(driverFirstname) = UPPER('Ayrton') 
AND UPPER(driverLastname) = UPPER('Senna');

-- @@05

SELECT COUNT(*)
AS numberTeams
FROM MoSpo_RacingTeam;

-- @@06

SELECT driverId, CONCAT(driverFirstname, ' ', driverLastname) AS driverName, driverDOB
FROM MoSpo_Driver
WHERE UPPER(SUBSTRING(driverLastname, 1, 1)) = UPPER(SUBSTRING(driverFirstname, 1, 1));

-- @@07

SELECT MoSpo_RacingTeam.teamName, count(*) AS 'numberOfDriver'
FROM MoSpo_Driver
INNER JOIN MoSpo_RacingTeam ON MoSpo_RacingTeam.teamName = MoSpo_Driver.driverTeam
GROUP BY MoSpo_RacingTeam.teamName;

-- @@08

SELECT MoSpo_Lap.lapRaceName AS raceName, MoSpo_Lap.lapRaceDate AS raceDate, MIN(lapInfoTime) AS lapTime
FROM MoSpo_LapInfo
INNER JOIN MoSpo_Lap ON MoSpo_Lap.lapRaceName = MoSpo_LapInfo.lapInfoRaceName AND MoSpo_Lap.lapRaceDate = MoSpo_LapInfo.lapInfoRaceDate
GROUP BY MoSpo_Lap.lapRaceName, MoSpo_Lap.lapRaceDate
HAVING lapTime IS NOT NULL;

-- @@09

SELECT raceName, AVG(pitstopsOccured) AS avgStops
FROM (
    SELECT pitstopRaceName AS raceName, COUNT(*) AS pitstopsOccured, year(pitstopRaceDate) AS theYear
    FROM MoSpo_PitStop
    GROUP BY pitstopRaceName, year(pitstopRaceDate) ) AS T
GROUP BY raceName;

-- @@10

SELECT DISTINCT MoSpo_Car.carMake AS carMake
FROM MoSpo_RaceEntry
INNER JOIN MoSpo_Car ON MoSpo_RaceEntry.raceEntryCarId = MoSpo_Car.carId
INNER JOIN MoSpo_LapInfo ON MoSpo_RaceEntry.raceEntryNumber = MoSpo_LapInfo.lapInfoRaceNumber AND MoSpo_RaceEntry.raceEntryRaceName = MoSpo_LapInfo.lapInfoRaceName AND MoSpo_RaceEntry.raceEntryRaceDate = MoSpo_LapInfo.lapInfoRaceDate
WHERE MoSpo_LapInfo.lapInfoCompleted = '0';

-- @@11

SELECT MoSpo_Race.raceName, MoSpo_Race.raceDate, COUNT(pitstopDuration) AS mostPitstops
FROM MoSpo_Race
LEFT JOIN MoSpo_PitStop ON MoSpo_PitStop.pitstopRaceName = MoSpo_Race.raceName AND MoSpo_PitStop.pitstopRaceDate =MoSpo_Race.raceDate
GROUP BY MoSpo_Race.raceName, MoSpo_Race.raceDate;

-- @@12

SELECT DISTINCT MoSpo_Driver.driverId, MoSpo_Driver.driverLastname
FROM MoSpo_RaceEntry
INNER JOIN MoSpo_LapInfo ON MoSpo_LapInfo.lapInfoRaceNumber = MoSpo_RaceEntry.raceEntryNumber AND MoSpo_LapInfo.lapInfoRaceName = MoSpo_RaceEntry.raceEntryRaceName AND MoSpo_LapInfo.lapInfoRaceDate = MoSpo_RaceEntry.raceEntryRaceDate
INNER JOIN MoSpo_Driver ON MoSpo_Driver.driverId = MoSpo_RaceEntry.raceEntryDriverId
LEFT JOIN (
    SELECT DISTINCT MoSpo_Driver.driverId, MoSpo_Driver.driverLastname
    FROM MoSpo_RaceEntry
    INNER JOIN MoSpo_LapInfo ON MoSpo_LapInfo.lapInfoRaceNumber = MoSpo_RaceEntry.raceEntryNumber AND MoSpo_LapInfo.lapInfoRaceName = MoSpo_RaceEntry.raceEntryRaceName AND MoSpo_LapInfo.lapInfoRaceDate = MoSpo_RaceEntry.raceEntryRaceDate
    INNER JOIN MoSpo_Driver ON MoSpo_Driver.driverId = MoSpo_RaceEntry.raceEntryDriverId
    WHERE MoSpo_LapInfo.lapInfoCompleted = '0') AS T
ON T.driverId = MoSpo_Driver.driverId
WHERE T.driverId IS NULL;

-- @@13

SELECT  MoSpo_Car.carMake, T_Two.rCount/T_One.cCount AS rRate
FROM MoSpo_Car
INNER JOIN (
	SELECT carMake, COUNT(*) AS cCount
	FROM (
		SELECT DISTINCT MoSpo_LapInfo.lapInfoRaceNumber, MoSpo_LapInfo.lapInfoRaceName, MoSpo_LapInfo.lapInfoRaceDate, MoSpo_LapInfo.lapInfoCompleted, MoSpo_RaceEntry.raceEntryCarId, MoSpo_Car.carMake
		FROM MoSpo_Car
		INNER JOIN MoSpo_RaceEntry ON MoSpo_RaceEntry.raceEntryCarId = MoSpo_Car.carId
		INNER JOIN MoSpo_LapInfo ON MoSpo_RaceEntry.raceEntryNumber = MoSpo_LapInfo.lapInfoRaceNumber AND MoSpo_RaceEntry.raceEntryRaceName = MoSpo_LapInfo.lapInfoRaceName AND MoSpo_RaceEntry.raceEntryRaceDate = MoSpo_LapInfo.lapInfoRaceDate
		LEFT JOIN (
			SELECT DISTINCT MoSpo_LapInfo.lapInfoRaceNumber, MoSpo_LapInfo.lapInfoRaceName, MoSpo_LapInfo.lapInfoRaceDate, MoSpo_LapInfo.lapInfoCompleted, MoSpo_RaceEntry.raceEntryCarId, MoSpo_Car.carMake
			FROM MoSpo_Car
			INNER JOIN MoSpo_RaceEntry ON MoSpo_RaceEntry.raceEntryCarId = MoSpo_Car.carId
			INNER JOIN MoSpo_LapInfo ON MoSpo_RaceEntry.raceEntryNumber = MoSpo_LapInfo.lapInfoRaceNumber AND MoSpo_RaceEntry.raceEntryRaceName = MoSpo_LapInfo.lapInfoRaceName AND MoSpo_RaceEntry.raceEntryRaceDate = MoSpo_LapInfo.lapInfoRaceDate
			WHERE YEAR(MoSpo_RaceEntry.raceEntryRaceDate) = 2018 AND (MoSpo_LapInfo.lapInfoCompleted = '0' OR MoSpo_RaceEntry.raceEntryCarId IS NULL)) AS T
	ON T.lapInfoRaceNumber = MoSpo_LapInfo.lapInfoRaceNumber AND T.lapInfoRaceName = MoSpo_LapInfo.lapInfoRaceName AND T.lapInfoRaceDate = MoSpo_LapInfo.lapInfoRaceDate
	WHERE YEAR(MoSpo_RaceEntry.raceEntryRaceDate) = 2018 AND (MoSpo_LapInfo.lapInfoCompleted = '1' OR MoSpo_RaceEntry.raceEntryCarId IS NULL)) AS T_One
	GROUP BY carMake ) AS T_One ON T_One.carMake = MoSpo_Car.carMake
INNER JOIN (
	SELECT carMake, COUNT(*) AS rCount
	FROM (
		SELECT DISTINCT MoSpo_LapInfo.lapInfoRaceNumber, MoSpo_LapInfo.lapInfoRaceName, MoSpo_LapInfo.lapInfoRaceDate, MoSpo_LapInfo.lapInfoCompleted, MoSpo_RaceEntry.raceEntryCarId, MoSpo_Car.carMake
		FROM MoSpo_Car
		INNER JOIN MoSpo_RaceEntry ON MoSpo_RaceEntry.raceEntryCarId = MoSpo_Car.carId
		INNER JOIN MoSpo_LapInfo ON MoSpo_RaceEntry.raceEntryNumber = MoSpo_LapInfo.lapInfoRaceNumber AND MoSpo_RaceEntry.raceEntryRaceName = MoSpo_LapInfo.lapInfoRaceName AND MoSpo_RaceEntry.raceEntryRaceDate = MoSpo_LapInfo.lapInfoRaceDate
		WHERE YEAR(MoSpo_RaceEntry.raceEntryRaceDate) = 2018 AND (MoSpo_LapInfo.lapInfoCompleted = '0' OR MoSpo_RaceEntry.raceEntryCarId IS NULL)) AS T_Two
	GROUP BY carMake ) AS T_Two ON T_Two.carMake = MoSpo_Car.carMake
GROUP BY  MoSpo_Car.carMake
HAVING rRate > (
	SELECT AVG(T_Three.rRate)
	FROM (
		SELECT  MoSpo_Car.carMake, T_Two.rCount/T_One.cCount AS rRate
		FROM MoSpo_Car
		INNER JOIN (
			SELECT carMake, COUNT(*) AS cCount
			FROM (
				SELECT DISTINCT MoSpo_LapInfo.lapInfoRaceNumber, MoSpo_LapInfo.lapInfoRaceName, MoSpo_LapInfo.lapInfoRaceDate, MoSpo_LapInfo.lapInfoCompleted, MoSpo_RaceEntry.raceEntryCarId, MoSpo_Car.carMake
				FROM MoSpo_Car
				INNER JOIN MoSpo_RaceEntry ON MoSpo_RaceEntry.raceEntryCarId = MoSpo_Car.carId
				INNER JOIN MoSpo_LapInfo ON MoSpo_RaceEntry.raceEntryNumber = MoSpo_LapInfo.lapInfoRaceNumber AND MoSpo_RaceEntry.raceEntryRaceName = MoSpo_LapInfo.lapInfoRaceName AND MoSpo_RaceEntry.raceEntryRaceDate = MoSpo_LapInfo.lapInfoRaceDate
				LEFT JOIN (
					SELECT DISTINCT MoSpo_LapInfo.lapInfoRaceNumber, MoSpo_LapInfo.lapInfoRaceName, MoSpo_LapInfo.lapInfoRaceDate, MoSpo_LapInfo.lapInfoCompleted, MoSpo_RaceEntry.raceEntryCarId, MoSpo_Car.carMake
					FROM MoSpo_Car
					INNER JOIN MoSpo_RaceEntry ON MoSpo_RaceEntry.raceEntryCarId = MoSpo_Car.carId
					INNER JOIN MoSpo_LapInfo ON MoSpo_RaceEntry.raceEntryNumber = MoSpo_LapInfo.lapInfoRaceNumber AND MoSpo_RaceEntry.raceEntryRaceName = MoSpo_LapInfo.lapInfoRaceName AND MoSpo_RaceEntry.raceEntryRaceDate = MoSpo_LapInfo.lapInfoRaceDate
					WHERE YEAR(MoSpo_RaceEntry.raceEntryRaceDate) = 2018 AND (MoSpo_LapInfo.lapInfoCompleted = '0' OR MoSpo_RaceEntry.raceEntryCarId IS NULL)) AS T
			ON T.lapInfoRaceNumber = MoSpo_LapInfo.lapInfoRaceNumber AND T.lapInfoRaceName = MoSpo_LapInfo.lapInfoRaceName AND T.lapInfoRaceDate = MoSpo_LapInfo.lapInfoRaceDate
			WHERE YEAR(MoSpo_RaceEntry.raceEntryRaceDate) = 2018 AND (MoSpo_LapInfo.lapInfoCompleted = '1' OR MoSpo_RaceEntry.raceEntryCarId IS NULL)) AS T_One
			GROUP BY carMake ) AS T_One ON T_One.carMake = MoSpo_Car.carMake
		INNER JOIN (
			SELECT carMake, COUNT(*) AS rCount
			FROM (
				SELECT DISTINCT MoSpo_LapInfo.lapInfoRaceNumber, MoSpo_LapInfo.lapInfoRaceName, MoSpo_LapInfo.lapInfoRaceDate, MoSpo_LapInfo.lapInfoCompleted, MoSpo_RaceEntry.raceEntryCarId, MoSpo_Car.carMake
				FROM MoSpo_Car
				INNER JOIN MoSpo_RaceEntry ON MoSpo_RaceEntry.raceEntryCarId = MoSpo_Car.carId
				INNER JOIN MoSpo_LapInfo ON MoSpo_RaceEntry.raceEntryNumber = MoSpo_LapInfo.lapInfoRaceNumber AND MoSpo_RaceEntry.raceEntryRaceName = MoSpo_LapInfo.lapInfoRaceName AND MoSpo_RaceEntry.raceEntryRaceDate = MoSpo_LapInfo.lapInfoRaceDate
				WHERE YEAR(MoSpo_RaceEntry.raceEntryRaceDate) = 2018 AND (MoSpo_LapInfo.lapInfoCompleted = '0' OR MoSpo_RaceEntry.raceEntryCarId IS NULL)) AS T_Two
			GROUP BY carMake ) AS T_Two ON T_Two.carMake = MoSpo_Car.carMake
		GROUP BY  MoSpo_Car.carMake)
	AS T_Three
);

-- @@14

DELIMITER $$
CREATE FUNCTION totalRaceTime(_raceNumInput INT, _raceNameInput VARCHAR(30), _raceDateInput DATE) RETURNS INT
DETERMINISTIC
BEGIN

DECLARE _carId INT;
DECLARE _curNum INT;
DECLARE _curName VARCHAR(30);
DECLARE _curDate DATE;

DECLARE totalTime INT;
DECLARE selectTime INT;
DECLARE tempNullCount INT;
DECLARE nullCount INT;
DECLARE n INT;
DECLARE i INT;

SET totalTime = 0;

SELECT raceEntryCarId INTO _carId FROM MoSpo_RaceEntry WHERE raceEntryNumber = _raceNumInput AND raceEntryRaceName = _raceNameInput AND raceEntryRaceDate = _raceDateInput;

IF _carId IS NULL THEN
	SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'procedure Race does not exist';
ELSE	
	CREATE TEMPORARY TABLE _TempTable (_raceEntryNumber INT, _raceEntryRaceName VARCHAR(30), _raceEntryRaceDate DATE);
	INSERT INTO _TempTable SELECT raceEntryNumber, raceEntryRaceName, raceEntryRaceDate FROM MoSpo_RaceEntry WHERE raceEntryCarId = _carId; 
	SELECT COUNT(*) INTO n FROM _TempTable;
	SET i = 0;
	
	WHILE i < n DO
		SELECT _raceEntryNumber, _raceEntryRaceName, _raceEntryRaceDate INTO _curNum, _curName, _curDate FROM _TempTable LIMIT i,1;
		SELECT COUNT(*) INTO tempNullCount FROM MoSpo_LapInfo WHERE lapInfoRaceNumber =_curNum AND lapInfoRaceName = _curName AND lapInfoRaceDate = _curDate AND lapInfoCompleted = '0';
		SELECT COUNT(*) INTO selectTime FROM MoSpo_LapInfo WHERE lapInfoRaceNumber =_curNum AND lapInfoRaceName = _curName AND lapInfoRaceDate = _curDate AND lapInfoCompleted = '0' AND lapInfoTime IS NULL;
		SET nullCount = nullCount + tempNullCount;

		IF tempNullCount > 0 AND selectTime > 0 THEN
			DROP TEMPORARY TABLE _TempTable;
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'procedure TimeForAll-Laps does not exist';
		END IF;

		SET selectTime = 0;
		SELECT SUM(lapInfoTime) INTO selectTime FROM MoSpo_LapInfo WHERE lapInfoRaceNumber =_curNum AND lapInfoRaceName = _curName AND lapInfoRaceDate = _curDate;
		SET totalTime = totalTime + selectTime;
		
		SET i = i + 1;
	END WHILE;
	
	DROP TEMPORARY TABLE _TempTable;

	IF nullCount > 0 THEN
		RETURN NULL;
	ELSE
		RETURN totalTime;
	END IF;
END IF;
END $$
DELIMITER ;

-- END OF ASSIGNMENT CODE
