package pl.kryptografia.elgamal;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;


public class ElGamalWindowController {

    @FXML
    private TextField kluczPrywatnya;

    @FXML
    private TextField kluczPublicznyg;

    @FXML
    private TextField kluczPublicznyh;

    @FXML
    private TextArea nazwaPliku;

    @FXML
    private TextArea tekstDoDeszyfrowania;

    @FXML
    private TextArea tekstJawny;

    @FXML
    private TextArea tekstWyjsciowyDeszyfrowanie;

    @FXML
    private TextArea tekstWyjsciowySzyfrowanie;

    @FXML
    protected void onGenerujButtonClick() {
        BigInteger p = ElGamal.generateP();
    }


}
