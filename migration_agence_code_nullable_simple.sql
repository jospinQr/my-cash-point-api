-- Migration simplifiée pour rendre la colonne agence_code nullable
-- Exécutez ce script dans votre base de données MySQL

USE cashpoint_db;

-- Modifier la colonne pour la rendre nullable
ALTER TABLE user MODIFY COLUMN agence_code VARCHAR(255) NULL;

