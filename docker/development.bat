@echo off
setlocal

set "DETACHED="

:: Check for the -d parameter
if "%1"=="-d" (
    set "DETACHED=-d"
    shift
)

echo "Starting project on development mode"
docker-compose -f ./Authorization/docker-compose-dev.yml -p greenshop-dev up %DETACHED% --build

endlocal