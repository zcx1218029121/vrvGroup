package entiry;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.WriteRegisterResponse;
import modbus.ModbusMasterHolder;

/**
 * vrv 空调实现
 * 空调内机
 */
public class VrvDevice implements DeviceSetter, DeviceGetter {
    private final Integer slaveID;
    /**
     * 空调内机id
     */
    private final Integer id;
    /**
     * 空调机组id
     */
    private final Integer groupId;

    /**
     * 空调开始读地址
     */
    private final Integer startReadAddress;
    /**
     * 空调设置地址
     */
    private final Integer startSettingAddress;


    public Integer getStartReadAddress() {
        return startReadAddress;
    }

    public Integer getStartSettingAddress() {
        return startSettingAddress;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public Integer getId() {
        return id;
    }

    public VrvDevice(Integer slaveID, Integer id, Integer groupId) {
        this.slaveID = slaveID;
        this.id = id;
        this.groupId = groupId;
        startReadAddress = (32 * groupId + id) * 6 ;
        startSettingAddress = (32 * groupId + id) * 4 + 4000 ;
    }

    @Override
    public Boolean getRunState() {
        try {
            return ModbusMasterHolder.getInstance.readHoldingRegistersRequest(slaveID, startReadAddress,DataType.ONE_BYTE_INT_UNSIGNED_LOWER).getShortData()[0] == Device.STATE_ON;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Integer getTempSetting() {
        try {
            return (int) ModbusMasterHolder.getInstance.readHoldingRegistersRequest(slaveID, startReadAddress + 1, DataType.ONE_BYTE_INT_UNSIGNED_LOWER).getShortData()[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    @Override
    public Integer getModeSetting() {
        try {
            return (int) ModbusMasterHolder.getInstance.readHoldingRegistersRequest(slaveID, startReadAddress + 2, DataType.EIGHT_BYTE_INT_SIGNED).getShortData()[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }

    @Override
    public Integer getWindDirectionSetting() {
        try {
            return (int) ModbusMasterHolder.getInstance.readHoldingRegistersRequest(slaveID, startReadAddress + 3, DataType.EIGHT_BYTE_INT_SIGNED).getShortData()[0];
        } catch (Exception e) {
            e.printStackTrace();
        }


        return 0;
    }

    @Override
    public Integer getRoomReturnAirTempe() throws ModbusTransportException {
        try {
            return (int) ModbusMasterHolder.getInstance.readHoldingRegistersRequest(slaveID, startReadAddress + 4, DataType.EIGHT_BYTE_INT_SIGNED).getShortData()[0];

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Integer getErr() {
        try {
            return (int) ModbusMasterHolder.getInstance.readHoldingRegistersRequest(slaveID, startReadAddress + 5, DataType.EIGHT_BYTE_INT_SIGNED).getShortData()[0];
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Device wapper() {
        Device device = new Device();
        device.setModeSetting(getModeSetting());
        device.setRunState(getRunState());
        device.setWindDirectionSetting(getWindDirectionSetting());
        device.setTempSetting(getTempSetting());
        device.setErr(getErr());
        return device;
    }

    public Device batchWapper() {

        BatchRead<Integer> batchRead = new BatchRead<>();
        batchRead.addLocator(0, BaseLocator.holdingRegister(2, startReadAddress, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
        batchRead.addLocator(1, BaseLocator.holdingRegister(2, startReadAddress + 1, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
        batchRead.addLocator(2, BaseLocator.holdingRegister(2, startReadAddress + 2, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
        batchRead.addLocator(3, BaseLocator.holdingRegister(2, startReadAddress + 3, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
        batchRead.addLocator(4, BaseLocator.holdingRegister(2, startReadAddress + 4, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
        batchRead.addLocator(5, BaseLocator.holdingRegister(2, startReadAddress + 4, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
        Device device = new Device();
        try {
            BatchResults<Integer> results = ModbusMasterHolder.getInstance.getModbusMaster().send(batchRead);
            device.setRunState(results.getIntValue(0) == Device.STATE_ON);
            device.setTempSetting(results.getIntValue(1));
            device.setModeSetting(results.getIntValue(2));
            device.setWindDirectionSetting(results.getIntValue(3));
            device.setErr(results.getIntValue(5));

        } catch (ModbusTransportException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        }
        return device;
    }

    @Override
    public WriteRegisterResponse setRunState(Short state) throws ModbusTransportException {
        return ModbusMasterHolder.getInstance.writeRegisterRequest(slaveID, startSettingAddress, state);
    }

    @Override
    public WriteRegisterResponse setTempSetting(Short temp) throws ModbusTransportException {
         return ModbusMasterHolder.getInstance.writeRegisterRequest(slaveID, startSettingAddress + 1, temp);
    }

    @Override
    public WriteRegisterResponse setModeSetting(Short mode) throws ModbusTransportException {
        return ModbusMasterHolder.getInstance.writeRegisterRequest(slaveID, startSettingAddress + 2, mode);
    }

    @Override
    public WriteRegisterResponse setWindDirectionSetting(Short wds) throws ModbusTransportException {
        return ModbusMasterHolder.getInstance.writeRegisterRequest(slaveID, startSettingAddress + 3, wds);
    }


}
