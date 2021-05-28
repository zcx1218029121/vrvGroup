package registry;

import com.serotonin.modbus4j.exception.ModbusTransportException;
import group.SimpleDeviceGroup;
import modbus.ModbusMasterHolder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ModelRegistry {

    private Map<Integer, SimpleDeviceGroup> simpleDeviceGroupMap = new ConcurrentHashMap<>();


    public void scan() {
        int low = 0;
        int height = 247;
        int mind = 0;

        while (low <= height) {
            mind = (low + height) / 2;
            try {
                ModbusMasterHolder.getInstance.readHoldingRegistersRequest(mind, (32 + 1) * 6, 1).getShortData();
                low = mind + 1;
            } catch (ModbusTransportException e) {
                e.printStackTrace();
                height = mind - 1;
            }
        }

        for (int i = 1; i <= mind; i++) {
            simpleDeviceGroupMap.put(i, new SimpleDeviceGroup(i, 1));
        }
    }

    public Map<Integer, SimpleDeviceGroup> getSimpleDeviceGroupMap() {
        return simpleDeviceGroupMap;
    }
}