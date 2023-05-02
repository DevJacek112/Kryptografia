package pl.kryptografia.elgamal;

import java.math.BigInteger;
import java.security.SecureRandom;

public class ElGamal {

    public static BigInteger generateP(){
        int bitLength = 2048;
        int certainty = 100;
        BigInteger prime = new BigInteger(bitLength, certainty, new SecureRandom());

        return prime;
    }




}
