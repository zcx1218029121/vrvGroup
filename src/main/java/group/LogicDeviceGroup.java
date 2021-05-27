package group;

import entiry.Device;
import entiry.DeviceGetter;
import entiry.DeviceSetter;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

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


    @Override
    public List<Device> bachReadDevices(int type) {
        return null;
    }
}
