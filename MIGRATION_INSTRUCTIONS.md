# Instructions de Migration - Colonne agence_code nullable

## Problème
La colonne `agence_code` dans la table `user` est actuellement définie comme `NOT NULL`, ce qui empêche l'enregistrement d'administrateurs sans agence.

## Solution
Exécutez le script SQL de migration pour rendre la colonne nullable.

## Méthode 1 : Via phpMyAdmin (Recommandé)

1. Ouvrez phpMyAdmin dans votre navigateur : http://localhost:8085
2. Connectez-vous avec :
   - Serveur : `mysql` (ou `localhost:3308` depuis l'extérieur du conteneur)
   - Utilisateur : `jospin`
   - Mot de passe : `joe@8452`
3. Sélectionnez la base de données `cashpoint_db`
4. Cliquez sur l'onglet "SQL"
5. Copiez et collez le contenu du fichier `migration_agence_code_nullable_simple.sql`
6. Cliquez sur "Exécuter"

## Méthode 2 : Via ligne de commande MySQL

```bash
# Se connecter au conteneur MySQL
docker exec -it mysql-db-mycashpoint mysql -u jospin -pjoe@8452 cashpoint_db

# Puis exécuter la commande SQL
ALTER TABLE user MODIFY COLUMN agence_code VARCHAR(255) NULL;
```

## Méthode 3 : Via fichier SQL

```bash
# Copier le script dans le conteneur
docker cp migration_agence_code_nullable_simple.sql mysql-db-mycashpoint:/tmp/migration.sql

# Exécuter le script
docker exec -i mysql-db-mycashpoint mysql -u jospin -pjoe@8452 cashpoint_db < migration_agence_code_nullable_simple.sql
```

## Vérification

Après avoir exécuté la migration, vérifiez que la colonne est bien nullable :

```sql
DESCRIBE user;
```

La colonne `agence_code` devrait maintenant avoir `NULL` dans la colonne "Null".

## Test

Essayez maintenant d'enregistrer un administrateur sans agence via Postman :

```json
{
    "username": "admin1",
    "password": "password123",
    "codeAgence": null,
    "role": "ADMIN"
}
```

