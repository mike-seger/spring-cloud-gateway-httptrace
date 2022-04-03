# spring-cloud-gateway-httptrace

## Build jars
```
./gradlew -p *eureka build
./gradlew -p *gateway build
./gradlew -p *sample build
```

## Run jars
```
find */build/libs/ -type f -name "*.jar"|grep -v plain|while read f ; do java -jar "$f" &; done
```

## Test API access
```
curl -v http://localhost:8080/sample/sample   
```

## Check httptrace
```
url -s http://localhost:8080/actuator/httptrace | jq '.traces |.[0] | .'
```


