package service;

import ui.event.Event;

import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

public interface MainScreenEventListener {
    <O> WindowListener clickClose(Event<O> event);
    <O> ActionListener clickInfoButton(Event<O> event);
    <O> ActionListener clickAddButton(Event<O> event);
    <O> ActionListener clickRemoveButton(Event<O> event);
    <O> ActionListener clickCalculateButton(Event<O> event);
    <O> ActionListener clickOpenFolderButton(Event<O> event);
}
