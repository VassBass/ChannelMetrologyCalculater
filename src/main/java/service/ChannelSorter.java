package service;

import constants.Sort;
import converters.VariableConverter;
import model.Channel;
import repository.ChannelRepository;
import repository.impl.ChannelRepositorySQLite;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

    public List<Channel> getCurrent(){
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

    public List<Channel> getAllForName(String name){
        this.sortString = VariableConverter.kirillToLatinLetters(name);
        this.currentSort = Sort.NAME;
        List<Channel>channels = new ArrayList<>(channelRepository.getAll());
        String nameWithOutCase = this.sortString.toUpperCase(Locale.ROOT);
        List<Channel>sortedList = new ArrayList<>();
        List<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        List<Channel>containsChannels = new ArrayList<>();
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

    public List<Channel> getAllForMeasurementName(String measurementName){
        this.sortString = VariableConverter.kirillToLatinLetters(measurementName);
        this.currentSort  = Sort.MEASUREMENT_NAME;

        return channelRepository.getAll().stream()
                .filter(c -> VariableConverter.kirillToLatinLetters(c.getMeasurement().getName()).equals(sortString))
                .collect(Collectors.toList());
    }

    public List<Channel> getAllForMeasurementValue(String measurementValue){
        this.sortString = VariableConverter.kirillToLatinLetters(measurementValue);
        this.currentSort = Sort.MEASUREMENT_VALUE;

        return channelRepository.getAll().stream()
                .filter(c -> VariableConverter.kirillToLatinLetters(c.getMeasurement().getValue()).equals(sortString))
                .collect(Collectors.toList());
    }

    public List<Channel> getAllForDepartment(String department){
        this.sortString = VariableConverter.kirillToLatinLetters(department);
        this.currentSort = Sort.DEPARTMENT;
        List<Channel>channels = new ArrayList<>(channelRepository.getAll());
        String departmentWithOutCase = this.sortString.toUpperCase(Locale.ROOT);
        List<Channel>sortedList = new ArrayList<>();
        List<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        List<Channel>containsChannels = new ArrayList<>();
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

    public List<Channel> getAllForArea(String area){
        this.sortString = VariableConverter.kirillToLatinLetters(area);
        this.currentSort = Sort.AREA;
        List<Channel>channels = new ArrayList<>(channelRepository.getAll());
        String areaWithOutCase = this.sortString.toUpperCase(Locale.ROOT);
        List<Channel>sortedList = new ArrayList<>();
        List<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        List<Channel>containsChannels = new ArrayList<>();
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

    public List<Channel> getAllForProcess(String process){
        this.sortString = VariableConverter.kirillToLatinLetters(process);
        this.currentSort = Sort.PROCESS;
        List<Channel>channels = new ArrayList<>(channelRepository.getAll());
        String processWithOutCase = this.sortString.toUpperCase(Locale.ROOT);
        List<Channel>sortedList = new ArrayList<>();
        List<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        List<Channel>containsChannels = new ArrayList<>();
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

    public List<Channel> getAllForInstallation(String installation){
        this.sortString = VariableConverter.kirillToLatinLetters(installation);
        this.currentSort = Sort.INSTALLATION;
        List<Channel>channels = new ArrayList<>(channelRepository.getAll());
        String installationWithOutCase = this.sortString.toUpperCase(Locale.ROOT);
        List<Channel>sortedList = new ArrayList<>();
        List<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        List<Channel>containsChannels = new ArrayList<>();
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

    public List<Channel> getAllForDate(String date){
        this.sortString = date;
        this.currentSort = Sort.DATE;

        return channelRepository.getAll().stream()
                .filter(c -> c.getDate().equals(date))
                .collect(Collectors.toList());
    }

    public List<Channel> getAllForFrequency(double frequency){
        this.sortString = String.valueOf(frequency);
        this.currentSort = Sort.FREQUENCY;

        return channelRepository.getAll().stream()
                .filter(c -> c.getFrequency() == frequency)
                .collect(Collectors.toList());
    }

    public List<Channel> getAllForTechnologyNumber(String technologyNumber){
        this.sortString = VariableConverter.kirillToLatinLetters(technologyNumber);
        this.currentSort = Sort.TECHNOLOGY_NUMBER;
        List<Channel>channels = new ArrayList<>(channelRepository.getAll());
        String numberWithOutCase = this.sortString.toUpperCase(Locale.ROOT);
        List<Channel>sortedList = new ArrayList<>();
        List<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        List<Channel>containsChannels = new ArrayList<>();
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

    public List<Channel> getAllForSensorName(String sensorName){
        this.sortString = VariableConverter.kirillToLatinLetters(sensorName);
        this.currentSort = Sort.SENSOR_NAME;
        List<Channel>channels = new ArrayList<>(channelRepository.getAll());
        String nameWithOutCase = this.sortString.toUpperCase(Locale.ROOT);
        List<Channel>sortedList = new ArrayList<>();
        List<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        List<Channel>containsChannels = new ArrayList<>();
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

    public List<Channel> getAllForSensorType(String sensorType){
        this.sortString = VariableConverter.kirillToLatinLetters(sensorType);
        this.currentSort = Sort.SENSOR_TYPE;

        return channelRepository.getAll().stream()
                .filter(c -> VariableConverter.kirillToLatinLetters(c.getSensor().getType()).equalsIgnoreCase(sortString))
                .collect(Collectors.toList());
    }

    public List<Channel> getAllForProtocolNumber(String protocolNumber){
        this.sortString = VariableConverter.kirillToLatinLetters(protocolNumber);
        this.currentSort = Sort.PROTOCOL_NUMBER;
        List<Channel>channels = new ArrayList<>(channelRepository.getAll());
        List<Channel>sortedList = new ArrayList<>();
        List<Channel>containsChannels = new ArrayList<>();
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

    public List<Channel> getAllForReference(String reference){
        this.sortString = VariableConverter.kirillToLatinLetters(reference);
        this.currentSort = Sort.REFERENCE;
        List<Channel>channels = new ArrayList<>(channelRepository.getAll());
        List<Channel>sortedList = new ArrayList<>();
        List<Channel>containsChannels = new ArrayList<>();
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

    public List<Channel> getAllForSuitability(boolean suitability){
        this.sortBoolean = suitability;
        this.currentSort = Sort.SUITABILITY;

        return channelRepository.getAll().stream()
                .filter(c -> c.isSuitability() == suitability)
                .collect(Collectors.toList());
    }
}