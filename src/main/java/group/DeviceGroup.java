package group;

import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
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
  List<Device> getDeviceVo2() throws ModbusTransportException, ErrorResponseException;
}
