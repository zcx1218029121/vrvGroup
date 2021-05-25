package modbus;

import com.fazecast.jSerialComm.SerialPort;
import com.serotonin.modbus4j.serial.SerialPortWrapper;
import entiry.Serial;

import java.io.InputStream;
import java.io.OutputStream;

public class SerialPortParam implements SerialPortWrapper {
    private int baudRate = 19200;
    private int dataBits = 8;
    private int stopBits = 1;
    private int parity = 0;
    private final SerialPort serialPort;


    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }


    public SerialPortParam() {
        serialPort = SerialPort.getCommPort("/dev/ttyUSB2");
        serialPort.setParity(SerialPort.EVEN_PARITY);
        System.err.println(serialPort);
        serialPort.openPort();
    }

    @Override
    public void close() throws Exception {
        if (serialPort != null) {
            serialPort.closePort();
        }
    }

    @Override
    public void open() throws Exception {
        serialPort.openPort();
    }

    @Override
    public InputStream getInputStream() {
        return serialPort.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() {
        return serialPort.getOutputStream();
    }

    @Override
    public int getBaudRate() {
        return baudRate;
    }

    @Override
    public int getDataBits() {
        return dataBits;
    }

    @Override
    public int getStopBits() {
        return stopBits;
    }

    @Override
    public int getParity() {
        return parity;
    }
}
