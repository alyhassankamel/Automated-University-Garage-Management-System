package com.augms.dao;

import com.augms.entity.ExitGate;
import com.augms.entity.GateStatus;

public interface ExitGateDAO {
    ExitGate findGate(String gateID);
    boolean openGate(String gateID);
    boolean closeGate(String gateID);
    GateStatus getGateStatus(String gateID);
}

