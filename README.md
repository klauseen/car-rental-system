<div align="center">
<h1 style="font-size: 3em; font-style: italic; font-weight: bold;">
Car Rental System
</h1>
<img src="images/logo.png" alt="Main Preview" width="200">
</div>
<h2 style = "font-size: 1.5em; font-weight: bold">
📝 Description
</h2>
<p>
Car Rental System is a desktop application designed to automate the workflow of a car rental agency. It emphasizes a user-friendly interface, security, and efficiency in fleet management.
</p>
<h2 style = "font-size: 1.5em; font weight: bold">
📺 Demonstration
</h2>
<div align="center">
<img src="images/demonstration.gif" alt ="Demonstration">
</div>

<h2 style="font-size: 1.5em; font-weight: bold; border-bottom: 1px solid #eaecef;">
  🚀 Installation & Setup
</h2>
<p>
  To run this application locally, follow these steps:
</p>
<ol>
  <li>
    <b>Create the Database:</b> Open your MySQL terminal or phpMyAdmin and run:
    <br>
    <code>CREATE DATABASE car_rental;</code>
  </li>
  <li>
    <b>Configure Connection:</b> Open <code>src/main/resources/db.properties</code> (or your database connection file) and update your credentials:
    <br>
    <code>db.url=jdbc:mysql://localhost:3306/car_rental</code><br>
    <code>db.username = root</code><br>
    <code>db.password = YOUR_PASSWORD_HERE</code>
  </li>
  <li>
    <b>Run the App:</b> Launch the <code>Login.java</code> file. The system will automatically generate the required tables.
  </li>
</ol>

<h2 style = "font-size: 1.5em; font-weight: bold">
🛠 Technologies used
</h2>
<p>
<b>Language used:</b> Java 17
<br>
<b>GUI Framework:</b> Java Swing
<br>
<b>Database:</b> MySQL 8.0
<br>
<b>Dependency Management:</b> Apache Maven
<br>
<b>Security:</b> jBcrypt for password hashing
</p> 


  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" alt="Maven">

  <h2 style = "font-size: 1.5em; font-weight: bold">
  🔑 Key Features
  </h2>
  <p>
  <b>Automated Database Setup:</b> Tables are automatically initialized on the first run.
  <br>
  <b>User Authentication:</b> Secure login system with encrypted credentials.
  <br>
  <b>Fleet Management:</b>
  Add, update and manage vehicle details with ease.
  <br>
  <b>Rental Processing:</b> Calculate total cost and manage bookings efficiently.
  </p>
