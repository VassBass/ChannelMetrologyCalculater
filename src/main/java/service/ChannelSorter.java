package service;

import constants.Sort;
import converters.VariableConverter;
import model.Channel;
import repository.ChannelRepository;
import repository.impl.ChannelRepositorySQLite;

import java.util.ArrayList;
import java.util.Locale;

public class ChannelSorter {
    private static ChannelSorter sorter;

    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();

    private ChannelSorter(){}

    public static ChannelSorter getInstance() {
        if (sorter == null) sorter = new ChannelSorter();

        return sorter;
    }

    private int currentSort = -1;
    private String sortString;
    private boolean sortBoolean;

    public boolean isOn(){
        return sorter != null;
    }

    public void setOff(){
        sorter = null;
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
                return new ArrayList<>(channelRepository.getAll());
        }
    }

    public ArrayList<Channel> getAllForName(String name){
        this.sortString = VariableConverter.kirillToLatinLetters(name);
        this.currentSort = Sort.NAME;
        ArrayList<Channel>channels = new ArrayList<>(channelRepository.getAll());
        String nameWithOutCase = this.sortString.toUpperCase(Locale.ROOT);
        ArrayList<Channel>sortedList = new ArrayList<>();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String chNameWithOutLowerCase = VariableConverter.kirillToLatinLetters(channel.getName()).toUpperCase(Locale.ROOT);
            if (VariableConverter.kirillToLatinLetters(channel.getName()).equals(this.sortString)){
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
        this.sortString = VariableConverter.kirillToLatinLetters(measurementName);
        this.currentSort  = Sort.MEASUREMENT_NAME;
        ArrayList<Channel>channels = new ArrayList<>(channelRepository.getAll());
        ArrayList<Channel>sortedList = new ArrayList<>();
        for (Channel channel : channels){
            String channelMeasurementName = VariableConverter.kirillToLatinLetters(channel.getMeasurement().getName());
            if (channelMeasurementName.equals(this.sortString)){
                sortedList.add(channel);
            }
        }
        return sortedList;
    }

    public ArrayList<Channel> getAllForMeasurementValue(String measurementValue){
        this.sortString = VariableConverter.kirillToLatinLetters(measurementValue);
        this.currentSort = Sort.MEASUREMENT_VALUE;
        ArrayList<Channel>channels = new ArrayList<>(channelRepository.getAll());
        ArrayList<Channel>sortedList = new ArrayList<>();
        for (Channel channel : channels){
            String channelMeasurementValue = VariableConverter.kirillToLatinLetters(channel.getMeasurement().getValue());
            if (channelMeasurementValue.equals(this.sortString)){
                sortedList.add(channel);
            }
        }
        return sortedList;
    }

    public ArrayList<Channel> getAllForDepartment(String department){
        this.sortString = VariableConverter.kirillToLatinLetters(department);
        this.currentSort = Sort.DEPARTMENT;
        ArrayList<Channel>channels = new ArrayList<>(channelRepository.getAll());
        String departmentWithOutCase = this.sortString.toUpperCase(Locale.ROOT);
        ArrayList<Channel>sortedList = new ArrayList<>();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String depWithOutLowerCase = VariableConverter.kirillToLatinLetters(channel.getDepartment()).toUpperCase(Locale.ROOT);
            if (VariableConverter.kirillToLatinLetters(channel.getDepartment()).equals(this.sortString)){
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
        this.sortString = VariableConverter.kirillToLatinLetters(area);
        this.currentSort = Sort.AREA;
        ArrayList<Channel>channels = new ArrayList<>(channelRepository.getAll());
        String areaWithOutCase = this.sortString.toUpperCase(Locale.ROOT);
        ArrayList<Channel>sortedList = new ArrayList<>();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String areWithOutLowerCase = VariableConverter.kirillToLatinLetters(channel.getArea()).toUpperCase(Locale.ROOT);
            if (VariableConverter.kirillToLatinLetters(channel.getArea()).equals(this.sortString)){
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
        this.sortString = VariableConverter.kirillToLatinLetters(process);
        this.currentSort = Sort.PROCESS;
        ArrayList<Channel>channels = new ArrayList<>(channelRepository.getAll());
        String processWithOutCase = this.sortString.toUpperCase(Locale.ROOT);
        ArrayList<Channel>sortedList = new ArrayList<>();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String proWithOutLowerCase = VariableConverter.kirillToLatinLetters(channel.getProcess()).toUpperCase(Locale.ROOT);
            if (VariableConverter.kirillToLatinLetters(channel.getProcess()).equals(this.sortString)){
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
        this.sortString = VariableConverter.kirillToLatinLetters(installation);
        this.currentSort = Sort.INSTALLATION;
        ArrayList<Channel>channels = new ArrayList<>(channelRepository.getAll());
        String installationWithOutCase = this.sortString.toUpperCase(Locale.ROOT);
        ArrayList<Channel>sortedList = new ArrayList<>();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String insWithOutLowerCase = VariableConverter.kirillToLatinLetters(channel.getInstallation()).toUpperCase(Locale.ROOT);
            if (VariableConverter.kirillToLatinLetters(channel.getInstallation()).equals(this.sortString)){
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
        ArrayList<Channel>channels = new ArrayList<>(channelRepository.getAll());
        ArrayList<Channel>sortedList = new ArrayList<>();
        for (Channel channel : channels){
            if (channel.getDate().equals(date)){
                sortedList.add(channel);
            }
        }
        return sortedList;
    }

    public ArrayList<Channel> getAllForFrequency(double frequency){
        this.sortString = String.valueOf(frequency);
        this.currentSort = Sort.FREQUENCY;
        ArrayList<Channel>channels = new ArrayList<>(channelRepository.getAll());
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
        this.sortString = VariableConverter.kirillToLatinLetters(technologyNumber);
        this.currentSort = Sort.TECHNOLOGY_NUMBER;
        ArrayList<Channel>channels = new ArrayList<>(channelRepository.getAll());
        String numberWithOutCase = this.sortString.toUpperCase(Locale.ROOT);
        ArrayList<Channel>sortedList = new ArrayList<>();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String numWithOutLowerCase = VariableConverter.kirillToLatinLetters(channel.getTechnologyNumber()).toUpperCase(Locale.ROOT);
            if (VariableConverter.kirillToLatinLetters(channel.getTechnologyNumber()).equals(this.sortString)){
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
        this.sortString = VariableConverter.kirillToLatinLetters(sensorName);
        this.currentSort = Sort.SENSOR_NAME;
        ArrayList<Channel>channels = new ArrayList<>(channelRepository.getAll());
        String nameWithOutCase = this.sortString.toUpperCase(Locale.ROOT);
        ArrayList<Channel>sortedList = new ArrayList<>();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String nWithOutLowerCase = VariableConverter.kirillToLatinLetters(channel.getSensor().getName()).toUpperCase(Locale.ROOT);
            if (VariableConverter.kirillToLatinLetters(channel.getSensor().getName()).equals(this.sortString)){
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
        this.sortString = VariableConverter.kirillToLatinLetters(sensorType);
        this.currentSort = Sort.SENSOR_TYPE;
        String type = this.sortString.toUpperCase(Locale.ROOT);
        ArrayList<Channel>channels = new ArrayList<>(channelRepository.getAll());
        ArrayList<Channel>sortedList = new ArrayList<>();
        for (Channel channel : channels){
            String sType = VariableConverter.kirillToLatinLetters(channel.getSensor().getType()).toUpperCase(Locale.ROOT);
            if (sType.equals(type)){
                sortedList.add(channel);
            }
        }
        return sortedList;
    }

    public ArrayList<Channel> getAllForProtocolNumber(String protocolNumber){
        this.sortString = VariableConverter.kirillToLatinLetters(protocolNumber);
        this.currentSort = Sort.PROTOCOL_NUMBER;
        ArrayList<Channel>channels = new ArrayList<>(channelRepository.getAll());
        ArrayList<Channel>sortedList = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String number = VariableConverter.kirillToLatinLetters(channel.getNumberOfProtocol());
            if (number.equals(this.sortString)){
                sortedList.add(channel);
            }else if (number.contains(this.sortString)){
                containsChannels.add(channel);
            }
        }
        sortedList.addAll(containsChannels);
        return sortedList;
    }

    public ArrayList<Channel> getAllForReference(String reference){
        this.sortString = VariableConverter.kirillToLatinLetters(reference);
        this.currentSort = Sort.REFERENCE;
        ArrayList<Channel>channels = new ArrayList<>(channelRepository.getAll());
        ArrayList<Channel>sortedList = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String ref = VariableConverter.kirillToLatinLetters(channel.getReference());
            if (ref.equals(this.sortString)){
                sortedList.add(channel);
            }else if (ref.contains(this.sortString)){
                containsChannels.add(channel);
            }
        }
        sortedList.addAll(containsChannels);
        return sortedList;
    }

    public ArrayList<Channel> getAllForSuitability(boolean suitability){
        this.sortBoolean = suitability;
        this.currentSort = Sort.SUITABILITY;
        ArrayList<Channel>channels = new ArrayList<>(channelRepository.getAll());
        ArrayList<Channel>sortedList = new ArrayList<>();
        for (Channel channel : channels){
            if (channel.isSuitability() == suitability){
                sortedList.add(channel);
            }
        }
        return sortedList;
    }
}