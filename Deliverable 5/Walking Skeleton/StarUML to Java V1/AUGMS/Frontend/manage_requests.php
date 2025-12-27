<?php
include 'includes/header.php';

if (!in_array(getUserType(), ['Admin', 'Manager'])) {
    redirect('dashboard.php');
}

$admin_id = $_SESSION['user_id'];
$success = '';
$error = '';

// Handle actions
if (isset($_GET['action']) && isset($_GET['id'])) {
    $request_id = (int)$_GET['id'];
    $action = $_GET['action'];
    
    if ($action == 'approve') {
        $pdo->beginTransaction();
        try {
            // Update request
            $stmt = $pdo->prepare("UPDATE Service_request SET status = 'Approved', approved_by = ?, completed_at = NOW() WHERE request_id = ?");
            $stmt->execute([$admin_id, $request_id]);
            
            // Approve vehicle if linked
            $stmt = $pdo->prepare("UPDATE Vehicle v 
                                   INNER JOIN Service_request sr ON v.vehicle_id = sr.vehicle_id 
                                   SET v.access_status = 1, v.approved_by = ? 
                                   WHERE sr.request_id = ?");
            $stmt->execute([$admin_id, $request_id]);
            
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
        if ($stmt->execute([$admin_id, $request_id])) {
            $success = 'Request rejected.';
        } else {
            $error = 'Failed to reject request.';
        }
    }
}

// Get filter
$status_filter = $_GET['status'] ?? 'all';

// Ensure price column exists in Service_type
try {
    $stmt = $pdo->query("SHOW COLUMNS FROM Service_type LIKE 'price'");
    if ($stmt->rowCount() == 0) {
        $pdo->exec("ALTER TABLE Service_type ADD COLUMN price DECIMAL(10,2) DEFAULT 0.00");
    }
} catch (Exception $e) {
    // Column might already exist
}

// Build query
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

// Get counts
$stmt = $pdo->query("SELECT status, COUNT(*) as count FROM Service_request GROUP BY status");
$status_counts = $stmt->fetchAll(PDO::FETCH_KEY_PAIR);
?>

<div class="dashboard">
    <div class="dashboard-header">
        <h2>⚙️ Manage Requests</h2>
        <p>Review and process parking and service requests</p>
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
                    <a href="?status=all" class="filter-btn <?= $status_filter == 'all' ? 'active' : '' ?>">All</a>
                    <a href="?status=Pending" class="filter-btn <?= $status_filter == 'Pending' ? 'active' : '' ?>">Pending</a>
                    <a href="?status=Approved" class="filter-btn <?= $status_filter == 'Approved' ? 'active' : '' ?>">Approved</a>
                    <a href="?status=Rejected" class="filter-btn <?= $status_filter == 'Rejected' ? 'active' : '' ?>">Rejected</a>
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
                                        <a href="?action=approve&id=<?= $req['request_id'] ?>&status=<?= $status_filter ?>" 
                                           class="action-btn action-btn-approve" 
                                           data-tooltip="Approve"
                                           data-confirm="Approve this request?">
                                            ✓
                                        </a>
                                        <a href="?action=reject&id=<?= $req['request_id'] ?>&status=<?= $status_filter ?>" 
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
</div>

<?php include 'includes/footer.php'; ?>