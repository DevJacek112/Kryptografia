package pl.kryptografia.elgamal;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class ElGamal {

    public static BigInteger generateP(){
        int bitLength = 2048;
        int certainty = 100;
        BigInteger prime = new BigInteger(bitLength, certainty, new SecureRandom());

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
        BigInteger h = g.modPow(a, p);
        return h;
    }




}
