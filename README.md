# Moments to remember

Full-stack application for event organization, consisting of a web platform for organizers and a mobile/web app for customers.

## ✨ Features
### 👤 User Management
- User login (Kupac / Organizator – Buyer / Organizer)
- Profile management (update personal data and password)
### 🛍️ Customer (Kupac)
- Browse events with image, title, and short description
- View event offers with pagination (or scroll on mobile)
- Detailed event view (image, description, price, ratings, comments)
- Leave comments and rate events (star-based rating system)
- View agency information and contact details
- Receive notifications about booking status
### 🛒 Booking System
- Schedule events by selecting date and number of guests
- Add multiple events to cart
- View and manage cart (add/remove events)
- Confirm bookings from cart
### 🔔 Notifications
- Receive updates when bookings are approved or rejected
### 🧑‍💼 Organizer (Organizator)
- Approve or reject customer bookings
- Create and manage events (add image, name, description, price)

### 💾 Data Storage & Backend
- Backend implemented using Node.js
- RESTful API for handling application logic and communication
- NoSQL database (MongoDB) used for persistent data storage
- Data models for users, events, bookings, and reviews
- API communication

## Web application screenshots
<img width="625" height="280" alt="Screenshot 2026-03-29 223003" src="https://github.com/user-attachments/assets/a499775e-8d18-489a-a721-a6ad72252866" />
<img width="625" height="280" alt="Screenshot 2026-03-29 215741" src="https://github.com/user-attachments/assets/9c6d268b-b9d8-40cc-bd08-3484293dc6be" />
<img width="625" height="280" alt="Screenshot 2026-03-29 215728" src="https://github.com/user-attachments/assets/f679476a-932f-406e-8bf2-82643c314477" />

## Mobile application screenshots

<img width="200" src="https://github.com/user-attachments/assets/1edb5648-2258-49f2-be80-28732807cde7" />
<img width="200" src="https://github.com/user-attachments/assets/c8fe0e1e-44e1-47b8-ae46-f443be3e0975" />
<img width="200" src="https://github.com/user-attachments/assets/c361794a-c548-4c20-90eb-dc282aeb4a85" />
<img width="200" src="https://github.com/user-attachments/assets/78c8132b-475a-4038-8ef5-60984b15d203" />

## 🛠️ Tech Stack
### 🔧 Backend
- Node.js 
- Express.js 
- MongoDB – NoSQL database
- Mongoose 
- dotenv (.env) – environment variable management
- REST API 

### 🌐 Web Frontend
- React.js  
- Next.js 
- Tailwind CSS 

### 📱 Mobile Application
- Android (Kotlin)
- Jetpack Compose 
- REST API integration
### ⚙️ Additional Tools
- Git & GitHub – version control
- Postman – API testing
- Figma

## 🚀 Getting Started

### Backend
```bash
cd backend
npm install
npm run start
```
### Web
```bash
cd frontend_web
npm install
npm run dev
```
### Android
Open the Android project in Android Studio and run the app.



