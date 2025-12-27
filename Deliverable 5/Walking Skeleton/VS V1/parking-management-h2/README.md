# Parking Management System

This project implements a Parking Management System using Spring Boot and H2 database. It provides functionalities for managing users, vehicles, and parking spots.

## Project Structure

```
parking-management-h2
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── parking
│   │   │               ├── ParkingManagementApplication.java
│   │   │               ├── config
│   │   │               │   └── H2Config.java
│   │   │               ├── controller
│   │   │               │   └── HealthController.java
│   │   │               ├── model
│   │   │               │   ├── User.java
│   │   │               │   ├── Vehicle.java
│   │   │               │   └── ParkingSpot.java
│   │   │               ├── repository
│   │   │               │   ├── UserRepository.java
│   │   │               │   └── VehicleRepository.java
│   │   │               └── service
│   │   │                   └── ParkingDataService.java
│   │   └── resources
│   │       ├── application.properties
│   │       ├── schema.sql
│   │       └── data.sql
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── parking
│                       └── ParkingManagementApplicationTests.java
├── pom.xml
└── README.md
```

## Setup Instructions

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd parking-management-h2
   ```

2. **Build the project**:
   Make sure you have Maven installed. Run the following command to build the project:
   ```bash
   mvn clean install
   ```

3. **Run the application**:
   You can run the application using the following command:
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**:
   Once the application is running, you can access it at `http://localhost:8080`.

## Usage

- The application allows users to manage parking spots, vehicles, and service requests.
- You can check the health status of the application by accessing the `/health` endpoint.

## Database

The project uses an H2 in-memory database. The schema and initial data are defined in `src/main/resources/schema.sql` and `src/main/resources/data.sql`, respectively.

## Testing

Unit tests are provided in the `src/test/java/com/example/parking/ParkingManagementApplicationTests.java` file to ensure the application context loads correctly.

## Dependencies

The project uses the following dependencies:
- Spring Boot
- Spring Data JPA
- H2 Database

Refer to the `pom.xml` file for a complete list of dependencies and their versions.