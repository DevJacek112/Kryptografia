package pl.kryptografia.elgamal;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class ElGamal {

    private static BigInteger p;
    private static BigInteger g;

    private static BigInteger A;

    private static BigInteger h;

    public static BigInteger[] podpisBufor;
    static int keyLen=512;

    public static void setG(BigInteger g) {
        ElGamal.g = g;
    }

    public static void setA(BigInteger a) {
        A = a;
    }

    public static void setH(BigInteger h) {
        ElGamal.h = h;
    }

    public static void setP(BigInteger p) {
        ElGamal.p = p;
    }

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
        max = max.subtract(new BigInteger("1"));

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
        messageDigest.update(wiadomosc);
        hasz = new BigInteger(1, messageDigest.digest());
        BigInteger pMinusJeden = p.subtract(BigInteger.ONE);
        r = BigInteger.probablePrime(keyLen,new Random());
        while(true)
            if (r.gcd(pMinusJeden).equals(BigInteger.ONE))break;
            else r=r.nextProbablePrime();
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

    public static void zapiszPodpisDoPliku(BigInteger[] podpis, String nazwaPliku) throws FileNotFoundException {
        try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(nazwaPliku))) {
            outputStream.writeInt(podpis.length);
            for (BigInteger bigInteger : podpis) {
                byte[] bajty = bigInteger.toByteArray();
                outputStream.writeInt(bajty.length);
                outputStream.write(bajty);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BigInteger[] wczytajPodpisZPliku(String nazwaPliku) throws FileNotFoundException {
        try (DataInputStream inputStream = new DataInputStream(new FileInputStream(nazwaPliku))) {
            int liczbaBajtow = inputStream.readInt();
            BigInteger[] podpis = new BigInteger[liczbaBajtow];
            for (int i = 0; i < liczbaBajtow; i++) {
                int dlugoscTablicy = inputStream.readInt();
                byte[] bajty = new byte[dlugoscTablicy];
                inputStream.readFully(bajty);
                podpis[i] = new BigInteger(bajty);
            }
            podpisBufor = podpis;
            return podpis;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
