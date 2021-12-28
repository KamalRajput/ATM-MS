:: Build All services
echo Building Services

call mvn -f ../card-service/pom.xml clean install -U -DskipTests
call mvn -f ../account-service/pom.xml clean install -U -DskipTests 
call mvn -f ../atm-info-service/pom.xml clean install -U -DskipTests
call mvn -f ../atm-aggregator-service/pom.xml clean install -U -DskipTests



