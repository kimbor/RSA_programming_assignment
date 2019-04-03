import java.util.*;
import java.math.BigInteger;

class Main {
//  public static final long[] numbersToTest = {-10l, 0l, 1l, 2l, 3l, 4l, 9l, 12l, 3169l, 10000l};
//  public static final long[] numbersToTest = {5915587277l};
//  public static final long[] numbersToTest = {3169l};
  public static final long[] numbersToTest = {15, 26, 1, 5, 3, 7, 5, 6, -2, -3, 6, 9, 342952340l, 4230493243l};
  
  public static void main(String[] args) {
    System.out.println(); // for prettier output, make sure we're starting on a new line
    // for (int i=0; i<numbersToTest.length; i++) {
    //   long startTime = System.nanoTime();
    //   System.out.print("Is " + numbersToTest[i] + " prime? " + isPrimeTrialAndError(numbersToTest[i]));
    //   long endTime = System.nanoTime();
    //   System.out.println("  Elapsed time: " + (endTime - startTime));
    // }

    // System.out.println();

    // for (int i=0; i<numbersToTest.length; i++) {
    //   long startTime = System.nanoTime();
    //   System.out.print("Is " + numbersToTest[i] + " prime? " + isPrime(numbersToTest[i]));
    //   long endTime = System.nanoTime();
    //   System.out.println("  Elapsed time: " + (endTime - startTime));
    // }

    // for (int i=0; i<numbersToTest.length; i++) {
    //   long startTime = System.nanoTime();
    //   System.out.print("Prime factors of " + numbersToTest[i] + " are: " + getPrimeFactors(numbersToTest[i]));
    //   long endTime = System.nanoTime();
    //   System.out.println("  Elapsed time: " + (endTime - startTime));
    // }

    // for (int i=0; i<numbersToTest.length-1; i+=2) {
    //   long startTime = System.nanoTime();
    //   System.out.print("Highest common factor of (" + numbersToTest[i] + ", " + numbersToTest[i+1] + ") = " + getHighestCommonFactor(numbersToTest[i], numbersToTest[i+1]));
    //   long endTime = System.nanoTime();
    //   System.out.println("  Elapsed time: " + (endTime - startTime));
    // }

    // for (int i=0; i<numbersToTest.length-1; i+=2) {
    //   long startTime = System.nanoTime();
    //   System.out.print("Inverse of (" + numbersToTest[i] + " mod " + numbersToTest[i+1] + ") = " + getModularInverse(numbersToTest[i], numbersToTest[i+1]));
    //   long endTime = System.nanoTime();
    //   System.out.println("  Elapsed time: " + (endTime - startTime));
    // }

    long[] numbersToTest = {31, 2773, 17, 157};

    for (int i=0; i<numbersToTest.length-1; i+=4) {
      long startTime = System.nanoTime();
      long cypherText = rsaEncrypt(numbersToTest[i], numbersToTest[i+1], (int)numbersToTest[i+2]);
      System.out.print("Cyphertext of " + numbersToTest[i] + " using public key (" + numbersToTest[i+1] + ", " + numbersToTest[i+2] + ") = " + cypherText);
      long endTime = System.nanoTime();
      System.out.println("  Elapsed time: " + (endTime - startTime));

      startTime = System.nanoTime();
      System.out.print("Decryption of " + cypherText + " using public key (" + numbersToTest[i+1] + ") and private key " + numbersToTest[i+3] + " = " + rsaDecryptWithPrivateKey(cypherText, numbersToTest[i+1], numbersToTest[i+3]));
      endTime = System.nanoTime();
      System.out.println("  Elapsed time: " + (endTime - startTime));

      startTime = System.nanoTime();
      System.out.print("Decryption of " + cypherText + " using public key (" + numbersToTest[i+1] + ", " + numbersToTest[i+2] + ") = " + rsaDecryptWithPublicKey(cypherText, numbersToTest[i+1], numbersToTest[i+2]));
      endTime = System.nanoTime();
      System.out.println("  Elapsed time: " + (endTime - startTime));
    }
  }

/**
Determine whether an integer is prime by doing an exhaustive search 
for any factors of the integer between 2 and one less than the integer.

Question 1
*/
  public static boolean isPrimeTrialAndError(long num) {
    // negative numbers, 0, and 1 are not considered prime by definition
    if (num <= 1)
      return false;

    // iterate from 2 to one less than the number to test
    // check to see if the number is divisible by each of these potential factors
    for (long potentialFactor=2; potentialFactor<num; potentialFactor++) {
//      System.out.println("Checking " + potentialFactor + " mod " + num);
      if (num % potentialFactor == 0) // if num modulo potentialFactor is zero
        return false;                 // then potentialFactor is an actual factor and this num is not prime
    }

    // no factors have been found, so this number must be prime
    return true;
  }

/**
Improves the efficiency of the isPrimeTrialAndError function in two ways:
-only check potential factors up to the square root of the number
-only check potential factors that are themselves prime
-this method is based on the Sieve of Erastothenes method

Question 2
*/
    public static boolean isPrime(long num) {
        // negative numbers, 0, and 1 are not considered prime by definition
        if (num <= 1)
          return false;

        List<Long> knownPrimes = new ArrayList();
        for (long potentialFactor=2; (potentialFactor*potentialFactor)<=num; potentialFactor++) {
          if (isPrimeHelper(potentialFactor, knownPrimes)) {
            knownPrimes.add(potentialFactor);
            if (num % potentialFactor == 0)
                return false;
          }
        }
        return true;
    }

  // num must be an integer greater than 1
  // knownPrimes must contain all primes up to the square root of num
  private static boolean isPrimeHelper(long num, List<Long> knownPrimes) {
    //System.out.println("calling isPrimeImprovedHelper(" + num + ", " + knownPrimes);    
    for (long primeFactor : knownPrimes) {
        if (num % primeFactor == 0)
            return false;
        if (primeFactor * primeFactor > num)
            return true;
    }
    return true;
    }
    
    // Question 3
    public static List<Long> getPrimeFactors(long num) {
        List<Long> knownFactors = new ArrayList();

        // negative numbers, 0, and 1 are not considered composite by definition
        if (num <= 1)
          return knownFactors;

        long curNum = num;
        long factor;
        while ( (factor=getFirstPrimeFactor(curNum)) != 0) {
            knownFactors.add(factor);
            curNum = curNum / factor;
        }
        knownFactors.add(curNum);
            
        return knownFactors;
    }
    
    private static long getFirstPrimeFactor(long num) {
        for (long potentialFactor = 2; (potentialFactor*potentialFactor)<= num; potentialFactor++) {
            if (isPrime(potentialFactor) && (num % potentialFactor == 0))
                return potentialFactor;
        }
        return 0;
    }
    
    // Question 4
    public static long getHighestCommonFactor(long num1, long num2) {
        if (num1 < 1 || num2 < 1)
            return 0;
            
        long x = num1;
        long y = num2;
        
        while (y != 0) {
            long r = x % y;
            x = y;
            y = r;
        }
        return x;
    }
    
    // Question 5
    public static long getModularInverse(long num, long modulus) {
        // num and modulus must be co-prime
        if (getHighestCommonFactor(num, modulus) != 1)
            return 0;
            
        long r0 = modulus;
        long r1 = num;
        long t0 = 0l;
        long t1 = 1l;
        
        long inverseMod = t1;
        long remainder = r1;

        while (remainder != 1l) {
          long quotient = r0 / r1; // truncated to whole number
//System.out.println("\tquotient of " + r0 + "/" + r1 + "=" + quotient);          
          remainder = r0 % r1;
//System.out.println("\tremainder of " + r0 + "%" + r1 + "=" + remainder);
          inverseMod = t0 - (quotient * t1);
//System.out.println("\tinverseMod of " + t0 + "-(" + quotient + "*" + t1 + ")=" + inverseMod);

          r0 = r1;
          r1 = remainder;
          t0 = t1;
          t1 = inverseMod;
        } 
        
        return (inverseMod >= 0 ? inverseMod : inverseMod + modulus);
    }

    // Question 6
  public static long rsaEncrypt(long message, long publicKeyN, long publicKeyE) {
    // we use BigInteger arithmetic here in order to avoid rounding during some of the math operations
    BigInteger messageBI = BigInteger.valueOf(message);
    BigInteger publicKeyNBI = BigInteger.valueOf(publicKeyN);
    // System.out.println("\t" + message + "^" + publicKeyE + "=" + messageBI.pow(publicKeyE));
    // System.out.println("\t" + message + "^" + publicKeyE + " mod " + publicKeyN + "=" + messageBI.pow(publicKeyE).mod(publicKeyNBI));
    return messageBI.pow((int) publicKeyE).mod(publicKeyNBI).longValue();
  }

  public static long rsaDecryptWithPrivateKey(long cypherText, long publicKeyN, long privateKeyD) {
    BigInteger cypherBI = BigInteger.valueOf(cypherText);
    BigInteger publicKeyNBI = BigInteger.valueOf(publicKeyN);

    return cypherBI.pow((int) privateKeyD).mod(publicKeyNBI).longValue();
  }

  public static long rsaDecryptWithPublicKey(long cypherText, long publicKeyN, long publicKeyE) {
    return rsaDecryptWithPrivateKey(cypherText, publicKeyN, getPrivateKeyD(publicKeyN, publicKeyE));
  }

  public static long getPrivateKeyD(long publicKeyN, long publicKeyE) {
    List<Long> primeFactors = getPrimeFactors(publicKeyN);
    if (primeFactors.size() != 2)
      throw new IllegalArgumentException("publicKeyN " + publicKeyN + " must be a product of 2 primes");

    long p = primeFactors.get(0).longValue();
    long q = primeFactors.get(1).longValue();
    return getModularInverse(publicKeyE, (p-1)*(q-1));
  }
}

