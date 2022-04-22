package ui.specialCharacters;

import converters.VariableConverter;
import ui.model.DefaultButton;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class SpecialCharactersPanel extends JPanel {
    private static final String DEGREE_CELSIUS = "\u2103";
    private static final String DEGREE_OF = "X\u207F";
    private static final String ALPHA = "\u03B1";
    private static final String BETA = "\u03B2";
    private static final String GAMMA = "\u03B3";
    private static final String DELTA_UP = "\u0394";
    private static final String DELTA_DOWN = "\u03B4";
    private static final String OMEGA_UP = "\u03A9";
    private static final String OMEGA_DOWN = "\u03C9";
    private static final String THETA = "\u03B8";
    private static final String LAMBDA = "\u03BB";
    private static final String MU = "\u03BC";
    private static final String PI = "\u03C0";
    private static final String FI = "\u03C6";

    private boolean crutch = false;

    private static final String SPECIAL_CHARACTERS = "Спеціальні символи";
    private static final String INSERT = "Вставити";
    private static final String BACK = "Назад";
    private static final String DEGREE = "Степінь";

    public static final int MODE_MAIN = 0;
    public static final int MODE_DEGREE = 1;

    private JButton degreeCelsius;
    private JButton alpha, beta, gamma, deltaUp, deltaDown, omegaUp, omegaDown, theta, lambda, mu, pi, fi;
    private JButton degreeOf;
    private JButton insert, back;
    private JTextField degreeOfText, fieldForInsert, buffer;

    public SpecialCharactersPanel(){
        super(new GridBagLayout());

        this.createElements();
        this.setReactions();
        this.build(MODE_MAIN);
    }

    private void createElements(){
        this.degreeCelsius = new DefaultButton(DEGREE_CELSIUS);

        this.alpha = new DefaultButton(ALPHA);
        this.beta = new DefaultButton(BETA);
        this.gamma = new DefaultButton(GAMMA);
        this.deltaUp = new DefaultButton(DELTA_UP);
        this.deltaDown = new DefaultButton(DELTA_DOWN);
        this.omegaUp = new DefaultButton(OMEGA_UP);
        this.omegaDown = new DefaultButton(OMEGA_DOWN);
        this.theta = new DefaultButton(THETA);
        this.lambda = new DefaultButton(LAMBDA);
        this.mu = new DefaultButton(MU);
        this.pi = new DefaultButton(PI);
        this.fi = new DefaultButton(FI);

        this.degreeOf = new DefaultButton(DEGREE_OF);

        this.insert = new DefaultButton(INSERT);
        this.insert.setEnabled(false);
        this.back = new DefaultButton(BACK);

        this.degreeOfText = new JTextField(5);
        this.degreeOfText.setToolTipText(DEGREE);
    }

    private void setReactions(){
        this.degreeOf.addActionListener(clickDegree);

        this.back.addActionListener(clickBack);
        this.insert.addActionListener(clickInsert);

        this.degreeCelsius.addActionListener(clickPaste);
        this.alpha.addActionListener(clickPaste);
        this.beta.addActionListener(clickPaste);
        this.gamma.addActionListener(clickPaste);
        this.deltaUp.addActionListener(clickPaste);
        this.deltaDown.addActionListener(clickPaste);
        this.omegaUp.addActionListener(clickPaste);
        this.omegaDown.addActionListener(clickPaste);
        this.theta.addActionListener(clickPaste);
        this.lambda.addActionListener(clickPaste);
        this.mu.addActionListener(clickPaste);
        this.pi.addActionListener(clickPaste);
        this.fi.addActionListener(clickPaste);

        this.degreeOfText.getDocument().addDocumentListener(changeDegree);

        this.degreeOfText.addFocusListener(degreeFocus);
    }

    private void build(int mode){
        this.setBackground(Color.WHITE);

        this.removeAll();
        if (mode == MODE_DEGREE) {
            this.buffer = fieldForInsert;
            this.add(degreeOfText, new Cell(0, 0, 2));
            this.add(back, new Cell(0, 1, 1));
            this.add(insert, new Cell(1, 1, 1));
        } else {
            this.add(degreeCelsius, new Cell(0, 0));

            this.add(alpha, new Cell(0,1));
            this.add(beta, new Cell(1,1));
            this.add(gamma, new Cell(2,1));
            this.add(deltaUp, new Cell(3,1));
            this.add(deltaDown, new Cell(0,2));
            this.add(omegaUp, new Cell(1,2));
            this.add(omegaDown, new Cell(2,2));
            this.add(theta, new Cell(3,2));
            this.add(lambda, new Cell(0,3));
            this.add(mu, new Cell(1,3));
            this.add(pi, new Cell(2,3));
            this.add(fi, new Cell(3,3));

            this.add(degreeOf, new Cell(0, 4));
        }
        TitledBorder border = BorderFactory.createTitledBorder(SPECIAL_CHARACTERS);
        border.setTitleJustification(TitledBorder.CENTER);
        this.setBorder(border);
        this.revalidate();
    }

    public void setFieldForInsert(JTextField textField){
        if (this.buffer == null){
            this.fieldForInsert = textField;
        }else {
            if (textField == null){
                build(MODE_MAIN);
            }
            this.fieldForInsert = textField;
            if (crutch) {
                this.buffer = textField;
            }else {
                this.crutch = true;
            }
        }
    }

    private final ActionListener clickDegree = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            build(MODE_DEGREE);
            degreeOfText.requestFocus();
        }
    };

    private final ActionListener clickBack = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            build(MODE_MAIN);
            if (buffer != null) buffer.requestFocus();
            buffer = null;
            crutch = false;
        }
    };

    private final ActionListener clickPaste = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            final JButton button = (JButton) e.getSource();
            if (fieldForInsert != null) {
                fieldForInsert.setCaretPosition(fieldForInsert.getDocument().getLength());
                fieldForInsert.replaceSelection(button.getText());
                fieldForInsert.requestFocus();
            }
        }
    };

    private final ActionListener clickInsert = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (buffer != null) {
                buffer.setCaretPosition(buffer.getDocument().getLength());
                buffer.replaceSelection(VariableConverter.superscript(degreeOfText.getText()));
                build(MODE_MAIN);
                buffer.requestFocus();
                buffer = null;
                crutch = false;
            }
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

    private final FocusListener degreeFocus = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            degreeOfText.selectAll();
        }
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
