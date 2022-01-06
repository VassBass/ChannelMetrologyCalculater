package support;

import application.Application;
import constants.MeasurementConstants;
import converters.VariableConverter;
import model.Channel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ChannelSorter {

    private ArrayList<Channel>currentSortedList = new ArrayList<>();

    public ArrayList<Channel>getCurrent(){
        return this.currentSortedList;
    }

    public ArrayList<Channel> getAllForName(String name){
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        String nameWithOutCase = name.toLowerCase(Locale.ROOT);
        this.currentSortedList.clear();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String chNameWithOutLowerCase = channel.getName().toLowerCase(Locale.ROOT);
            if (channel.getName().equals(name)){
                this.currentSortedList.add(channel);
            }else if (chNameWithOutLowerCase.equals(nameWithOutCase)){
                equalsChannelsWithOutCase.add(channel);
            }else if (channel.getName().contains(name)){
                containsChannels.add(channel);
            }
        }
        this.currentSortedList.addAll(equalsChannelsWithOutCase);
        this.currentSortedList.addAll(containsChannels);
        return this.currentSortedList;
    }

    public ArrayList<Channel> getAllForMeasurementName(MeasurementConstants measurementName){
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        this.currentSortedList.clear();
        for (Channel channel : channels){
            MeasurementConstants channelMeasurementName = channel.getMeasurement().getNameConstant();
            if (channelMeasurementName == measurementName){
                this.currentSortedList.add(channel);
            }
        }
        return this.currentSortedList;
    }

    public ArrayList<Channel> getAllForMeasurementName(String measurementName){
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        this.currentSortedList.clear();
        for (Channel channel : channels){
            String channelMeasurementName = channel.getMeasurement().getName();
            if (channelMeasurementName.equals(measurementName)){
                this.currentSortedList.add(channel);
            }
        }
        return this.currentSortedList;
    }

    public ArrayList<Channel> getAllForMeasurementValue(MeasurementConstants measurementValue){
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        this.currentSortedList.clear();
        for (Channel channel : channels){
            MeasurementConstants channelMeasurementValue = channel.getMeasurement().getValueConstant();
            if (channelMeasurementValue == measurementValue){
                currentSortedList.add(channel);
            }
        }
        return this.currentSortedList;
    }

    public ArrayList<Channel> getAllForMeasurementValue(String measurementValue){
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        this.currentSortedList.clear();
        for (Channel channel : channels){
            String channelMeasurementValue = channel.getMeasurement().getValue();
            if (channelMeasurementValue.equals(measurementValue)){
                this.currentSortedList.add(channel);
            }
        }
        return this.currentSortedList;
    }

    public ArrayList<Channel> getAllForDepartment(String department){
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        String departmentWithOutCase = department.toLowerCase(Locale.ROOT);
        this.currentSortedList.clear();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String depWithOutLowerCase = channel.getDepartment().toLowerCase(Locale.ROOT);
            if (channel.getDepartment().equals(department)){
                this.currentSortedList.add(channel);
            }else if (depWithOutLowerCase.equals(departmentWithOutCase)){
                equalsChannelsWithOutCase.add(channel);
            }else if (channel.getDepartment().contains(department)){
                containsChannels.add(channel);
            }
        }
        this.currentSortedList.addAll(equalsChannelsWithOutCase);
        this.currentSortedList.addAll(containsChannels);
        return this.currentSortedList;
    }

    public ArrayList<Channel> getAllForArea(String area){
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        String areaWithOutCase = area.toLowerCase(Locale.ROOT);
        this.currentSortedList.clear();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String areWithOutLowerCase = channel.getArea().toLowerCase(Locale.ROOT);
            if (channel.getArea().equals(area)){
                this.currentSortedList.add(channel);
            }else if (areWithOutLowerCase.equals(areaWithOutCase)){
                equalsChannelsWithOutCase.add(channel);
            }else if (channel.getArea().contains(area)){
                containsChannels.add(channel);
            }
        }
        this.currentSortedList.addAll(equalsChannelsWithOutCase);
        this.currentSortedList.addAll(containsChannels);
        return this.currentSortedList;
    }

    public ArrayList<Channel> getAllForProcess(String process){
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        String processWithOutCase = process.toLowerCase(Locale.ROOT);
        this.currentSortedList.clear();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String proWithOutLowerCase = channel.getProcess().toLowerCase(Locale.ROOT);
            if (channel.getProcess().equals(process)){
                this.currentSortedList.add(channel);
            }else if (proWithOutLowerCase.equals(processWithOutCase)){
                equalsChannelsWithOutCase.add(channel);
            }else if (channel.getProcess().contains(process)){
                containsChannels.add(channel);
            }
        }
        this.currentSortedList.addAll(equalsChannelsWithOutCase);
        this.currentSortedList.addAll(containsChannels);
        return this.currentSortedList;
    }

    public ArrayList<Channel> getAllForInstallation(String installation){
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        String installationWithOutCase = installation.toLowerCase(Locale.ROOT);
        this.currentSortedList.clear();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String insWithOutLowerCase = channel.getInstallation().toLowerCase(Locale.ROOT);
            if (channel.getInstallation().equals(installation)){
                this.currentSortedList.add(channel);
            }else if (insWithOutLowerCase.equals(installationWithOutCase)){
                equalsChannelsWithOutCase.add(channel);
            }else if (channel.getInstallation().contains(installation)){
                containsChannels.add(channel);
            }
        }
        this.currentSortedList.addAll(equalsChannelsWithOutCase);
        this.currentSortedList.addAll(containsChannels);
        return this.currentSortedList;
    }

    public ArrayList<Channel> getAllForDate(Calendar date){
        String dateString = VariableConverter.dateToString(date);
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        this.currentSortedList.clear();
        for (Channel channel : channels){
            String channelDateString = VariableConverter.dateToString(channel.getDate());
            if (channelDateString.equals(dateString)){
                this.currentSortedList.add(channel);
            }
        }
        return this.currentSortedList;
    }

    public ArrayList<Channel> getAllForFrequency(double frequency){
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        this.currentSortedList.clear();
        for (Channel channel : channels){
            double channelFrequency = channel.getFrequency();
            if (channelFrequency == frequency){
                this.currentSortedList.add(channel);
            }
        }
        return this.currentSortedList;
    }

    public ArrayList<Channel> getAllForTechnologyNumber(String technologyNumber){
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        String numberWithOutCase = technologyNumber.toLowerCase(Locale.ROOT);
        this.currentSortedList.clear();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String numWithOutLowerCase = channel.getTechnologyNumber().toLowerCase(Locale.ROOT);
            if (channel.getTechnologyNumber().equals(technologyNumber)){
                this.currentSortedList.add(channel);
            }else if (numWithOutLowerCase.equals(numberWithOutCase)){
                equalsChannelsWithOutCase.add(channel);
            }else if (channel.getTechnologyNumber().contains(technologyNumber)){
                containsChannels.add(channel);
            }
        }
        this.currentSortedList.addAll(equalsChannelsWithOutCase);
        this.currentSortedList.addAll(containsChannels);
        return this.currentSortedList;
    }

    public ArrayList<Channel> getAllForSensorName(String sensorName){
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        String nameWithOutCase = sensorName.toLowerCase(Locale.ROOT);
        this.currentSortedList.clear();
        ArrayList<Channel>equalsChannelsWithOutCase = new ArrayList<>();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String nWithOutLowerCase = channel.getSensor().getName().toLowerCase(Locale.ROOT);
            if (channel.getSensor().getName().equals(sensorName)){
                this.currentSortedList.add(channel);
            }else if (nWithOutLowerCase.equals(nameWithOutCase)){
                equalsChannelsWithOutCase.add(channel);
            }else if (channel.getSensor().getName().contains(sensorName)){
                containsChannels.add(channel);
            }
        }
        this.currentSortedList.addAll(equalsChannelsWithOutCase);
        this.currentSortedList.addAll(containsChannels);
        return this.currentSortedList;
    }

    public ArrayList<Channel> getAllForSensorType(String sensorType){
        String type = sensorType.toLowerCase(Locale.ROOT);
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        this.currentSortedList.clear();
        for (Channel channel : channels){
            String sType = channel.getSensor().getType().toLowerCase(Locale.ROOT);
            if (sType.equals(type)){
                this.currentSortedList.add(channel);
            }
        }
        return this.currentSortedList;
    }

    public ArrayList<Channel> getAllForProtocolNumber(String protocolNumber){
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        this.currentSortedList.clear();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String number = channel.getNumberOfProtocol();
            if (number.equals(protocolNumber)){
                this.currentSortedList.add(channel);
            }else if (number.contains(protocolNumber)){
                containsChannels.add(channel);
            }
        }
        this.currentSortedList.addAll(containsChannels);
        return this.currentSortedList;
    }

    public ArrayList<Channel> getAllForReference(String reference){
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        this.currentSortedList.clear();
        ArrayList<Channel>containsChannels = new ArrayList<>();
        for (Channel channel : channels){
            String ref = channel.getReference();
            if (ref.equals(reference)){
                this.currentSortedList.add(channel);
            }else if (ref.contains(reference)){
                containsChannels.add(channel);
            }
        }
        this.currentSortedList.addAll(containsChannels);
        return this.currentSortedList;
    }

    public ArrayList<Channel> getAllForSuitability(boolean suitability){
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        this.currentSortedList.clear();
        for (Channel channel : channels){
            if (channel.isSuitability() == suitability){
                this.currentSortedList.add(channel);
            }
        }
        return this.currentSortedList;
    }
}
