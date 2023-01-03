package ui.model;

import org.apache.poi.ss.usermodel.Cell;

import java.awt.*;

/**
 * Builder for java.awt.GridBagConstrains
 * @see GridBagConstraints
 */
public class CellBuilder {

    /**
     * @see GridBagConstraints#NONE
     */
    public static final int NONE = 0;

    /**
     * @see GridBagConstraints#BOTH
     */
    public static final int BOTH = 1;

    /**
     * @see GridBagConstraints#HORIZONTAL
     */
    public static final int HORIZONTAL = 2;

    /**
     * @see GridBagConstraints#VERTICAL
     */
    public static final int VERTICAL = 3;

    private final GridBagConstraints cell;

    public CellBuilder() {
        cell = new GridBagConstraints();
        cell.weightx = 1.0;
        cell.fill = BOTH;
    }

    public CellBuilder x(int x) {
        cell.gridx = x;
        return this;
    }

    public CellBuilder y(int y) {
        cell.gridy = y;
        return this;
    }

    public CellBuilder coordinates(int x, int y) {
        cell.gridx = x;
        cell.gridy = y;
        return this;
    }

    public CellBuilder fill(int fill) {
        cell.fill = fill;
        return this;
    }

    public CellBuilder weightX(double weight) {
        cell.weightx = weight;
        return this;
    }

    public CellBuilder weightY(double weight) {
        cell.weighty = weight;
        return this;
    }

    public CellBuilder weight(double x, double y) {
        cell.weightx = x;
        cell.weighty = y;
        return this;
    }

    public CellBuilder width(int width) {
        cell.gridwidth = width;
        return this;
    }

    public CellBuilder height(int height) {
        cell.gridheight = height;
        return this;
    }

    public CellBuilder size(int width, int height) {
        cell.gridwidth = width;
        cell.gridheight = height;
        return this;
    }

    public GridBagConstraints create() {
        return cell;
    }
}
