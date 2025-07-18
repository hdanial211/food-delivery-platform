# 🚀 Restaurant Frontend & Backend Integration Guide

## 📋 Overview
This guide explains how to integrate the restaurant frontend with the Java backend for the Food Delivery Platform.

## 🏗️ Architecture
```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   Restaurant    │────▶│   Java Backend  │────▶│   MySQL         │
│   Frontend      │◀────│   API Server    │◀────│   Database      │
│   (Port 8000)   │     │   (Port 8080)   │     │   (Port 3306)   │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

## 🔧 Prerequisites

### 1. MySQL Database Setup
```sql
-- Create database
CREATE DATABASE food_delivery;

-- Create user (optional)
CREATE USER 'food_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON food_delivery.* TO 'food_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Java Backend Requirements
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

### 3. Frontend Requirements
- Python 3.6+ (for HTTP server)
- Modern web browser

## 🚀 Quick Start

### Step 1: Configure Database
1. Install MySQL if not already installed
2. Create database: `food_delivery`
3. Update `myproject/config.json` with your database credentials

### Step 2: Start Backend Server
```bash
cd myproject
mvn compile exec:java -Dexec.mainClass="com.fooddeliveryplatform.Main"
```

### Step 3: Start Frontend Server
```bash
cd restaurant
python3 -m http.server 8000
```

### Step 4: One-Command Start (Recommended)
```bash
./run-servers.sh
```

## 🔗 API Endpoints

### Authentication
- `POST /api/auth/login` - Restaurant login
- `POST /api/auth/register` - Restaurant registration

### Restaurant Management
- `GET /api/restaurants` - Get all restaurants
- `GET /api/restaurants/{id}` - Get specific restaurant
- `POST /api/restaurants` - Create new restaurant
- `PUT /api/restaurants/{id}` - Update restaurant
- `DELETE /api/restaurants/{id}` - Delete restaurant

### Menu Management
- `GET /api/fooditems` - Get all menu items
- `GET /api/fooditems/{id}` - Get specific menu item
- `POST /api/fooditems` - Create new menu item
- `PUT /api/fooditems/{id}` - Update menu item
- `DELETE /api/fooditems/{id}` - Delete menu item

### Order Management
- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get specific order
- `POST /api/orders` - Create new order
- `PUT /api/orders/{id}` - Update order status
- `DELETE /api/orders/{id}` - Delete order

## 📱 Frontend Features

### 1. Restaurant Dashboard
- **Real-time statistics**: Orders, revenue, pending orders
- **Recent orders**: Quick overview of latest orders
- **Responsive design**: Works on desktop, tablet, and mobile

### 2. Order Management
- **Status tracking**: PENDING → ACCEPTED → PREPARING → READY → DELIVERED
- **Order details**: Customer info, items, total amount
- **Status updates**: One-click status changes

### 3. Menu Management
- **CRUD operations**: Create, Read, Update, Delete menu items
- **Availability toggle**: Quick enable/disable items
- **Image support**: Upload and display item images
- **Categories**: Organize items by category

### 4. Restaurant Profile
- **Basic info**: Name, description, operating hours
- **Delivery settings**: Radius, active status
- **Logo management**: Upload restaurant logo

## 🔐 Authentication Flow

### Login Process
1. Restaurant owner visits `http://localhost:8000/login.html`
2. Enters credentials (demo: `restaurant@demo.com` / `demo123`)
3. Frontend sends POST request to `/api/auth/login`
4. Backend returns JWT token
5. Token stored in localStorage for subsequent requests

### Protected Routes
All API endpoints require JWT token in Authorization header:
```
Authorization: Bearer <jwt-token>
```

## 🎨 Frontend Structure

```
restaurant/
├── index.html          # Main dashboard
├── login.html          # Login page
├── styles.css          # Styling
├── script.js           # JavaScript functionality
└── README.md           # Documentation
```

## 🐛 Troubleshooting

### Common Issues

#### 1. Backend won't start
```bash
# Check if MySQL is running
sudo systemctl status mysql

# Check config.json exists
ls -la myproject/config.json

# Check MySQL credentials
mysql -u root -p -e "SHOW DATABASES;"
```

#### 2. Frontend can't connect to backend
```bash
# Check if backend is running
curl http://localhost:8080/api/restaurants

# Check CORS settings (should be handled by backend)
```

#### 3. Database connection issues
```bash
# Test MySQL connection
mysql -u root -p food_delivery -e "SELECT 1;"

# Check database exists
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS food_delivery;"
```

#### 4. Port conflicts
```bash
# Check what's using port 8080
lsof -i :8080

# Check what's using port 8000
lsof -i :8000
```

## 📊 Testing the Integration

### 1. Test Backend API
```bash
# Test restaurant endpoint
curl http://localhost:8080/api/restaurants

# Test menu items endpoint
curl http://localhost:8080/api/fooditems
```

### 2. Test Frontend
1. Open browser to `http://localhost:8000/login.html`
2. Login with demo credentials
3. Navigate through dashboard sections
4. Try adding a menu item
5. Test order status updates

### 3. Test Full Flow
1. Create restaurant profile
2. Add menu items
3. Simulate customer orders
4. Update order status
5. Check analytics

## 🔧 Development Tips

### Backend Development
- Use Postman to test API endpoints
- Check logs in terminal for errors
- Database migrations run automatically on startup

### Frontend Development
- Use browser DevTools for debugging
- Check Network tab for API calls
- Console logs for JavaScript errors

### Database Development
- Use MySQL Workbench or similar tool
- Check migration files in `myproject/src/main/resources/db/migration/`
- Manual testing with SQL queries

## 🚀 Production Deployment

### Backend
1. Build JAR: `mvn package`
2. Deploy to server with Java 17+
3. Configure production database
4. Set environment variables for sensitive config

### Frontend
1. Build optimized version
2. Deploy to CDN or web server
3. Configure API base URL
4. Set up SSL certificates

## 📞 Support

For issues:
1. Check browser console for frontend errors
2. Check terminal output for backend errors
3. Verify database connectivity
4. Ensure all ports are available

## 🎯 Next Steps

1. **Add authentication endpoints** to backend
2. **Implement real-time updates** with WebSocket
3. **Add analytics dashboard** with charts
4. **Implement file upload** for restaurant logos
5. **Add multi-language support**
6. **Implement push notifications**

## 📋 Quick Commands Reference

```bash
# Start everything
./run-servers.sh

# Start backend only
cd myproject && mvn compile exec:java -Dexec.mainClass="com.fooddeliveryplatform.Main"

# Start frontend only
cd restaurant && python3 -m http.server 8000

# Stop all servers
Ctrl+C in terminal
```

## ✅ Success Checklist

- [ ] MySQL database created and accessible
- [ ] Backend server running on port 8080
- [ ] Frontend server running on port 8000
- [ ] Can access login page at http://localhost:8000/login.html
- [ ] Can login and access dashboard
- [ ] Can view/add menu items
- [ ] Can view/update orders
- [ ] Can update restaurant profile
