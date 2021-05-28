package group;
import entiry.Device;
import entiry.DeviceGetter;
import entiry.DeviceSetter;
import entiry.VrvDevice;

import java.util.List;

public interface DeviceGroup extends DeviceSetter {

    DeviceSetter getDeviceSetter(int id);

    DeviceGetter getDeviceGetter(int id);


    /**
     * 批量读全部设备
     *
     * @return 设备vo
     */
    List<Device> bachReadDevices(int type);

    List<VrvDevice> getDevices();
}
