package pl.kryptografia.aes;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class WindowController {

    @FXML
    private TextArea tekstJawny;

    @FXML
    protected void onSzyfrujButtonClick() {

        System.out.println(Arrays.toString(tekstJawny.getText().getBytes(StandardCharsets.UTF_8)));

        byte[][] podzielone = AES.podzielTablice(tekstJawny.getText().getBytes(StandardCharsets.UTF_8));

        System.out.println(Arrays.deepToString(podzielone));

        byte[][] zamienionyNaBajty = AES.SubBytes(podzielone);

        System.out.println(Arrays.deepToString(zamienionyNaBajty));

        byte[][] przetasowaneKolumny = AES.shiftRows(zamienionyNaBajty);

        System.out.println(Arrays.deepToString(przetasowaneKolumny));


    }
}