# MobileHOA

This is a Spring MVC project that provides a platform for managing various aspects of a homeowners association (HOA) such as resident information, payments, community incidents, bills, and requests.

The project is built using the following technologies:

- Java 19
- Spring 6
- Thymeleaf
- MS SQL Server
- JavaScript

## Features

- **User authentication**: the program utilizes Google oauth2 to authenticate users and grants them access to their profiles.
- **Profile editing**: users can edit their profiles and update their personal information, such as their name, email, and phone number.
- **Error handling**: the program handles errors that may occur during profile or address editing and displays appropriate error messages to the user.
- **Session management**: the program uses HTTP sessions to manage user information and maintain state across multiple requests.
- **Security**: the program may have implemented security measures to protect user information and prevent unauthorized access to pages that are predetermined by their roles.
- **Incidents**: handles incidents reported by users. It allows users to create and submit new incidents, view their own incidents, and view all incidents marked on a dynamic Google map.
- **Bills**: automatically generates bills for users on a monthly basis. Allows users to view their own bills, view their payment history, and pay their bills online. It also allows administrators to view and manage bills.
- **Requests**: allows users to create and submit new requests, view their own requests, and view all requests. It also allows administrators to view and manage requests.
- **Payments**: handles payment processing for bills. It allows users to enter their payment information, view their payment history, and view their pending payments.

## Preview

To preview the project, please visit [https://mobile-hoa.com/](https://mobile-hoa.com/)

## Configuration

To connect with local Microsoft SQL Server (Windows authentication):

1. Download SSMS, SQL Server (developer).
2. SQL server configuration: enable all SQL server services. Under SQL server network configuration enable TCP/IP and shared memory. TCP/IP properties allip -> 1433.
3. Add SQL Server JDBC driver to POM.
4. Add SQL server JDBC driver (`mssql-jdbc_auth.dll`) from [Microsoft's website](https://docs.microsoft.com/en-us/sql/connect/jdbc/building-the-connection-url?redirectedfrom=MSDN&view=sql-server-ver16) to the Java JDK bin folder.
5. Specify data sources.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
