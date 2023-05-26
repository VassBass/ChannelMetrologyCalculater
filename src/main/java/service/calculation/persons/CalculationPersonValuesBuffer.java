package service.calculation.persons;

import model.OS;

import java.util.Objects;

public class CalculationPersonValuesBuffer {
    private static volatile CalculationPersonValuesBuffer instance;
    public static CalculationPersonValuesBuffer getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (CalculationPersonValuesBuffer.class) {
                if (Objects.isNull(instance)) instance = new CalculationPersonValuesBuffer();
            }
        }
        return instance;
    }

    private String firstMakerName;
    private String firstMakerPosition;
    private String secondMakerName;
    private String secondMakerPosition;
    private String formerName;
    private String formerPosition;
    private String headOfMetrologyDepartment;
    private String headOfCheckedChannelDepartmentName;
    private String headOfCheckedChannelDepartmentPosition;
    private String headOfASPCDepartment;
    private OS os;

    private CalculationPersonValuesBuffer(){
        os = OS.getCurrentOS();
    }

    public String getFirstMakerName() {
        return firstMakerName;
    }

    public OS getOs() {
        return os;
    }

    public void setOs(OS os) {
        this.os = os;
    }

    public void setFirstMakerName(String firstMakerName) {
        this.firstMakerName = firstMakerName;
    }

    public String getFirstMakerPosition() {
        return firstMakerPosition;
    }

    public void setFirstMakerPosition(String firstMakerPosition) {
        this.firstMakerPosition = firstMakerPosition;
    }

    public String getSecondMakerName() {
        return secondMakerName;
    }

    public void setSecondMakerName(String secondMakerName) {
        this.secondMakerName = secondMakerName;
    }

    public String getSecondMakerPosition() {
        return secondMakerPosition;
    }

    public void setSecondMakerPosition(String secondMakerPosition) {
        this.secondMakerPosition = secondMakerPosition;
    }

    public String getFormerName() {
        return formerName;
    }

    public void setFormerName(String formerName) {
        this.formerName = formerName;
    }

    public String getFormerPosition() {
        return formerPosition;
    }

    public void setFormerPosition(String formerPosition) {
        this.formerPosition = formerPosition;
    }

    public String getHeadOfMetrologyDepartment() {
        return headOfMetrologyDepartment;
    }

    public void setHeadOfMetrologyDepartment(String headOfMetrologyDepartment) {
        this.headOfMetrologyDepartment = headOfMetrologyDepartment;
    }

    public String getHeadOfCheckedChannelDepartmentName() {
        return headOfCheckedChannelDepartmentName;
    }

    public void setHeadOfCheckedChannelDepartmentName(String name) {
        this.headOfCheckedChannelDepartmentName = name;
    }

    public void setHeadOfCheckedChannelDepartmentPosition(String position) {
        this.headOfCheckedChannelDepartmentPosition = position;
    }

    public String getHeadOfCheckedChannelDepartmentPosition() {
        return headOfCheckedChannelDepartmentPosition;
    }

    public String getHeadOfASPCDepartment() {
        return headOfASPCDepartment;
    }

    public void setHeadOfASPCDepartment(String headOfASPCDepartment) {
        this.headOfASPCDepartment = headOfASPCDepartment;
    }
}
