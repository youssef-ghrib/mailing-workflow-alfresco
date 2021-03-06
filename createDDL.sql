CREATE TABLE COURRIERARRIVE (REF VARCHAR(255) NOT NULL, DATERECEPTION DATE, OBJET VARCHAR(255), PRIORITE VARCHAR(255), REFEXP VARCHAR(255), STATUT VARCHAR(255), TAGS VARCHAR(255), COURRIERDEP_REF VARCHAR(255), EMPDEST_ID INTEGER, ETABLISSEMENTSOURCE_ID INTEGER, PERSONNEEXTSOURCE_ID INTEGER, SERVICEEXTSOURCE_ID INTEGER, SERVICEINTDEST_ID INTEGER, PRIMARY KEY (REF))
CREATE TABLE COURRIERDEPART (REF VARCHAR(255) NOT NULL, DATEENVOI DATETIME, OBJET VARCHAR(255), PRIORITE VARCHAR(255), STATUT VARCHAR(255), TAGS VARCHAR(255), EMPSOURCE_ID INTEGER, ETABLISSEMENTDEST_ID INTEGER, PERSONNEEXTDEST_ID INTEGER, SERVICEEXTDEST_ID INTEGER, SERVICEINTSOURCE_ID INTEGER, PRIMARY KEY (REF))
CREATE TABLE EMPLOYE (ID INTEGER AUTO_INCREMENT NOT NULL, EMAIL VARCHAR(255), FIRSTLOGIN TINYINT(1) default 0, NOM VARCHAR(255), PASSWORD VARCHAR(255), PRENOM VARCHAR(255), ROLE VARCHAR(255), USERNAME VARCHAR(255), SERVICEINT_ID INTEGER, PRIMARY KEY (ID))
CREATE TABLE ETABLISSEMENT (ID INTEGER AUTO_INCREMENT NOT NULL, ADRESSE VARCHAR(255), NOM VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE FICHIER (NOM VARCHAR(255) NOT NULL, CHEMIN VARCHAR(255), COURRIERARRIVE_REF VARCHAR(255), COURRIERDEPART_REF VARCHAR(255), PRIMARY KEY (NOM))
CREATE TABLE HISTORIQUE (ID INTEGER AUTO_INCREMENT NOT NULL, COMMENTAIRES VARCHAR(255), DATEFINTRAITEMENT DATETIME, DATERECEPTION DATETIME, STATUT VARCHAR(255), COURRIERARR_REF VARCHAR(255), EMPLOYE_ID INTEGER, PRIMARY KEY (ID))
CREATE TABLE PARAMETRE (NOM VARCHAR(255) NOT NULL, VALEUR VARCHAR(255), PRIMARY KEY (NOM))
CREATE TABLE PERSONNEEXTERNE (ID INTEGER AUTO_INCREMENT NOT NULL, NOM VARCHAR(255), PRENOM VARCHAR(255), SERVICEEXT_ID INTEGER, PRIMARY KEY (ID))
CREATE TABLE SERVICEEXTERNE (ID INTEGER AUTO_INCREMENT NOT NULL, NOM VARCHAR(255), ETABLISSEMENT_ID INTEGER, PRIMARY KEY (ID))
CREATE TABLE SERVICEINTERNE (ID INTEGER AUTO_INCREMENT NOT NULL, NOM VARCHAR(255), PRIMARY KEY (ID))
ALTER TABLE COURRIERARRIVE ADD CONSTRAINT FK_COURRIERARRIVE_SERVICEEXTSOURCE_ID FOREIGN KEY (SERVICEEXTSOURCE_ID) REFERENCES SERVICEEXTERNE (ID)
ALTER TABLE COURRIERARRIVE ADD CONSTRAINT FK_COURRIERARRIVE_ETABLISSEMENTSOURCE_ID FOREIGN KEY (ETABLISSEMENTSOURCE_ID) REFERENCES ETABLISSEMENT (ID)
ALTER TABLE COURRIERARRIVE ADD CONSTRAINT FK_COURRIERARRIVE_EMPDEST_ID FOREIGN KEY (EMPDEST_ID) REFERENCES EMPLOYE (ID)
ALTER TABLE COURRIERARRIVE ADD CONSTRAINT FK_COURRIERARRIVE_SERVICEINTDEST_ID FOREIGN KEY (SERVICEINTDEST_ID) REFERENCES SERVICEINTERNE (ID)
ALTER TABLE COURRIERARRIVE ADD CONSTRAINT FK_COURRIERARRIVE_PERSONNEEXTSOURCE_ID FOREIGN KEY (PERSONNEEXTSOURCE_ID) REFERENCES PERSONNEEXTERNE (ID)
ALTER TABLE COURRIERARRIVE ADD CONSTRAINT FK_COURRIERARRIVE_COURRIERDEP_REF FOREIGN KEY (COURRIERDEP_REF) REFERENCES COURRIERDEPART (REF)
ALTER TABLE COURRIERDEPART ADD CONSTRAINT FK_COURRIERDEPART_SERVICEINTSOURCE_ID FOREIGN KEY (SERVICEINTSOURCE_ID) REFERENCES SERVICEINTERNE (ID)
ALTER TABLE COURRIERDEPART ADD CONSTRAINT FK_COURRIERDEPART_SERVICEEXTDEST_ID FOREIGN KEY (SERVICEEXTDEST_ID) REFERENCES SERVICEEXTERNE (ID)
ALTER TABLE COURRIERDEPART ADD CONSTRAINT FK_COURRIERDEPART_ETABLISSEMENTDEST_ID FOREIGN KEY (ETABLISSEMENTDEST_ID) REFERENCES ETABLISSEMENT (ID)
ALTER TABLE COURRIERDEPART ADD CONSTRAINT FK_COURRIERDEPART_EMPSOURCE_ID FOREIGN KEY (EMPSOURCE_ID) REFERENCES EMPLOYE (ID)
ALTER TABLE COURRIERDEPART ADD CONSTRAINT FK_COURRIERDEPART_PERSONNEEXTDEST_ID FOREIGN KEY (PERSONNEEXTDEST_ID) REFERENCES PERSONNEEXTERNE (ID)
ALTER TABLE EMPLOYE ADD CONSTRAINT FK_EMPLOYE_SERVICEINT_ID FOREIGN KEY (SERVICEINT_ID) REFERENCES SERVICEINTERNE (ID)
ALTER TABLE FICHIER ADD CONSTRAINT FK_FICHIER_COURRIERARRIVE_REF FOREIGN KEY (COURRIERARRIVE_REF) REFERENCES COURRIERARRIVE (REF)
ALTER TABLE FICHIER ADD CONSTRAINT FK_FICHIER_COURRIERDEPART_REF FOREIGN KEY (COURRIERDEPART_REF) REFERENCES COURRIERDEPART (REF)
ALTER TABLE HISTORIQUE ADD CONSTRAINT FK_HISTORIQUE_COURRIERARR_REF FOREIGN KEY (COURRIERARR_REF) REFERENCES COURRIERARRIVE (REF)
ALTER TABLE HISTORIQUE ADD CONSTRAINT FK_HISTORIQUE_EMPLOYE_ID FOREIGN KEY (EMPLOYE_ID) REFERENCES EMPLOYE (ID)
ALTER TABLE PERSONNEEXTERNE ADD CONSTRAINT FK_PERSONNEEXTERNE_SERVICEEXT_ID FOREIGN KEY (SERVICEEXT_ID) REFERENCES SERVICEEXTERNE (ID)
ALTER TABLE SERVICEEXTERNE ADD CONSTRAINT FK_SERVICEEXTERNE_ETABLISSEMENT_ID FOREIGN KEY (ETABLISSEMENT_ID) REFERENCES ETABLISSEMENT (ID)
