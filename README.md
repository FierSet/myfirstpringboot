# My First Spring Boot

A Spring Boot URL shortener with a Thymeleaf web page, REST endpoints, and a SQLite database.

## Requirements

- JDK 25
- The included Maven wrapper (`mvnw.cmd` on Windows, `./mvnw` on Linux/macOS)

## Build and run locally

From the project root, build and test:

```powershell
.\mvnw.cmd clean package
```

Run the packaged application:

```powershell
java -jar .\target\myfirstpringboot-0.0.1-SNAPSHOT.jar
```

Open [http://localhost:8080](http://localhost:8080).

The default SQLite URL is `jdbc:sqlite:./dblink.db`, so the database is created in the working directory when the app starts. Spring initializes the `Links` table from `src/main/resources/schema.sql`. To use a different database location, set `SQLITE_URL`, for example:

```powershell
$env:SQLITE_URL = "jdbc:sqlite:C:/data/dblink.db"
java -jar .\target\myfirstpringboot-0.0.1-SNAPSHOT.jar
```

## Deploy to Azure App Service manually

1. Build the executable JAR locally:

   ```powershell
   .\mvnw.cmd clean package
   ```

2. Make a ZIP containing the JAR at the ZIP root. For example, from the project root:

   ```powershell
   Compress-Archive -LiteralPath .\target\myfirstpringboot-0.0.1-SNAPSHOT.jar -DestinationPath .\target\mglshort-deploy.zip -CompressionLevel NoCompression
   ```

3. In the Azure Portal, open the App Service's **Deployment Center**, choose **Manual Deployment (Push)** and **Publish files**, select the ZIP, and save to upload it.

4. Configure the App Service to use a Java runtime compatible with the JDK version used to build this project. The current Maven configuration targets Java 25; if that runtime is unavailable in the App Service options, change `java.version` in `pom.xml` to an available supported version and rebuild.

5. If the App Service does not start the application automatically, set its startup command to:

   ```text
   java -jar /home/site/wwwroot/myfirstpringboot-0.0.1-SNAPSHOT.jar
   ```

6. In **Settings → Environment variables**, set `SQLITE_URL` to a SQLite file path on persistent App Service storage, such as:

   ```text
   jdbc:sqlite:/home/dblink.db
   ```

   Ensure persistent storage is enabled for the App Service. The app creates the database and schema on startup. The local `dblink.db` and its records are not included in the deployment ZIP; migrate them separately if you need to keep that data.

The App Service restarts after a manual upload. Check the App Service deployment and application logs if the deployment or startup fails.
