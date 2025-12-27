-- =============================================
-- Parking Management System Database Schema
-- Fully constrained for H2 Database
-- =============================================
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    uni_ID VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    user_type VARCHAR(20) CHECK (user_type IN ('Student', 'Staff', 'Admin', 'Visitor', 'Security')) NOT NULL,
    access_status BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE Parking_garage (
    garage_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    garage_access BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE Gates (
    gate_id INT AUTO_INCREMENT PRIMARY KEY,
    garage_id INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    UNIQUE (garage_id, name),
    FOREIGN KEY (garage_id)
        REFERENCES Parking_garage(garage_id)
        ON DELETE CASCADE
);

CREATE TABLE Parking_spots (
    spot_id INT AUTO_INCREMENT PRIMARY KEY,
    spot_status VARCHAR(20) CHECK (spot_status IN ('Available', 'Occupied', 'Reserved', 'Maintenance', 'Disabled')) NOT NULL DEFAULT 'Available',
    garage_id INT NOT NULL,
    FOREIGN KEY (garage_id)
        REFERENCES Parking_garage(garage_id)
        ON DELETE CASCADE
);

CREATE TABLE Sensors (
    sensor_id INT AUTO_INCREMENT PRIMARY KEY,
    sensor_type VARCHAR(20) CHECK (sensor_type IN ('Occupancy', 'Gate', 'Other')) NOT NULL,
    spot_id INT NULL,
    gate_id INT NULL,
    UNIQUE (spot_id),
    UNIQUE (gate_id),
    FOREIGN KEY (spot_id)
        REFERENCES Parking_spots(spot_id)
        ON DELETE CASCADE,
    FOREIGN KEY (gate_id)
        REFERENCES Gates(gate_id)
        ON DELETE CASCADE
);

CREATE TABLE Vehicle (
    vehicle_id INT AUTO_INCREMENT PRIMARY KEY,
    license_plate VARCHAR(20) NOT NULL UNIQUE,
    color VARCHAR(20) NOT NULL,
    model VARCHAR(50) NOT NULL,
    user_id INT NOT NULL,
    access_status BOOLEAN NOT NULL DEFAULT FALSE,
    approved_by INT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (approved_by) REFERENCES Users(user_id)
);

CREATE TABLE Service_type (
    service_id INT AUTO_INCREMENT PRIMARY KEY,
    service_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(500) NULL
);

CREATE TABLE Service_request (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    vehicle_id INT NULL,
    service_id INT NOT NULL,
    spot_id INT NULL,
    status VARCHAR(20) CHECK (status IN ('Pending', 'Approved', 'Rejected', 'In Progress', 'Completed', 'Cancelled')) NOT NULL DEFAULT 'Pending',
    approved_by INT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (vehicle_id) REFERENCES Vehicle(vehicle_id),
    FOREIGN KEY (service_id) REFERENCES Service_type(service_id),
    FOREIGN KEY (spot_id) REFERENCES Parking_spots(spot_id),
    FOREIGN KEY (approved_by) REFERENCES Users(user_id)
);

CREATE TABLE Invoice (
    invoice_id INT AUTO_INCREMENT PRIMARY KEY,
    request_id INT NOT NULL UNIQUE,
    amount DECIMAL(10,2) NOT NULL,
    date_issued DATE NOT NULL DEFAULT CURRENT_DATE,
    time_issued TIME NOT NULL DEFAULT CURRENT_TIME,
    FOREIGN KEY (request_id)
        REFERENCES Service_request(request_id)
        ON DELETE CASCADE
);

CREATE TABLE Payment_method (
    method_id INT AUTO_INCREMENT PRIMARY KEY,
    method_type VARCHAR(20) CHECK (method_type IN ('Cash', 'Credit Card', 'Debit Card', 'Mobile Pay', 'Campus Card')) NOT NULL,
    payment_info VARCHAR(200) NULL
);

CREATE TABLE Payment (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    date_paid DATE NOT NULL DEFAULT CURRENT_DATE,
    time_paid TIME NOT NULL DEFAULT CURRENT_TIME,
    status VARCHAR(20) CHECK (status IN ('Completed', 'Failed', 'Refunded', 'Pending')) NOT NULL DEFAULT 'Completed',
    method_id INT NOT NULL,
    FOREIGN KEY (invoice_id) REFERENCES Invoice(invoice_id),
    FOREIGN KEY (method_id) REFERENCES Payment_method(method_id)
);

CREATE TABLE Reports (
    report_id INT AUTO_INCREMENT PRIMARY KEY,
    report_type VARCHAR(50) NOT NULL,
    user_id INT NOT NULL,
    date_generated DATE NOT NULL DEFAULT CURRENT_DATE,
    time_generated TIME NOT NULL DEFAULT CURRENT_TIME,
    details TEXT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE TABLE Log_record (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    spot_id INT NULL,
    gate_id INT NULL,
    request_id INT NULL,
    report_id INT NULL,
    vehicle_id INT NULL,
    user_id INT NULL,
    sensor_id INT NULL,
    log_type VARCHAR(50) NOT NULL,
    details TEXT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (spot_id) REFERENCES Parking_spots(spot_id),
    FOREIGN KEY (gate_id) REFERENCES Gates(gate_id),
    FOREIGN KEY (request_id) REFERENCES Service_request(request_id),
    FOREIGN KEY (report_id) REFERENCES Reports(report_id),
    FOREIGN KEY (vehicle_id) REFERENCES Vehicle(vehicle_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (sensor_id) REFERENCES Sensors(sensor_id)
);

CREATE TABLE Requests (
    user_id INT NOT NULL,
    report_id INT NOT NULL,
    PRIMARY KEY (user_id, report_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (report_id) REFERENCES Reports(report_id) ON DELETE CASCADE
);

CREATE TABLE Controls (
    user_id INT NOT NULL,
    garage_id INT NOT NULL,
    PRIMARY KEY (user_id, garage_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (garage_id) REFERENCES Parking_garage(garage_id) ON DELETE CASCADE
);

CREATE TABLE Owns (
    vehicle_id INT NOT NULL,
    request_id INT NOT NULL,
    PRIMARY KEY (vehicle_id, request_id),
    FOREIGN KEY (vehicle_id) REFERENCES Vehicle(vehicle_id) ON DELETE CASCADE,
    FOREIGN KEY (request_id) REFERENCES Service_request(request_id) ON DELETE CASCADE
);

CREATE INDEX IX_Vehicle_User ON Vehicle(user_id);
CREATE INDEX IX_ServiceRequest_User ON Service_request(user_id);
CREATE INDEX IX_ServiceRequest_Status ON Service_request(status);
CREATE INDEX IX_Log_record_Timestamp ON Log_record(timestamp);
CREATE INDEX IX_Parking_spots_Garage ON Parking_spots(garage_id);