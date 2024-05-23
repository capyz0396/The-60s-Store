select * from customers;

select * from tokens;

select * from products;

select * from roles;

UPDATE customers
SET confirmation_status = 1
WHERE customerid = 3;

UPDATE customers
SET email = 'jakdjlAD@gmail.com'
WHERE customerid = 3;

DELETE FROM customers
WHERE customerid IN (4, 5);
