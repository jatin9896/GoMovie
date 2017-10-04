#/usr/local/bin

# sbt assembly

docker build -t img_api ./api

docker build -t img_selector ./selector

docker build -t img_notifier ./notifier

docker-compose up -d
