package repository;

import model.Channel;

import java.util.ArrayList;

public interface ChannelRepository {
    ArrayList<Channel> getAll();
    void add(Channel channel);
    void remove(String channelCode);
    void rewriteInCurrentThread(ArrayList<Channel>channels);
    void set(Channel oldChannel, Channel newChannel);
    void clear();
    void export(ArrayList<Channel>channels);
}
