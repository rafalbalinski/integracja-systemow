package com.example.system.integration.table;

import com.example.system.integration.reader.DatabaseReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TableWindowConfiguration {

    static String file = "src/main/resources/assets/laptop";
    static String[] headers = {"Producent", "Przekątna ekranu", "Rozdzielczość ekranu", "Rodzaj powierzchni ekranu", "Czy ekran jest dotykowy", "Procesor", "Liczba rdzeni fizycznych", "Prędkość taktowania MHz", "Wielkość pamięci RAM", "Pojemność dysku", "Rodzaj dysku", "Układ graficzny", "Pamięć układu graficznego", "System operacyjny", "Napęd fizyczny"};
    static String[] rowValidators = {"[a-zA-Z]", "\\d+(\\.\\d+)?\"", "^\\d{2,4}x\\d{2,4}$", "^(matowa|blyszczaca)$", "^(tak|nie)$", ".", "^(1|2|4|8|16|32|64|128)$", "^\\d+$", "^\\d+(?:\\.\\d+)?[MG]B$", "^\\d+(?:\\.\\d+)?[MG]B$", "^(SSD|HDD)$", ".", "^\\d+(?:\\.\\d+)?[MG]B$", ".", "."};

    @Bean
    public TableWindow tableWindow(final DatabaseReader databaseReader) {
        final TableWindow tableWindow = new TableWindow(headers, rowValidators, file, databaseReader);
        tableWindow.showWindow();
        return tableWindow;
    }
}
