#!/bin/bash
# Runs automatically on first container start (files in /docker-entrypoint-initdb.d/
# are executed once, only when the data volume is empty). POSTGRES_DB creates the
# first database; this script creates the rest so all 5 services can share one
# Postgres instance while still having their own database/schema.
set -e
set -u

function create_database() {
  local database=$1
  echo "Creating database '$database'"
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname postgres <<-EOSQL
    CREATE DATABASE $database;
EOSQL
}

if [ -n "${POSTGRES_MULTIPLE_DATABASES:-}" ]; then
  echo "Multiple database creation requested: $POSTGRES_MULTIPLE_DATABASES"
  for db in $(echo "$POSTGRES_MULTIPLE_DATABASES" | tr ',' ' '); do
    create_database "$db"
  done
  echo "Multiple databases created"
fi
