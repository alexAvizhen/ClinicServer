-- drop clinic tables
DROP TABLE `clinic`;
DROP TABLE `clinic_department`;
DROP TABLE `department_street`;
DROP TABLE `doctor`;
DROP TABLE `patient`;
DROP TABLE `appointment`;

-- create clinic table
CREATE TABLE `clinic` (
  `id` INT NOT NULL,
  `address` VARCHAR(45) NULL,
  `description` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));
-- create clinic departments table
CREATE TABLE `clinic_department` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `clinic_id` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `clinic_id_fk_idx` (`clinic_id` ASC) VISIBLE,
  CONSTRAINT `clinic_id_fk`
    FOREIGN KEY (`clinic_id`)
    REFERENCES `clinic` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
-- create streets table
CREATE TABLE `department_street` (
  `department_id` INT NOT NULL,
  `street` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`department_id`, `street`),
  CONSTRAINT `department_id_street_fk`
    FOREIGN KEY (`department_id`)
    REFERENCES `clinic_department` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
-- create doctor table
CREATE TABLE `doctor` (
  `id` INT NOT NULL,
  `login` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `surname` VARCHAR(45) NULL,
  `name` VARCHAR(45) NULL,
  `phone_number` VARCHAR(45) NULL,
  `birth_date` DATE NULL,
  `department_id` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `doctor_department_id_fk_idx` (`department_id` ASC) VISIBLE,
  CONSTRAINT `doctor_department_id_fk`
    FOREIGN KEY (`department_id`)
    REFERENCES `clinic_department` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
-- create patient table
CREATE TABLE `patient` (
  `id` INT NOT NULL,
  `login` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `surname` VARCHAR(45) NULL,
  `name` VARCHAR(45) NULL,
  `phone_number` VARCHAR(45) NULL,
  `birth_date` DATE NULL,
  `address` VARCHAR(45) NULL,
  `department_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `patient_department_id_fk_idx` (`department_id` ASC) VISIBLE,
  CONSTRAINT `patient_department_id_fk`
    FOREIGN KEY (`department_id`)
    REFERENCES `clinic_department` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
-- create appointment table
CREATE TABLE `appointment` (
  `id` INT NOT NULL,
  `appointment_date` DATE NULL,
  `comment_to_appointment` VARCHAR(145) NULL,
  `doctor_id` INT NOT NULL,
  `patient_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `appointment_doctor_id_fk_idx` (`doctor_id` ASC) VISIBLE,
  INDEX `appointment_patient_id_fk_idx` (`patient_id` ASC) VISIBLE,
  CONSTRAINT `appointment_doctor_id_fk`
    FOREIGN KEY (`doctor_id`)
    REFERENCES `doctor` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `appointment_patient_id_fk`
    FOREIGN KEY (`patient_id`)
    REFERENCES `patient` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
-- fill tables
-- clinic
INSERT INTO `clinic` (`id`, `address`, `description`) VALUES ('1', 'Лидская, 14', 'Полкиклинника на каменной горке');
-- clinic departments
INSERT INTO `clinic_department` (`id`, `name`, `clinic_id`) VALUES ('1', 'Терапевтическое', '1');
INSERT INTO `clinic_department` (`id`, `name`, `clinic_id`) VALUES ('2', 'Неврологическое', '1');
INSERT INTO `clinic_department` (`id`, `name`, `clinic_id`) VALUES ('3', 'Стоматологическое', '1');
INSERT INTO `clinic_department` (`id`, `name`, `clinic_id`) VALUES ('4', 'Травмато-хирургическое', '1');
INSERT INTO `clinic_department` (`id`, `name`, `clinic_id`) VALUES ('5', 'Регистратура', '1');
-- fill clinic departments streets
INSERT INTO `department_street` (`department_id`, `street`) VALUES ('1', 'Лидская');
INSERT INTO `department_street` (`department_id`, `street`) VALUES ('1', 'Неманская');
INSERT INTO `department_street` (`department_id`, `street`) VALUES ('1', 'Колесникова');
INSERT INTO `department_street` (`department_id`, `street`) VALUES ('1', 'Кунцевщина');
-- fill doctors
INSERT INTO `doctor` (`id`, `login`, `password`, `surname`, `name`, `phone_number`, `birth_date`, `department_id`) VALUES ('9', 'root', 'root', 'root', 'root', 'root', '1991-12-12', '1');
INSERT INTO `doctor` (`id`, `login`, `password`, `surname`, `name`, `phone_number`, `birth_date`, `department_id`) VALUES ('1', 'фрол', 'фрол', 'Фролов', 'Альфред', '3337922', '1974-12-12', '1');
INSERT INTO `doctor` (`id`, `login`, `password`, `surname`, `name`, `phone_number`, `birth_date`, `department_id`) VALUES ('2', 'кудр', 'кудр', 'Кудрявцев', 'Артём', '3447922', '1984-12-12', '1');
INSERT INTO `doctor` (`id`, `login`, `password`, `surname`, `name`, `phone_number`, `birth_date`, `department_id`) VALUES ('3', 'комисс', 'комисс', 'Комиссарова', 'Злата', '5647922', '1981-12-12', '2');
INSERT INTO `doctor` (`id`, `login`, `password`, `surname`, `name`, `phone_number`, `birth_date`, `department_id`) VALUES ('4', 'мурав', 'мурав', 'Муравьёв', 'Александр', '8647952', '1981-12-12', '2');
INSERT INTO `doctor` (`id`, `login`, `password`, `surname`, `name`, `phone_number`, `birth_date`, `department_id`) VALUES ('5', 'сухан', 'сухан', 'Суханов', 'Валерий', '8547352', '1981-12-12', '3');
INSERT INTO `doctor` (`id`, `login`, `password`, `surname`, `name`, `phone_number`, `birth_date`, `department_id`) VALUES ('6', 'исак', 'исак', 'Исакова', 'Влада', '1647452', '1971-12-12', '3');
INSERT INTO `doctor` (`id`, `login`, `password`, `surname`, `name`, `phone_number`, `birth_date`, `department_id`) VALUES ('7', 'каныг', 'каныг', 'Каныгин', 'Игнатий', '3747452', '1961-12-12', '4');
INSERT INTO `doctor` (`id`, `login`, `password`, `surname`, `name`, `phone_number`, `birth_date`, `department_id`) VALUES ('8', 'петух', 'петух', 'Петухова', 'Мария', '3723452', '1991-12-12', '5');
-- fill patients
INSERT INTO `patient` (`id`, `login`, `password`, `surname`, `name`, `phone_number`, `birth_date`, `address`, `department_id`) VALUES ('1', 'кулик', 'кулик', 'Куликов', 'Василий', '33234567', '1985-12-12', 'Лещинского, 16', '1');
INSERT INTO `patient` (`id`, `login`, `password`, `surname`, `name`, `phone_number`, `birth_date`, `address`, `department_id`) VALUES ('2', 'русак', 'русак', 'Русаков', 'Давид', '22234567', '1982-12-12', 'Неманская, 91', '1');