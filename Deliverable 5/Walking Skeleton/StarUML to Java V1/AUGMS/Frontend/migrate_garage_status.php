<?php
/**
 * Database Migration Script
 * Adds garage_status column to Parking_garage table if it doesn't exist
 * Run this script once to ensure the garage status feature works properly
 */

require_once 'config/Database.php';

try {
    // Check if the column already exists
    $stmt = $pdo->query("SHOW COLUMNS FROM Parking_garage LIKE 'garage_status'");
    
    if ($stmt->rowCount() == 0) {
        // Column doesn't exist, add it
        $pdo->exec("ALTER TABLE Parking_garage ADD COLUMN garage_status ENUM('Open', 'Closed') DEFAULT 'Open'");
        
        // Set all existing garages to 'Open' by default
        $pdo->exec("UPDATE Parking_garage SET garage_status = 'Open' WHERE garage_status IS NULL");
        
        echo "✅ Successfully added 'garage_status' column to Parking_garage table.\n";
        echo "✅ All existing garages have been set to 'Open' status.\n";
    } else {
        echo "ℹ️  Column 'garage_status' already exists in Parking_garage table.\n";
    }
    
    echo "\n✅ Migration completed successfully!\n";
    
} catch (PDOException $e) {
    echo "❌ Error: " . $e->getMessage() . "\n";
    echo "Please check your database connection and table structure.\n";
}

