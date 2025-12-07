🚕 RideShare Backend

Spring Boot • MongoDB • JWT Authentication

A clean and secure mini ride-sharing backend built with Spring Boot and MongoDB. It supports JWT-based login, role-based access (User & Driver), proper validation, and global error handling.

⚙️ Tech Stack

Java + Spring Boot

Spring Data MongoDB

Spring Security (BCrypt + JWT)

Jakarta Bean Validation

MongoDB

✨ Core Features
🔐 Authentication & Users

User & Driver registration

Passwords encrypted with BCrypt

Login returns a JWT token containing:

Username

Role

Issue time

Expiry

🚕 Ride Management

Ride Fields

id

userId (passenger)

driverId

pickupLocation

dropLocation

status → REQUESTED, ACCEPTED, COMPLETED

createdAt

Role-Based Flow

✅ User can request a ride

✅ Driver can view & accept pending rides

✅ User or Driver can complete a ride

✅ User can view their ride history

🧱 Project Structure
src/main/java/org/example/rideshare
│
├── model        → User, Ride
├── repository   → UserRepository, RideRepository
├── dto          → RegisterRequest, LoginRequest, CreateRideRequest
├── service      → AuthService, RideService
├── controller   → AuthController, RideController
├── config       → SecurityConfig (BCrypt + Security Filter Chain)
├── util         → JwtUtil
└── exception    → GlobalExceptionHandler & Custom Exceptions

🔌 API Endpoints
🔓 Auth (Public)
➤ Register

POST /api/auth/register

{
  "username": "john",
  "password": "1234",
  "role": "ROLE_USER"
}

➤ Login

POST /api/auth/login

{
  "username": "john",
  "password": "1234"
}


✅ Response:

{ "token": "eyJhbGciOi..." }

🔐 Ride (JWT Required)

Header:

Authorization: Bearer <token>

Role	Method	Endpoint	Action
USER	POST	/api/v1/rides	Create ride
DRIVER	GET	/api/v1/driver/rides/requests	View pending rides
DRIVER	POST	/api/v1/driver/rides/{id}/accept	Accept ride
BOTH	POST	/api/v1/rides/{id}/complete	Complete ride
USER	GET	/api/v1/user/rides	View own rides
✅ Validation & Error Handling

DTO validation using:

@NotBlank

@Size

Centralized Global Exception Handler

All errors return clean JSON:

{
  "error": "BAD_REQUEST / NOT_FOUND / VALIDATION_ERROR",
  "message": "Clear error message",
  "timestamp": "2025-01-20T12:00:00Z"
}

▶️ How to Run

Start MongoDB:

mongodb://localhost:27017


Update config if needed:
src/main/resources/application.properties

Port

Database name

JWT secret

Run the app:

From IntelliJ → Run RideshareApplication

Or using Maven

✅ Server starts at:

http://localhost:8081

🚀 Why This Project Matters

Clean JWT-based authentication

Proper role-based access

Real-world ride booking workflow

Production-style architecture

Perfect for backend interviews & portfolio
