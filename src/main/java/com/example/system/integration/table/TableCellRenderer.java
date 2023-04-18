package com.example.system.integration.table;

import com.example.system.integration.enums.TableRowState;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableCellRenderer extends DefaultTableCellRenderer {
    TableRowState[][] rowsStates;

    public TableCellRenderer(TableRowState[][] rowsStates) {
        this.rowsStates = rowsStates;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(rowsStates[row][column] == TableRowState.DUPLICATE){
            cellComponent.setBackground(Color.GREEN);
        } else if(rowsStates[row][column] == TableRowState.INVALID){
            cellComponent.setBackground(Color.RED);
        } else if (rowsStates[row][column] == TableRowState.DIRTY) {
            cellComponent.setBackground(Color.WHITE);
        } else {
            cellComponent.setBackground(Color.GRAY);
        }

        return cellComponent;
    }
}