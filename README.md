## Introduction
This project is a set of examples for service virtualisation used in 2023 Bank of APIs Summer Hackathon.\
Team: OB Dev 1

## Code Setup
* Java Versions: 1.8 
* Maven: Any
* Hoverfly

## Getting Started
1. Start the Client service in TEST mode if you want to invoke hoverfly, set <code>-Dspring.profiles.active=TEST</code>
2. Start the  Account service, set <code>-Dhttp.proxyUser=yourUserName -Dhttp.proxyPassword=youPassword</code>
3. Start hoverfly: <code>hoverctl start</code>
4. Go to <code>http://localhost:8888/dashboard</code>, you should be able to see the hoverfly dashboard
5. Click <code>Capture</code> and hit the endpoints. Hoverfly will capture the request as its running in capture mode, you can see the count in capture section
6. Bring Account service down.
7. Click <code>Simulate</code> nad hit teh endpoints. Hoverfly will simulate the response, you will see the count of simulate has increased 1.
