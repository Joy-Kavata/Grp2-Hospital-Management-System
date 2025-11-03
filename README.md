# Hospital Management System (Group 2)

A **Java-based Hospital Management System** built using **JavaFX**, **RMI (Remote Method Invocation)**, and **SQL Server** for database management.  
This project helps in managing hospital operations such as patient registration, doctor management, billing, and patient history tracking.

---

##  Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Installation & Setup](#installation--setup)
- [Running the Project](#running-the-project)
- [License](#license)

---

##  Overview

The Hospital Management System simplifies how hospitals manage their data.  
It allows hospital staff to:
- Register patients and doctors  
- View and update patient records  
- Generate bills  
- Manage patient history  
- Access a graphical dashboard through JavaFX

All operations are handled using **RMI** for client-server communication and **SQL Server** for data storage.

---

##  Features

- **Doctor Management** — Add, update, and view doctors  
- **Patient Management** — Register and manage patient details  
- **Billing System** — Generate and view billing records  
- **Patient History** — Store and retrieve patient history  
- **Dashboard Interface** — Interactive GUI with JavaFX  
- **RMI-based Architecture** — Client-server communication  
- **SQL Server Integration** — Reliable data persistence  

---

## Technologies Used

| Component | Technology |
|------------|-------------|
| Programming Language | Java (JDK 11 or higher) |
| GUI Framework | JavaFX |
| Database | Microsoft SQL Server |
| Communication | Java RMI (Remote Method Invocation) |
| Styling | CSS (dark-theme.css) |
| JDBC Driver | mssql-jdbc-13.2.0.jre11.jar |

---

## Project Structure
hospital-managent-system
│   README.md
│   run.bat
│   
├───javafx
│   └───lib
│           javafx-swt.jar
│           javafx.base.jar
│           javafx.controls.jar
│           javafx.fxml.jar
│           javafx.graphics.jar
│           javafx.media.jar
│           javafx.properties
│           javafx.swing.jar
│           javafx.web.jar
│           jdk.jsobject.jar
│           jfx.incubator.input.jar
│           jfx.incubator.richtext.jar
│
├───lib
│       mssql-jdbc-13.2.0.jre11.jar
│
├───out
│   ├───backend
│   │       Billing.class
│   │       BillingDAO.class
│   │       DatabaseConnection.class
│   │       Doctor.class
│   │       DoctorDAO.class
│   │       HospitalClient.class
│   │       HospitalServer.class
│   │       HospitalService.class
│   │       HospitalServiceImpl.class
│   │       Patient.class
│   │       PatientDAO.class
│   │       PatientHistoryDAO.class
│   │       TestDatabase.class
│   │
│   └───gui
│           HospitalDashboard.class
│
├───src
│   ├───backend
│   │       Billing.java
│   │       BillingDAO.java
│   │       DatabaseConnection.java
│   │       Doctor.java
│   │       DoctorDAO.java
│   │       HospitalClient.java
│   │       HospitalServer.java
│   │       HospitalService.java
│   │       HospitalServiceImpl.java
│   │       Patient.java
│   │       PatientDAO.java
│   │       PatientHistoryDAO.java
│   │       TestDatabase.java
│   │
│   └───gui
│           HospitalDashboard.java
│
└───styles
        dark-theme.css

---

## Installation & Setup

###  Prerequisites
Make sure you have:
- **Java JDK 11+**
- **SQL Server** installed and running
- **JavaFX SDK** (already included in the `javafx/lib` folder)
- An IDE like **IntelliJ IDEA**, **Eclipse**, or **VS Code (with Java Extension Pack)**

---

### Clone the Project
```bash
git clone https://github.com:MarsMwau/Grp2-Hospital-Management-System.git
cd hospital-management-system
```

---

## Running the Project

### Using the Run Script

- Simply double-click run.bat or run:
```bash
run.bat
```

---

## License

- This project is for academic use and built as part of a group coursework project.

## Notes

- Ensure the RMI server is running before launching the client.
- Make sure the correct JDBC driver and JavaFX SDK paths are configured in your IDE.
- If you face module errors, check that JavaFX libraries are correctly referenced.
