package service;

import ui.event.EventDataSource;

import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import java.awt.event.WindowListener;

public interface MainScreenEventListener {
    <O> WindowListener clickClose(EventDataSource<O> event);
    <O> ActionListener clickInfoButton(EventDataSource<O> event);
    <O> ActionListener clickAddButton(EventDataSource<O> event);
    <O> ActionListener clickRemoveButton(EventDataSource<O> event);
    <O> ActionListener clickCalculateButton(EventDataSource<O> event);
    <O> ActionListener clickOpenFolderButton(EventDataSource<O> event);
    <O> ListSelectionListener selectChannel(EventDataSource<O> eventDataSource);
    <O> ActionListener clickChooseOS(EventDataSource<O> eventDataSource);
    <O> ActionListener clickSearchButton(EventDataSource<O> eventDataSource);
    <O> ItemListener changeSearchField(EventDataSource<O> eventDataSource);
    <O> FocusListener focusOnSearchValue(EventDataSource<O> eventDataSource);
}
