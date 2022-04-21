package ui.channelInfo.complexElements;

import converters.VariableConverter;
import model.Measurement;
import ui.model.DefaultButton;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SpecialCharactersPanel extends JPanel {
    private static final String SPECIAL_CHARACTERS = "Спеціальні символи";
    private static final String INSERT = "Вставити";
    private static final String BACK = "Назад";
    private static final String DEGREE = "Степінь";

    public static final int MODE_MAIN = 0;
    public static final int MODE_DEGREE = 1;

    private JButton degreeCelsius, degreeOf;
    private JButton insert, back;
    private JTextField degreeOfText, fieldForInsert, buffer;

    public SpecialCharactersPanel(){
        super(new GridBagLayout());

        this.createElements();
        this.setReactions();
        this.build(MODE_MAIN);
    }

    private void createElements(){
        this.degreeCelsius = new DefaultButton(Measurement.DEGREE_CELSIUS);
        this.degreeOf = new DefaultButton("X\u207F");
        this.insert = new DefaultButton(INSERT);
        this.insert.setEnabled(false);
        this.back = new DefaultButton(BACK);

        this.degreeOfText = new JTextField(5);
        this.degreeOfText.setToolTipText(DEGREE);
    }

    private void setReactions(){
        this.degreeOf.addActionListener(clickDegree);
        this.back.addActionListener(clickBack);
        this.degreeCelsius.addActionListener(clickPaste);
        this.insert.addActionListener(clickInsert);

        this.degreeOfText.getDocument().addDocumentListener(changeDegree);
    }

    private void build(int mode){
        this.removeAll();
        if (mode == MODE_DEGREE) {
            this.buffer = fieldForInsert;
            this.add(degreeOfText, new Cell(0, 0, 2));
            this.add(back, new Cell(0, 1, 1));
            this.add(insert, new Cell(1, 1, 1));
        } else {
            this.add(degreeCelsius, new Cell(0, 0));
            this.add(degreeOf, new Cell(1, 0));
        }
        TitledBorder border = BorderFactory.createTitledBorder(SPECIAL_CHARACTERS);
        border.setTitleJustification(TitledBorder.CENTER);
        this.setBorder(border);
        this.revalidate();
    }

    public void setFieldForInsert(JTextField textField){
        this.fieldForInsert = textField;
    }

    private final ActionListener clickDegree = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    build(MODE_DEGREE);
                    degreeOfText.requestFocus();
                }
            });
        }
    };

    private final ActionListener clickBack = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            build(MODE_MAIN);
            buffer.requestFocus();
        }
    };

    private final ActionListener clickPaste = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            final JButton button = (JButton) e.getSource();
            fieldForInsert.setCaretPosition(fieldForInsert.getDocument().getLength());
            fieldForInsert.replaceSelection(button.getText());
            fieldForInsert.requestFocus();
        }
    };

    private final ActionListener clickInsert = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            buffer.setCaretPosition(buffer.getDocument().getLength());
            buffer.replaceSelection(VariableConverter.superscript(degreeOfText.getText()));
            build(MODE_MAIN);
            buffer.requestFocus();
        }
    };

    private final DocumentListener changeDegree = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            insert.setEnabled(VariableConverter.isItInt(degreeOfText.getText()));
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            insert.setEnabled(VariableConverter.isItInt(degreeOfText.getText()));
        }

        @Override public void changedUpdate(DocumentEvent e) {}
    };

    private static class Cell extends GridBagConstraints {
        Cell(int x, int y){
            super();

            this.gridx = x;
            this.gridy = y;
        }

        Cell(int x, int y, int width){
            super();

            this.fill = BOTH;
            this.weightx = 1.0;
            this.insets = new Insets(5,0,5,0);

            this.gridx = x;
            this.gridy = y;
            this.gridwidth = width;
        }
    }
}
