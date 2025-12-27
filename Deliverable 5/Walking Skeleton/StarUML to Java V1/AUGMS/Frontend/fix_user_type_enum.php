<?php
/**
 * Database Migration Script
 * Updates the user_type column in Users table to include 'Manager'
 * Run this script once to ensure Manager type can be saved
 */

require_once 'config/Database.php';

try {
    // Check current column definition
    $stmt = $pdo->query("SHOW COLUMNS FROM Users WHERE Field = 'user_type'");
    $column = $stmt->fetch(PDO::FETCH_ASSOC);
    
    if ($column) {
        echo "<pre>";
        echo "Current user_type column definition:\n";
        echo "Type: " . $column['Type'] . "\n";
        echo "Null: " . $column['Null'] . "\n";
        echo "Default: " . ($column['Default'] ?? 'NULL') . "\n";
        echo str_repeat("=", 50) . "\n\n";
        
        // Check if it's an ENUM
        if (strpos($column['Type'], 'enum') !== false || strpos(strtolower($column['Type']), 'enum') !== false) {
            // Check if Manager is already in the enum
            if (stripos($column['Type'], 'Manager') === false) {
                echo "⚠️  'Manager' is not in the ENUM values. Updating...\n\n";
                
                // Get current enum values
                preg_match("/enum\((.*)\)/i", $column['Type'], $matches);
                if (isset($matches[1])) {
                    $current_values = $matches[1];
                    // Check if Manager is already there (case-insensitive)
                    $values_array = array_map('trim', explode(',', str_replace("'", "", $current_values)));
                    if (!in_array('Manager', $values_array) && !in_array('manager', $values_array)) {
                        // Add Manager to the enum
                        $new_enum = $current_values . ",'Manager'";
                        
                        // Update the column
                        $pdo->exec("ALTER TABLE Users MODIFY COLUMN user_type ENUM($new_enum)");
                        
                        echo "✅ Successfully updated user_type column to include 'Manager'\n";
                        echo "New ENUM values: $new_enum\n";
                    } else {
                        echo "✅ 'Manager' is already in the ENUM values.\n";
                    }
                }
            } else {
                echo "✅ 'Manager' is already in the ENUM values.\n";
            }
        } else {
            // If it's not an ENUM (e.g., VARCHAR), it should work fine
            echo "ℹ️  user_type is not an ENUM (it's " . $column['Type'] . "), so 'Manager' should work fine.\n";
            echo "If you're still having issues, the problem might be elsewhere.\n";
        }
        
        echo "\n✅ Check completed!\n";
        echo "</pre>";
    } else {
        echo "❌ Error: Could not find user_type column in Users table.\n";
    }
    
} catch (PDOException $e) {
    echo "❌ Error: " . $e->getMessage() . "\n";
    echo "Please check your database connection and table structure.\n";
}

