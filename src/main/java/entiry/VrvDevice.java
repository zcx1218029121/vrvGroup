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
        startReadAddress = (32 * groupId + id) * 6;
        startSettingAddress = (32 * groupId + id) * 4 + 4000;
    }

    @Override
    public Boolean getRunState() {
        return getIntValue(startReadAddress) == Device.STATE_ON;
    }

    public Integer getRunStateInt() {
        return getIntValue(startReadAddress);
    }

    @Override
    public Integer getTempSetting() {
        return getIntValue(startReadAddress + 1);
    }

    @Override
    public Integer getModeSetting() {
        return getIntValue(startReadAddress + 2);

    }

    @Override
    public Integer getWindDirectionSetting() {
        return getIntValue(startReadAddress + 3);

    }

    @Override
    public Integer getRoomReturnAirTempe() {
        return getIntValue(startReadAddress + 4);
    }

    @Override
    public Integer getErr() {
        return getIntValue(startReadAddress + 5);

    }

    @Override
    public Device wapper() {
        Device device = new Device();
        device.setId(id);
        device.setModeSetting(getModeSetting());
        device.setRunState(getRunStateInt());
        device.setWindDirectionSetting(getWindDirectionSetting());
        device.setTempSetting(getTempSetting());
        device.setErr(getErr());
        return device;
    }

    public Device batchWapper() {
        BatchRead<Integer> batchRead = new BatchRead<>();
        batchRead.addLocator(0, BaseLocator.holdingRegister(slaveID, startReadAddress, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
        batchRead.addLocator(1, BaseLocator.holdingRegister(slaveID, startReadAddress + 1, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
        batchRead.addLocator(2, BaseLocator.holdingRegister(slaveID, startReadAddress + 2, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
        batchRead.addLocator(3, BaseLocator.holdingRegister(slaveID, startReadAddress + 3, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
        batchRead.addLocator(4, BaseLocator.holdingRegister(slaveID, startReadAddress + 4, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
        batchRead.addLocator(5, BaseLocator.holdingRegister(slaveID, startReadAddress + 5, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
        Device device = new Device();
        try {
            BatchResults<Integer> results = ModbusMasterHolder.getInstance.getModbusMaster().send(batchRead);
            device.setId(id);
            device.setRunState(getRunStateInt());
            device.setTempSetting(results.getIntValue(1));
            device.setModeSetting(results.getIntValue(2));
            device.setWindDirectionSetting(results.getIntValue(3));
            device.setRoomTemp(results.getIntValue(4));
            device.setErr(results.getIntValue(5));

        } catch (ModbusTransportException | ErrorResponseException e) {
            e.printStackTrace();
        }
        return device;
    }

    @Override
    public boolean setRunState(Short state) {
        return setValue(startReadAddress, state);
    }

    @Override
    public boolean setTempSetting(Short temp) {
        return setValue(startSettingAddress + 1, temp);
    }

    @Override
    public boolean setModeSetting(Short mode) {
        return setValue(startSettingAddress + 2, mode);
    }

    @Override
    public boolean setWindDirectionSetting(Short wds) {
        return setValue(startSettingAddress + 3, wds);
    }

    /**
     * 写方法抽象 如果写超时 自动翻倍超时时间
     *
     * @param value
     * @return
     */
    private boolean setValue(int address, Short value) {
        try {
            return !ModbusMasterHolder.getInstance.writeRegisterRequest(slaveID, address, value).isException();
        } catch (ModbusTransportException e) {
            // 如果是超时异常就 翻倍超时时间
            ModbusMasterHolder.getInstance.autoCapacity();
        }
        return false;
    }

    private Integer getIntValue(int address) {
        try {
            return (int) ModbusMasterHolder.getInstance.readHoldingRegistersRequest(slaveID, address, 1).getShortData()[0];
        } catch (Exception e) {
            // 如果是超时异常就 翻倍超时时间
            ModbusMasterHolder.getInstance.autoCapacity();
            e.printStackTrace();
        }
        return 0;
    }


}
