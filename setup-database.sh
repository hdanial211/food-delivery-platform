#!/bin/bash

# Script untuk setup database MySQL untuk Food Delivery Platform

echo "🗄️ Setting up MySQL database for Food Delivery Platform..."
echo ""

# Check if MySQL is installed
if ! command -v mysql &> /dev/null; then
    echo "❌ MySQL is not installed. Please install MySQL first:"
    echo "   Ubuntu/Debian: sudo apt install mysql-server"
    echo "   macOS: brew install mysql"
    echo "   Windows: Download from mysql.com"
    exit 1
fi

# Check if MySQL is running
if ! mysqladmin ping &>/dev/null; then
    echo "🔧 Starting MySQL service..."
    sudo systemctl start mysql || brew services start mysql || echo "Please start MySQL manually"
    sleep 5
fi

# Database setup
DB_NAME="food_delivery"
DB_USER="food_user"
DB_PASS="password"

echo "📋 Creating database and user..."
mysql -u root -e "
CREATE DATABASE IF NOT EXISTS ${DB_NAME};
CREATE USER IF NOT EXISTS '${DB_USER}'@'localhost' IDENTIFIED BY '${DB_PASS}';
GRANT ALL PRIVILEGES ON ${DB_NAME}.* TO '${DB_USER}'@'localhost';
FLUSH PRIVILEGES;
"

echo "✅ Database setup complete!"
echo ""
echo "📊 Database: ${DB_NAME}"
echo "👤 User: ${DB_USER}"
echo "🔑 Password: ${DB_PASS}"
echo ""
echo "🚀 Now you can start the backend server:"
echo "   cd myproject && mvn compile exec:java -Dexec.mainClass=\"com.fooddeliveryplatform.Main\""
