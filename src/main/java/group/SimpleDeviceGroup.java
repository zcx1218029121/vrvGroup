package group;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.WriteRegisterResponse;
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
    private final ArrayList<VrvDevice> devices = new ArrayList<>();

    public SimpleDeviceGroup addDevice() {
        devices.add(new VrvDevice(salveID, devices.size()+1, id));
        return this;
    }

    public Integer getId() {
        return id;
    }

    public ArrayList<VrvDevice> getDevices() {
        return devices;
    }

    public SimpleDeviceGroup(int salveID, Integer id) {
        this.salveID = salveID;
        this.id = id;
    }

    public SimpleDeviceGroup(Integer id, Integer size, int salveID) {
        this.id = id;
        this.salveID = salveID;
        for (int i = 1; i <= size; i++) {
            this.addDevice();
        }
    }

    @Override
    public WriteRegisterResponse setRunState(Short state) {
        devices.forEach(setters -> {
            try {
                setters.setRunState(state);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return null;
    }

    @Override
    public WriteRegisterResponse setTempSetting(Short temp) {
        devices.forEach(setters -> {
            try {
                setters.setTempSetting(temp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return null;
    }

    @Override
    public WriteRegisterResponse setModeSetting(Short mode) {

        devices.forEach(setters -> {
            try {
                setters.setModeSetting(mode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return null;
    }

    @Override
    public WriteRegisterResponse setWindDirectionSetting(Short wds) {

        devices.forEach(setters -> {
            try {
                setters.setWindDirectionSetting(wds);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return null;
    }

    @Override
    public DeviceSetter getDeviceSetter(int id) {
        return devices.get(id);
    }

    @Override
    public DeviceGetter getDeviceGetter(int id) {
        return devices.get(id);
    }

    @Override
    public List<Device> getDeviceVo() {
        ArrayList<Device> d = new ArrayList<>();
        devices.forEach(vrvDevice -> d.add(vrvDevice.batchWapper()));
        return d;
    }


    public List<Device> getDeviceVo2() throws ModbusTransportException, ErrorResponseException {
        ArrayList<Device> d = new ArrayList<>();
        BatchRead<Integer> batchRead = new BatchRead<>();
        devices.forEach(vrvDevice -> {
            batchRead.addLocator(vrvDevice.getId() * 6, BaseLocator.holdingRegister(1, vrvDevice.getStartReadAddress(), DataType.EIGHT_BYTE_INT_SIGNED));
            batchRead.addLocator(vrvDevice.getId() * 6 + 1, BaseLocator.holdingRegister(1, vrvDevice.getStartReadAddress() + 1, DataType.EIGHT_BYTE_INT_SIGNED));
            batchRead.addLocator(vrvDevice.getId() * 6 + 2, BaseLocator.holdingRegister(1, vrvDevice.getStartReadAddress() + 2, DataType.EIGHT_BYTE_INT_SIGNED));
            batchRead.addLocator(vrvDevice.getId() * 6 + 3, BaseLocator.holdingRegister(1, vrvDevice.getStartReadAddress() + 3, DataType.EIGHT_BYTE_INT_SIGNED));
            batchRead.addLocator(vrvDevice.getId() * 6 + 4, BaseLocator.holdingRegister(1, vrvDevice.getStartReadAddress() + 4, DataType.EIGHT_BYTE_INT_SIGNED));
            batchRead.addLocator(vrvDevice.getId() * 6 + 5, BaseLocator.holdingRegister(1, vrvDevice.getStartReadAddress() + 5, DataType.EIGHT_BYTE_INT_SIGNED));
        });

        BatchResults<Integer> results = ModbusMasterHolder.getInstance.getModbusMaster().send(batchRead);
        devices.forEach(vrvDevice -> {
            Device device = new Device();
            device.setRunState(results.getIntValue(vrvDevice.getId() * 6) == Device.STATE_ON);
            device.setTempSetting(results.getIntValue(vrvDevice.getId() * 6 + 1));
            device.setModeSetting(results.getIntValue(vrvDevice.getId() * 6 + 2));
            device.setWindDirectionSetting(results.getIntValue(vrvDevice.getId() * 6 + 3));
            device.setErr(results.getIntValue(vrvDevice.getId() * 6 + 5));

        });

        return d;
    }


}
