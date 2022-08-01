# Video sharing and social media platform

<!-- ABOUT THE PROJECT -->

## About The Project

This project provides a video sharing service that makes it easy to watch online videos. Utilized Spring Boot and Maven to manage dependencies.
Users can watch, upload videos to share with others. Users can also follow other users, and comment, save, like, and tip videos.

### Features

* Implemented News feed notifications feature with RabbitMQ, and stored notifications for subscribers with Redis
* Built the distributed file system with FastDFS for file storing, and Nginx as a load balancing to distribute traffic
* Implemented fuzzy/full-text search with ElasticSearch, and achieved video recommendations with Mahout

### Demo

* Login:
![Screen Shot 2022-06-03 at 2 47 01 PM](https://user-images.githubusercontent.com/85295969/171949525-09507dbf-dd77-4d9f-8575-7a6ef5c44580.png)


![Screen Shot 2022-06-03 at 2 56 29 PM](https://user-images.githubusercontent.com/85295969/171949639-99f84434-9937-4b2f-bdde-5a6bf4dc2f0a.png)

* Users' profile
![Screen Shot 2022-06-03 at 3 17 56 PM](https://user-images.githubusercontent.com/85295969/171949664-00194dff-60dd-4bd6-a81f-649c9d16cba9.png)

* Users' subscriptions and fans
![Screen Shot 2022-06-03 at 3 18 36 PM](https://user-images.githubusercontent.com/85295969/171949855-ae49be5e-a9a9-48f9-a3ea-39016cda5c0e.png)

* Upload videos
![Screen Shot 2022-06-03 at 3 19 09 PM](https://user-images.githubusercontent.com/85295969/171949896-f369b532-b676-4b11-878f-ec141b4e8087.png)

* Videos details
![Screen Shot 2022-06-03 at 3 19 59 PM](https://user-images.githubusercontent.com/85295969/171950328-24733629-a9df-48be-9088-bc36e0eee03f.png)

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

