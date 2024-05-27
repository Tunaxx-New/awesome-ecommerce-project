#!/bin/bash

# Define backup directory
backup_dir="/path/to/backup/directory"

# Define MySQL container name
mysql_container="mysql_container"

# Backup filename
backup_file="$backup_dir/mysql_backup_$(date +'%Y%m%d_%H%M%S').sql"

# Docker command to execute backup
docker exec $mysql_container sh -c 'exec mysqldump --all-databases -uroot -p"$MYSQL_ROOT_PASSWORD"' > $backup_file

# Check if backup was successful
if [ $? -eq 0 ]; then
    echo "MySQL backup successful. Backup saved as: $backup_file"
else
    echo "Error: MySQL backup failed."
fi
