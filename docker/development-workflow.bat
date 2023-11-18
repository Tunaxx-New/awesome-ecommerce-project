echo "Starting project on development mode with logs"
docker-compose -f ./Authorization/docker-compose-dev.yml -p greenshop-dev up --build
echo "Show logs"
docker-compose -f ./Authorization/docker-compose-dev.yml logs
echo "Docker compose down"
docker-compose -f ./Authorization/docker-compose-dev.yml down