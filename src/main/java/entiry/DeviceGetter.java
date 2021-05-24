package entiry;

import com.serotonin.modbus4j.exception.ModbusTransportException;

public interface DeviceGetter {

    /**
     * 获取运行状态
     *
     * @return 是否开启
     */
    Boolean getRunState() throws ModbusTransportException;

    /**
     * 获取温度设定值
     *
     * @return 温度
     */
    Integer getTempSetting() throws ModbusTransportException;

    /**
     * 模式设定
     *
     * @return 模式
     */
    Integer getModeSetting() throws ModbusTransportException;

    Integer getWindDirectionSetting() throws ModbusTransportException;

    Integer getRoomReturnAirTempe() throws ModbusTransportException;

    Integer getErr() throws ModbusTransportException;

    Device wapper();
}
