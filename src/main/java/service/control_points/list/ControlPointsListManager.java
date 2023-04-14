package service.control_points.list;

public interface ControlPointsListManager {
    void showAllControlPointsInTable();
    void showSortedControlPointsInTable();
    void shutdownService();
    void removeControlPoints();
    void showControlPointsDetails();
    void addControlPoints();
    void updateControlPointsList();
}
