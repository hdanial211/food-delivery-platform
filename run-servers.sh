#!/bin/bash

# Script untuk menjalankan backend dan frontend secara serentak

echo "🚀 Starting Food Delivery Platform..."
echo ""

# Function to check if port is in use
check_port() {
    if lsof -Pi :$1 -sTCP:LISTEN -t >/dev/null 2>&1; then
        echo "❌ Port $1 is already in use"
        return 1
    fi
    return 0
}

# Function to kill process on port
kill_port() {
    echo "🔍 Killing process on port $1..."
    fuser -k $1/tcp 2>/dev/null || true
}

# Kill existing processes on ports 8080 and 8000
echo "🧹 Cleaning up existing processes..."
kill_port 8080
kill_port 8000
sleep 2

# Start backend server
echo "📦 Starting Backend Server (Java)..."
cd myproject
mvn compile exec:java -Dexec.mainClass="com.fooddeliveryplatform.Main" &
BACKEND_PID=$!
cd ..

# Wait for backend to start
echo "⏳ Waiting for backend to start..."
sleep 10

# Start frontend server
echo "🌐 Starting Frontend Server (Restaurant Dashboard)..."
cd restaurant
python3 -m http.server 8000 &
FRONTEND_PID=$!
cd ..

echo ""
echo "✅ Servers started successfully!"
echo ""
echo "📋 Access URLs:"
echo "   🏪 Restaurant Dashboard: http://localhost:8000/login.html"
echo "   🔧 Backend API: http://localhost:8080/api"
echo ""
echo "🛑 To stop all servers, press Ctrl+C"
echo ""

# Wait for user to stop
trap "echo 'Stopping servers...'; kill $BACKEND_PID $FRONTEND_PID 2>/dev/null; exit" INT
wait
