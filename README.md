# Extended HATEOTAS


## Modules
* `extended-hateotas-rest` - pure REST HATEOTAS API for single domain (here for simplicity one app for both Application and Security domain)
* `extended-hateotas-facade` - REST facade for front-end

## Usage

Run

```bash
mvnw clean package
java -jar extended-hateotas-rest/extended-hateotas-rest-0.0.1-SNAPSHOT.war
java -jar extended-hateotas-rest/extended-hateotas-facade-0.0.1-SNAPSHOT.war
```

to start the applications and navigate to [http://localhost:8000/swagger-ui.html](http://localhost:8000/swagger-ui.html) and [http://localhost:7000/swagger-ui.html](http://localhost:7000/swagger-ui.html)
to access Swagger UI for the REST API and Facade respectively.

