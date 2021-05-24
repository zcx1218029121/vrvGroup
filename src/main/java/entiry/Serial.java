package entiry;

/**
 * Package:        com.loafer.demo.pojo
 *
 * @author loafer
 * Description:
 * Date:    2021/4/2 9:13
 * Version:    1.0
 */
public class Serial {
    private String name;

    private int baudRate;
    private int dataBits;
    private int stopBits;
    private int parity;

    public int getStopBits() {
        return stopBits;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    public int getParity() {
        return parity;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public int getDataBits() {
        return dataBits;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Serial{" +
                "name='" + name + '\'' +
                ", baudRate=" + baudRate +
                ", dataBits=" + dataBits +
                ", stopBits=" + stopBits +
                ", parity=" + parity +
                '}';
    }
}
