package entiry;


/**
 * 所有空调的抽象
 * 读写分离
 */
public class Device {
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


    private Boolean runState;

    private int tempSetting;

    private int modeSetting;

    private int windDirectionSetting;
    private int err;

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public Boolean getRunState() {
        return runState;
    }

    public void setRunState(Boolean runState) {
        this.runState = runState;
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

    public void setWindDirectionSetting(int windDirectionSetting) {
        this.windDirectionSetting = windDirectionSetting;
    }
}
