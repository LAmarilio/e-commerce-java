CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE users (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name text NOT NULL,
    role text,
    email text UNIQUE NOT NULL,
    password text NOT NULL,
    created_at timestamptz DEFAULT now(),
    updated_at timestamptz
);

CREATE TABLE products (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name text NOT NULL,
    description text,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity int NOT NULL,
    version int NOT NULL DEFAULT 0,
    created_at timestamptz DEFAULT now(),
    updated_at timestamptz
);

CREATE TABLE orders (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id uuid NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status text NOT NULL CHECK (status IN ('PENDING', 'PAID', 'SHIPPED', 'CANCELED')),
    total_amount DECIMAL(10, 2) NOT NULL,
    created_at timestamptz DEFAULT now(),
    updated_at timestamptz
);

CREATE TABLE order_items (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id uuid NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id uuid NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    quantity int NOT NULL,
    unit_value DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) GENERATED ALWAYS AS (quantity * unit_value) STORED
);
