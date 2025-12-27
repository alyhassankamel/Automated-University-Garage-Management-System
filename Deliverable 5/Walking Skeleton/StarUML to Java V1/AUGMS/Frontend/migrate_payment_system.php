<?php
/**
 * Database Migration Script
 * Adds payment-related columns to Service_type and Service_request tables
 * Run this script once to set up the payment system
 */

require_once 'config/Database.php';

try {
    echo "<pre>";
    echo "Setting up Payment System...\n";
    echo str_repeat("=", 50) . "\n\n";
    
    // 1. Add price column to Service_type table
    echo "1. Adding price column to Service_type table...\n";
    try {
        $stmt = $pdo->query("SHOW COLUMNS FROM Service_type LIKE 'price'");
        if ($stmt->rowCount() == 0) {
            $pdo->exec("ALTER TABLE Service_type ADD COLUMN price DECIMAL(10,2) DEFAULT 0.00");
            echo "   ✅ Added price column\n";
        } else {
            echo "   ℹ️  Price column already exists\n";
        }
    } catch (Exception $e) {
        echo "   ⚠️  Error: " . $e->getMessage() . "\n";
    }
    
    // 2. Populate prices with random values if they're all 0 or NULL
    echo "\n2. Setting random prices for services...\n";
    $stmt = $pdo->query("SELECT service_id, service_name FROM Service_type");
    $services = $stmt->fetchAll(PDO::FETCH_ASSOC);
    
    $prices = [
        'Parking Permit' => 150.00,
        'Reserved Spot' => 200.00,
        'Car Wash' => 50.00,
        'Oil Change' => 120.00,
        'Maintenance' => 300.00
    ];
    
    foreach ($services as $service) {
        $price = $prices[$service['service_name']] ?? rand(50, 500);
        $stmt = $pdo->prepare("UPDATE Service_type SET price = ? WHERE service_id = ?");
        $stmt->execute([$price, $service['service_id']]);
        echo "   ✅ {$service['service_name']}: EGP {$price}\n";
    }
    
    // 3. Add payment columns to Service_request table
    echo "\n3. Adding payment columns to Service_request table...\n";
    
    $columns_to_add = [
        'payment_method' => "ALTER TABLE Service_request ADD COLUMN payment_method ENUM('Credit', 'Cash', 'Pending') DEFAULT 'Pending'",
        'payment_amount' => "ALTER TABLE Service_request ADD COLUMN payment_amount DECIMAL(10,2) DEFAULT NULL",
        'card_number' => "ALTER TABLE Service_request ADD COLUMN card_number VARCHAR(20) DEFAULT NULL",
        'card_cvv' => "ALTER TABLE Service_request ADD COLUMN card_cvv VARCHAR(4) DEFAULT NULL",
        'card_expiry' => "ALTER TABLE Service_request ADD COLUMN card_expiry VARCHAR(5) DEFAULT NULL",
        'cardholder_name' => "ALTER TABLE Service_request ADD COLUMN cardholder_name VARCHAR(100) DEFAULT NULL",
        'payment_status' => "ALTER TABLE Service_request ADD COLUMN payment_status ENUM('Pending', 'Paid', 'Failed') DEFAULT 'Pending'",
        'receipt_id' => "ALTER TABLE Service_request ADD COLUMN receipt_id VARCHAR(50) DEFAULT NULL UNIQUE"
    ];
    
    foreach ($columns_to_add as $col_name => $sql) {
        try {
            $stmt = $pdo->query("SHOW COLUMNS FROM Service_request LIKE '$col_name'");
            if ($stmt->rowCount() == 0) {
                $pdo->exec($sql);
                echo "   ✅ Added $col_name column\n";
            } else {
                echo "   ℹ️  $col_name column already exists\n";
            }
        } catch (Exception $e) {
            echo "   ⚠️  Error adding $col_name: " . $e->getMessage() . "\n";
        }
    }
    
    echo "\n✅ Payment system setup completed successfully!\n";
    echo "</pre>";
    
} catch (PDOException $e) {
    echo "❌ Error: " . $e->getMessage() . "\n";
    echo "Please check your database connection and table structure.\n";
}

