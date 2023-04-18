package com.example.system.integration.reader;

import com.example.system.integration.entity.Laptop;
import com.example.system.integration.repository.LaptopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.swing.table.DefaultTableModel;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseReader {
    private final LaptopRepository laptopRepository;

    String[][] rows = new String[24][15];

    public String[][] readFromDatabase() {
        List<Laptop> laptops = laptopRepository.findAll();
        rows = new String[laptops.size()][15];

        for (int i = 0; i < laptops.size(); i++) {
            Laptop laptop = laptops.get(i);
            rows[i][0] = laptop.getManufacturer();
            rows[i][1] = laptop.getSize();
            rows[i][2] = laptop.getResolution();
            rows[i][3] = laptop.getMatrixType();
            rows[i][4] = laptop.getIsTouch();
            rows[i][5] = laptop.getProcessor();
            rows[i][6] = laptop.getPhysicalCores();
            rows[i][7] = laptop.getClockSpeed();
            rows[i][8] = laptop.getRam();
            rows[i][9] = laptop.getDiscCapacity();
            rows[i][10] = laptop.getDiscType();
            rows[i][11] = laptop.getGraphicCard();
            rows[i][12] = laptop.getGraphicCardMemory();
            rows[i][13] = laptop.getOperationSystem();
            rows[i][14] = laptop.getDiscReader();
        }

        return rows;
    }

    public void exportDataToDatabase(DefaultTableModel tableModel) {
        laptopRepository.deleteAll();

        for (int row = 0; row < tableModel.getRowCount(); row++) {
            Laptop laptop = new Laptop();
            laptop.setManufacturer(getStringValue(tableModel.getValueAt(row, 0)));
            laptop.setSize(getStringValue(tableModel.getValueAt(row, 1)));
            laptop.setResolution(getStringValue(tableModel.getValueAt(row, 2)));
            laptop.setMatrixType(getStringValue(tableModel.getValueAt(row, 3)));
            laptop.setIsTouch(getStringValue(tableModel.getValueAt(row, 4)));
            laptop.setProcessor(getStringValue(tableModel.getValueAt(row, 5)));
            laptop.setPhysicalCores(getStringValue(tableModel.getValueAt(row, 6)));
            laptop.setClockSpeed(getStringValue(tableModel.getValueAt(row, 7)));
            laptop.setRam(getStringValue(tableModel.getValueAt(row, 8)));
            laptop.setDiscCapacity(getStringValue(tableModel.getValueAt(row, 9)));
            laptop.setDiscType(getStringValue(tableModel.getValueAt(row, 10)));
            laptop.setGraphicCard(getStringValue(tableModel.getValueAt(row, 11)));
            laptop.setGraphicCardMemory(getStringValue(tableModel.getValueAt(row, 12)));
            laptop.setOperationSystem(getStringValue(tableModel.getValueAt(row, 13)));
            laptop.setDiscReader(getStringValue(tableModel.getValueAt(row, 14)));
            laptopRepository.save(laptop);
        }
    }

    private String getStringValue(Object value) {
        if (value == null) { return ""; }
        return String.valueOf(value);
    }
}

