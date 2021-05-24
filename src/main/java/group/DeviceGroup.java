package group;

import entiry.Device;
import entiry.DeviceGetter;
import entiry.DeviceSetter;

import java.util.List;

public interface DeviceGroup extends DeviceSetter{

  DeviceSetter getDeviceSetter(int id);
  DeviceGetter getDeviceGetter(int id);

  /**
   * 批量读
   * @return 设备vo
   */
  List<Device> getDeviceVo();
}
