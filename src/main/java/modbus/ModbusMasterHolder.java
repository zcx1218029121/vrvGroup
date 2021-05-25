package modbus;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.WriteRegisterRequest;
import com.serotonin.modbus4j.msg.WriteRegisterResponse;
import entiry.Serial;

public enum ModbusMasterHolder {
    getInstance();

    ModbusMasterHolder() {
        Serial serial = new Serial();
        modbusMaster = new ModbusFactory().createRtuMaster(new SerialPortParam());
        try {
            modbusMaster.init();
            modbusMaster.setTimeout(5000);
        } catch (ModbusInitException e) {
            e.printStackTrace();
        }
    }

    ModbusMaster modbusMaster;

    public ModbusMaster getModbusMaster() {
        return modbusMaster;
    }

    public ReadHoldingRegistersResponse readHoldingRegistersRequest(int slaveId, int startOffset, int numberOfRegisters) throws ModbusTransportException {
        ReadHoldingRegistersRequest readHoldingRegistersRequest = new ReadHoldingRegistersRequest(slaveId, startOffset, numberOfRegisters);
        return (ReadHoldingRegistersResponse) modbusMaster.send(readHoldingRegistersRequest);
    }

    public WriteRegisterResponse writeRegisterRequest(int slaveId, int startOffset, int writeValue) throws ModbusTransportException {
        WriteRegisterRequest writeRegisterRequest = new WriteRegisterRequest(slaveId, startOffset, writeValue);
        return (WriteRegisterResponse) modbusMaster.send(writeRegisterRequest);
    }
}
