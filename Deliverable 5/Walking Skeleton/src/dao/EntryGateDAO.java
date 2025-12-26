package com.augms.dao;

import com.augms.entity.EntryGate;
import com.augms.entity.GateStatus;

public interface EntryGateDAO {
    EntryGate findGate(String gateID);
    boolean openGate(String gateID);
    boolean closeGate(String gateID);
    GateStatus getGateStatus(String gateID);
}

