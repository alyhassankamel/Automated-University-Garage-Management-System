<?php
include 'includes/header.php';

if (getUserType() != 'Manager') {
    redirect('dashboard.php');
}

$manager_id = $_SESSION['user_id'];
$success = '';
$error = '';

// Handle garage status toggle
if (isset($_GET['garage_action']) && isset($_GET['garage_id'])) {
    $garage_id = (int)$_GET['garage_id'];
    $action = $_GET['garage_action'];
    
    // Check if garage_status column exists, if not add it
    try {
        $stmt = $pdo->query("SHOW COLUMNS FROM Parking_garage LIKE 'garage_status'");
        if ($stmt->rowCount() == 0) {
            $pdo->exec("ALTER TABLE Parking_garage ADD COLUMN garage_status ENUM('Open', 'Closed') DEFAULT 'Open'");
        }
    } catch (Exception $e) {
        // Column might already exist or table doesn't exist
    }
    
    if ($action == 'open') {
        $stmt = $pdo->prepare("UPDATE Parking_garage SET garage_status = 'Open' WHERE garage_id = ?");
        if ($stmt->execute([$garage_id])) {
            $success = 'Garage opened successfully!';
        } else {
            $error = 'Failed to open garage.';
        }
    } elseif ($action == 'close') {
        $stmt = $pdo->prepare("UPDATE Parking_garage SET garage_status = 'Closed' WHERE garage_id = ?");
        if ($stmt->execute([$garage_id])) {
            $success = 'Garage closed successfully!';
        } else {
            $error = 'Failed to close garage.';
        }
    }
}

// Handle request actions (same as admin)
if (isset($_GET['action']) && isset($_GET['id'])) {
    $request_id = (int)$_GET['id'];
    $action = $_GET['action'];
    
    if ($action == 'approve') {
        $pdo->beginTransaction();
        try {
            // Update request
            $stmt = $pdo->prepare("UPDATE Service_request SET status = 'Approved', approved_by = ?, completed_at = NOW() WHERE request_id = ?");
            $stmt->execute([$manager_id, $request_id]);
            
            // Approve vehicle if linked
            $stmt = $pdo->prepare("UPDATE Vehicle v 
                                   INNER JOIN Service_request sr ON v.vehicle_id = sr.vehicle_id 
                                   SET v.access_status = 1, v.approved_by = ? 
                                   WHERE sr.request_id = ?");
            $stmt->execute([$manager_id, $request_id]);
            
            // Update spot status if selected
            $stmt = $pdo->prepare("UPDATE Parking_spots ps 
                                   INNER JOIN Service_request sr ON ps.spot_id = sr.spot_id 
                                   SET ps.spot_status = 'Reserved' 
                                   WHERE sr.request_id = ?");
            $stmt->execute([$request_id]);
            
            $pdo->commit();
            $success = 'Request approved successfully!';
        } catch (Exception $e) {
            $pdo->rollBack();
            $error = 'Failed to approve request.';
        }
    } elseif ($action == 'reject') {
        $stmt = $pdo->prepare("UPDATE Service_request SET status = 'Rejected', approved_by = ?, completed_at = NOW() WHERE request_id = ?");
        if ($stmt->execute([$manager_id, $request_id])) {
            $success = 'Request rejected.';
        } else {
            $error = 'Failed to reject request.';
        }
    }
}

// Get active tab
$active_tab = $_GET['tab'] ?? 'requests';

// Get request filter
$status_filter = $_GET['status'] ?? 'all';

// Get all garages with status
try {
    $stmt = $pdo->query("SHOW COLUMNS FROM Parking_garage LIKE 'garage_status'");
    $has_status_column = $stmt->rowCount() > 0;
    
    if (!$has_status_column) {
        // If column doesn't exist, add it
        $pdo->exec("ALTER TABLE Parking_garage ADD COLUMN garage_status ENUM('Open', 'Closed') DEFAULT 'Open'");
        // Set all existing garages to 'Open' by default
        $pdo->exec("UPDATE Parking_garage SET garage_status = 'Open' WHERE garage_status IS NULL");
    }
    
    // Always select garage_status explicitly and use COALESCE to default to 'Open'
    $garages_stmt = $pdo->query("SELECT pg.*, 
                                COALESCE(pg.garage_status, 'Open') as garage_status,
                                COUNT(ps.spot_id) as total_spots,
                                SUM(CASE WHEN ps.spot_status = 'Available' THEN 1 ELSE 0 END) as available_spots,
                                SUM(CASE WHEN ps.spot_status = 'Reserved' THEN 1 ELSE 0 END) as reserved_spots
                                FROM Parking_garage pg
                                LEFT JOIN Parking_spots ps ON pg.garage_id = ps.garage_id
                                GROUP BY pg.garage_id
                                ORDER BY pg.name");
    $garages = $garages_stmt->fetchAll(PDO::FETCH_ASSOC);
} catch (Exception $e) {
    $garages = [];
    $error = 'Error loading garages: ' . $e->getMessage();
}

// Ensure price column exists in Service_type
try {
    $stmt = $pdo->query("SHOW COLUMNS FROM Service_type LIKE 'price'");
    if ($stmt->rowCount() == 0) {
        $pdo->exec("ALTER TABLE Service_type ADD COLUMN price DECIMAL(10,2) DEFAULT 0.00");
    }
} catch (Exception $e) {
    // Column might already exist
}

// Build requests query
$sql = "SELECT sr.*, st.service_name, st.price as service_price, u.name as user_name, u.user_type, u.uni_ID,
        v.license_plate, v.model as vehicle_model, ps.spot_number, pg.name as garage_name,
        approver.name as approver_name
        FROM Service_request sr 
        JOIN Service_type st ON sr.service_id = st.service_id 
        JOIN Users u ON sr.user_id = u.user_id 
        LEFT JOIN Vehicle v ON sr.vehicle_id = v.vehicle_id 
        LEFT JOIN Parking_spots ps ON sr.spot_id = ps.spot_id 
        LEFT JOIN Parking_garage pg ON ps.garage_id = pg.garage_id
        LEFT JOIN Users approver ON sr.approved_by = approver.user_id";

if ($status_filter != 'all') {
    $sql .= " WHERE sr.status = ?";
}
$sql .= " ORDER BY sr.status = 'Pending' DESC, sr.created_at DESC";

if ($status_filter != 'all') {
    $stmt = $pdo->prepare($sql);
    $stmt->execute([$status_filter]);
} else {
    $stmt = $pdo->query($sql);
}
$requests = $stmt->fetchAll(PDO::FETCH_ASSOC);

// Get request counts
$stmt = $pdo->query("SELECT status, COUNT(*) as count FROM Service_request GROUP BY status");
$status_counts = $stmt->fetchAll(PDO::FETCH_KEY_PAIR);

// Get user filter
$user_filter = $_GET['filter'] ?? 'all';

// Build users query
if ($user_filter == 'all') {
    $stmt = $pdo->query("SELECT u.*, 
                         (SELECT COUNT(*) FROM Vehicle WHERE user_id = u.user_id) as vehicle_count,
                         (SELECT COUNT(*) FROM Service_request WHERE user_id = u.user_id) as request_count
                         FROM Users u ORDER BY u.created_at DESC");
} else {
    $stmt = $pdo->prepare("SELECT u.*, 
                           (SELECT COUNT(*) FROM Vehicle WHERE user_id = u.user_id) as vehicle_count,
                           (SELECT COUNT(*) FROM Service_request WHERE user_id = u.user_id) as request_count
                           FROM Users u WHERE u.user_type = ? ORDER BY u.created_at DESC");
    $stmt->execute([$user_filter]);
}
$users = $stmt->fetchAll(PDO::FETCH_ASSOC);

// Get user type counts
$stmt = $pdo->query("SELECT user_type, COUNT(*) as count FROM Users GROUP BY user_type");
$type_counts = $stmt->fetchAll(PDO::FETCH_KEY_PAIR);
$total_users = array_sum($type_counts);
?>

<div class="dashboard">
    <div class="dashboard-header">
        <h2>👔 Manager Dashboard</h2>
        <p>Manage requests, users, and garage operations</p>
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
    
    <!-- Tabs -->
    <div class="tabs-container" style="margin-bottom:30px;border-bottom:2px solid var(--light-gray);">
        <div class="tabs">
            <a href="?tab=requests&status=<?= $status_filter ?>" 
               class="tab <?= $active_tab == 'requests' ? 'active' : '' ?>">
                📋 Manage Requests
            </a>
            <a href="?tab=garages" 
               class="tab <?= $active_tab == 'garages' ? 'active' : '' ?>">
                🏢 Garage Management
            </a>
            <a href="?tab=users&filter=<?= $user_filter ?>" 
               class="tab <?= $active_tab == 'users' ? 'active' : '' ?>">
                👥 User Management
            </a>
        </div>
    </div>
    
    <?php if ($active_tab == 'requests'): ?>
    
    <!-- Requests Tab -->
    <!-- Stats Cards -->
    <div class="cards-container">
        <div class="card animate-fadeInUp">
            <h3>⏳ Pending</h3>
            <div class="card-value"><?= $status_counts['Pending'] ?? 0 ?></div>
        </div>
        <div class="card card-teal animate-fadeInUp delay-1">
            <h3>✅ Approved</h3>
            <div class="card-value"><?= $status_counts['Approved'] ?? 0 ?></div>
        </div>
        <div class="card card-gold animate-fadeInUp delay-2">
            <h3>❌ Rejected</h3>
            <div class="card-value"><?= $status_counts['Rejected'] ?? 0 ?></div>
        </div>
    </div>
    
    <!-- Requests Table -->
    <div class="table-container">
        <div class="table-header">
            <h3>📋 All Requests</h3>
            <div style="display:flex;gap:15px;align-items:center;flex-wrap:wrap;">
                <div class="filter-buttons">
                    <a href="?tab=requests&status=all" class="filter-btn <?= $status_filter == 'all' ? 'active' : '' ?>">All</a>
                    <a href="?tab=requests&status=Pending" class="filter-btn <?= $status_filter == 'Pending' ? 'active' : '' ?>">Pending</a>
                    <a href="?tab=requests&status=Approved" class="filter-btn <?= $status_filter == 'Approved' ? 'active' : '' ?>">Approved</a>
                    <a href="?tab=requests&status=Rejected" class="filter-btn <?= $status_filter == 'Rejected' ? 'active' : '' ?>">Rejected</a>
                </div>
                <div class="table-search">
                    <input type="text" placeholder="Search...">
                </div>
            </div>
        </div>
        
        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Receipt ID</th>
                        <th>User</th>
                        <th>Type</th>
                        <th>Service</th>
                        <th>Price</th>
                        <th>Payment</th>
                        <th>Vehicle</th>
                        <th>Spot</th>
                        <th>Status</th>
                        <th>Date</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <?php if (empty($requests)): ?>
                        <tr>
                            <td colspan="12">
                                <div class="empty-state">
                                    <div class="icon">📭</div>
                                    <h4>No requests found</h4>
                                    <p>No requests match your filter</p>
                                </div>
                            </td>
                        </tr>
                    <?php else: ?>
                        <?php foreach ($requests as $req): ?>
                        <tr>
                            <td><strong>#<?= $req['request_id'] ?></strong></td>
                            <td>
                                <?php if (!empty($req['receipt_id'])): ?>
                                    <strong style="color:var(--primary-blue);font-size:0.9rem;"><?= htmlspecialchars($req['receipt_id']) ?></strong>
                                <?php else: ?>
                                    <span style="color:var(--gray);font-size:0.85rem;">-</span>
                                <?php endif; ?>
                            </td>
                            <td>
                                <div>
                                    <strong><?= htmlspecialchars($req['user_name']) ?></strong>
                                    <small style="display:block;color:var(--gray);"><?= htmlspecialchars($req['uni_ID']) ?></small>
                                </div>
                            </td>
                            <td><span class="badge badge-info"><?= $req['user_type'] ?></span></td>
                            <td><?= htmlspecialchars($req['service_name']) ?></td>
                            <td>
                                <strong style="color:var(--primary-blue);">EGP <?= number_format($req['service_price'] ?? 0, 2) ?></strong>
                            </td>
                            <td>
                                <?php if ($req['status'] == 'Approved'): ?>
                                    <?php if (($req['payment_status'] ?? 'Pending') == 'Paid'): ?>
                                        <span class="badge badge-approved" style="font-size:0.85rem;">
                                            ✅ <?= htmlspecialchars($req['payment_method'] ?? 'N/A') ?><br>
                                            <small>EGP <?= number_format($req['payment_amount'] ?? 0, 2) ?></small>
                                        </span>
                                    <?php else: ?>
                                        <span class="badge badge-pending" style="font-size:0.85rem;">
                                            ⏳ Pending Payment
                                        </span>
                                    <?php endif; ?>
                                <?php else: ?>
                                    <span style="color:var(--gray);">-</span>
                                <?php endif; ?>
                            </td>
                            <td>
                                <?php if ($req['license_plate']): ?>
                                    <div>
                                        <strong><?= htmlspecialchars($req['license_plate']) ?></strong>
                                        <small style="display:block;color:var(--gray);"><?= htmlspecialchars($req['vehicle_model']) ?></small>
                                    </div>
                                <?php else: ?>
                                    <span style="color:var(--gray);">N/A</span>
                                <?php endif; ?>
                            </td>
                            <td>
                                <?php if ($req['spot_number']): ?>
                                    <div>
                                        <strong><?= htmlspecialchars($req['spot_number']) ?></strong>
                                        <small style="display:block;color:var(--gray);"><?= htmlspecialchars($req['garage_name']) ?></small>
                                    </div>
                                <?php else: ?>
                                    <span style="color:var(--gray);">N/A</span>
                                <?php endif; ?>
                            </td>
                            <td>
                                <span class="badge badge-<?= strtolower($req['status']) ?>">
                                    <?= $req['status'] ?>
                                </span>
                            </td>
                            <td>
                                <div>
                                    <?= date('M d, Y', strtotime($req['created_at'])) ?>
                                    <small style="display:block;color:var(--gray);"><?= date('H:i', strtotime($req['created_at'])) ?></small>
                                </div>
                            </td>
                            <td>
                                <?php if ($req['status'] == 'Pending'): ?>
                                    <div class="action-btns">
                                        <a href="?tab=requests&action=approve&id=<?= $req['request_id'] ?>&status=<?= $status_filter ?>" 
                                           class="action-btn action-btn-approve" 
                                           data-tooltip="Approve"
                                           data-confirm="Approve this request?">
                                            ✓
                                        </a>
                                        <a href="?tab=requests&action=reject&id=<?= $req['request_id'] ?>&status=<?= $status_filter ?>" 
                                           class="action-btn action-btn-reject"
                                           data-tooltip="Reject"
                                           data-confirm="Reject this request?">
                                            ✗
                                        </a>
                                    </div>
                                <?php else: ?>
                                    <small style="color:var(--gray);">
                                        By: <?= htmlspecialchars($req['approver_name'] ?? 'System') ?>
                                    </small>
                                <?php endif; ?>
                            </td>
                        </tr>
                        <?php endforeach; ?>
                    <?php endif; ?>
                </tbody>
            </table>
        </div>
    </div>
    
    <?php elseif ($active_tab == 'garages'): ?>
    
    <!-- Garage Management Tab -->
    <div class="cards-container">
        <?php foreach ($garages as $garage): ?>
        <div class="card animate-fadeInUp" style="position:relative;">
            <div style="display:flex;justify-content:space-between;align-items:start;margin-bottom:15px;">
                <div>
                    <h3>🏢 <?= htmlspecialchars($garage['name']) ?></h3>
                    <p style="color:var(--gray);margin-top:5px;">
                        <?= htmlspecialchars($garage['location'] ?? 'No location specified') ?>
                    </p>
                </div>
                <?php 
                $garage_status = $garage['garage_status'] ?? 'Open';
                $is_open = ($garage_status == 'Open');
                ?>
                <span class="badge badge-<?= $is_open ? 'approved' : 'rejected' ?>" 
                      style="font-size:0.9rem;padding:8px 15px;">
                    <?= $is_open ? '🟢 Open' : '🔴 Closed' ?>
                </span>
            </div>
            
            <div class="stats-grid" style="margin:20px 0;">
                <div class="stat-item">
                    <div class="number"><?= $garage['total_spots'] ?? 0 ?></div>
                    <div class="label">Total Spots</div>
                </div>
                <div class="stat-item">
                    <div class="number" style="color:var(--primary-teal);"><?= $garage['available_spots'] ?? 0 ?></div>
                    <div class="label">Available</div>
                </div>
                <div class="stat-item">
                    <div class="number" style="color:var(--primary-gold);"><?= $garage['reserved_spots'] ?? 0 ?></div>
                    <div class="label">Reserved</div>
                </div>
            </div>
            
            <div style="display:flex;gap:10px;margin-top:20px;">
                <?php if ($is_open): ?>
                    <a href="?tab=garages&garage_action=close&garage_id=<?= $garage['garage_id'] ?>" 
                       class="btn btn-danger btn-sm"
                       data-confirm="Close this garage? All parking operations will be suspended.">
                        🔴 Close Garage
                    </a>
                <?php else: ?>
                    <a href="?tab=garages&garage_action=open&garage_id=<?= $garage['garage_id'] ?>" 
                       class="btn btn-success btn-sm"
                       data-confirm="Open this garage? Parking operations will resume.">
                        🟢 Open Garage
                    </a>
                <?php endif; ?>
            </div>
        </div>
        <?php endforeach; ?>
        
        <?php if (empty($garages)): ?>
        <div class="card" style="text-align:center;padding:40px;">
            <div class="empty-state">
                <div class="icon">🏢</div>
                <h4>No garages found</h4>
                <p>No parking garages are registered in the system</p>
            </div>
        </div>
        <?php endif; ?>
    </div>
    
    <?php elseif ($active_tab == 'users'): ?>
    
    <!-- Users Tab -->
    <!-- Stats Cards -->
    <div class="cards-container">
        <div class="card animate-fadeInUp">
            <h3>👥 Total Users</h3>
            <div class="card-value"><?= $total_users ?></div>
        </div>
        <div class="card card-teal animate-fadeInUp delay-1">
            <h3>🎓 Students</h3>
            <div class="card-value"><?= $type_counts['Student'] ?? 0 ?></div>
        </div>
        <div class="card card-gold animate-fadeInUp delay-2">
            <h3>👨‍💼 Staff</h3>
            <div class="card-value"><?= $type_counts['Staff'] ?? 0 ?></div>
        </div>
        <div class="card animate-fadeInUp delay-3">
            <h3>🔧 Admins</h3>
            <div class="card-value"><?= $type_counts['Admin'] ?? 0 ?></div>
        </div>
        <div class="card animate-fadeInUp delay-4">
            <h3>👔 Managers</h3>
            <div class="card-value"><?= $type_counts['Manager'] ?? 0 ?></div>
        </div>
    </div>
    
    <!-- Users Table -->
    <div class="table-container">
        <div class="table-header">
            <h3>📋 All Users</h3>
            <div style="display:flex;gap:15px;align-items:center;flex-wrap:wrap;">
                <div class="filter-buttons">
                    <a href="?tab=users&filter=all" class="filter-btn <?= $user_filter == 'all' ? 'active' : '' ?>">All</a>
                    <a href="?tab=users&filter=Student" class="filter-btn <?= $user_filter == 'Student' ? 'active' : '' ?>">Students</a>
                    <a href="?tab=users&filter=Staff" class="filter-btn <?= $user_filter == 'Staff' ? 'active' : '' ?>">Staff</a>
                    <a href="?tab=users&filter=Admin" class="filter-btn <?= $user_filter == 'Admin' ? 'active' : '' ?>">Admins</a>
                    <a href="?tab=users&filter=Manager" class="filter-btn <?= $user_filter == 'Manager' ? 'active' : '' ?>">Managers</a>
                </div>
                <div class="table-search">
                    <input type="text" placeholder="Search users...">
                </div>
            </div>
        </div>
        
        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>User ID</th>
                        <th>Name</th>
                        <th>Phone</th>
                        <th>Type</th>
                        <th>Vehicles</th>
                        <th>Requests</th>
                        <th>Status</th>
                        <th>Joined</th>
                    </tr>
                </thead>
                <tbody>
                    <?php if (empty($users)): ?>
                        <tr>
                            <td colspan="8">
                                <div class="empty-state">
                                    <div class="icon">👥</div>
                                    <h4>No users found</h4>
                                </div>
                            </td>
                        </tr>
                    <?php else: ?>
                        <?php foreach ($users as $u): ?>
                        <tr>
                            <td><strong><?= htmlspecialchars($u['uni_ID']) ?></strong></td>
                            <td>
                                <div style="display:flex;align-items:center;gap:10px;">
                                    <div style="width:40px;height:40px;border-radius:50%;background:linear-gradient(135deg,var(--primary-blue),var(--primary-teal));display:flex;align-items:center;justify-content:center;color:white;font-weight:600;">
                                        <?= strtoupper(substr($u['name'], 0, 1)) ?>
                                    </div>
                                    <span><?= htmlspecialchars($u['name']) ?></span>
                                </div>
                            </td>
                            <td><?= htmlspecialchars($u['phone']) ?></td>
                            <td>
                                <?php
                                $user_type_display = ucfirst($u['user_type'] ?? 'Unknown');
                                $type_classes = [
                                    'Student' => 'badge-info',
                                    'Staff' => 'badge-approved',
                                    'Admin' => 'badge-warning',
                                    'Manager' => 'badge-pending'
                                ];
                                $icons = ['Student' => '🎓', 'Staff' => '👨‍💼', 'Admin' => '🔧', 'Manager' => '👔'];
                                $badge_class = $type_classes[$user_type_display] ?? 'badge-info';
                                $icon = $icons[$user_type_display] ?? '👤';
                                ?>
                                <span class="badge <?= $badge_class ?>">
                                    <?= $icon ?> <?= htmlspecialchars($user_type_display) ?>
                                </span>
                            </td>
                            <td>
                                <span style="background:var(--light-bg);padding:5px 12px;border-radius:15px;font-weight:600;">
                                    🚗 <?= $u['vehicle_count'] ?>
                                </span>
                            </td>
                            <td>
                                <span style="background:var(--light-bg);padding:5px 12px;border-radius:15px;font-weight:600;">
                                    📋 <?= $u['request_count'] ?>
                                </span>
                            </td>
                            <td>
                                <span class="badge <?= $u['access_status'] ? 'badge-approved' : 'badge-rejected' ?>">
                                    <?= $u['access_status'] ? 'Active' : 'Inactive' ?>
                                </span>
                            </td>
                            <td>
                                <div>
                                    <?= date('M d, Y', strtotime($u['created_at'])) ?>
                                    <small style="display:block;color:var(--gray);">
                                        <?= date('H:i', strtotime($u['created_at'])) ?>
                                    </small>
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

<style>
.tabs-container {
    margin-bottom: 30px;
}

.tabs {
    display: flex;
    gap: 10px;
    flex-wrap: wrap;
}

.tab {
    padding: 12px 24px;
    background: var(--light-bg);
    border-radius: 8px;
    text-decoration: none;
    color: var(--dark-blue);
    font-weight: 600;
    transition: all 0.3s ease;
    border-bottom: 3px solid transparent;
    margin-bottom: -2px;
}

.tab:hover {
    background: var(--light-gray);
    color: var(--primary-blue);
}

.tab.active {
    background: var(--primary-blue);
    color: white;
    border-bottom-color: var(--primary-blue);
}
</style>

<?php include 'includes/footer.php'; ?>

