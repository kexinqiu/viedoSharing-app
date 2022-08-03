# Video sharing and social media platform

<!-- ABOUT THE PROJECT -->

## About The Project

This project provides a video sharing service that makes it easy to watch online videos. Utilized Spring Boot and Maven to manage dependencies.
Users can watch, upload videos to share with others. Users can also follow other users, and comment, save, like, and tip videos.

### Features

* Implemented News feed notifications feature with RabbitMQ, and stored notifications for subscribers with Redis
* Built the distributed file system with FastDFS for file storing, and Nginx as a load balancing to distribute traffic
* Implemented fuzzy/full-text search with ElasticSearch, and achieved video recommendations with Mahout


### Built With
#### Built service on Mac:
1. install Spring Boot
2. install Maven
3. install MySQL
4. install RabbitMQ
   navigate into the bin director
   ```sh
   cd \bin
   ```
   start RocketMQ service:
   ```sh
   rabbitmq-service.bat start
   ```
   create new window and navigate into the bin director
   ```sh
   rabbitmq-service.bat start -n localhost:9876 autoCreateTopicEnable=true
   ```
5. install Redis
    start Redis

#### Built services on Centos7 Linux:

* intasll FastDFS
```sh
tracker：fdfs_trackerd /etc/fdfs/tracker.conf
```
```sh
storage：fdfs_storaged /etc/fdfs/storage.conf
```

