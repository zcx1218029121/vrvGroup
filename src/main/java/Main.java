import registry.ModelRegistry;


public class Main {

    public static void main(String[] args) {
        ModelRegistry modelRegistry = new ModelRegistry();
        modelRegistry.scan();
        modelRegistry.getSimpleDeviceGroupMap().forEach((integer, simpleDeviceGroup) -> {
            System.out.println(integer);
            simpleDeviceGroup.bachReadDevices(0).forEach(System.out::println);
        });
    }


}