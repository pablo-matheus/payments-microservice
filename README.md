# Purchase Microservice

## Requirements

- Install Docker
- Install Docker Compose
- JDK 17 (Optional)
- Maven (Optional)

## How to Run the Code

### Using Docker

To run the code, it is just use the Docker Compose to build and run the application container:

```bash
$ docker-compose up
```

If it is necessary to rebuild the project, the flags `--build --force-recreate --no-deps` can be used after the `docker-compose up` command.

### Using IDEs

The newer Spring Boot versions require the JDK 17, so it will be necessary for you to install and configure the Java 17 version 
if you prefer to run this project using an IDE or the terminal. The following command can be used:

```bash
$ mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

> After running the application, it is possible to access the API documentation to see the available endpoints and test the application: http://localhost:8080/swagger-ui/index.html

> The access to the H2 database console can be done by accessing http://localhost:8080/h2-console and using the following credentials: 
> - User Name: sa
> - Password: password