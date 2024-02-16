import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;


public class HornersScheme extends JFrame {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    private Double[] coefficients;
    private JFileChooser fileChooser = null;
    private JMenuItem aboutMenuItem;
    private JMenuItem saveToTextMenuItem;
    private JMenuItem saveToGraphicsMenuItem;
    private JMenuItem searchValueMenuItem;
    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldStep;
    private Box hBoxResult;
    private GornerTableCellRenderer renderer = new
            GornerTableCellRenderer();
    private GornerTableModel data;
    protected JLabel aboutNameTF;

    public HornersScheme(Double[] coefficients) {
        super("Tabulating a polynomial on a segment using Horner's scheme");
        this.coefficients = coefficients;
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH)/2,(kit.getScreenSize().height - HEIGHT)/2);
        // Создать меню
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        // Добавить в меню пункт меню "Файл"
        JMenu aboutMenu = new JMenu("About");
        menuBar.add(aboutMenu);

        JMenu fileMenu = new JMenu("File");
        // Добавить его в главное меню
        menuBar.add(fileMenu);
        // Создать пункт меню "Таблица"
        JMenu tableMenu = new JMenu("Table");
        // Добавить его в главное меню
        menuBar.add(tableMenu);
        // Создать новое "действие" по сохранению в текстовый файл
        Action aboutAction = new AbstractAction("Author") {
            public void actionPerformed(ActionEvent event) {
                JDialog dialog = new JDialog(HornersScheme.this, "Author", true);
                dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                dialog.setSize(360, 360);

                aboutNameTF = new JLabel("Miroslav Kolos 6 group");

                Box box = Box.createVerticalBox();
                box.add(aboutNameTF);

                Box hbox = Box.createHorizontalBox();
                hbox.add(Box.createHorizontalStrut(20));
                hbox.add(box);

                dialog.getContentPane().add(hbox);

                dialog.setVisible(true);
            }
        };
        aboutMenuItem = aboutMenu.add(aboutAction);

        Action saveToTextAction = new AbstractAction("Save in .txt file") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(HornersScheme.this) == JFileChooser.APPROVE_OPTION)
                    saveToTextFile(fileChooser.getSelectedFile());
            }
        };
        saveToTextMenuItem = fileMenu.add(saveToTextAction);
        saveToTextMenuItem.setEnabled(false);
        Action saveToGraphicsAction = new AbstractAction ("Save data for graphics table") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(HornersScheme.this) == JFileChooser.APPROVE_OPTION)
                    saveToGraphicsFile(fileChooser.getSelectedFile());
            }
        };

        //saveToGraphicsFile(fileChooser.getSelectedFile());

        saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
        saveToGraphicsMenuItem.setEnabled(false);
        Action searchValueAction = new AbstractAction("Find the value of a polynomial") {
            public void actionPerformed(ActionEvent event) {
                String value = JOptionPane.showInputDialog(HornersScheme.this, "Enter a value", "Find value", JOptionPane.QUESTION_MESSAGE);
                renderer.setNeedle(value);
                getContentPane().repaint();
            }
        };
        searchValueMenuItem = tableMenu.add(searchValueAction);
        searchValueMenuItem.setEnabled(true);
        JLabel labelForFrom = new JLabel("X changes over the interval from:");

        textFieldFrom = new JTextField("0.0", 10);

        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());

        JLabel labelForTo = new JLabel("until:");
        textFieldTo = new JTextField("1.0", 10);

        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
        JLabel labelForStep = new JLabel("with interval of");
        // со значением по умолчанию 1.0
        textFieldStep = new JTextField("0.1", 10);
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());
        Box hboxRange = Box.createHorizontalBox();
        hboxRange.setBorder(BorderFactory.createBevelBorder(1));
        hboxRange.add(Box.createHorizontalGlue());
        hboxRange.add(labelForFrom);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldFrom);
        hboxRange.add(Box.createHorizontalStrut(20));
        hboxRange.add(labelForTo);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldTo);
        hboxRange.add(Box.createHorizontalStrut(20));
        hboxRange.add(labelForStep);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldStep);
        hboxRange.add(Box.createHorizontalGlue());
        hboxRange.setPreferredSize(new Dimension((int) hboxRange.getMaximumSize().getWidth(), (int) (hboxRange.getMinimumSize().getHeight()) * 2));
        hboxRange.setPreferredSize(new Dimension((int) hboxRange.getMaximumSize().getWidth(), (int) (hboxRange.getMinimumSize().getHeight()) * 2));
        getContentPane().add(hboxRange, BorderLayout.SOUTH);
        JButton buttonCalc = new JButton("Calculate");

        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    Double from = Double.parseDouble(textFieldFrom.getText());
                    Double to = Double.parseDouble(textFieldTo.getText());
                    Double step = Double.parseDouble(textFieldStep.getText());
                    data = new GornerTableModel(from, to, step, HornersScheme.this.coefficients);
                    JTable table = new JTable(data);
                    table.setDefaultRenderer(Double.class, renderer);
                    table.setRowHeight(30);
                    hBoxResult.removeAll();
                    hBoxResult.add(new JScrollPane(table));
                    getContentPane().validate();
                    saveToTextMenuItem.setEnabled(true);
                    saveToGraphicsMenuItem.setEnabled(true);
                }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(HornersScheme.this, "Error in floating point number format", "Incorrect number format", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JButton buttonReset = new JButton("Clear fields");
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                textFieldFrom.setText("0.0");
                textFieldTo.setText("1.0");
                textFieldStep.setText("0.1");
                hBoxResult.removeAll();
                hBoxResult.add(new JPanel());
                saveToTextMenuItem.setEnabled(false);
                saveToGraphicsMenuItem.setEnabled(false);
                getContentPane().validate();
            }
        });
        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.setPreferredSize(new Dimension((int) hboxButtons.getMaximumSize().getWidth(), (int) hboxButtons.getMinimumSize().getHeight() * 2));
        getContentPane().add(hboxButtons, BorderLayout.NORTH);
        hBoxResult = Box.createHorizontalBox();
        hBoxResult.add(new JPanel());
        getContentPane().add(hBoxResult, BorderLayout.CENTER);
    }



    protected void saveToGraphicsFile(File selectedFile)
    {
        try {

            DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile));
            for (int i = 0; i<data.getRowCount(); i++)
            {
                out.writeDouble((Double)data.getValueAt(i,0));
                out.writeDouble((Double)data.getValueAt(i,1));
            }
            out.close();
        }
        catch (Exception e) {
        }
    }
    protected void saveToTextFile(File selectedFile) { try {

        PrintStream out = new PrintStream(selectedFile);

        out.println("Results of polynomial tabulation using Horner's scheme");
        out.print("Polynomial: ");
        for (int i=0; i<coefficients.length; i++) {
            out.print(coefficients[i] + "*X^" + (coefficients.length-i-1));
            if (i!=coefficients.length-1) out.print(" + ");
        }
        out.println("");
        out.println("Interval from " + data.getFrom() + " until " +
                data.getTo() + " in increments " + data.getStep());
        out.println("====================================================");
        for (int i = 0; i<data.getRowCount(); i++) {
            out.println("Value at point " + data.getValueAt(i,0) + " equals " + data.getValueAt(i,1));
        }
        out.close();
    } catch (FileNotFoundException e) {
    } }

}

