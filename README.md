# Loggar - Log Consolidator

Loggar is a Spring Boot project designed to consolidate logs and messages from various sources. It provides a RESTful API for users to send system log data or messages, which are then stored in Redis. A background job periodically picks up these logs from Redis and sends the logs to Elasticsearch for centralized storage and analysis before cleaning up the Redis list.

Built as a way to consolidate messages and errors across multiple projects, into one ElasticCloud deployment, to make monitoring all projects easier.

## Features

- **REST API Endpoints:** Exposes endpoints for users to send system log data.
- **Redis Storage:** Logs are temporarily stored in Redis for efficient handling, and data integrity.
- **Background Job:** A scheduled job retrieves logs from Redis and sends them to Elasticsearch, before clearing the redis queue.
- **Retry Mechanism:** Implements a retry mechanism for handling failures when sending logs to Elasticsearch.
- **Testing:** Includes both unit and integration tests to ensure robust functionality.
- **Docker:** Production version runs on DigitalOcean using Docker to manage the environment
- **CI/CD:** Github Actions automates the build and testing process, making pushing updates easy and efficient.

## Docker
To run locally clone repo and run the app:

#### Build Project:
```bash
mvn clean install -Plocal
```

#### Spin up app and redis instance using Docker
```bash
docker-compose up -d --build
```

## Usage
### Example Curl Command to Send Log Entry

```bash
curl -X POST \
  http://localhost:8080/api/v1/log \
  -H 'Content-Type: application/json' \
  -d '{
    "message": "Example Log Message",
    "level": "INFO",
    "client": "ExampleClient"
}'
```

### Example Curl Command to Batch Send Log Entries

```bash
curl -X POST \
  http://localhost:8080/api/v1/logs \
  -H 'Content-Type: application/json' \
  -d '[
    {"message": "Example Log Message 1",
	"level": "INFO",
    "client": "ExampleClient" 
    },
    {"message": "Example Log Message 2",
	"level": "INFO",
    "client": "ExampleClient"
    },
    {"message": "Example Log Message 3",
	"level": "INFO",
    "client": "ExampleClient"
    }
]'
```

