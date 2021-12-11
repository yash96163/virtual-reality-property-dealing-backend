# Virtual Reality Property Dealing Backend

## Made using Spring Boot

### Instructions
1. Make sure you are running your local postgres server before starting the application.
2. The documentation of the routes can be found on /swagger-ui.html
3. Before running the application, set environment variables JWT_SECRET and ISSUER, otherwise authentication will not work. (https://www.jetbrains.com/help/objc/add-environment-variables-and-program-arguments.html)
4. Run using the following commands (for docker compose)
    1. mvn package (should be run everytime app was modified)
    2. docker-compose up