# HATEOASDualLayer
HATEOAS Dual Layer Proof-of-concept

## Modules
* `hateoas-dual-layer-backend` - REST HATEOAS API for single domain (here for simplicity one app for both Application and Security domain)
* `hateoas-dual-layer-frontend-facade` - REST HATEOAS front-end facade API

## Usage

Run

```bash
./mvnw clean package
java -Dspring.profiles.active=standalone -jar hateoas-dual-layer-backend/target/hateoas-dual-layer-backend-0.0.1-SNAPSHOT.war &
java -Dspring.profiles.active=standalone -jar hateoas-dual-layer-frontend-facade/target/hateoas-dual-layer-frontend-facade-0.0.1-SNAPSHOT.war &
```

or

```shell
mvnw clean package
start "" java -jar hateoas-dual-layer-backend/target/extended-hateotas-rest-0.0.1-SNAPSHOT.war
start "" java -jar hateoas-dual-layer-frontend-facade/target/hateoas-dual-layer-frontend-facade-0.0.1-SNAPSHOT.war
```

to start the applications and navigate to [http://localhost:8000/swagger-ui.html](http://localhost:8000/swagger-ui.html) and [http://localhost:7000/swagger-ui.html](http://localhost:7000/swagger-ui.html)
to access Swagger UI for the Backend and Frontend Facade respectively.

### Tomcat deployment

* Download Tomcat 8.5 or above.
* Create (or open if it already exists) file ${catalina.home}/bin/setenv.{bat,sh} depending on operating system.
* On Windows: Add line `set "CATALINA_OPTS=%CATALINA_OPTS% -Dspring.profiles.active=tomcat"` to file setenv.bat.
* On Unix/Linux/MacOSX: Add line `export CATALINA_OPTS="$CATALINA_OPTS -Dspring.profiles.active=tomcat"` to file setenv.sh.
* Copy hateoas-dual-layer-backend-0.0.1-SNAPSHOT.war to ${catalina.home}/webapps directory and rename it to backend.war
* Copy hateoas-dual-layer-frontend-facade-0.0.1-SNAPSHOT.war to ${catalina.home}/webapps directory and rename it to frontend-facade.war
* Start Tomcat using startup.{bat,sh} or catalina.{bat,sh} startup script depending in your operating system and personal preference.
* Navigate to [http://localhost:8080/backend/swagger-ui.html](http://localhost:8080/backend/swagger-ui.html) and [http://localhost:8080/frontend-facade/swagger-ui.html](http://localhost:8080/frontend-facade/swagger-ui.html) 
to access Swagger UIs for both APIs.

