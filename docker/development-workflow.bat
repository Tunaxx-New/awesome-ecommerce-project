echo "Starting project on development mode"
docker-compose -f ./Authorization/docker-compose-dev.yml -p ecommerce-greenshop-dev up --build
echo "Show logs"
docker-compose -f ./Authorization/docker-compose-dev.yml logs
echo "Docker compose down"
docker-compose -f ./Authorization/docker-compose-dev.yml down