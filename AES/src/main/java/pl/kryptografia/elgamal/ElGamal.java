package pl.kryptografia.elgamal;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class ElGamal {

    private static BigInteger p;
    private static BigInteger g;
    private static BigInteger h;

    public static BigInteger generateP(){
        int bitLength = 2048;
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

        //System.out.println("The random BigInteger = \n"+res);
        //System.out.println("max is = \n"+max);
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

        //System.out.println("The random BigInteger = \n"+res);
        //System.out.println("max is = \n"+max);

        return res;
    }

    public static BigInteger calculateH(BigInteger g, BigInteger a, BigInteger p){
        BigInteger pom = g.modPow(a, p);
        h = pom;
        return pom;
    }

    public static BigInteger[] podpis(String wiadomosc, BigInteger kluczPrywatnyA){
        BigInteger s1, s2, r, rPrim, hasz;
        BigInteger[] podpis = new BigInteger[2];
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        messageDigest.update(wiadomosc.getBytes());
        hasz = new BigInteger(1, messageDigest.digest());
        Random rand = new Random();
        BigInteger pMinusJeden = p.subtract(BigInteger.ONE);
        do {
            r = new BigInteger(pMinusJeden.bitLength(), rand); // losowa liczba o takiej samej długości w bitach jak pMinusJeden
        } while (r.compareTo(BigInteger.ZERO) == 0 || r.compareTo(pMinusJeden) >= 0 || !r.gcd(pMinusJeden).equals(BigInteger.ONE)); // jeśli wynik jest zerem, większy niż pMinusJeden lub nie jest względnie pierwszy z pMinusJeden, to losuj dalej
        s1 = g.modPow(r, p);
        rPrim = r.modInverse(pMinusJeden);
        s2 = ((hasz.subtract(kluczPrywatnyA.multiply(s1))).multiply(rPrim)).mod(pMinusJeden);
        podpis[0] = s1;
        podpis[1] = s2;
        return podpis;
    }




}
