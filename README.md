Spring Boot backend for an Airbnb App, providing REST APIs for user management, property listings, and bookings.![AirBnb Schema Design_page-0001](https://github.com/user-attachments/assets/49965787-f735-456f-b1e5-4bcb26930588)

AirBnb Schema Design
![AirBnb Schema Design](https://github.com/user-attachments/assets/a89a4cea-e988-41d4-9098-f0e0d266d32a)
AirBnb Work Flow
![AirBnb Work Flow](https://github.com/user-attachments/assets/650417fe-29db-4ca2-877e-775c8764f893)

Overview
This project is a full-featured Hotel Management and Booking System inspired by Airbnb. It provides users with the ability to browse, book, and manage hotel reservations seamlessly. The system is built with Spring Boot for backend services and integrates Stripe for secure payment processing.

Features
-> User Authentication & Authorization
1.Implemented using Spring Security
2.Supports role-based access for Admins, Hotel Managers, and Customers

-> Hotel Management
1.Add, edit, and delete hotels and rooms
2.View availability and manage bookings

-> Booking System
1.Users can book rooms with real-time availability
2.Booking history and management dashboard for users

-> Payment Gateway Integration
1.Stripe API integration for secure online payments
2.Supports one-time payments and refund processing

-> RESTful API Architecture
1.Backend implemented with Spring Boot
2.Provides endpoints for hotel listings, bookings, payments, and user management

-> Database
1.Persistent storage using MySQL (or PostgreSQL)
2.Entity relationships designed for hotels, rooms, users, and bookings

-> Security
1.Password hashing and JWT token-based authentication
2.Role-based access control for sensitive operations

-> Technologies Used
1.Backend: Spring Boot, Spring Security, Spring Data JPA
2.Database: MySQL / PostgreSQL
3.Payment: Stripe API
4.Authentication: JWT Tokens
5.Build Tools: Maven / Gradle
