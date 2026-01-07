#!/bin/bash

echo "Starting SOSPOS Garson Backend..."
echo "================================"

cd backend

# Check if virtual environment exists
if [ ! -d "venv" ]; then
    echo "Creating virtual environment..."
    python3 -m venv venv
fi

# Activate virtual environment
source venv/bin/activate

# Install requirements
echo "Installing dependencies..."
pip install -r requirements.txt

# Run the application
echo "Starting Flask server..."
echo "Backend will be available at http://localhost:5000"
python app.py
