
1. zookeeper 실행

- zookeeper.properties 파일 안 tmp 경로 편집

```
.\bin\windows\zookeeper-server-start.bat  .\config\zookeeper.properties
```

2. kafka 실행

- server.properties 파일 안 tmp 경로 편집

```
.\bin\windows\kafka-server-start.bat .\config\server.properties
```

3. kafka 리스트 보기

```
.\bin\windows\kafka-topics.bat --bootstrap-server localhost:9092 --list
.\bin\windows\kafka-topics.bat --bootstrap-server localhost:9092 --describe
.\bin\windows\kafka-topics.bat --bootstrap-server localhost:9092 --describe --topic quickstart-events
```

4. kafka topic 만들기

```
.\bin\windows\kafka-topics.bat --bootstrap-server localhost:9092 --create --topic quickstart-events --partitions 1
```

5. kafka producer 실행

```text
.\bin\windows\kafka-console-producer --broker-list localhost:9092 --topic quickstart-events
```

6. kafka consumer 실행

```text
.\bin\windows\kafka-console-consumer --bootstrap-server localhost:9092 --topic quickstart-events
.\bin\windows\kafka-console-consumer --bootstrap-server localhost:9092 --topic quickstart-events --from-beginning
```

7. kafka connector 실행

```text
.\bin\windows\connect-distributed .\etc\kafka\connect-distributed.properties
```

8. kafka topic 삭제
- kafka config 폴더 server.properties `delete.topic.enable=true` 추가

```text
delete.topic.enable=true
```
