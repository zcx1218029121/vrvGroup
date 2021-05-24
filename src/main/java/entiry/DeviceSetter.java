package entiry;

import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.WriteRegisterResponse;


/**
 * 读写分离
 *
 */
public interface DeviceSetter {

    /**
     * 获取运行状态
     *
     * @return 是否开启
     */
    WriteRegisterResponse setRunState(Short state) throws ModbusTransportException;

    /**
     * 设置温度设定值
     *
     * @return
     */
    WriteRegisterResponse setTempSetting(Short temp) throws ModbusTransportException;

    /**
     * 模式设定
     *
     * @return 模式
     */
    WriteRegisterResponse setModeSetting(Short mode) throws ModbusTransportException;

    WriteRegisterResponse setWindDirectionSetting(Short wds) throws ModbusTransportException;

}
