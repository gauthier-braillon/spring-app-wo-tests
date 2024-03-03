## Get started

### 1. Set an API Key as Environment Variable
In order to run the service, you need to set the `WEATHER_API_KEY` environment variable to a valid API key retrieved from ~~darksky.net~~ [openweathermap.org](https://openweathermap.org/).

_Note: in a previous version this example used darksky.net as the weather API. Since they've shut down their API for public access, we've since switched over to openweathermap.org_

A simple way is to rename the `env.sample` file to `.env`, fill in your API key from _openweathermap.org_ and source it before running your application:

```bash
source .env
```

### 2. Start a PostgreSQL database
The easiest way is to use the provided `startDatabase.sh` script. This script starts a Docker container which contains a database with the following configuration:
    
  * port: `15432`
  * username: `testuser`
  * password: `password`
  * database name: `postgres`
  
If you don't want to use the script make sure to have a database with the same configuration or modify your `application.properties`.

### 3. Run the Application
Once you've provided the API key and started a PostgreSQL database you can run the application using

```bash
./gradlew bootRun
```

The application will start on port `8080` so you can send a sample request to `http://localhost:8080/hello` to see if you're up and running.


## Application Architecture

```
 ╭┄┄┄┄┄┄┄╮      ┌──────────┐      ┌──────────┐
 ┆   ☁   ┆  ←→  │    ☕     │  ←→  │    💾     │
 ┆  Web  ┆ HTTP │  Spring  │      │ Database │
 ╰┄┄┄┄┄┄┄╯      │  Service │      └──────────┘
                └──────────┘
                     ↑ JSON/HTTP
                     ↓
                ┌──────────┐
                │    ☁     │
                │ Weather  │
                │   API    │
                └──────────┘
```

The sample application is almost as easy as it gets. It stores `Person`s in an in-memory database (using _Spring Data_) and provides a _REST_ interface with three endpoints:

  * `GET /hello`: Returns _"Hello World!"_. Always.
  * `GET /hello/{lastname}`: Looks up the person with `lastname` as its last name and returns _"Hello {Firstname} {Lastname}"_ if that person is found.
  * `GET /weather`: Calls a downstream [weather API](https://openweathermap.org/current#name) via HTTP and returns a summary for the current weather conditions in Hamburg, Germany

### Internal Architecture
The **Spring Service** itself has a pretty common internal architecture:

  * `Controller` classes provide _REST_ endpoints and deal with _HTTP_ requests and responses
  * `Repository` classes interface with the _database_ and take care of writing and reading data to/from persistent storage
  * `Client` classes talk to other APIs, in our case it fetches _JSON_ via _HTTP_ from the openweathermap.org weather API


  ```
  Request  ┌────────── Spring Service ───────────┐
   ─────────→ ┌─────────────┐    ┌─────────────┐ │   ┌─────────────┐
   ←───────── │  Controller │ ←→ │  Repository │←──→ │  Database   │
  Response │  └─────────────┘    └─────────────┘ │   └─────────────┘
           │         ↓                           │
           │    ┌──────────┐                     │
           │    │  Client  │                     │
           │    └──────────┘                     │
           └─────────│───────────────────────────┘
                     │
                     ↓   
                ┌──────────┐
                │    ☁     │
                │ Weather  │
                │   API    │
                └──────────┘
  ```  