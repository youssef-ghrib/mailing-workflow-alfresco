ALTER TABLE COURRIERARRIVE DROP FOREIGN KEY FK_COURRIERARRIVE_SERVICEEXTSOURCE_ID
ALTER TABLE COURRIERARRIVE DROP FOREIGN KEY FK_COURRIERARRIVE_ETABLISSEMENTSOURCE_ID
ALTER TABLE COURRIERARRIVE DROP FOREIGN KEY FK_COURRIERARRIVE_EMPDEST_ID
ALTER TABLE COURRIERARRIVE DROP FOREIGN KEY FK_COURRIERARRIVE_SERVICEINTDEST_ID
ALTER TABLE COURRIERARRIVE DROP FOREIGN KEY FK_COURRIERARRIVE_PERSONNEEXTSOURCE_ID
ALTER TABLE COURRIERARRIVE DROP FOREIGN KEY FK_COURRIERARRIVE_COURRIERDEP_REF
ALTER TABLE COURRIERDEPART DROP FOREIGN KEY FK_COURRIERDEPART_SERVICEINTSOURCE_ID
ALTER TABLE COURRIERDEPART DROP FOREIGN KEY FK_COURRIERDEPART_SERVICEEXTDEST_ID
ALTER TABLE COURRIERDEPART DROP FOREIGN KEY FK_COURRIERDEPART_ETABLISSEMENTDEST_ID
ALTER TABLE COURRIERDEPART DROP FOREIGN KEY FK_COURRIERDEPART_EMPSOURCE_ID
ALTER TABLE COURRIERDEPART DROP FOREIGN KEY FK_COURRIERDEPART_PERSONNEEXTDEST_ID
ALTER TABLE EMPLOYE DROP FOREIGN KEY FK_EMPLOYE_SERVICEINT_ID
ALTER TABLE FICHIER DROP FOREIGN KEY FK_FICHIER_COURRIERARRIVE_REF
ALTER TABLE FICHIER DROP FOREIGN KEY FK_FICHIER_COURRIERDEPART_REF
ALTER TABLE HISTORIQUE DROP FOREIGN KEY FK_HISTORIQUE_COURRIERARR_REF
ALTER TABLE HISTORIQUE DROP FOREIGN KEY FK_HISTORIQUE_EMPLOYE_ID
ALTER TABLE PERSONNEEXTERNE DROP FOREIGN KEY FK_PERSONNEEXTERNE_SERVICEEXT_ID
ALTER TABLE SERVICEEXTERNE DROP FOREIGN KEY FK_SERVICEEXTERNE_ETABLISSEMENT_ID
DROP TABLE COURRIERARRIVE
DROP TABLE COURRIERDEPART
DROP TABLE EMPLOYE
DROP TABLE ETABLISSEMENT
DROP TABLE FICHIER
DROP TABLE HISTORIQUE
DROP TABLE PARAMETRE
DROP TABLE PERSONNEEXTERNE
DROP TABLE SERVICEEXTERNE
DROP TABLE SERVICEINTERNE
