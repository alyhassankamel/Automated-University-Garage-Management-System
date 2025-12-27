<?php
require_once 'config/Database.php';

try {
    $stmt = $pdo->query("SHOW COLUMNS FROM Users");
    $columns = $stmt->fetchAll(PDO::FETCH_ASSOC);
    
    echo "<pre>";
    echo "Columns in Users table:\n";
    echo str_repeat("=", 50) . "\n";
    foreach ($columns as $col) {
        echo "Column: " . $col['Field'] . " | Type: " . $col['Type'] . "\n";
    }
    echo "</pre>";
} catch (PDOException $e) {
    echo "Error: " . $e->getMessage() . "\n";
}
?>

