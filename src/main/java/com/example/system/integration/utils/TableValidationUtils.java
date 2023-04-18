package com.example.system.integration.utils;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TableValidationUtils {
    public static boolean cellWithPattern(String cell, String validator) {
        Pattern pattern = Pattern.compile(validator);
        Matcher matcher = pattern.matcher(cell);
        return matcher.find();
    }

    public static int[] rowsWithDuplications(DefaultTableModel tableModel) {
        List<Integer> duplicates = new ArrayList<>();
        Set<String> uniqueRows = new HashSet<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            StringBuilder rowStringBuilder = new StringBuilder();
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                Object value = tableModel.getValueAt(i, j);
                rowStringBuilder.append(value == null ? "" : value.toString());
                rowStringBuilder.append("\t");
            }
            String rowString = rowStringBuilder.toString();
            if (uniqueRows.contains(rowString)) {
                duplicates.add(i);
            } else {
                uniqueRows.add(rowString);
            }
        }
        int[] result = new int[duplicates.size()];
        for (int i = 0; i < duplicates.size(); i++) {
            result[i] = duplicates.get(i);
        }
        return result;
    }
}
