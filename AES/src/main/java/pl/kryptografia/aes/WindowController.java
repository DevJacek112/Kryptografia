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

        byte[] zamienionyNaBajty = AES.SubBytes(tekstJawny.getText().getBytes(StandardCharsets.UTF_8));
        byte[][] podzielone = AES.podzielTablice(zamienionyNaBajty);


        System.out.println(Arrays.toString(zamienionyNaBajty));
        System.out.println(Arrays.deepToString(podzielone));

    }
}