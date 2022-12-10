# simple-order-manager-java

## PostgreSQL

### Creating container
```
docker run --name som-postgres -e POSTGRES_USER=som -e POSTGRES_PASSWORD=som -p 5432:5432 -d postgres:14
```

### Starting container
```
docker start som-postgres
```