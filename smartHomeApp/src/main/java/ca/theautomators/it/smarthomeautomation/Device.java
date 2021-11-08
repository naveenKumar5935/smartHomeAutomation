package ca.theautomators.it.smarthomeautomation;

public class Device {

    private String type;
    private String identifier;

    FirebaseConnect fC;

    public Device(String identifier){

        fC = FirebaseConnect.getInstance();

        this.identifier = identifier;
        this.type = fC.getDeviceType(identifier);
    }

    public String getSensorData(String identifier){

        return fC.getSensorData(identifier);
    }

    public void sendControl(String controlData){

        fC.sendControlData(controlData);
    }

    public String getType(){

        return type;
    }

    public String getIdentifier(){

        return identifier;
    }


}
