<?php
require_once 'config/Database.php';

// Start session if not already started
if (session_status() == PHP_SESSION_NONE) {
    session_start();
}

if (isLoggedIn()) {
    redirect('dashboard.php');
}

$error = '';
$success = '';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $uni_id = trim($_POST['uni_id']);
    $name = trim($_POST['name']);
    $phone = trim($_POST['phone']);
    $password = $_POST['password']; // Keep original for session
    $hashed_password = password_hash($password, PASSWORD_DEFAULT);
    $user_type = trim($_POST['user_type']);
    
    // Validate user_type
    $allowed_types = ['Student', 'Staff', 'Admin', 'Manager'];
    if (!in_array($user_type, $allowed_types)) {
        $error = 'Invalid user type selected.';
    }
    
    $stmt = $pdo->prepare("SELECT * FROM Users WHERE uni_ID = ? OR phone = ?");
    $stmt->execute([$uni_id, $phone]);
    
    if ($stmt->rowCount() > 0) {
        $error = 'University ID or Phone number already exists';
    } elseif (empty($error)) {
        try {
            $stmt = $pdo->prepare("INSERT INTO Users (uni_ID, name, phone, password, user_type) VALUES (?, ?, ?, ?, ?)");
            if ($stmt->execute([$uni_id, $name, $phone, $hashed_password, $user_type])) {
                
                // ✅ AUTO-LOGIN: Set session variables
                $_SESSION['user_id'] = $pdo->lastInsertId();
                $_SESSION['user_name'] = $name;
                $_SESSION['user_type'] = $user_type;
                $_SESSION['uni_id'] = $uni_id;
                $_SESSION['logged_in'] = true;
                
                // ✅ Redirect to dashboard
                header('Location: dashboard.php');
                exit();
                
            } else {
                $error = 'Registration failed. Please try again.';
            }
        } catch (PDOException $e) {
            // Better error handling to see what's wrong
            $error_msg = $e->getMessage();
            
            // Check if it's an ENUM constraint issue
            if (stripos($error_msg, 'enum') !== false || stripos($error_msg, 'user_type') !== false) {
                // Try to fix the schema automatically
                try {
                    $stmt = $pdo->query("SHOW COLUMNS FROM Users WHERE Field = 'user_type'");
                    $column = $stmt->fetch(PDO::FETCH_ASSOC);
                    if ($column && stripos($column['Type'], 'enum') !== false) {
                        if (stripos($column['Type'], 'Manager') === false) {
                            preg_match("/enum\((.*)\)/i", $column['Type'], $matches);
                            if (isset($matches[1])) {
                                $current_values = $matches[1];
                                $new_enum = $current_values . ",'Manager'";
                                $pdo->exec("ALTER TABLE Users MODIFY COLUMN user_type ENUM($new_enum)");
                                // Retry the insert
                                $stmt = $pdo->prepare("INSERT INTO Users (uni_ID, name, phone, password, user_type) VALUES (?, ?, ?, ?, ?)");
                                if ($stmt->execute([$uni_id, $name, $phone, $hashed_password, $user_type])) {
                                    $_SESSION['user_id'] = $pdo->lastInsertId();
                                    $_SESSION['user_name'] = $name;
                                    $_SESSION['user_type'] = $user_type;
                                    $_SESSION['uni_id'] = $uni_id;
                                    $_SESSION['logged_in'] = true;
                                    header('Location: dashboard.php');
                                    exit();
                                }
                            }
                        }
                    }
                } catch (Exception $fix_error) {
                    $error = 'Registration failed. The database schema may need to be updated. Please run fix_user_type_enum.php or use create_manager.php to create a manager account.';
                }
            } else {
                $error = 'Registration failed: ' . $error_msg;
            }
            // Log the error for debugging
            error_log("Registration error: " . $error_msg . " | User type: " . $user_type);
        }
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - EUI Parking System</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="loading-screen">
        <div class="loader">
            <div class="loader-car">🚗</div>
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
            <div class="logo">EUI</div>
            <h1>EUI Parking System</h1>
        </div>
    </header>
    
    <div class="auth-container">
        <div class="auth-box">
            <h2>
                📝 Create Account
                <span>Join us to manage your parking easily</span>
            </h2>
            
            <?php if ($error): ?>
                <div class="alert alert-error">
                    <span class="alert-icon">❌</span>
                    <?= htmlspecialchars($error) ?>
                </div>
            <?php endif; ?>
            
            <form method="POST">
                <div class="form-group">
                    <label>Select User Type</label>
                    <div class="user-type-selector">
                        <div class="user-type-option">
                            <input type="radio" name="user_type" value="Student" id="student" required checked>
                            <label for="student">
                                <span class="icon">🎓</span>
                                <span class="text">Student</span>
                            </label>
                        </div>
                        <div class="user-type-option">
                            <input type="radio" name="user_type" value="Staff" id="staff">
                            <label for="staff">
                                <span class="icon">👨‍💼</span>
                                <span class="text">Staff</span>
                            </label>
                        </div>
                        <div class="user-type-option">
                            <input type="radio" name="user_type" value="Admin" id="admin">
                            <label for="admin">
                                <span class="icon">🔧</span>
                                <span class="text">Admin</span>
                            </label>
                        </div>
                        <div class="user-type-option">
                            <input type="radio" name="user_type" value="Manager" id="manager">
                            <label for="manager">
                                <span class="icon">👔</span>
                                <span class="text">Manager</span>
                            </label>
                        </div>
                    </div>
                </div>
                
                <div class="form-group">
                    <label>University ID</label>
                    <input type="text" name="uni_id" required placeholder="e.g., STU20240001">
                </div>
                
                <div class="form-group">
                    <label>Full Name</label>
                    <input type="text" name="name" required placeholder="Enter your full name">
                </div>
                
                <div class="form-group">
                    <label>Phone Number</label>
                    <input type="text" name="phone" required placeholder="e.g., 01012345678">
                </div>
                
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" required placeholder="Create a strong password" minlength="6">
                    <span class="password-toggle">👁️</span>
                </div>
                
                <button type="submit" class="btn btn-primary">
                    ✨ Create Account
                </button>
            </form>
            
            <p class="auth-link">
                Already have an account? <a href="index.php">Sign in here</a>
            </p>
        </div>
    </div>
    
    <footer class="footer">
        <p>&copy; <?= date('Y') ?> Egypt University of Informatics</p>
    </footer>
    
    <script src="script.js"></script>
</body>
</html>