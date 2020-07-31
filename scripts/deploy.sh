#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'
#-i ~/.ssh/authorized_keys \
scp target/farter-1.0-SNAPSHOT.jar \
    banana@192.168.1.38:/Users/banana

echo 'Restart server...'

ssh  banana@192.168.1.38 << EOF

pgrep java | xargs kill -9
nohup java -jar farter-1.0-SNAPSHOT.jar > log.txt &

EOF

echo 'Bye'