package constants;

public enum ChannelConstants {
    CODE,
    NAME,
    MEASUREMENT,
    DEPARTMENT,
    AREA,
    PROCESS,
    INSTALLATION,
    FULL_PATH,
    THIS_DATE,
    NEXT_DATE,
    TECHNOLOGY_NUMBER,
    SENSOR,
    PROTOCOL_NUMBER;

    public static ChannelConstants getConstantFromInt(int INT){
        switch (INT){
            default:
                return ChannelConstants.CODE;
            case 1:
                return ChannelConstants.NAME;
            case 2:
                return ChannelConstants.MEASUREMENT;
            case 3:
                return ChannelConstants.DEPARTMENT;
            case 4:
                return ChannelConstants.AREA;
            case 5:
                return ChannelConstants.PROCESS;
            case 6:
                return ChannelConstants.INSTALLATION;
            case 7:
                return ChannelConstants.FULL_PATH;
            case 8:
                return ChannelConstants.THIS_DATE;
            case 9:
                return ChannelConstants.NEXT_DATE;
            case 10:
                return ChannelConstants.TECHNOLOGY_NUMBER;
            case 11:
                return ChannelConstants.SENSOR;
            case 12:
                return ChannelConstants.PROTOCOL_NUMBER;
        }
    }

    public static String getStringFromConstant(ChannelConstants constant){
        switch (constant){
            default:
                return Strings.CODE;
            case NAME:
                return Strings._NAME;
            case MEASUREMENT:
                return Strings.TYPE_OF_MEASUREMENT;
            case DEPARTMENT:
                return Strings.DEPARTMENT;
            case AREA:
                return Strings.AREA;
            case PROCESS:
                return Strings.PROCESS;
            case INSTALLATION:
                return Strings.INSTALLATION;
            case THIS_DATE:
                return Strings.THIS_DATE;
            case NEXT_DATE:
                return Strings.NEXT_DATE;
            case TECHNOLOGY_NUMBER:
                return Strings.TECHNOLOGY_NUMBER;
            case PROTOCOL_NUMBER:
                return Strings.PROTOCOL_NUMBER;
            case FULL_PATH:
                return Strings.FULL_PATH;
            case SENSOR:
                return Strings.SENSOR;
        }
    }
}
