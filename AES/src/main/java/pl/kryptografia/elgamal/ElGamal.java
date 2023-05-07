package pl.kryptografia.elgamal;

import pl.kryptografia.aes.AES;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class ElGamal {

    private static BigInteger p;
    private static BigInteger g;

    private static BigInteger A;

    private static BigInteger h;

    public static BigInteger podpisBufor[];
    static int keyLen=512;

    public static BigInteger generateP(){
        int bitLength = 512;
        int certainty = 100;
        BigInteger prime = new BigInteger(bitLength, certainty, new SecureRandom());
        p = prime;
        return prime;
    }

    public static BigInteger findGInRange(BigInteger max){

        BigInteger min = new BigInteger("1");
        max = max.subtract(new BigInteger("1"));

        BigInteger bigInteger = max.subtract(min);
        Random randNum = new Random();
        int len = max.bitLength();
        BigInteger res = new BigInteger(len, randNum);
        if (res.compareTo(min) < 0)
            res = res.add(min);
        if (res.compareTo(bigInteger) >= 0)
            res = res.mod(bigInteger).add(min);

        g = res;
        return res;
    }

    public static BigInteger findAInRange(BigInteger max){

        BigInteger min = new BigInteger("1");
        max = max.subtract(new BigInteger("1")); //wykald mowi ze ma byc p - 1, chatbot, ze -2, zobaczymy ktory bedzie dzialac (mam nadzieje, ze ktorys bedzie), jesli zostanie -1 to mozna uzyc funkcji powyzej, zamiast dublowac kod

        BigInteger bigInteger = max.subtract(min);
        Random randNum = new Random();
        int len = max.bitLength();
        BigInteger res = new BigInteger(len, randNum);
        if (res.compareTo(min) < 0)
            res = res.add(min);
        if (res.compareTo(bigInteger) >= 0)
            res = res.mod(bigInteger).add(min);

        A = res;
        return res;
    }

    public static BigInteger calculateH(BigInteger g, BigInteger a, BigInteger p){
        BigInteger pom = g.modPow(a, p);
        h = pom;
        return pom;
    }

    public static BigInteger[] podpis(byte[] wiadomosc, BigInteger kluczPrywatnyA){
        BigInteger s1, s2, r, rPrim, hasz;
        BigInteger[] podpis = new BigInteger[2];
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
//        if (!czyPlik) messageDigest.update(wiadomosc.getBytes());
        messageDigest.update(wiadomosc);
        hasz = new BigInteger(1, messageDigest.digest());
        BigInteger pMinusJeden = p.subtract(BigInteger.ONE);
        r = BigInteger.probablePrime(keyLen,new Random());
        while(true)
            if (r.gcd(pMinusJeden).equals(BigInteger.ONE))break;
            else r=r.nextProbablePrime(); // jeśli wynik jest zerem, większy niż pMinusJeden lub nie jest względnie pierwszy z pMinusJeden, to losuj dalej
        s1 = g.modPow(r, p);
        rPrim = r.modInverse(pMinusJeden);
        s2 = ((hasz.subtract(kluczPrywatnyA.multiply(s1))).multiply(rPrim)).mod(pMinusJeden);
        podpis[0] = s1;
        podpis[1] = s2;
        podpisBufor = podpis;
        return podpis;
    }

    public static boolean weryfikacja(byte[] wiadomosc){
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        messageDigest.update(wiadomosc);
        BigInteger hash = new BigInteger(1, messageDigest.digest());
        BigInteger S1=podpisBufor[0];
        BigInteger S2=podpisBufor[1];
        BigInteger wynik1=g.modPow(hash, p);
        BigInteger wynik2=h.modPow(S1, p).multiply(S1.modPow(S2, p)).mod(p);
        if (wynik1.compareTo(wynik2) == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static byte[] pobierzPlikIZamienNaTabliceBajtow(String nazwaPliku) throws IOException {
        File file = new File(nazwaPliku);
        FileInputStream fis = new FileInputStream(file);
        byte[] bytesArray = new byte[(int) file.length()];
        fis.read(bytesArray);
        fis.close();
        return bytesArray;
    }

    public static void zapiszDoPliku(byte[] tablica, String nazwaPliku){
        try {
            FileOutputStream fos = new FileOutputStream(nazwaPliku);
            fos.write(tablica);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
