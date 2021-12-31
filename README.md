

# ATM-MS
ATM Microservice is a java API designed using spring boot which mimics the working of an ATM.  The services created under this project are designed to distribute the functionality across different services.
#  ATM workflow:
User -> enters card -> Card details are validated -> user gets a welcome message ->
	Then user have options like:
1.	Balance Enquiry
2.	Withdraw Money

# Features
•	Spring Boot 2.6.2

•	Java 8

•	Maven 3.6.2

•	Spring Data JPA

•	Lombok

•	MySql DB

# Setup to run this project :
1.	Clone from GitHub.
    git clone git@github.com:KamalRajput/ATM-MS.git
2. Go inside directory docker.
            
    run build.bat file 

3. once the abpve command completes successfully, run docker compose command:
      docker-compose up --build

in case of failure (docker-compose down --rmi all) and run docker-compose -up --build again.

# Microservice Design
  Services created under the project are designed to be distributed and works independently.
  There are four services:
  
    atm-aggregator-service 
  
  		: which has the rest end points exposed for /authenticate ,/checkBalance, and /withdraw.
		  This service is the one which will be hit by restclient 
  
  card-service : stores card related info
  
  atminfo-service : stores atm related info like denomination and remaining balance in atm
  
  account-service : used when checkBalance and withdraw endpoints will be called.
     
# Ports exposed:
     atm-aggregator-service (9093)
     card-service (9090)
     account-service (9091)
     atminfo-service (9092)
     
## mysql service can be connected on localhost:8080
![image](https://user-images.githubusercontent.com/34761964/147593385-e7794a41-2644-4bab-8eac-0808a8cbef63.png)



      
# Initial data setup
 #### account-service : 
  ![image](https://user-images.githubusercontent.com/34761964/147591118-b44ae743-2414-4b73-bdc1-46ecb041bd76.png)

 #### card-service:
  ![image](https://user-images.githubusercontent.com/34761964/147591080-73620e9b-9c00-4b5c-8ef0-6e5108f5c09d.png)

  
 #### atminfo-service:
  
  ![image](https://user-images.githubusercontent.com/34761964/147591153-111da330-bf56-435b-997f-58a874649c36.png)


# usage :
# /authenticate
![image](https://user-images.githubusercontent.com/34761964/147591235-10f2567f-1458-464a-b1b2-7b2f88cca657.png)

### invalid cardId
![image](https://user-images.githubusercontent.com/34761964/147591287-7a700346-3b38-46f9-ab84-6284b77bb4e9.png)

### invalid pin
![image](https://user-images.githubusercontent.com/34761964/147591338-fa1a2e26-f801-4d84-a2fc-324fcdcdc9e8.png)

# checkBalance
![image](https://user-images.githubusercontent.com/34761964/147591409-0e81537b-c77d-4543-83dd-e853c59a6e95.png)

### invalid hashedAccountNum
![image](https://user-images.githubusercontent.com/34761964/147591461-1f639f33-c073-4551-95e0-532fe1492bb9.png)

# withdraw
![image](https://user-images.githubusercontent.com/34761964/147591556-619db513-aa6f-4c92-9b89-d0f452c2534e.png)

### invalid amount entered
![image](https://user-images.githubusercontent.com/34761964/147591590-bcafd362-6fcd-4802-9e84-f88aaf6d0da6.png)

### amount not available in account
![image](https://user-images.githubusercontent.com/34761964/147591813-07e19514-5fba-4eb6-839f-733ffe359534.png)


# Future Enhancements

•	Service Registry: Eureka Service registry can be added into the project to discover the state of all microservices, I have added it in the project but have to remove it at last minute because of issues while running with docker compose.

•	Security:  Spring Security can be added in the project to ensure basic level authentication . Currently API expose data which is sensitive.

# This project has been tested on:

•	Windows OS

•	JDK 1.8

•	Docker version 19.03.8, build afacb8b

![image](https://user-images.githubusercontent.com/34761964/147592226-96302a32-2a0a-4c22-9c2a-0b00030c51fa.png)


