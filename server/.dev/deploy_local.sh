docker build --file docker/db_sql/Dockerfile . -t kronos/db_sql
export $(cat docker-compose.env) && docker stack config --compose-file docker-compose.yml,docker-compose-local.yml | docker compose -f - up -d
