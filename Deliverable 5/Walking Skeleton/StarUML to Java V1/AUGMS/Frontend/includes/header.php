<?php
require_once __DIR__ . '/../config/Database.php';

if (!isLoggedIn() && !in_array(basename($_SERVER['PHP_SELF']), ['index.php', 'register.php'])) {
    redirect('index.php');
}

$currentPage = basename($_SERVER['PHP_SELF']);
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EUI Parking System</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <!-- Loading Screen -->
    <div class="loading-screen">
        <div class="loader">
            <p style="margin-top:15px;font-size:1.2rem;">Loading...</p>
            <div class="loader-dots">
                <span></span>
                <span></span>
                <span></span>
            </div>
        </div>
    </div>

    <header class="header">
        <div class="logo-container">
            <img src="images/logo.png" alt="EUI Logo" class="logo">
            <h1>EUI Parking System</h1>
        </div>
        
        <?php if (isLoggedIn()): ?>
        <div class="menu-toggle">
            <span></span>
            <span></span>
            <span></span>
        </div>
        <nav class="nav-links">
            <a href="dashboard.php" class="<?= $currentPage == 'dashboard.php' ? 'active' : '' ?>">📊 Dashboard</a>
            <?php if (in_array(getUserType(), ['Student', 'Staff'])): ?>
                <a href="add_vehicle.php" class="<?= $currentPage == 'add_vehicle.php' ? 'active' : '' ?>">🚗 My Vehicles</a>
                <a href="add_request.php" class="<?= $currentPage == 'add_request.php' ? 'active' : '' ?>">📝 New Request</a>
            <?php endif; ?>
            <?php if (in_array(getUserType(), ['Admin', 'Manager'])): ?>
                <a href="manage_requests.php" class="<?= $currentPage == 'manage_requests.php' ? 'active' : '' ?>">⚙️ Manage</a>
            <?php endif; ?>
            <?php if (getUserType() == 'Manager'): ?>
                <a href="manager.php" class="<?= $currentPage == 'manager.php' ? 'active' : '' ?>">👔 Manager</a>
            <?php endif; ?>
            <a href="logout.php">🚪 Logout</a>
        </nav>
        <?php endif; ?>
    </header>