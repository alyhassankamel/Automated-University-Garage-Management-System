<?php
include 'includes/header.php';

if (!in_array(getUserType(), ['Student', 'Staff'])) {
    redirect('dashboard.php');
}

$user_id = $_SESSION['user_id'];
$error = '';
$success = '';

// Get vehicles
$stmt = $pdo->prepare("SELECT * FROM Vehicle WHERE user_id = ?");
$stmt->execute([$user_id]);
$vehicles = $stmt->fetchAll(PDO::FETCH_ASSOC);

// Get services with prices
// Ensure price column exists
try {
    $stmt = $pdo->query("SHOW COLUMNS FROM Service_type LIKE 'price'");
    if ($stmt->rowCount() == 0) {
        $pdo->exec("ALTER TABLE Service_type ADD COLUMN price DECIMAL(10,2) DEFAULT 0.00");
    }
} catch (Exception $e) {
    // Column might already exist
}

$stmt = $pdo->query("SELECT * FROM Service_type ORDER BY service_name");
$services = $stmt->fetchAll(PDO::FETCH_ASSOC);

// Get available spots from OPEN garages only
// First, ensure garage_status column exists
try {
    $stmt = $pdo->query("SHOW COLUMNS FROM Parking_garage LIKE 'garage_status'");
    $has_status_column = $stmt->rowCount() > 0;
    
    if (!$has_status_column) {
        $pdo->exec("ALTER TABLE Parking_garage ADD COLUMN garage_status ENUM('Open', 'Closed') DEFAULT 'Open'");
        $pdo->exec("UPDATE Parking_garage SET garage_status = 'Open' WHERE garage_status IS NULL");
    }
} catch (Exception $e) {
    // Column might already exist
}

// Query spots only from open garages
$stmt = $pdo->query("SELECT ps.*, pg.name as garage_name 
                     FROM Parking_spots ps 
                     JOIN Parking_garage pg ON ps.garage_id = pg.garage_id 
                     WHERE ps.spot_status = 'Available' 
                     AND COALESCE(pg.garage_status, 'Open') = 'Open'
                     ORDER BY pg.name, ps.spot_number");
$spots = $stmt->fetchAll(PDO::FETCH_ASSOC);

// Handle form
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $service_id = $_POST['service_id'];
    $vehicle_id = !empty($_POST['vehicle_id']) ? $_POST['vehicle_id'] : null;
    $spot_id = !empty($_POST['spot_id']) ? $_POST['spot_id'] : null;
    
    // Validate that if a spot is selected, it's from an open garage
    if ($spot_id) {
        // Check if spot exists, is available, and from an open garage
        $stmt = $pdo->prepare("SELECT ps.spot_status, COALESCE(pg.garage_status, 'Open') as garage_status
                               FROM Parking_spots ps 
                               JOIN Parking_garage pg ON ps.garage_id = pg.garage_id 
                               WHERE ps.spot_id = ?");
        $stmt->execute([$spot_id]);
        $spot_info = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if (!$spot_info) {
            $error = 'Selected parking spot does not exist.';
        } elseif ($spot_info['spot_status'] != 'Available') {
            $error = 'Selected parking spot is not available.';
        } elseif ($spot_info['garage_status'] == 'Closed') {
            $error = 'Cannot select parking spot from a closed garage. Please select a spot from an open garage.';
        }
    }
    
    // Only proceed if no validation errors
    if (empty($error)) {
        // Generate unique receipt ID
        $receipt_id = 'REC-' . $user_id . '-' . time() . '-' . rand(1000, 9999);
        
        // Ensure receipt_id column exists
        try {
            $stmt = $pdo->query("SHOW COLUMNS FROM Service_request LIKE 'receipt_id'");
            if ($stmt->rowCount() == 0) {
                $pdo->exec("ALTER TABLE Service_request ADD COLUMN receipt_id VARCHAR(50) DEFAULT NULL");
                // Make it unique if possible
                try {
                    $pdo->exec("ALTER TABLE Service_request ADD UNIQUE KEY unique_receipt_id (receipt_id)");
                } catch (Exception $e) {
                    // Unique constraint might already exist or fail
                }
            }
        } catch (Exception $e) {
            // Column might already exist
        }
        
        $stmt = $pdo->prepare("INSERT INTO Service_request (user_id, vehicle_id, service_id, spot_id, receipt_id) VALUES (?, ?, ?, ?, ?)");
        if ($stmt->execute([$user_id, $vehicle_id, $service_id, $spot_id, $receipt_id])) {
            $success = 'Request submitted successfully! Receipt ID: <strong>' . $receipt_id . '</strong>. Please wait for admin approval.';
        } else {
            $error = 'Failed to submit request. Please try again.';
        }
    }
}
?>

<div class="dashboard">
    <div class="dashboard-header">
        <h2>📝 New Service Request</h2>
        <p>Request parking permits and car services</p>
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
        <!-- Request Form -->
        <div class="card animate-fadeInUp">
            <h3>➕ Create New Request</h3>
            
            <form method="POST" style="margin-top:20px;">
                <div class="form-group">
                    <label>Service Type *</label>
                    <select name="service_id" id="service_id" required onchange="updateServicePrice()">
                        <option value="">-- Select Service --</option>
                        <?php foreach ($services as $s): ?>
                            <option value="<?= $s['service_id'] ?>" data-price="<?= number_format($s['price'] ?? 0, 2) ?>">
                                <?= htmlspecialchars($s['service_name']) ?> - EGP <?= number_format($s['price'] ?? 0, 2) ?>
                            </option>
                        <?php endforeach; ?>
                    </select>
                    <div id="service_price_display" style="margin-top:10px;padding:10px;background:var(--light-bg);border-radius:5px;display:none;">
                        <strong style="color:var(--primary-blue);">💰 Price: EGP <span id="selected_price">0.00</span></strong>
                        <small style="display:block;color:var(--gray);margin-top:5px;">Payment will be required after approval</small>
                    </div>
                </div>
                
                <div class="form-group">
                    <label>Select Vehicle</label>
                    <select name="vehicle_id">
                        <option value="">-- No Vehicle --</option>
                        <?php foreach ($vehicles as $v): ?>
                            <option value="<?= $v['vehicle_id'] ?>">
                                <?= htmlspecialchars($v['license_plate']) ?> - <?= htmlspecialchars($v['model']) ?>
                                <?= $v['access_status'] ? '✓' : '(Pending)' ?>
                            </option>
                        <?php endforeach; ?>
                    </select>
                    <?php if (empty($vehicles)): ?>
                        <small style="color:var(--gray);display:block;margin-top:5px;">
                            ⚠️ <a href="add_vehicle.php" style="color:var(--primary-blue);">Add a vehicle first</a>
                        </small>
                    <?php endif; ?>
                </div>
                
                <div class="form-group">
                    <label>Parking Spot (Optional)</label>
                    <select name="spot_id">
                        <option value="">-- Select Spot --</option>
                        <?php 
                        $current_garage = '';
                        foreach ($spots as $sp): 
                            if ($current_garage != $sp['garage_name']):
                                if ($current_garage != '') echo '</optgroup>';
                                $current_garage = $sp['garage_name'];
                                echo '<optgroup label="' . htmlspecialchars($current_garage) . '">';
                            endif;
                        ?>
                            <option value="<?= $sp['spot_id'] ?>">
                                Spot <?= htmlspecialchars($sp['spot_number']) ?>
                            </option>
                        <?php endforeach; ?>
                        <?php if ($current_garage != '') echo '</optgroup>'; ?>
                    </select>
                </div>
                
                <button type="submit" class="btn btn-primary">
                    📤 Submit Request
                </button>
            </form>
        </div>
        
        <!-- Available Services -->
        <div class="card card-teal animate-fadeInUp delay-1">
            <h3>📌 Available Services</h3>
            <ul style="list-style:none;padding:0;margin-top:15px;">
                <?php foreach ($services as $s): ?>
                    <li style="padding:15px;border-bottom:1px solid var(--light-gray);display:flex;align-items:flex-start;gap:12px;">
                        <span style="font-size:1.5rem;">
                            <?php
                            $icons = [
                                'Parking Permit' => '🅿️',
                                'Reserved Spot' => '📍',
                                'Car Wash' => '🚿',
                                'Oil Change' => '🛢️',
                                'Maintenance' => '🔧'
                            ];
                            echo $icons[$s['service_name']] ?? '📋';
                            ?>
                        </span>
                        <div>
                            <strong style="color:var(--dark-blue);"><?= htmlspecialchars($s['service_name']) ?></strong>
                            <p style="font-size:0.9rem;color:var(--gray);margin-top:3px;">
                                <?= htmlspecialchars($s['description'] ?? 'No description available') ?>
                            </p>
                            <p style="font-size:1rem;color:var(--primary-blue);font-weight:600;margin-top:5px;">
                                💰 EGP <?= number_format($s['price'] ?? 0, 2) ?>
                            </p>
                        </div>
                    </li>
                <?php endforeach; ?>
            </ul>
        </div>
        
        <!-- Parking Info -->
        <div class="card card-gold animate-fadeInUp delay-2">
            <h3>🅿️ Available Spots</h3>
            <div class="stats-grid" style="margin-top:15px;">
                <?php
                $garages = [];
                foreach ($spots as $sp) {
                    if (!isset($garages[$sp['garage_name']])) {
                        $garages[$sp['garage_name']] = 0;
                    }
                    $garages[$sp['garage_name']]++;
                }
                foreach ($garages as $name => $count):
                ?>
                <div class="stat-item">
                    <div class="number"><?= $count ?></div>
                    <div class="label"><?= htmlspecialchars($name) ?></div>
                </div>
                <?php endforeach; ?>
            </div>
        </div>
    </div>
</div>

<script>
function updateServicePrice() {
    const select = document.getElementById('service_id');
    const priceDisplay = document.getElementById('service_price_display');
    const priceSpan = document.getElementById('selected_price');
    
    if (select.value) {
        const selectedOption = select.options[select.selectedIndex];
        const price = selectedOption.getAttribute('data-price');
        priceSpan.textContent = price;
        priceDisplay.style.display = 'block';
    } else {
        priceDisplay.style.display = 'none';
    }
}
</script>

<?php include 'includes/footer.php'; ?>