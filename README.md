# HMS - Hospital Management System (Group 2)

A **modern, Java-based Hospital Management System** built using **JavaFX**, **JDBC**, and **SQL Server**.  
This project streamlines hospital operations with a responsive dashboard, real-time statistics, and comprehensive patient/doctor management.

---

## Table of Contents
- [Overview](#-overview)
- [Key Features](#-key-features)
- [Technologies Used](#-technologies-used)
- [Project Structure](#-project-structure)
- [Installation & Setup](#-installation--setup)
- [Running the Project](#-running-the-project)
- [License](#-license)

---

## Overview

**MedFlow** replaces traditional paper-based records with a sleek, digital solution. It features a "SaaS-style" dashboard that gives administrators an immediate overview of hospital health.

It allows hospital staff to:
- **Visualize Data:** View real-time charts and status cards for patients, doctors, and billing.
- **Manage Records:** Perform full CRUD (Create, Read, Update, Delete) operations on patients and doctors.
- **Handle Billing:** Create invoices and track payment statuses (Paid/Pending).
- **Personalize:** Switch between **Dark Mode** and **Light Mode** seamlessly.

---

## Key Features

### **Modern Dashboard**
- **Live Statistics Cards:** Real-time counts of Total Patients, Available Doctors, and Pending Bills.
- **Financial Analytics:** Pie chart visualization of billing status.
- **Recent Activity Feed:** List of the most recently registered patients.
- **Quick Actions:** One-click navigation for common tasks.

### **Patient & Doctor Management**
- **Registration:** Add new patients and doctors with ease.
- **Editing:** Update existing records (Names, Specialties, Age, etc.) via pop-up dialogs.
- **Deletion:** Remove outdated records securely.
- **Assignment:** Assign specific doctors to patients.

### **UI & Experience**
- **Theming:** Toggle between a professional **Dark Theme** and a clean **Light Theme**.
- **Responsive Design:** Sidebar navigation with a collapsible/expandable layout.
- **Animations:** Subtle hover effects on buttons and cards for a polished feel.
- **Login System:** Secure entry point for administrators.

---

## Technologies Used

| Component | Technology |
|------------|-------------|
| **Language** | Java (JDK 21+) |
| **Frontend** | JavaFX (Controls, FXML, CSS) |
| **Database** | Microsoft SQL Server |
| **Connectivity** | JDBC (mssql-jdbc) |
| **Architecture** | MVC (Model-View-Controller) Pattern |
| **Styling** | Custom CSS (`dark-theme.css`, `light-theme.css`) |

---

## Project Structure

```text
hospital-managent-system
C:.
│   output.txt
│   patient.ser
│   patients.ser
│   patients_report.txt
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
├───src
│   ├───backend
│   │   │   Billing.java
│   │   │   BillingDAO.java
│   │   │   DatabaseConnection.java
│   │   │   Doctor.java
│   │   │   DoctorDAO.java
│   │   │   HospitalClient.java
│   │   │   HospitalServer.java
│   │   │   HospitalService.java
│   │   │   HospitalServiceImpl.java
│   │   │   LoginDAO.java
│   │   │   Patient.java
│   │   │   PatientDAO.java
│   │   │   PatientFileExporter.java
│   │   │   PatientHistoryDAO.java
│   │   │   PatientSerializationExporter.java
│   │   │   PatientSerializationLoader.java
│   │   │   TestDatabase.java
│   │   │
│   │   └───models
│   │           PatientModel.java
│   │
│   └───gui
│           HospitalDashboard.java
│           LoginPage.java
│
└───styles
        dark-theme.css
        light-theme.css
```
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

---

## Notes

- Ensure the RMI server is running before launching the client.
- Make sure the correct JDBC driver and JavaFX SDK paths are configured in your IDE.
- If you face module errors, check that JavaFX libraries are correctly referenced.
- Login Credentials: Ensure you have the correct admin credentials in your LoginDAO or database.

---