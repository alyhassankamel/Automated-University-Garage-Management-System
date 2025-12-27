<?php
include 'includes/header.php';

if (!in_array(getUserType(), ['Student', 'Staff'])) {
    redirect('dashboard.php');
}

$user_id = $_SESSION['user_id'];
$error = '';
$success = '';

// Handle delete
if (isset($_GET['delete'])) {
    $vehicle_id = (int)$_GET['delete'];
    $stmt = $pdo->prepare("DELETE FROM Vehicle WHERE vehicle_id = ? AND user_id = ?");
    if ($stmt->execute([$vehicle_id, $user_id])) {
        $success = 'Vehicle deleted successfully!';
    }
}

// Handle add
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $license_plate = trim($_POST['license_plate']);
    $color = trim($_POST['color']);
    $model = trim($_POST['model']);
    
    $stmt = $pdo->prepare("SELECT * FROM Vehicle WHERE license_plate = ?");
    $stmt->execute([$license_plate]);
    
    if ($stmt->rowCount() > 0) {
        $error = 'This license plate is already registered';
    } else {
        $stmt = $pdo->prepare("INSERT INTO Vehicle (license_plate, color, model, user_id) VALUES (?, ?, ?, ?)");
        if ($stmt->execute([$license_plate, $color, $model, $user_id])) {
            $success = 'Vehicle added successfully! Waiting for admin approval.';
        } else {
            $error = 'Failed to add vehicle';
        }
    }
}

// Get vehicles
$stmt = $pdo->prepare("SELECT * FROM Vehicle WHERE user_id = ? ORDER BY created_at DESC");
$stmt->execute([$user_id]);
$vehicles = $stmt->fetchAll(PDO::FETCH_ASSOC);
?>

<div class="dashboard">
    <div class="dashboard-header">
        <h2>🚗 My Vehicles</h2>
        <p>Manage your registered vehicles</p>
    </div>
    
    <?php if ($success): ?>
        <div class="alert alert-success">
            <span class="alert-icon">✅</span>
            <?= $success ?>
        </div>
    <?php endif; ?>
    
    <?php if ($error): ?>
        <div class="alert alert-error">
            <span class="alert-icon">❌</span>
            <?= $error ?>
        </div>
    <?php endif; ?>
    
    <div class="cards-container">
        <!-- Add Vehicle Form -->
        <div class="card animate-fadeInUp">
            <h3>➕ Add New Vehicle</h3>
            
            <form method="POST" style="margin-top:20px;">
                <div class="form-group">
                    <label>License Plate *</label>
                    <input type="text" name="license_plate" required placeholder="e.g., ABC 1234">
                </div>
                
                <div class="form-group">
                    <label>Color *</label>
                    <input type="text" name="color" required placeholder="e.g., White">
                </div>
                
                <div class="form-group">
                    <label>Model *</label>
                    <input type="text" name="model" required placeholder="e.g., Toyota Corolla 2022">
                </div>
                
                <button type="submit" class="btn btn-primary">
                    🚗 Add Vehicle
                </button>
            </form>
        </div>
        
        <!-- Vehicle Stats -->
        <div class="card card-teal animate-fadeInUp delay-1">
            <h3>📊 Vehicle Stats</h3>
            <div class="stats-grid" style="margin-top:20px;">
                <div class="stat-item">
                    <div class="number"><?= count($vehicles) ?></div>
                    <div class="label">Total</div>
                </div>
                <div class="stat-item">
                    <div class="number"><?= count(array_filter($vehicles, fn($v) => $v['access_status'] == 1)) ?></div>
                    <div class="label">Approved</div>
                </div>
                <div class="stat-item">
                    <div class="number"><?= count(array_filter($vehicles, fn($v) => $v['access_status'] == 0)) ?></div>
                    <div class="label">Pending</div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Vehicles Table -->
    <div class="table-container">
        <div class="table-header">
            <h3>🚗 My Registered Vehicles</h3>
            <div class="table-search">
                <input type="text" placeholder="Search vehicles...">
            </div>
        </div>
        
        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>License Plate</th>
                        <th>Model</th>
                        <th>Color</th>
                        <th>Status</th>
                        <th>Added Date</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <?php if (empty($vehicles)): ?>
                        <tr>
                            <td colspan="6">
                                <div class="empty-state">
                                    <div class="icon">🚗</div>
                                    <h4>No vehicles yet</h4>
                                    <p>Add your first vehicle to get started</p>
                                </div>
                            </td>
                        </tr>
                    <?php else: ?>
                        <?php foreach ($vehicles as $v): ?>
                        <tr>
                            <td><strong><?= htmlspecialchars($v['license_plate']) ?></strong></td>
                            <td><?= htmlspecialchars($v['model']) ?></td>
                            <td>
                                <span style="display:inline-flex;align-items:center;gap:8px;">
                                    <span style="width:15px;height:15px;border-radius:50%;background:<?= strtolower($v['color']) ?>;border:1px solid #ddd;"></span>
                                    <?= htmlspecialchars($v['color']) ?>
                                </span>
                            </td>
                            <td>
                                <span class="badge <?= $v['access_status'] ? 'badge-approved' : 'badge-pending' ?>">
                                    <?= $v['access_status'] ? 'Approved' : 'Pending' ?>
                                </span>
                            </td>
                            <td><?= date('M d, Y', strtotime($v['created_at'])) ?></td>
                            <td>
                                <div class="action-btns">
                                    <a href="?delete=<?= $v['vehicle_id'] ?>" 
                                       class="action-btn action-btn-reject"
                                       data-confirm="Delete this vehicle?">
                                        🗑️
                                    </a>
                                </div>
                            </td>
                        </tr>
                        <?php endforeach; ?>
                    <?php endif; ?>
                </tbody>
            </table>
        </div>
    </div>
</div>

<?php include 'includes/footer.php'; ?>