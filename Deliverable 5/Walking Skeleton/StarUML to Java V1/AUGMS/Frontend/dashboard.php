<?php
include 'includes/header.php';

$user_type = getUserType();
$user_id = $_SESSION['user_id'];

// Get data based on user type
if ($user_type == 'Student' || $user_type == 'Staff') {
    // Get user's vehicles
    $stmt = $pdo->prepare("SELECT * FROM Vehicle WHERE user_id = ?");
    $stmt->execute([$user_id]);
    $vehicles = $stmt->fetchAll(PDO::FETCH_ASSOC);
    $vehicle_count = count($vehicles);
    
    // Get user's requests with payment info
    $stmt = $pdo->prepare("SELECT sr.*, st.service_name, st.price as service_price, v.license_plate, ps.spot_number 
                           FROM Service_request sr 
                           JOIN Service_type st ON sr.service_id = st.service_id 
                           LEFT JOIN Vehicle v ON sr.vehicle_id = v.vehicle_id 
                           LEFT JOIN Parking_spots ps ON sr.spot_id = ps.spot_id 
                           WHERE sr.user_id = ? ORDER BY sr.created_at DESC LIMIT 10");
    $stmt->execute([$user_id]);
    $requests = $stmt->fetchAll(PDO::FETCH_ASSOC);
    $request_count = count($requests);
    
    // Count by status
    $stmt = $pdo->prepare("SELECT status, COUNT(*) as count FROM Service_request WHERE user_id = ? GROUP BY status");
    $stmt->execute([$user_id]);
    $status_counts = $stmt->fetchAll(PDO::FETCH_KEY_PAIR);
}

if ($user_type == 'Admin' || $user_type == 'Manager') {
    // Get pending requests count
    $stmt = $pdo->query("SELECT COUNT(*) FROM Service_request WHERE status = 'Pending'");
    $pending_count = $stmt->fetchColumn();
    
    // Get approved today
    $stmt = $pdo->query("SELECT COUNT(*) FROM Service_request WHERE status = 'Approved' AND DATE(completed_at) = CURDATE()");
    $approved_today = $stmt->fetchColumn();
    
    // Get total users
    $stmt = $pdo->query("SELECT COUNT(*) FROM Users");
    $total_users = $stmt->fetchColumn();
    
    // Get total vehicles
    $stmt = $pdo->query("SELECT COUNT(*) FROM Vehicle");
    $total_vehicles = $stmt->fetchColumn();
    
    // Get pending requests
    $stmt = $pdo->query("SELECT sr.*, st.service_name, u.name as user_name, u.user_type, 
                         v.license_plate, ps.spot_number 
                         FROM Service_request sr 
                         JOIN Service_type st ON sr.service_id = st.service_id 
                         JOIN Users u ON sr.user_id = u.user_id 
                         LEFT JOIN Vehicle v ON sr.vehicle_id = v.vehicle_id 
                         LEFT JOIN Parking_spots ps ON sr.spot_id = ps.spot_id 
                         WHERE sr.status = 'Pending' ORDER BY sr.created_at DESC LIMIT 10");
    $pending_requests = $stmt->fetchAll(PDO::FETCH_ASSOC);
}

if ($user_type == 'Manager') {
    // Get user counts by type
    $stmt = $pdo->query("SELECT user_type, COUNT(*) as count FROM Users GROUP BY user_type");
    $user_counts = $stmt->fetchAll(PDO::FETCH_ASSOC);
}
?>

<div class="dashboard">
    <div class="dashboard-header">
        <h2>Welcome back, <?= htmlspecialchars($_SESSION['user_name']) ?> 👋</h2>
        <p>Here's what's happening with your parking today</p>
        <span class="user-badge">
            <?php 
            $icons = ['Student' => '🎓', 'Staff' => '👨‍💼', 'Admin' => '🔧', 'Manager' => '👔'];
            echo ($icons[$user_type] ?? '👤') . ' ' . $user_type;
            ?>
        </span>
    </div>
    
    <?php if ($user_type == 'Student' || $user_type == 'Staff'): ?>
    
    <!-- Student/Staff Dashboard -->
    <div class="cards-container">
        <div class="card animate-fadeInUp">
            <h3>🚗 My Vehicles</h3>
            <div class="card-value"><?= $vehicle_count ?></div>
            <p style="color:var(--gray);">Registered vehicles</p>
            <a href="add_vehicle.php" class="btn btn-gold btn-sm" style="margin-top:15px;">
                ➕ Add Vehicle
            </a>
        </div>
        
        <div class="card card-teal animate-fadeInUp delay-1">
            <h3>📋 My Requests</h3>
            <div class="card-value"><?= $request_count ?></div>
            <p style="color:var(--gray);">Total requests</p>
            <a href="add_request.php" class="btn btn-success btn-sm" style="margin-top:15px;">
                ➕ New Request
            </a>
        </div>
        
        <div class="card card-gold animate-fadeInUp delay-2">
            <h3>📊 Request Status</h3>
            <div class="stats-grid" style="margin-top:15px;">
                <div class="stat-item">
                    <div class="number"><?= $status_counts['Pending'] ?? 0 ?></div>
                    <div class="label">Pending</div>
                </div>
                <div class="stat-item">
                    <div class="number"><?= $status_counts['Approved'] ?? 0 ?></div>
                    <div class="label">Approved</div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Recent Requests Table -->
    <div class="table-container">
        <div class="table-header">
            <h3>📋 Recent Requests</h3>
            <div class="table-search">
                <input type="text" placeholder="Search requests...">
            </div>
        </div>
        
        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>Service</th>
                        <th>Vehicle</th>
                        <th>Parking Spot</th>
                        <th>Status</th>
                        <th>Date</th>
                    </tr>
                </thead>
                <tbody>
                    <?php if (empty($requests)): ?>
                        <tr>
                            <td colspan="5">
                                <div class="empty-state">
                                    <div class="icon">📭</div>
                                    <h4>No requests yet</h4>
                                    <p>Create your first parking request</p>
                                </div>
                            </td>
                        </tr>
                    <?php else: ?>
                        <?php foreach ($requests as $req): ?>
                        <tr>
                            <td>
                                <strong><?= htmlspecialchars($req['service_name']) ?></strong>
                                <small style="display:block;color:var(--gray);">EGP <?= number_format($req['service_price'] ?? 0, 2) ?></small>
                            </td>
                            <td><?= htmlspecialchars($req['license_plate'] ?? 'N/A') ?></td>
                            <td><?= htmlspecialchars($req['spot_number'] ?? 'N/A') ?></td>
                            <td>
                                <span class="badge badge-<?= strtolower($req['status']) ?>">
                                    <?= $req['status'] ?>
                                </span>
                                <?php 
                                $payment_status = $req['payment_status'] ?? 'Pending';
                                if ($req['status'] == 'Approved' && $payment_status != 'Paid'): 
                                ?>
                                    <br><a href="Payment.php?id=<?= $req['request_id'] ?>" 
                                           style="color:var(--primary-blue);font-size:0.85rem;margin-top:5px;display:inline-block;">
                                        💳 Pay Now
                                    </a>
                                <?php elseif ($payment_status == 'Paid'): ?>
                                    <br><small style="color:var(--primary-teal);font-size:0.85rem;">✅ Paid</small>
                                <?php endif; ?>
                            </td>
                            <td><?= date('M d, Y - H:i', strtotime($req['created_at'])) ?></td>
                        </tr>
                        <?php endforeach; ?>
                    <?php endif; ?>
                </tbody>
            </table>
        </div>
    </div>
    
    <?php elseif ($user_type == 'Admin' || $user_type == 'Manager'): ?>
    
    <!-- Admin/Manager Dashboard -->
    <div class="cards-container">
        <div class="card animate-fadeInUp">
            <h3>⏳ Pending Requests</h3>
            <div class="card-value"><?= $pending_count ?></div>
            <p style="color:var(--gray);">Awaiting approval</p>
            <a href="manage_requests.php" class="btn btn-primary btn-sm" style="margin-top:15px;">
                View All →
            </a>
        </div>
        
        <div class="card card-teal animate-fadeInUp delay-1">
            <h3>✅ Approved Today</h3>
            <div class="card-value"><?= $approved_today ?></div>
            <p style="color:var(--gray);">Requests approved today</p>
        </div>
        
        <div class="card card-gold animate-fadeInUp delay-2">
            <h3>👥 Total Users</h3>
            <div class="card-value"><?= $total_users ?></div>
            <p style="color:var(--gray);">Registered users</p>
            <?php if ($user_type == 'Manager'): ?>
            <a href="view_users.php" class="btn btn-gold btn-sm" style="margin-top:15px;">
                View Users →
            </a>
            <?php endif; ?>
        </div>
        
        <div class="card animate-fadeInUp delay-3">
            <h3>🚗 Total Vehicles</h3>
            <div class="card-value"><?= $total_vehicles ?></div>
            <p style="color:var(--gray);">Registered vehicles</p>
        </div>
    </div>
    
    <?php if ($user_type == 'Manager'): ?>
    <!-- User Statistics for Manager -->
    <div class="table-container" style="margin-bottom:30px;">
        <h3 style="margin-bottom:20px;color:var(--primary-blue);">👥 User Statistics</h3>
        <div class="stats-grid">
            <?php foreach ($user_counts as $uc): ?>
            <div class="stat-item">
                <div class="number"><?= $uc['count'] ?></div>
                <div class="label"><?= $uc['user_type'] ?>s</div>
            </div>
            <?php endforeach; ?>
        </div>
    </div>
    <?php endif; ?>
    
    <!-- Pending Requests Table -->
    <div class="table-container">
        <div class="table-header">
            <h3>⏳ Pending Requests</h3>
            <div class="table-search">
                <input type="text" placeholder="Search requests...">
            </div>
        </div>
        
        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>User</th>
                        <th>Type</th>
                        <th>Service</th>
                        <th>Vehicle</th>
                        <th>Date</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <?php if (empty($pending_requests)): ?>
                        <tr>
                            <td colspan="6">
                                <div class="empty-state">
                                    <div class="icon">✅</div>
                                    <h4>All caught up!</h4>
                                    <p>No pending requests at the moment</p>
                                </div>
                            </td>
                        </tr>
                    <?php else: ?>
                        <?php foreach ($pending_requests as $req): ?>
                        <tr>
                            <td>
                                <strong><?= htmlspecialchars($req['user_name']) ?></strong>
                            </td>
                            <td>
                                <span class="badge badge-info"><?= $req['user_type'] ?></span>
                            </td>
                            <td><?= htmlspecialchars($req['service_name']) ?></td>
                            <td><?= htmlspecialchars($req['license_plate'] ?? 'N/A') ?></td>
                            <td><?= date('M d, Y', strtotime($req['created_at'])) ?></td>
                            <td>
                                <div class="action-btns">
                                    <a href="manage_requests.php?action=approve&id=<?= $req['request_id'] ?>" 
                                       class="action-btn action-btn-approve" 
                                       data-tooltip="Approve"
                                       data-confirm="Approve this request?">
                                        ✓
                                    </a>
                                    <a href="manage_requests.php?action=reject&id=<?= $req['request_id'] ?>" 
                                       class="action-btn action-btn-reject"
                                       data-tooltip="Reject"
                                       data-confirm="Reject this request?">
                                        ✗
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
    
    <?php endif; ?>
</div>

<?php include 'includes/footer.php'; ?>