package group;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.locator.BaseLocator;
import entiry.Device;
import entiry.DeviceGetter;
import entiry.DeviceSetter;
import entiry.VrvDevice;
import modbus.ModbusMasterHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public class LogicDeviceGroup implements DeviceGroup {
    private String name;
    private ConcurrentHashMap<String, DeviceGroup> deviceGroupConcurrentHashMap = new ConcurrentHashMap<>();


    public void addGroup(String key, DeviceGroup deviceGroup) {
        deviceGroupConcurrentHashMap.put(key, deviceGroup);
    }

    public void setRunState(String id, Short state) {
        deviceGroupConcurrentHashMap.get(id).setRunState(state);
    }


    public void setTempSetting(String id, Short temp) {
        deviceGroupConcurrentHashMap.get(id).setTempSetting(temp);
    }


    public void setModeSetting(String id, Short mode) {
        deviceGroupConcurrentHashMap.get(id).setModeSetting(mode);
    }


    public void setWindDirectionSetting(String id, Short wds) {
        deviceGroupConcurrentHashMap.get(id).setWindDirectionSetting(wds);
    }


    public DeviceGroup getDeviceGroup(String id) {
        return deviceGroupConcurrentHashMap.get(id);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean setRunState(Short state) {
        deviceGroupConcurrentHashMap.forEach((s, deviceGroup) -> deviceGroup.setRunState(state));
        return true;
    }

    @Override
    public boolean setTempSetting(Short temp) {
        AtomicBoolean flag = new AtomicBoolean(true);
        deviceGroupConcurrentHashMap.forEach((s, deviceGroup) -> {
            if (!deviceGroup.setTempSetting(temp)) {
                flag.set(false);
            }
        });
        return true;
    }

    @Override
    public boolean setModeSetting(Short mode) {
        deviceGroupConcurrentHashMap.forEach((s, deviceGroup) -> deviceGroup.setModeSetting(mode));
        return true;
    }

    @Override
    public boolean setWindDirectionSetting(Short wds) {
        deviceGroupConcurrentHashMap.forEach((s, deviceGroup) -> {
            deviceGroup.setWindDirectionSetting(wds);
        });
        return true;
    }

    @Override
    public DeviceSetter getDeviceSetter(int id) {
        return null;
    }

    @Override
    public DeviceGetter getDeviceGetter(int id) {
        return null;
    }

    /**
     * 批量读取空调信息
     *
     * @param type 读取方式
     *             0 一口气全读 70台空调1.5秒
     *             1 一台空调一台空调 读70台空调大概8秒
     *             2 一个属性一个属性的读 读70台空调大概20秒
     * @return 批量读
     */
    @Override
    public List<Device> bachReadDevices(int type) {
        BatchRead<Integer> batchRead = new BatchRead<>();
        ArrayList<Device> d = new ArrayList<>();
        deviceGroupConcurrentHashMap.forEach((s, deviceGroup) -> deviceGroup.getDevices().forEach(vrvDevice -> {
            batchRead.addLocator(vrvDevice.getSlaveID() * 100000 + vrvDevice.getId() * 6, BaseLocator.holdingRegister(1, vrvDevice.getStartReadAddress(), DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            batchRead.addLocator(vrvDevice.getSlaveID() * 100000 + vrvDevice.getId() * 6 + 1, BaseLocator.holdingRegister(1, vrvDevice.getStartReadAddress() + 1, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            batchRead.addLocator(vrvDevice.getSlaveID() * 100000 + vrvDevice.getId() * 6 + 2, BaseLocator.holdingRegister(1, vrvDevice.getStartReadAddress() + 2, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            batchRead.addLocator(vrvDevice.getSlaveID() * 100000 + vrvDevice.getId() * 6 + 3, BaseLocator.holdingRegister(1, vrvDevice.getStartReadAddress() + 3, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            batchRead.addLocator(vrvDevice.getSlaveID() * 100000 + vrvDevice.getId() * 6 + 4, BaseLocator.holdingRegister(1, vrvDevice.getStartReadAddress() + 4, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            batchRead.addLocator(vrvDevice.getSlaveID() * 100000 + vrvDevice.getId() * 6 + 5, BaseLocator.holdingRegister(1, vrvDevice.getStartReadAddress() + 5, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
        }));
        try {
            BatchResults<Integer> results = ModbusMasterHolder.getInstance.getModbusMaster().send(batchRead);

            deviceGroupConcurrentHashMap.forEach((s, deviceGroup) -> deviceGroup.getDevices().forEach(vrvDevice -> {
                Device device = new Device();
                device.setRunState(results.getIntValue(vrvDevice.getSlaveID() * 100000 + vrvDevice.getId() * 6));
                device.setTempSetting(results.getIntValue(vrvDevice.getSlaveID() * 100000 + vrvDevice.getId() * 6 + 1));
                device.setModeSetting(results.getIntValue(vrvDevice.getSlaveID() * 100000 + vrvDevice.getId() * 6 + 2));
                device.setWindDirectionSetting(results.getIntValue(vrvDevice.getSlaveID() * 100000 + vrvDevice.getId() * 6 + 3));
                device.setId(vrvDevice.getId());
                device.setRoomTemp(results.getIntValue(vrvDevice.getSlaveID() * 100000 + vrvDevice.getId() * 6 + 4));
                device.setErr(results.getIntValue(vrvDevice.getSlaveID() * 100000 + vrvDevice.getId() * 6 + 5));
                d.add(device);
            }));
        } catch (ModbusTransportException | ErrorResponseException e) {
            e.printStackTrace();
        }

        return d;
    }

    @Override
    public List<VrvDevice> getDevices() {
        return null;
    }

}
