package group;

import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.WriteRegisterResponse;
import entiry.Device;
import entiry.DeviceGetter;
import entiry.DeviceSetter;
import entiry.VrvDevice;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class LogicDeviceGroup implements DeviceGroup {
    private String name;
    private ConcurrentHashMap<String, DeviceGroup> deviceGroupConcurrentHashMap = new ConcurrentHashMap<>();


    public void addGroup(String key, DeviceGroup deviceGroup) {
        deviceGroupConcurrentHashMap.put(key, deviceGroup);
    }

    public void setRunState(String id, Short state) throws ModbusTransportException {
        deviceGroupConcurrentHashMap.get(id).setRunState(state);
    }


    public void setTempSetting(String id, Short temp) throws ModbusTransportException {
        deviceGroupConcurrentHashMap.get(id).setTempSetting(temp);
    }


    public void setModeSetting(String id, Short mode) throws ModbusTransportException {
        deviceGroupConcurrentHashMap.get(id).setModeSetting(mode);
    }


    public void setWindDirectionSetting(String id, Short wds) throws ModbusTransportException {
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

    public static void main(String[] args) {
        LogicDeviceGroup logicDeviceGroup = new LogicDeviceGroup();
        LogicDeviceGroup lnb = new LogicDeviceGroup();
        logicDeviceGroup.setName("欧诗漫VRV空调集群");
        // 集群1 30台空调
        logicDeviceGroup.addGroup("集群1", new SimpleDeviceGroup(2, 30));

        lnb.setName("萧山测试");
        lnb.addGroup(logicDeviceGroup.getName(), logicDeviceGroup);
        lnb.setTempSetting((short) 26);

    }

    @Override
    public WriteRegisterResponse setRunState(Short state) {
        deviceGroupConcurrentHashMap.forEach((s, deviceGroup) -> {
            try {
                deviceGroup.setRunState(state);
            } catch (ModbusTransportException e) {
                e.printStackTrace();
            }
        });
        return null;
    }

    @Override
    public WriteRegisterResponse setTempSetting(Short temp) {
        deviceGroupConcurrentHashMap.forEach((s, deviceGroup) -> {
            try {
                deviceGroup.setTempSetting(temp);
            } catch (ModbusTransportException e) {
                e.printStackTrace();
            }
        });
        return null;
    }

    @Override
    public WriteRegisterResponse setModeSetting(Short mode) {
        deviceGroupConcurrentHashMap.forEach((s, deviceGroup) -> {
            try {
                deviceGroup.setModeSetting(mode);
            } catch (ModbusTransportException e) {
                e.printStackTrace();
            }
        });
        return null;
    }

    @Override
    public WriteRegisterResponse setWindDirectionSetting(Short wds) {
        deviceGroupConcurrentHashMap.forEach((s, deviceGroup) -> {
            try {
                deviceGroup.setWindDirectionSetting(wds);
            } catch (ModbusTransportException e) {
                e.printStackTrace();
            }
        });
        return null;
    }

    @Override
    public DeviceSetter getDeviceSetter(int id) {
        return null;
    }

    @Override
    public DeviceGetter getDeviceGetter(int id) {
        return null;
    }

    @Override
    public List<Device> getDeviceVo() {
        return null;
    }

    @Override
    public List<Device> getDeviceVo2() throws ModbusTransportException, ErrorResponseException {
        return null;
    }
}
