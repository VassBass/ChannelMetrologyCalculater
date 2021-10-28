package constants;

public enum CalibratorType {

    FLUKE724("Fluke 724"),
    FLUKE725("Fluke 725"),
    PROVA123("Prova-123"),
    FLUKE718_30G("Fluke 718 30G"),
    FLUKE750PD2("Fluke 750PD2"),
    FLUKE750PD2_small("Fluke 750PD2 (для Р<60 мм вод ст)");

    private final String type;
    CalibratorType(String type){this.type = type;}
    public String getType(){return this.type;}
}
