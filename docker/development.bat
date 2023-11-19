echo "Starting project on development mode"
docker-compose -f ./Authorization/docker-compose-dev.yml -p greenshop-dev up --build > docker_log.txt 2>&1

REM Wait until the specified word appears in the logs
:waitLoop
docker-compose logs | findstr "Tomcat started on port(s): 8080" > nul
if %errorlevel% neq 0 (
    timeout /nobreak /t 1 > nul
    goto waitLoop
)
exit 0