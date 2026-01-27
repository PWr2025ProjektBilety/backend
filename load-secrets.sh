#!/bin/bash

export DB_PASSWORD="$(cat $DB_PASSWORD_FILE)"
export DB_APP="$(cat $DB_APP_FILE)"
export DB_NAME="$(cat $DB_NAME_FILE)"
export DB_URL=jdbc:postgresql://postgres:5432/${DB_APP}
export JWT_SECRET="$(cat $JWT_SECRET_FILE)"

exec java -jar /app/app.jar