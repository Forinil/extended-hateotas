# HATEOASDualLayer
HATEOAS Dual Layer Proof-of-concept

## Modules
* `hateoas-dual-layer-backend` - REST HATEOAS API for single domain (here for simplicity one app for both Application and Security domain)
* `hateoas-dual-layer-frontend-facade` - REST HATEOAS front-end facade API

## Profiles

* `standalone` - run as standalone application
* `tomcat`- run in Tomcat server container
* `mock_data` - use mocked data
* `jpa_data` - use JPA to fetch data (must be used with one of database-specific profiles)
* `h2` - use h2 database (must be used in conjunction with profile `jpa_data`)
* `local_cache` - use local cache for caching query results
* `remote_cache` - use remote cache for caching query results

To run application correctly either `standalone` or `tomcat` profile and either `mock_data` or `jpa_data` profile 
must be enabled along with profile `local_cache` (profile `remote_cache` is not yet implemented). 
If `jpa_data` profile is enabled, one of the database-specific profiles
should be enabled as well.

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
start "" java -Dspring.profiles.active=standalone -jar hateoas-dual-layer-backend/target/hateoas-dual-layer-backend-0.0.1-SNAPSHOT.war
start "" java -Dspring.profiles.active=standalone -jar hateoas-dual-layer-frontend-facade/target/hateoas-dual-layer-frontend-facade-0.0.1-SNAPSHOT.war
```

to start the applications and navigate to [http://localhost:8000/swagger-ui.html](http://localhost:8000/swagger-ui.html) and [http://localhost:7000/swagger-ui.html](http://localhost:7000/swagger-ui.html)
to access Swagger UI for the Backend and Frontend Facade respectively.

### Tomcat deployment

#### By setting up local Tomcat installation

* Download Tomcat 8.5 or above.
* Create (or open if it already exists) file ${catalina.home}/bin/setenv.{bat,sh} depending on operating system.
* On Windows: Add line `set "CATALINA_OPTS=%CATALINA_OPTS% -Dspring.profiles.active=tomcat"` to file setenv.bat.
* On Unix/Linux/MacOSX: Add line `export CATALINA_OPTS="$CATALINA_OPTS -Dspring.profiles.active=tomcat"` to file setenv.sh.
* Copy hateoas-dual-layer-backend-0.0.1-SNAPSHOT.war to ${catalina.home}/webapps directory and rename it to backend.war
* Copy hateoas-dual-layer-frontend-facade-0.0.1-SNAPSHOT.war to ${catalina.home}/webapps directory and rename it to frontend-facade.war
* Start Tomcat using startup.{bat,sh} or catalina.{bat,sh} startup script depending in your operating system and personal preference.
* Navigate to [http://localhost:8080/backend/swagger-ui.html](http://localhost:8080/backend/swagger-ui.html) and [http://localhost:8080/frontend-facade/swagger-ui.html](http://localhost:8080/frontend-facade/swagger-ui.html) 
to access Swagger UIs for both APIs.

#### Via Cargo Plugin

Run 

```bash
./mvnw --projects hateoas-dual-layer-cargo-deployer cargo:run
```

or 

```shell
mvnw --projects hateoas-dual-layer-cargo-deployer cargo:run
```

depending on your operating system to automatically download, setup and run Tomcat 9.0.1 with both backend and frontend facade applications deployed.
