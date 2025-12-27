-- Initial data for the Parking Management System

INSERT INTO Users (uni_ID, name, phone, user_type, access_status) VALUES
('U001', 'Alice Smith', '1234567890', 'Student', 1),
('U002', 'Bob Johnson', '0987654321', 'Staff', 1),
('U003', 'Charlie Brown', '1122334455', 'Admin', 1);

INSERT INTO Parking_garage (name, garage_access) VALUES
('Main Garage', 1),
('East Garage', 1);

INSERT INTO Gates (garage_id, name) VALUES
(1, 'Gate 1'),
(1, 'Gate 2'),
(2, 'Gate 3');

INSERT INTO Parking_spots (spot_status, garage_id) VALUES
('Available', 1),
('Occupied', 1),
('Reserved', 2),
('Maintenance', 2);

INSERT INTO Vehicle (license_plate, color, Model, User_ID, Access_status, Approved_by) VALUES
('ABC123', 'Red', 'Toyota', 1, 1, NULL),
('XYZ789', 'Blue', 'Honda', 2, 1, 3);

INSERT INTO Service_type (service_name, description) VALUES
('Parking Permit', 'Monthly parking permit'),
('Valet', 'Valet parking service');

INSERT INTO Service_request (User_ID, vehicle_ID, service_ID, Spot_ID, status, Approved_by) VALUES
(1, 1, 1, NULL, 'Pending', NULL),
(2, 2, 2, 3, 'Approved', 3);

INSERT INTO Invoice (request_ID, amount) VALUES
(1, 100.00),
(2, 50.00);

INSERT INTO Payment_method (method_type, payment_info) VALUES
('Credit Card', '**** **** **** 1234'),
('Cash', NULL);

INSERT INTO Payment (invoice_ID, amount, status, method_ID) VALUES
(1, 100.00, 'Completed', 1),
(2, 50.00, 'Pending', 2);

INSERT INTO Reports (report_type, user_id, details) VALUES
('Occupancy Report', 3, 'Report on parking occupancy for the month.'),
('Revenue', 3, 'Monthly revenue report.');

INSERT INTO Log_record (spot_ID, gate_ID, request_ID, vehicle_ID, user_ID, log_type, details) VALUES
(1, 1, 1, 1, 1, 'Entry', 'Vehicle entered the garage.'),
(2, 2, 2, 2, 2, 'Exit', 'Vehicle exited the garage.');