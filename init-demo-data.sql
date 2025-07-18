-- Demo data untuk Food Delivery Platform

USE food_delivery;

-- Insert demo restaurant
INSERT INTO restaurants (name, description, opening_time, closing_time, delivery_radius, logo_url, user_id, is_active) 
VALUES 
('Demo Restaurant', 'Best food in town', '08:00', '22:00', 10, 'https://via.placeholder.com/300x200?text=Demo+Restaurant', 1, 1);

-- Insert demo menu items
INSERT INTO food_items (name, description, price, category, image_url, is_available, restaurant_id) 
VALUES 
('Nasi Lemak', 'Traditional Malaysian coconut rice', 8.50, 'Main Course', 'https://via.placeholder.com/300x200?text=Nasi+Lemak', 1, 1),
('Chicken Rice', 'Steamed chicken with fragrant rice', 7.00, 'Main Course', 'https://via.placeholder.com/300x200?text=Chicken+Rice', 1, 1),
('Roti Canai', 'Flaky flatbread with curry', 3.50, 'Appetizer', 'https://via.placeholder.com/300x200?text=Roti+Canai', 1, 1),
('Teh Tarik', 'Malaysian pulled tea', 2.50, 'Beverage', 'https://via.placeholder.com/300x200?text=Teh+Tarik', 1, 1),
('Cendol', 'Traditional dessert with coconut milk', 4.00, 'Dessert', 'https://via.placeholder.com/300x200?text=Cendol', 1, 1);

-- Insert demo user (restaurant owner)
INSERT INTO users (username, email, password, full_name, phone, address, role, is_active) 
VALUES 
('restaurant_owner', 'restaurant@demo.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Demo Restaurant Owner', '0123456789', '123 Demo Street', 'RESTAURANT', 1);

-- Insert demo orders
INSERT INTO orders (customer_id, restaurant_id, total_amount, delivery_address, status, created_at) 
VALUES 
(2, 1, 25.50, '456 Customer Street', 'PENDING', NOW()),
(3, 1, 15.00, '789 Another Street', 'ACCEPTED', NOW());

-- Insert demo order items
INSERT INTO order_items (order_id, food_id, quantity, price_per_unit) 
VALUES 
(1, 1, 2, 8.50),
(1, 3, 1, 3.50),
(1, 4, 2, 2.50),
(2, 2, 1, 7.00),
(2, 5, 2, 4.00);

-- Insert demo payments
INSERT INTO payments (order_id, amount, method, status, transaction_id, created_at) 
Saya telah buat script `setup-database.sh` untuk membantu anda setup MySQL database yang diperlukan oleh backend. Anda boleh jalankan script ini dengan perintah:

```bash
./setup-database.sh
```

Ini akan:
- Semak sama ada MySQL dipasang dan berjalan
- Buat database `food_delivery`
- Buat user `food_user` dengan password `password`
- Berikan privileges yang sesuai

Selepas itu, anda boleh jalankan backend dengan:

```bash
cd myproject
mvn compile exec:java -Dexec.mainClass="com.fooddeliveryplatform.Main"
```

Ini akan membolehkan backend berfungsi dengan betul dan frontend restoran yang saya buat tadi akan dapat berhubung dengan backend tanpa masalah.

Jika anda perlukan bantuan lanjut untuk setup atau ujian, beritahu saya.
