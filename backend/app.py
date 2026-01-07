from flask import Flask, request, jsonify
from flask_cors import CORS
from datetime import datetime
import json

app = Flask(__name__)
CORS(app)

# ============================================================================
# WARNING: This is a DEVELOPMENT/DEMO application
# ============================================================================
# SECURITY NOTICE: The following security issues exist in this demo:
# 1. Passwords are stored in plain text (use bcrypt/argon2 in production)
# 2. No JWT/session management (implement proper auth in production)
# 3. In-memory database (use PostgreSQL/MySQL in production)
# 4. No input sanitization (validate all inputs in production)
# 5. No rate limiting (add rate limiting in production)
# ============================================================================

# In-memory database (replace with real database in production)
users = [
    {"id": 1, "username": "garson1", "password": "123456", "role": "waiter"},
    {"id": 2, "username": "admin", "password": "admin123", "role": "admin"}
]

tables = [
    {"id": 1, "table_number": 1, "status": "empty", "waiter_id": None},
    {"id": 2, "table_number": 2, "status": "empty", "waiter_id": None},
    {"id": 3, "table_number": 3, "status": "empty", "waiter_id": None},
    {"id": 4, "table_number": 4, "status": "empty", "waiter_id": None},
    {"id": 5, "table_number": 5, "status": "empty", "waiter_id": None},
    {"id": 6, "table_number": 6, "status": "empty", "waiter_id": None},
    {"id": 7, "table_number": 7, "status": "empty", "waiter_id": None},
    {"id": 8, "table_number": 8, "status": "empty", "waiter_id": None},
]

menu_items = [
    {"id": 1, "name": "Izgara Köfte", "description": "Özel soslu ızgara köfte", "price": 85.00, "category": "food", "available": True},
    {"id": 2, "name": "Tavuk Şiş", "description": "Marine edilmiş tavuk şiş", "price": 75.00, "category": "food", "available": True},
    {"id": 3, "name": "Adana Kebap", "description": "Acılı ızgara Adana kebap", "price": 95.00, "category": "food", "available": True},
    {"id": 4, "name": "Karışık Pizza", "description": "Özel soslu karışık pizza", "price": 120.00, "category": "food", "available": True},
    {"id": 5, "name": "Mantı", "description": "Ev yapımı mantı", "price": 70.00, "category": "food", "available": True},
    {"id": 6, "name": "Kola", "description": "330ml soğuk kola", "price": 15.00, "category": "drink", "available": True},
    {"id": 7, "name": "Ayran", "description": "Ev yapımı ayran", "price": 10.00, "category": "drink", "available": True},
    {"id": 8, "name": "Çay", "description": "Demli çay", "price": 5.00, "category": "drink", "available": True},
    {"id": 9, "name": "Kahve", "description": "Türk kahvesi", "price": 20.00, "category": "drink", "available": True},
    {"id": 10, "name": "Limonata", "description": "Ev yapımı limonata", "price": 25.00, "category": "drink", "available": True},
    {"id": 11, "name": "Baklava", "description": "Antep fıstıklı baklava", "price": 45.00, "category": "dessert", "available": True},
    {"id": 12, "name": "Künefe", "description": "Özel peynirli künefe", "price": 55.00, "category": "dessert", "available": True},
    {"id": 13, "name": "Sütlaç", "description": "Fırın sütlaç", "price": 30.00, "category": "dessert", "available": True},
    {"id": 14, "name": "Dondurma", "description": "Ev yapımı dondurma", "price": 25.00, "category": "dessert", "available": True},
]

orders = []
order_id_counter = 1


def find_user(username, password):
    for user in users:
        if user["username"] == username and user["password"] == password:
            return user
    return None


def find_menu_item(item_id):
    for item in menu_items:
        if item["id"] == item_id:
            return item
    return None


def find_table(table_id):
    for table in tables:
        if table["id"] == table_id:
            return table
    return None


@app.route('/')
def index():
    return jsonify({
        "success": True,
        "message": "SOSPOS Garson API v1.0",
        "endpoints": [
            "/api/login",
            "/api/tables",
            "/api/menu",
            "/api/orders"
        ]
    })


@app.route('/api/login', methods=['POST'])
def login():
    data = request.json
    
    # Validate input
    if not data or not data.get('username') or not data.get('password'):
        return jsonify({
            "success": False,
            "message": "Kullanıcı adı ve şifre gerekli"
        }), 400
    
    username = data.get('username')
    password = data.get('password')
    
    user = find_user(username, password)
    
    if user:
        # In production, generate a real JWT token
        user_data = user.copy()
        user_data['token'] = f"token_{user['id']}_{datetime.now().timestamp()}"
        del user_data['password']
        
        return jsonify({
            "success": True,
            "message": "Giriş başarılı",
            "data": user_data
        })
    else:
        return jsonify({
            "success": False,
            "message": "Kullanıcı adı veya şifre hatalı"
        }), 401


@app.route('/api/tables', methods=['GET'])
def get_tables():
    # Add orders to tables
    tables_with_orders = []
    for table in tables:
        table_data = table.copy()
        table_orders = [o for o in orders if o["table_id"] == table["id"]]
        
        # Add menu item details to orders
        for order in table_orders:
            order["menu_item"] = find_menu_item(order["menu_item_id"])
        
        table_data["orders"] = table_orders
        
        # Update table status based on orders
        if table_orders:
            table_data["status"] = "occupied"
        
        tables_with_orders.append(table_data)
    
    return jsonify({
        "success": True,
        "data": tables_with_orders
    })


@app.route('/api/tables/<int:table_id>', methods=['GET'])
def get_table(table_id):
    table = find_table(table_id)
    
    if not table:
        return jsonify({
            "success": False,
            "message": "Masa bulunamadı"
        }), 404
    
    table_data = table.copy()
    table_orders = [o for o in orders if o["table_id"] == table_id]
    
    # Add menu item details to orders
    for order in table_orders:
        order["menu_item"] = find_menu_item(order["menu_item_id"])
    
    table_data["orders"] = table_orders
    
    return jsonify({
        "success": True,
        "data": table_data
    })


@app.route('/api/tables/<int:table_id>', methods=['PUT'])
def update_table(table_id):
    table = find_table(table_id)
    
    if not table:
        return jsonify({
            "success": False,
            "message": "Masa bulunamadı"
        }), 404
    
    data = request.json
    
    if "status" in data:
        table["status"] = data["status"]
        
        # If closing table, delete all orders
        if data["status"] == "empty":
            global orders
            orders = [o for o in orders if o["table_id"] != table_id]
    
    if "waiter_id" in data:
        table["waiter_id"] = data["waiter_id"]
    
    return jsonify({
        "success": True,
        "message": "Masa güncellendi",
        "data": table
    })


@app.route('/api/menu', methods=['GET'])
def get_menu():
    return jsonify({
        "success": True,
        "data": menu_items
    })


@app.route('/api/menu/category/<category>', methods=['GET'])
def get_menu_by_category(category):
    filtered_items = [item for item in menu_items if item["category"] == category]
    
    return jsonify({
        "success": True,
        "data": filtered_items
    })


@app.route('/api/orders/table/<int:table_id>', methods=['GET'])
def get_orders_by_table(table_id):
    table_orders = [o for o in orders if o["table_id"] == table_id]
    
    # Add menu item details to orders
    for order in table_orders:
        order["menu_item"] = find_menu_item(order["menu_item_id"])
    
    return jsonify({
        "success": True,
        "data": table_orders
    })


@app.route('/api/orders', methods=['POST'])
def create_order():
    global order_id_counter
    
    data = request.json
    
    # Validate required fields
    if "table_id" not in data or "menu_item_id" not in data or "quantity" not in data:
        return jsonify({
            "success": False,
            "message": "Eksik bilgi"
        }), 400
    
    # Validate menu item exists
    menu_item = find_menu_item(data["menu_item_id"])
    if not menu_item:
        return jsonify({
            "success": False,
            "message": "Menü öğesi bulunamadı"
        }), 404
    
    # Validate table exists
    table = find_table(data["table_id"])
    if not table:
        return jsonify({
            "success": False,
            "message": "Masa bulunamadı"
        }), 404
    
    order = {
        "id": order_id_counter,
        "table_id": data["table_id"],
        "menu_item_id": data["menu_item_id"],
        "quantity": data["quantity"],
        "status": data.get("status", "pending"),
        "notes": data.get("notes"),
        "created_at": datetime.now().isoformat(),
        "menu_item": menu_item
    }
    
    orders.append(order)
    order_id_counter += 1
    
    # Update table status
    table["status"] = "occupied"
    
    return jsonify({
        "success": True,
        "message": "Sipariş oluşturuldu",
        "data": order
    }), 201


@app.route('/api/orders/<int:order_id>', methods=['PUT'])
def update_order(order_id):
    order = None
    for o in orders:
        if o["id"] == order_id:
            order = o
            break
    
    if not order:
        return jsonify({
            "success": False,
            "message": "Sipariş bulunamadı"
        }), 404
    
    data = request.json
    
    if "status" in data:
        order["status"] = data["status"]
    
    if "quantity" in data:
        order["quantity"] = data["quantity"]
    
    if "notes" in data:
        order["notes"] = data["notes"]
    
    return jsonify({
        "success": True,
        "message": "Sipariş güncellendi",
        "data": order
    })


@app.route('/api/orders/send-to-kitchen', methods=['POST'])
def send_orders_to_kitchen():
    data = request.json
    order_ids = data.get("order_ids", [])
    
    updated_count = 0
    for order_id in order_ids:
        for order in orders:
            if order["id"] == order_id:
                order["status"] = "preparing"
                updated_count += 1
                break
    
    return jsonify({
        "success": True,
        "message": f"{updated_count} sipariş mutfağa gönderildi",
        "data": f"{updated_count} orders sent"
    })


@app.route('/api/orders/<int:order_id>', methods=['DELETE'])
def delete_order(order_id):
    global orders
    
    order = None
    for o in orders:
        if o["id"] == order_id:
            order = o
            break
    
    if not order:
        return jsonify({
            "success": False,
            "message": "Sipariş bulunamadı"
        }), 404
    
    orders = [o for o in orders if o["id"] != order_id]
    
    return jsonify({
        "success": True,
        "message": "Sipariş silindi",
        "data": "Order deleted"
    })


if __name__ == '__main__':
    # Note: In production, set debug=False and use a proper WSGI server like Gunicorn
    import os
    debug_mode = os.environ.get('FLASK_DEBUG', 'True') == 'True'
    app.run(host='0.0.0.0', port=5000, debug=debug_mode)
