CREATE DATABASE trabajo
	DEFAULT CHARACTER SET utf8;

USE trabajo;

CREATE TABLE personas (
	
	id INT NOT NULL UNIQUE AUTO_INCREMENT,
	nombre VARCHAR(255) NOT NULL,
	fecha_registro VARCHAR(10) NOT NULL,
	coste_base INT NOT NULL,
	transporte TINYINT NOT NULL,
	dias_asistidos TINYINT NOT NULL,
	dias_faltados TINYINT NOT NULL,
	dias_mes TINYINT NOT NULL,
	PRIMARY KEY(id)

);
