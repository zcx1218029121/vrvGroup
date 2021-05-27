package entiry;

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
    boolean setRunState(Short state) ;

    /**
     * 设置温度设定值
     *
     * @return
     */
    boolean setTempSetting(Short temp) ;

    /**
     * 模式设定
     *
     * @return 模式
     */
    boolean setModeSetting(Short mode) ;

    boolean setWindDirectionSetting(Short wds) ;

}
