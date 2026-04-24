CREATE TABLE Medecin ( 
Num_Medecin number(4) NOT NULL, 
Nom varchar2(30) NOT NULL, 
Prenom varchar2(30) NOT NULL,
Specialite varchar2(30) NOT NULL,
Telephone varchar2(10) CHECK (LENGTH(Telephone) = 10), 
CONSTRAINT pk_Medecin 
PRIMARY KEY(Num_Medecin));

CREATE TABLE Patient (
Num_Patient number(4) NOT NULL,
Nom varchar2(30) NOT NULL,
Prenom varchar2(30) NOT NULL,
Date_Naissance date NOT NULL,
Telephone varchar2(10) CHECK (LENGTH (Telephone)=10),
Adresse varchar2(50),
CONSTRAINT pk_Patient 
PRIMARY KEY(Num_Patient)
);
CREATE TABLE RendezVous (
Num_RendezVous number(4) NOT NULL ,
Num_Patient number(4),
Num_Medecin number(4),
Date_Heure_RendezVous TIMESTAMP NOT NULL,
Statut varchar2(20) NOT NULL,
CONSTRAINT Check_Statut 
CHECK ( Statut IN ('Planifié','Annulé','Effectué')),
CONSTRAINT pk_RendezVous 
PRIMARY KEY (Num_RendezVous),
CONSTRAINT fk_RendezVous_Medecin 
FOREIGN KEY (Num_Medecin) 
REFERENCES Medecin(Num_Medecin)
ON DELETE CASCADE,
CONSTRAINT fk_RendezVous_Patient 
FOREIGN KEY (Num_Patient) 
REFERENCES Patient(Num_Patient)
ON DELETE CASCADE,
CONSTRAINT uq_RendezVous 
UNIQUE (Num_Medecin , Date_Heure_RendezVous)
);
CREATE SEQUENCE seq_Patient START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_Medecin START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_RendezVous START WITH 1 INCREMENT BY 1;

INSERT INTO Medecin VALUES ( seq_Medecin.NEXTVAL ,'Kanoun','Moahamed Larbi','Orthopédiste','0550123456');
INSERT INTO Medecin VALUES ( seq_Medecin.NEXTVAL ,'Bouanani','Hayet','Radiologue','0550111222');
INSERT INTO Medecin VALUES ( seq_Medecin.NEXTVAL ,'Kanoun','Rimmel','Hématologue','0661987654');
INSERT INTO Medecin VALUES ( seq_Medecin.NEXTVAL ,'Sellam','Manel','Ophtalmologue','0770555666');
INSERT INTO Medecin VALUES ( seq_Medecin.NEXTVAL ,'Kanoun','Amine','Cardiologue','0770456789');

INSERT INTO Patient VALUES ( seq_Patient.NEXTVAL ,'Gaouaoui','Hassina',TO_DATE('12-02-1950','DD-MM-YYYY'),'0699244645','Alger');
INSERT INTO Patient VALUES ( seq_Patient.NEXTVAL ,'Aouir','Sara',TO_DATE('02-05-2006','DD-MM-YYYY'),'0622349562','Setif');
INSERT INTO Patient VALUES ( seq_Patient.NEXTVAL ,'Tebib','Djaouida',TO_DATE('29-12-1960','DD-MM-YYYY'),'0699244645','Blida');
INSERT INTO Patient VALUES ( seq_Patient.NEXTVAL ,'Djebali','Chahrazed',TO_DATE('16-02-1969','DD-MM-YYYY'),'0788985233','Tipasa');
INSERT INTO Patient VALUES ( seq_Patient.NEXTVAL ,'Tayebi','Fatma',TO_DATE('26-06-1946','DD-MM-YYYY'),'0552436999','Blida');
INSERT INTO Patient VALUES ( seq_Patient.NEXTVAL,'Guendouz','Zineb',TO_DATE('24-06-2006','DD-MM-YYYY'),'0778925633','Alger');

INSERT INTO RendezVous VALUES ( seq_RendezVous.NEXTVAL , 1 , 1 , TIMESTAMP '2026-04-28 08:30:00','Planifié');
INSERT INTO RendezVous VALUES ( seq_RendezVous.NEXTVAL , 2 , 2 , TIMESTAMP '2026-04-28 09:30:00','Planifié');
INSERT INTO RendezVous VALUES ( seq_RendezVous.NEXTVAL , 1 , 2 , TIMESTAMP '2026-04-29 14:00:00','Planifié');

SELECT *
FROM Patient
WHERE Nom LIKE '%'  :Nom  '%'
   OR Telephone LIKE '%'  :numero  '%';
   

UPDATE Patient
SET Nom = :nom,
    Prenom = :prenom,
    Telephone = :telephone,
    Adresse = :adresse
WHERE Num_Patient = :id_patient;

DELETE FROM Patient
WHERE Num_Patient = :id_patient;


UPDATE Medecin
SET Nom = :nom,
    Prenom = :prenom,
    Specialite = :specialite,
    Telephone = :telephone
WHERE Num_Medecin = :id_medecin;
DELETE FROM Medecin
WHERE Num_Medecin = :id_medecin;
SELECT *
FROM Medecin;

UPDATE RendezVous
SET Statut = 'Annulé'
WHERE Num_RendezVous = :id_rdv;
SELECT r.Num_RendezVous,
       TO_CHAR(r.Date_Heure_RendezVous, 'DD-MM-YYYY HH24:MI') AS Date_RDV ,
       p.Nom AS Nom_Patient,
       m.Nom AS Nom_Medecin,
       r.Statut
FROM RendezVous r
JOIN Patient p ON r.Num_Patient = p.Num_Patient
JOIN Medecin m ON r.Num_Medecin = m.Num_Medecin;
SELECT *
FROM RendezVous
WHERE Num_Medecin = :id_medecin;
SELECT *
FROM RendezVous
WHERE TRUNC(Date_Heure_RendezVous) = :date_recherche;

-- 1. Supprimer l'ancienne contrainte UNIQUE
ALTER TABLE RendezVous 
DROP CONSTRAINT uq_RendezVous;

-- 2. Créer un INDEX UNIQUE avec TRUNC à la minute
CREATE UNIQUE INDEX uq_RendezVous_trunc 
ON RendezVous (Num_Medecin, TRUNC(Date_Heure_RendezVous, 'MI'));

commit;
