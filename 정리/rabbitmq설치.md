1. erlang 설치, rabbitmq 설치 => service 구동 확인
2. command
> rabbitmq-plugins enable rabbitmq_management

> rabbitmq-service.bat stop

> rabbitmq-service.bat install

> rabbitmq-service.bat start
 
3. 접속
```
http://localhost:15672
Username : guest
Password : guest
```
 
5. 서버 목록
 
```
epmd : 4369
Erlang Distribution : 25672
AMQP 0-9-1 Without and With TLS : 5671, 5672
Management Plugin : 15672
STOMP : 61613, 61614
MQTT : 1883, 8883
```
