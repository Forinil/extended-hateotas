# HATEOASDualLayer
HATEOAS Dual Layer Proof-of-concept

## Modules
* `hateoas-dual-layer-backend` - REST HATEOAS API for single domain (here for simplicity one app for both Application and Security domain)
* `hateoas-dual-layer-frontend-facade` - REST HATEOAS front-end facade API

## Usage

Run

```bash
./mvnw clean package
java -jar hateoas-dual-layer-backend/target/hateoas-dual-layer-backend-0.0.1-SNAPSHOT.war &
java -jar hateoas-dual-layer-frontend-facade/target/hateoas-dual-layer-frontend-facade-0.0.1-SNAPSHOT.war &
```

or

```shell
mvnw clean package
start "" java -jar hateoas-dual-layer-backend/target/extended-hateotas-rest-0.0.1-SNAPSHOT.war
start "" java -jar hateoas-dual-layer-frontend-facade/target/hateoas-dual-layer-frontend-facade-0.0.1-SNAPSHOT.war
```

to start the applications and navigate to [http://localhost:8000/swagger-ui.html](http://localhost:8000/swagger-ui.html) and [http://localhost:7000/swagger-ui.html](http://localhost:7000/swagger-ui.html)
to access Swagger UI for the REST API and Facade respectively.

