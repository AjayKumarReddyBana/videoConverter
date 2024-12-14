# Video Conversion Application

## Overview

The Video Conversion Application is a web-based service that allows users to upload videos, monitor their conversion progress, and download the final processed files. The application provides three update mechanisms for users:

- **Progress Bar**: Real-time updates on the conversion progress via a visual progress bar.
- **Email Notifications**: Progress updates and completion notifications sent directly to the user's email.
- **API Tracking**: Allows users to check the status using the provided Job ID via a REST API.

This project demonstrates best practices in software development with features like WebSockets for real-time updates, email notifications, and event-driven architecture.

---

## Features

### 1. **Video Upload**
- Upload videos with metadata such as name, size, language, and email.
- Select the update mechanism (`progress_bar`, `email`, or `api`).

### 2. **Real-time Progress Updates**
- **WebSockets** provide instant updates via a progress bar.

### 3. **Email Notifications**
- Users can opt to receive email updates with the video conversion status and a download link upon completion.

### 4. **API Tracking**
- Users can query the status of their video conversion using a Job ID.

### 5. **Download Link**
- Once the conversion is complete, users receive a download link via email or the UI.

---

## Technologies Used

### **Backend**
- **Spring Boot**: Core framework for building RESTful APIs.
- **JavaMailSender**: For sending email notifications.
- **WebSockets**: For real-time progress updates.
- **Spring Data JPA**: For database interactions.
- **MySQL**: Persistent data storage.

### **Frontend**
- **HTML/CSS/JavaScript**: For the user interface.
- **SockJS and Stomp.js**: WebSocket client libraries.

### **Testing**
- **JUnit 5**: Unit and integration testing.
- **WebTestClient**: For API testing.

---

## Setup Instructions

### 1. **Clone the Repository**
```bash
git clone https://github.com/AjayKumarReddyBana/videoConverter.git
cd videoConverter
```

### 2. **Backend Setup**

#### Prerequisites
- Java 17 or higher
- Maven
- MySQL database

#### Configuration
- Update the `application.properties` file in the `src/main/resources/` directory with your database and email configuration:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/video_conversion
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@example.com
spring.mail.password=your_email_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

#### Run the Backend
```bash
mvn clean install
mvn spring-boot:run
```
The backend will start at `http://localhost:8080`.

---

### 3. **Frontend Setup**

- Open the `src/main/resources/static/index.html` file in your browser to access the UI.
- Ensure the backend is running before using the application.

---

## API Endpoints

### **1. Upload Video**
```http
POST /api/demo/uploadVideo
```
**Payload:**
```json
{
  "name": "Video Name",
  "size": 50,
  "language": "English",
  "email": "user@example.com",
  "updateType": "progress_bar"
}
```

**Response:**
```json
{
  "message": "Video uploaded successfully with job id: 93. Total estimated time: 25 seconds.",
  "id": 93
}
```

---

### **2. Track Progress**
```http
GET /api/demo/getStatus?id={jobId}
```
**Response:**
```json
{
  "message": "Progress: 75%"
}
```
or
```json
{
  "message": "Conversion complete. Download at: http://example.com/download/93"
}
```

---

## Real-time Updates via WebSocket
- Subscribe to updates using the endpoint:
```javascript
const socket = new SockJS('http://localhost:8080/api/demo/ws-progress');
const stompClient = Stomp.over(socket);
stompClient.connect({}, () => {
    stompClient.subscribe('/topic/progress/{jobId}', (message) => {
        console.log('Progress:', message.body);
    });
});
```

---

## Integration Tests

Integration tests are located in the `src/test/java/com/mizutest/demo` directory. These tests:
1. Spin up the server.
2. Simulate API calls for video upload, tracking, and progress updates.
3. Print logs for each step of the way.

Run tests with:
```bash
mvn test
```

---

## Enhancements

### Potential Improvements
- **Integrate Kafka**: Replace WebSockets with Kafka for real-time messaging to handle high traffic.
- **Scalability**: Deploy the application on cloud platforms like AWS or Azure for better scalability.
- **User Authentication**: Add OAuth2 or JWT-based authentication for secure access.
- **Upload Resumption**: Support for paused and resumed uploads using a chunk-based approach.
- **Video Previews**: Allow users to preview converted videos before downloading.
- **Analytics Dashboard**: Provide metrics like conversion times, failure rates, and user engagement.

---

## Contributing

### Pull Requests
- Fork the repository.
- Create a new branch for your feature.
- Submit a pull request.

---

## License
This project is licensed under the MIT License. See the LICENSE file for details.

---

## Contact
For questions or support:
- **Email**: ajaykumareddybana@gmail.com
- **GitHub**: [AjayKumarReddyBana](https://github.com/AjayKumarReddyBana)

--- 

This README provides a comprehensive guide to setting up, using, and extending your Video Conversion Application. Let me know if you'd like further refinements!
