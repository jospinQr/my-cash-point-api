-- Migration pour rendre la colonne agence_code nullable dans la table user
-- Ce script permet aux administrateurs de ne pas avoir d'agence
-- 
-- IMPORTANT: Exécutez ce script directement dans votre base de données MySQL
-- Vous pouvez utiliser phpMyAdmin (http://localhost:8085) ou un client MySQL

USE cashpoint_db;

-- Étape 1: Supprimer la contrainte de clé étrangère si elle existe
-- (Remplacer 'fk_user_agence' par le nom réel de votre contrainte si différent)
SET @constraint_name = (
    SELECT CONSTRAINT_NAME 
    FROM information_schema.KEY_COLUMN_USAGE 
    WHERE TABLE_SCHEMA = 'cashpoint_db' 
    AND TABLE_NAME = 'user' 
    AND COLUMN_NAME = 'agence_code' 
    AND REFERENCED_TABLE_NAME IS NOT NULL
    LIMIT 1
);

SET @sql = IF(@constraint_name IS NOT NULL, 
    CONCAT('ALTER TABLE user DROP FOREIGN KEY ', @constraint_name), 
    'SELECT "Aucune contrainte de clé étrangère trouvée" AS message'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Étape 2: Modifier la colonne pour la rendre nullable
ALTER TABLE user MODIFY COLUMN agence_code VARCHAR(255) NULL;

-- Étape 3: Recréer la contrainte de clé étrangère (optionnel, pour maintenir l'intégrité référentielle)
-- ALTER TABLE user 
-- ADD CONSTRAINT fk_user_agence 
-- FOREIGN KEY (agence_code) REFERENCES agences(code);

