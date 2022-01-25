package controller;

import application.Application;
import constants.Sort;
import converters.VariableConverter;
import model.Channel;

import java.util.ArrayList;
import java.util.Locale;

public class ChannelSorter {

    private int currentSort = -1;
    private String sortString;
    private boolean sortBoolean;

    public boolean isOn(){
        return this.currentSort != -1;
    }

    public void setOff(){
        this.currentSort = -1;
    }

    public ArrayList<Channel>getCurrent(){
        switch (currentSort){
            case Sort.NAME:
                return getAllForName(this.sortString);
            case Sort.MEASUREMENT_NAME:
                return getAllForMeasurementName(this.sortString);
            case Sort.MEASUREMENT_VALUE:
                return getAllForMeasurementValue(this.sortString);
            case Sort.DEPARTMENT:
                return getAllForDepartment(this.sortString);
            case Sort.AREA:
                return getAllForArea(this.sortString);
            case Sort.PROCESS:
                return getAllForProcess(this.sortString);
            case Sort.INSTALLATION:
                return getAllForInstallation(this.sortString);
            case Sort.DATE:
                return getAllForDate(this.sortString);
            case Sort.FREQUENCY:
                return getAllForFrequency(Double.parseDouble(this.sortString));
            case Sort.TECHNOLOGY_NUMBER:
                return getAllForTechnologyNumber(this.sortString);
            case Sort.PROTOCOL_NUMBER:
                return getAllForProtocolNumber(this.sortString);
            case Sort.REFERENCE:
                return getAllForReference(this.sortString);
            case Sort.SENSOR_NAME:
                return getAllForSensorName(this.sortString);
            case Sort.SENSOR_TYPE:
                return getAllForSensorType(this.sortString);
            case Sort.SUITABILITY:
                return getAllForSuitability(this.sortBoolean);
            default:
                return Application.context.channelsController.getAll();
        }
    }

    public ArrayList<Channel> getAllForName(String name){
        this.sortString = name;
        this.currentSort = Sort.NAME;
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        String nameWithOutCase = name.toLowerCase(Locale.ROOT);
        ArrayList<Channel>sortedList = new ArrayList<>();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String chNameWithOutLowerCase = channel.getName().toLowerCase(Locale.ROOT);
            if (channel.getName().equals(name)){
                sortedList.add(channel);
            }else if (chNameWithOutLowerCase.equals(nameWithOutCase)){
                equalsChannelsWithOutCase.add(channel);
            }else if (chNameWithOutLowerCase.contains(nameWithOutCase)){
                containsChannels.add(channel);
            }
        }
        sortedList.addAll(equalsChannelsWithOutCase);
        sortedList.addAll(containsChannels);
        return sortedList;
    }

    public ArrayList<Channel> getAllForMeasurementName(String measurementName){
        this.sortString = measurementName;
        this.currentSort  = Sort.MEASUREMENT_NAME;
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        ArrayList<Channel>sortedList = new ArrayList<>();
        for (Channel channel : channels){
            String channelMeasurementName = channel.getMeasurement().getName();
            if (channelMeasurementName.equals(measurementName)){
                sortedList.add(channel);
            }
        }
        return sortedList;
    }

    public ArrayList<Channel> getAllForMeasurementValue(String measurementValue){
        this.sortString = measurementValue;
        this.currentSort = Sort.MEASUREMENT_VALUE;
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        ArrayList<Channel>sortedList = new ArrayList<>();
        for (Channel channel : channels){
            String channelMeasurementValue = channel.getMeasurement().getValue();
            if (channelMeasurementValue.equals(measurementValue)){
                sortedList.add(channel);
            }
        }
        return sortedList;
    }

    public ArrayList<Channel> getAllForDepartment(String department){
        this.sortString = department;
        this.currentSort = Sort.DEPARTMENT;
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        String departmentWithOutCase = department.toLowerCase(Locale.ROOT);
        ArrayList<Channel>sortedList = new ArrayList<>();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String depWithOutLowerCase = channel.getDepartment().toLowerCase(Locale.ROOT);
            if (channel.getDepartment().equals(department)){
                sortedList.add(channel);
            }else if (depWithOutLowerCase.equals(departmentWithOutCase)){
                equalsChannelsWithOutCase.add(channel);
            }else if (depWithOutLowerCase.contains(departmentWithOutCase)){
                containsChannels.add(channel);
            }
        }
        sortedList.addAll(equalsChannelsWithOutCase);
        sortedList.addAll(containsChannels);
        return sortedList;
    }

    public ArrayList<Channel> getAllForArea(String area){
        this.sortString = area;
        this.currentSort = Sort.AREA;
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        String areaWithOutCase = area.toLowerCase(Locale.ROOT);
        ArrayList<Channel>sortedList = new ArrayList<>();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String areWithOutLowerCase = channel.getArea().toLowerCase(Locale.ROOT);
            if (channel.getArea().equals(area)){
                sortedList.add(channel);
            }else if (areWithOutLowerCase.equals(areaWithOutCase)){
                equalsChannelsWithOutCase.add(channel);
            }else if (areWithOutLowerCase.contains(areaWithOutCase)){
                containsChannels.add(channel);
            }
        }
        sortedList.addAll(equalsChannelsWithOutCase);
        sortedList.addAll(containsChannels);
        return sortedList;
    }

    public ArrayList<Channel> getAllForProcess(String process){
        this.sortString = process;
        this.currentSort = Sort.PROCESS;
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        String processWithOutCase = process.toLowerCase(Locale.ROOT);
        ArrayList<Channel>sortedList = new ArrayList<>();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String proWithOutLowerCase = channel.getProcess().toLowerCase(Locale.ROOT);
            if (channel.getProcess().equals(process)){
                sortedList.add(channel);
            }else if (proWithOutLowerCase.equals(processWithOutCase)){
                equalsChannelsWithOutCase.add(channel);
            }else if (proWithOutLowerCase.contains(processWithOutCase)){
                containsChannels.add(channel);
            }
        }
        sortedList.addAll(equalsChannelsWithOutCase);
        sortedList.addAll(containsChannels);
        return sortedList;
    }

    public ArrayList<Channel> getAllForInstallation(String installation){
        this.sortString = installation;
        this.currentSort = Sort.INSTALLATION;
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        String installationWithOutCase = installation.toLowerCase(Locale.ROOT);
        ArrayList<Channel>sortedList = new ArrayList<>();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String insWithOutLowerCase = channel.getInstallation().toLowerCase(Locale.ROOT);
            if (channel.getInstallation().equals(installation)){
                sortedList.add(channel);
            }else if (insWithOutLowerCase.equals(installationWithOutCase)){
                equalsChannelsWithOutCase.add(channel);
            }else if (insWithOutLowerCase.contains(installationWithOutCase)){
                containsChannels.add(channel);
            }
        }
        sortedList.addAll(equalsChannelsWithOutCase);
        sortedList.addAll(containsChannels);
        return sortedList;
    }

    public ArrayList<Channel> getAllForDate(String date){
        this.sortString = date;
        this.currentSort = Sort.DATE;
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        ArrayList<Channel>sortedList = new ArrayList<>();
        for (Channel channel : channels){
            String channelDateString = VariableConverter.dateToString(channel.getDate());
            if (channelDateString.equals(date)){
                sortedList.add(channel);
            }
        }
        return sortedList;
    }

    public ArrayList<Channel> getAllForFrequency(double frequency){
        this.sortString = String.valueOf(frequency);
        this.currentSort = Sort.FREQUENCY;
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        ArrayList<Channel>sortedList = new ArrayList<>();
        for (Channel channel : channels){
            double channelFrequency = channel.getFrequency();
            if (channelFrequency == frequency){
                sortedList.add(channel);
            }
        }
        return sortedList;
    }

    public ArrayList<Channel> getAllForTechnologyNumber(String technologyNumber){
        this.sortString = technologyNumber;
        this.currentSort = Sort.TECHNOLOGY_NUMBER;
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        String numberWithOutCase = technologyNumber.toLowerCase(Locale.ROOT);
        ArrayList<Channel>sortedList = new ArrayList<>();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String numWithOutLowerCase = channel.getTechnologyNumber().toLowerCase(Locale.ROOT);
            if (channel.getTechnologyNumber().equals(technologyNumber)){
                sortedList.add(channel);
            }else if (numWithOutLowerCase.equals(numberWithOutCase)){
                equalsChannelsWithOutCase.add(channel);
            }else if (numWithOutLowerCase.contains(numberWithOutCase)){
                containsChannels.add(channel);
            }
        }
        sortedList.addAll(equalsChannelsWithOutCase);
        sortedList.addAll(containsChannels);
        return sortedList;
    }

    public ArrayList<Channel> getAllForSensorName(String sensorName){
        this.sortString = sensorName;
        this.currentSort = Sort.SENSOR_NAME;
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        String nameWithOutCase = sensorName.toLowerCase(Locale.ROOT);
        ArrayList<Channel>sortedList = new ArrayList<>();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String nWithOutLowerCase = channel.getSensor().getName().toLowerCase(Locale.ROOT);
            if (channel.getSensor().getName().equals(sensorName)){
                sortedList.add(channel);
            }else if (nWithOutLowerCase.equals(nameWithOutCase)){
                equalsChannelsWithOutCase.add(channel);
            }else if (nWithOutLowerCase.contains(nameWithOutCase)){
                containsChannels.add(channel);
            }
        }
        sortedList.addAll(equalsChannelsWithOutCase);
        sortedList.addAll(containsChannels);
        return sortedList;
    }

    public ArrayList<Channel> getAllForSensorType(String sensorType){
        this.sortString = sensorType;
        this.currentSort = Sort.SENSOR_TYPE;
        String type = sensorType.toLowerCase(Locale.ROOT);
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        ArrayList<Channel>sortedList = new ArrayList<>();
        for (Channel channel : channels){
            String sType = channel.getSensor().getType().toLowerCase(Locale.ROOT);
            if (sType.equals(type)){
                sortedList.add(channel);
            }
        }
        return sortedList;
    }

    public ArrayList<Channel> getAllForProtocolNumber(String protocolNumber){
        this.sortString = protocolNumber;
        this.currentSort = Sort.PROTOCOL_NUMBER;
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        ArrayList<Channel>sortedList = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String number = channel.getNumberOfProtocol();
            if (number.equals(protocolNumber)){
                sortedList.add(channel);
            }else if (number.contains(protocolNumber)){
                containsChannels.add(channel);
            }
        }
        sortedList.addAll(containsChannels);
        return sortedList;
    }

    public ArrayList<Channel> getAllForReference(String reference){
        this.sortString = reference;
        this.currentSort = Sort.REFERENCE;
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        ArrayList<Channel>sortedList = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String ref = channel.getReference();
            if (ref.equals(reference)){
                sortedList.add(channel);
            }else if (ref.contains(reference)){
                containsChannels.add(channel);
            }
        }
        sortedList.addAll(containsChannels);
        return sortedList;
    }

    public ArrayList<Channel> getAllForSuitability(boolean suitability){
        this.sortBoolean = suitability;
        this.currentSort = Sort.SUITABILITY;
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        ArrayList<Channel>sortedList = new ArrayList<>();
        for (Channel channel : channels){
            if (channel.isSuitability() == suitability){
                sortedList.add(channel);
            }
        }
        return sortedList;
    }
}