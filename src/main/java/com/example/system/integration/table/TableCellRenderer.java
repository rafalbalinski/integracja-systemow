package com.example.system.integration.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableCellRenderer extends DefaultTableCellRenderer {
    int[][] rowsStates;

    public TableCellRenderer(int[][] rowsStates) {
        this.rowsStates = rowsStates;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if(rowsStates[row][column] == -1){
            cellComponent.setBackground(Color.RED);}
        else {
            cellComponent.setBackground(Color.WHITE);
        }

        return cellComponent;
    }
}