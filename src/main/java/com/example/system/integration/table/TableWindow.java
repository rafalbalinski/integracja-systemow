package com.example.system.integration.table;


import com.example.system.integration.enums.TableRowState;
import com.example.system.integration.reader.DatabaseReader;
import com.example.system.integration.reader.FileReader;
import com.example.system.integration.utils.TableValidationUtils;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
public class TableWindow extends JFrame {
    String[] headers;
    String[] rowValidators;
    FileReader fileReader;
    DatabaseReader databaseReader;
    TableRowState[][] rowsStates = new TableRowState[0][15];
    DefaultTableModel tableModel;
    JFrame frame;
    JTable table;
    JLabel label = new JLabel("Integracja Systemów - Rafał Baliński");
    JButton readTxtButton = new JButton("wczytaj dane z pliku TXT");
    JButton readXmlButton = new JButton("wczytaj dane z pliku XML");
    JButton readDatabaseButton = new JButton("wczytaj dane z bazy danych");
    JButton writeTxtButton = new JButton("zapisz dane do pliku TXT");
    JButton writeXmlButton = new JButton("zapisz dane do pliku XML");
    JButton writeDatabaseButton = new JButton("zapisz dane do bazy danych");
    JButton validateButton = new JButton("waliduj dane");

    public TableWindow(String[] headers, String[] rowValidators, String file, DatabaseReader databaseReader) {
        this.headers = headers;
        this.rowValidators = rowValidators;
        this.fileReader = new FileReader(headers, file);
        this.databaseReader = databaseReader;
    }

    public void showWindow() {
        frame = new JFrame("Laptop table");

        tableModel = new DefaultTableModel(new String[0][15], headers);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1700, 500));

        BorderLayout borderLayout = new BorderLayout();
        setLayout(borderLayout);
        frame.add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 10));
        buttonPanel.add(readTxtButton);
        buttonPanel.add(readXmlButton);
        buttonPanel.add(readDatabaseButton);
        buttonPanel.add(writeTxtButton);
        buttonPanel.add(writeXmlButton);
        buttonPanel.add(writeDatabaseButton);
        buttonPanel.add(validateButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(1700, 500);
        frame.setVisible(true);
        initButtonListeners();
    }

    private void validateTable() {
        rowsStates = new TableRowState[tableModel.getRowCount()][15];

        for (int duplicationIndex : TableValidationUtils.rowsWithDuplications(tableModel))
            for (int col = 0; col < tableModel.getColumnCount(); col++)
                rowsStates[duplicationIndex][col] = TableRowState.DUPLICATE;

        for (int row = 0; row < tableModel.getRowCount(); row++)
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                Object cell = tableModel.getValueAt(row, col);
                if (cell == null || !TableValidationUtils.cellWithPattern((String)cell, rowValidators[col])) {
                    rowsStates[row][col] = TableRowState.INVALID;
                }
            }

        for (int col = 0; col < headers.length; col++) {
            table.getColumnModel().getColumn(col).setCellRenderer(new TableCellRenderer(rowsStates));
        }

        frame.setSize(1701, 500);
        frame.setSize(1700, 500);
    }

    public void markRowAsDirty(int dirtyRowIndex) {
        Arrays.fill(rowsStates[dirtyRowIndex], TableRowState.DIRTY);
        for (int col = 0; col < headers.length; col++) {
            table.getColumnModel().getColumn(col).setCellRenderer(new TableCellRenderer(rowsStates));
        }
    }

    private void initButtonListeners() {
        AtomicBoolean isDataCurrentlyBeingRead = new AtomicBoolean(false);

        readTxtButton.addActionListener(e -> {
            isDataCurrentlyBeingRead.set(true);
            String[][] rows = fileReader.readTxtFile();
            for (String[] row : rows) tableModel.addRow(row);
            validateTable();
            isDataCurrentlyBeingRead.set(false);
        });

        readXmlButton.addActionListener(e -> {
            isDataCurrentlyBeingRead.set(true);
            String[][] rows = fileReader.readXMLFile();
            for (String[] row : rows) tableModel.addRow(row);
            validateTable();
            isDataCurrentlyBeingRead.set(false);
        });

        readDatabaseButton.addActionListener(e -> {
            isDataCurrentlyBeingRead.set(true);
            String[][] rows = databaseReader.readFromDatabase();
            for (String[] row : rows) tableModel.addRow(row);
            validateTable();
            isDataCurrentlyBeingRead.set(false);
        });

        writeTxtButton.addActionListener(e -> fileReader.exportDataToTxtFile(tableModel));

        writeXmlButton.addActionListener(e -> fileReader.exportDataToXmlFile(tableModel));

        writeDatabaseButton.addActionListener(e -> databaseReader.exportDataToDatabase(tableModel));

        validateButton.addActionListener(e -> validateTable());

        table.getModel().addTableModelListener(e -> {
            if (!isDataCurrentlyBeingRead.get()) {
                int row = e.getFirstRow();
                markRowAsDirty(row);
            }
        });
    }
}
