# Purchases Microservice

## Requirements

- Install Docker
- Install Docker Compose

## How to Run the Code

To run the code, it is just use the Docker Compose to build and run the application container:

```bash
$ docker-compose up
```

If it is necessary to rebuild the project, the flags `--build --force-recreate --no-deps` can be used after the `docker-compose up` command.

> After running the application, it is possible to access the API documentation to see the available endpoints and test the application: http://localhost:8080/swagger-ui/index.html

> The access to the H2 database console can be done by accessing http://localhost:8080/h2-console and using the following credentials: 
> - User Name: sa
> - Password: password