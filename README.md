
# Delivery Fee Calculator API

This API calculates delivery fees based on weather conditions, vehicle type, and city. It provides a single endpoint to calculate the delivery fee.

## Endpoints

### POST /api/delivery-fee

This endpoint calculates the delivery fee based on the provided `City` and `VehicleType`.

#### Request Body

```json
{
  "city": "TALLINN",
  "vehicleType": "BIKE"
}
```

- **city**: The city for which the delivery fee is calculated. Possible values:
  - `TALLINN`
  - `TARTU`
  - `PARNU`
  
- **vehicleType**: The type of vehicle used for delivery. Possible values:
  - `CAR`
  - `SCOOTER`
  - `BIKE`

#### Response (200 OK)

When the delivery fee is successfully calculated:

```json
{
  "totalFee": 4.0
}
```

- **totalFee**: The calculated delivery fee.

#### Response (400 Bad Request)

If the selected vehicle type is forbidden for the given city or weather conditions:

```json
"Usage of selected vehicle type is forbidden"
```

### Example Requests

#### 1. Calculate fee for **TALLINN** and **BIKE**

**Request**:

```json
{
  "city": "TALLINN",
  "vehicleType": "BIKE"
}
```

**Response**:

```json
{
  "totalFee": 4.0
}
```

#### 2. Forbidden fee calculation for **TALLINN** and **BIKE** (due to weather or other rules)

**Request**:

```json
{
  "city": "TALLINN",
  "vehicleType": "BIKE"
}
```

**Response**:

```json
"Usage of selected vehicle type is forbidden"
```

### Error Codes

- **400 Bad Request**: Returned when the selected `vehicleType` is not allowed in the specified city due to weather conditions or other rules.
- **500 Internal Server Error**: Returned for unexpected errors during fee calculation.

## Running the Application

### Prerequisites

- Java 21
- Maven

### Steps to Run the Application

1. Clone the repository:

```bash
git clone https://github.com/your-username/fujitsu-delivery-fee-api.git
cd fujitsu-delivery-fee-api
```

2. Build the project using Maven:

```bash
mvn clean install
```

3. Run the Spring Boot application:

```bash
mvn spring-boot:run
```

4. The application will be accessible at `http://localhost:8080`.
