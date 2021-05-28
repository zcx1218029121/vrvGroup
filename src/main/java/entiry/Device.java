package entiry;


/**
 * 所有空调的抽象
 * 读写分离
 */
public class Device {
    private int id;
    /**
     * 空调关机
     */
    public static short STATE_OFF = 0B01;

    /**
     * 空调开机
     */
    public static short STATE_ON = 0B10;

    public static short MOD_COOL = 0B1;
    public static short MOD_DEHUM = 0B10;
    public static short MOD_AIR_SUPPLY = 0B100;
    public static short MOD_HOT = 0B1000;


    private Integer runState;

    private int tempSetting;
    private int slaveId;

    public void setSlaveId(int slaveId) {
        this.slaveId = slaveId;
    }

    private int modeSetting;

    private int windDirectionSetting;
    private int err;

    private int roomTemp;

    public int getRoomTemp() {
        return roomTemp;
    }

    public void setRoomTemp(int roomTemp) {
        this.roomTemp = roomTemp;
    }

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public Integer getRunState() {
        return runState;
    }

    public void setRunState(Integer runState) {
        this.runState = runState;
    }

    public int getSlaveId() {
        return slaveId;
    }

    public static void setModAirSupply(short modAirSupply) {
        MOD_AIR_SUPPLY = modAirSupply;
    }

    public int getTempSetting() {
        return tempSetting;
    }

    public void setTempSetting(int tempSetting) {
        this.tempSetting = tempSetting;
    }

    public int getModeSetting() {
        return modeSetting;
    }

    public void setModeSetting(int modeSetting) {
        this.modeSetting = modeSetting;
    }

    public int getWindDirectionSetting() {
        return windDirectionSetting;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", runState=" + runState +
                ", tempSetting=" + tempSetting +
                ", slaveId=" + slaveId +
                ", modeSetting=" + modeSetting +
                ", windDirectionSetting=" + windDirectionSetting +
                ", err=" + err +
                ", roomTemp=" + roomTemp +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWindDirectionSetting(int windDirectionSetting) {
        this.windDirectionSetting = windDirectionSetting;
    }
}
