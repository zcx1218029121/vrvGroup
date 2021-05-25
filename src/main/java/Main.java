import group.LogicDeviceGroup;
import group.SimpleDeviceGroup;


public class Main {

    public static void main(String[] args) {
        LogicDeviceGroup logicDeviceGroup = new LogicDeviceGroup();
        SimpleDeviceGroup mode1 = new SimpleDeviceGroup(1, 16,1);
        SimpleDeviceGroup mode2 = new SimpleDeviceGroup(1, 6,2);
        SimpleDeviceGroup mode3 = new SimpleDeviceGroup(1, 13,3);
        SimpleDeviceGroup mode4 = new SimpleDeviceGroup(1, 14,4);
        SimpleDeviceGroup mode5 = new SimpleDeviceGroup(1, 14,5);

        logicDeviceGroup.addGroup("mode1",mode1);
        logicDeviceGroup.addGroup("mode2",mode2);
        logicDeviceGroup.addGroup("mode3",mode3);
        logicDeviceGroup.addGroup("mode4",mode4);
        logicDeviceGroup.addGroup("mode5",mode5);

        logicDeviceGroup.setName("欧诗漫空调集群");


        logicDeviceGroup.setTempSetting((short)26);
    }


}