below are the prerequisite
    Java 17
    Apache Maven
    Lombok plugin should be installed in eclipse
    (or)
    Enable Lombok Annotation while starting up the service in Intellji

Once you have pull the source code from github

please do maven update and mvn clean install

Student Application

H2 Database URL:
http://localhost:8081/student/h2-console

http://localhost:8082/receipt/h2-console

this is default user name and password
username: sa
password:


Swagger:

Student Service:
    http://localhost:8081/swagger-ui/index.html
 
Fee collection Service:
    http://localhost:8082/swagger-ui/index.html

Launch the service in the below order
    1. eureka-servce
    2. gateway-service
    3. student-service
    4. fee-collection-service

Note: please wait for few seconds so that all the services can be discovered by eureka server

make sure all the service is up and running with below url
    http://localhost:8761/
    
Services:
    eureka:
        http://localhost:8761/
    gateway
        http://localhost:8765/


Student-Services Api details:

Description: Getting Student Record

URL : http://localhost:8081/students/1
    Method : GET
    Functionality: getting one single student object
    success: 200 as status code (Success)
    error: 404 as status code (Not Found)
    Internal Server: 500 (Server error)

2. 

Description: Inserting student record

URL : http://localhost:8081/students
    Method : POST
    Functionality: inserting one single student object
    success: 201 as status code (Created)
    Conflict: 409 if the student name is already exist
    Validation error: 400 as status code (Bad Request)
    content-type: application/json
    Internal Server error: 500
    
    Request Body (JSON):
    {
    "studentName":"Abdul",
    "grade":"A",
    "mobileNumber":"9940566527",
    "schoolName":"Sky"
    }

3. 

Description: Collecting fee for the Student

URL:http://localhost:8081/collect/student/fee
Method: POST
success: 201 as status code (Created)
Validation error: 400 as status code (Bad Request)
Conflict error: 401 as status code (Conflict Request)
Not Found: if the student is not available in database
Internal Server error: 500
content-type: application/json


    Request Body (JSON):
    {
    "studentId":1,
    "cardNumber":"5958598598",
    "cardType":"VISA",
    "amount":0.10
    }

Validation:
    
    1. studentId should not null or empty
    2. Card number should not be empty
    3. amount should not be empty or null

API 4: 

Description: Getting Receipt for the student

URL: http://localhost:8081/view/receipt/{receiptid}

Method: GET
success: 200 as status code (success)
content-type: application/json
Internal Server error: 500

Fee Collection Service API Details:

1. Description: Generating Receipt for the student

URL: http://localhost:8082/fees/receipts/{receiptid}

Method: GET
success: 200 as status code (success)
content-type: application/json
Internal Server error: 500

2. Description: checking whether student have already paid the fees. 

URL: http://localhost:8082/student/{studentid}/fee

Method: GET
success: 200 as status code (success)
conflict: 409 as status code (if the student have paid the fees)
content-type: application/json
Internal Server error: 500

3. Description: Collecting fee for the student

URL: http://localhost:8082/fees/collect
Method: POST
success: 201 as status code (created)
content-type: application/json
Internal Server error: 500

4. Description: Chekcing whether receipts available for the receipt id

URL: http://localhost:8082/receipts/{receiptid}
Method: Get
success: 200 as statuc code
No Record Found: 204
content-type: application/json
Internal Server error: 500


