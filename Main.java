import java.util.*;

class Main {
//  public static final long[] numbersToTest = {-10l, 0l, 1l, 2l, 3l, 4l, 9l, 12l, 3169l, 10000l};
//  public static final long[] numbersToTest = {5915587277l};
//  public static final long[] numbersToTest = {3169l};
  public static final long[] numbersToTest = {499017086208l, 676126714752l, 5988737349l, 578354589l};
  
  public static void main(String[] args) {
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

    for (int i=0; i<numbersToTest.length-1; i+=2) {
      long startTime = System.nanoTime();
      System.out.print("Highest common factor of (" + numbersToTest[i] + ", " + numbersToTest[i+1] + ") = " + getHighestCommonFactor(numbersToTest[i], numbersToTest[i+1]));
      long endTime = System.nanoTime();
      System.out.println("  Elapsed time: " + (endTime - startTime));
    }
  }

/**
Determine whether an integer is prime by doing an exhaustive search 
for any factors of the integer between 2 and one less than the integer.
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
Improve the efficiency of the previous function in two ways:
-only check potential factors up to the square root of the number
-only check potential factors that are themselves prime
-this method is known as the "Sieve of Erastothenes"
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
    
    // public static long getModularInverse(long num, long modulus) {
    //     // num and modulus must be co-prime
    //     if (getHighestCommonFactor(num, modulus) != 1)
    //         return 0;
            
    //     long r0 = num;
    //     long r1 = modulus;
    //     long s0 = 1l;
    //     long s1 = 0l;
    //     long t0 = 0l;
    //     long t1 = 1l;
        
    //     do {
    //         long quotient = r0 mod r1;
    //     } while (r1 != 0);
        
    //     return s1;
    // }
}
