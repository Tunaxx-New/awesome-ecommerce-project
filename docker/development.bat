echo "Starting project on development mode"
docker-compose -f ./Authorization/docker-compose-dev.yml -p greenshop-dev up -d --build --force-recreate