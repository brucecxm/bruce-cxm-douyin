-- Use database
USE vue_multi_page;

-- Clear existing data
DELETE FROM messages;
DELETE FROM favorites;
DELETE FROM orders;
DELETE FROM cart;
DELETE FROM recent_view;
DELETE FROM addresses;
DELETE FROM comments;
DELETE FROM products;
DELETE FROM users;

-- Insert one test user (password: 'test')
INSERT INTO users (username, password, nickname, email, phone, avatar, gender, birthday, bio) VALUES
('test', '$2a$10$/pJjTVYFYXLRLeaCHzHu1ufUaeU9CmfbkoSIPD2/Erqol/ySMHpjC', 'TestUser', 'test@example.com', '13800138003', 'https://via.placeholder.com/100', 'male', '2000-01-01', 'I am test user');

-- Insert product data using the last inserted user ID
INSERT INTO products (title, description, images, price, quantity, category, location, status, user_id) VALUES
('iPhone 13 Pro 256GB Blue', '99% new, no scratches, complete accessories', 'https://via.placeholder.com/200x200?text=iPhone+13+Pro', 5999.00, 1, 'electronics', 'Beijing', 'available', LAST_INSERT_ID()),
('MacBook Pro 16 M1 Pro', 'Brand new, unopened, official warranty', 'https://via.placeholder.com/200x200?text=MacBook+Pro', 12999.00, 1, 'electronics', 'Shanghai', 'available', LAST_INSERT_ID());

-- Insert comments for products
-- Get the first product ID
SET @product1_id = (SELECT id FROM products WHERE title = 'iPhone 13 Pro 256GB Blue');
-- Get the second product ID  
SET @product2_id = (SELECT id FROM products WHERE title = 'MacBook Pro 16 M1 Pro');
-- Get the user ID
SET @user_id = (SELECT id FROM users WHERE username = 'test');

-- Insert comments for the first product
INSERT INTO comments (product_id, user_id, rating, content, likes, replies) VALUES
(@product1_id, @user_id, 5, 'Great product, very satisfied!', 10, 2),
(@product1_id, @user_id, 4, 'Good quality, fast delivery.', 5, 1),
(@product2_id, @user_id, 5, 'Excellent product, highly recommended!', 15, 3);

-- Query inserted data
SELECT 'Users' as table_name, COUNT(*) as count FROM users UNION ALL
SELECT 'Products' as table_name, COUNT(*) as count FROM products UNION ALL
SELECT 'Comments' as table_name, COUNT(*) as count FROM comments;