package constants;

public enum SensorType {

    TXA_2388_typeK("Термопара TXA-2388 (тип К)"),
    TXA_0395_typeK("Термопара TXA-0395 (тип К)"),
    TCM_50M("ТСМ-50М"),
    TCP_100("ТОП  Pt 100"),
    DELTABAR_S("Deltabar S"),
    YOKOGAWA("Yokogawa"),
    JUMO_dTRANS_p02("JUMO dTRANS p02"),
    FISHER_ROSEMOUNT_3051S("Fisher-Rosemount 3051S"),
    TP0198_2("ТП 0198/2");

    private final String type;
    SensorType(String type){this.type = type;}
    public String getType(){return this.type;}

    public static SensorType getConstantFromString(String string){
        for (SensorType type : SensorType.values()){
            if (string.equals(type.getType())){
                return type;
            }
        }
        return null;
    }
}
