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


/**
 * vrv空调集群
 * 群控方案
 * 一.对设备进行批量写操作
 * 二.对设备进行批量操作
 * 三.对设备进行单个控制操作
 * 4.支持组嵌套  逻辑集群 (很多层)----》--------集群 --》设备
 */
public class SimpleDeviceGroup implements DeviceGroup {
    private final int salveID;
    private final Integer id;
    private final List<VrvDevice> devices = new ArrayList<>();

    public SimpleDeviceGroup addDevice() {
        devices.add(new VrvDevice(salveID, devices.size(), id));
        return this;
    }

    public Integer getId() {
        return id;
    }

    public List<VrvDevice> getDevices() {
        return devices;
    }

    public SimpleDeviceGroup(int salveID, Integer id) {
        this.salveID = salveID;
        this.id = id;
        int low = 0;
        int height = 31;
        int mind = (low + height) / 2;
        while (low <= height) {
            mind = (low + height) / 2;
            try {
                short s = ModbusMasterHolder.getInstance.readHoldingRegistersRequest(salveID, (32 + mind) * 6 + 2, 1).getShortData()[0];
                if (s == 0) {
                    height = mind - 1;
                } else {
                    low = mind + 1;
                }
            } catch (ModbusTransportException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i <= mind; i++) {
            this.addDevice();
        }

    }

    public SimpleDeviceGroup(Integer id, Integer size, int salveID) {
        this.id = id;
        this.salveID = salveID;
        for (int i = 1; i <= size; i++) {
            this.addDevice();
        }
    }

    @Override
    public boolean setRunState(Short state) {
        for (VrvDevice setters : devices) {
            setters.setRunState(state);
        }
        return true;
    }

    @Override
    public boolean setTempSetting(Short temp) {
        devices.forEach(setters -> setters.setTempSetting(temp));
        return true;
    }

    @Override
    public boolean setModeSetting(Short mode) {
        devices.forEach(setter -> setter.setModeSetting(mode));
        return true;
    }

    @Override
    public boolean setWindDirectionSetting(Short wds) {
        devices.forEach(setters -> setters.setWindDirectionSetting(wds));
        return true;
    }

    @Override
    public DeviceSetter getDeviceSetter(int id) {
        return devices.get(id);
    }

    @Override
    public DeviceGetter getDeviceGetter(int id) {
        return devices.get(id);
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
        switch (type) {
            case 0:
                return bachReadDevicesOnesAll();
            case 1:
                return bachReadDevicesOneByOne();
            default:
                return bachReadDevicesAtom();
        }

    }


    private List<Device> bachReadDevicesOneByOne() {
        ArrayList<Device> d = new ArrayList<>();
        devices.forEach(vrvDevice -> d.add(vrvDevice.batchWapper()));
        return d;
    }

    private List<Device> bachReadDevicesOnesAll() {
        ArrayList<Device> d = new ArrayList<>();
        BatchRead<Integer> batchRead = new BatchRead<>();
        devices.forEach(vrvDevice -> {
            batchRead.addLocator(vrvDevice.getId() * 6, BaseLocator.holdingRegister(salveID, vrvDevice.getStartReadAddress(), DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            batchRead.addLocator(vrvDevice.getId() * 6 + 1, BaseLocator.holdingRegister(salveID, vrvDevice.getStartReadAddress() + 1, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            batchRead.addLocator(vrvDevice.getId() * 6 + 2, BaseLocator.holdingRegister(salveID, vrvDevice.getStartReadAddress() + 2, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            batchRead.addLocator(vrvDevice.getId() * 6 + 3, BaseLocator.holdingRegister(salveID, vrvDevice.getStartReadAddress() + 3, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            batchRead.addLocator(vrvDevice.getId() * 6 + 4, BaseLocator.holdingRegister(salveID, vrvDevice.getStartReadAddress() + 4, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            batchRead.addLocator(vrvDevice.getId() * 6 + 5, BaseLocator.holdingRegister(salveID, vrvDevice.getStartReadAddress() + 5, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
        });
        try {
            BatchResults<Integer> results = ModbusMasterHolder.getInstance.getModbusMaster().send(batchRead);
            devices.forEach(vrvDevice -> {
                Device device = new Device();
                device.setSlaveId(vrvDevice.getSlaveID());
                device.setRunState(results.getIntValue(vrvDevice.getId() * 6));
                device.setTempSetting(results.getIntValue(vrvDevice.getId() * 6 + 1));
                device.setModeSetting(results.getIntValue(vrvDevice.getId() * 6 + 2));
                device.setWindDirectionSetting(results.getIntValue(vrvDevice.getId() * 6 + 3));
                device.setId(vrvDevice.getId());
                device.setRoomTemp(results.getIntValue(vrvDevice.getId() * 6 + 4));
                device.setErr(results.getIntValue(vrvDevice.getId() * 6 + 5));

                if (device.getTempSetting() > 0) {
                    d.add(device);
                }

            });
        } catch (ModbusTransportException | ErrorResponseException e) {
            e.printStackTrace();
        }
        return d;
    }


    private List<Device> bachReadDevicesAtom() {
        ArrayList<Device> d = new ArrayList<>();
        devices.forEach(vrvDevice -> d.add(vrvDevice.wapper()));
        return d;
    }


}
