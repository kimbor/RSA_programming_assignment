import java.util.*;

class Main {
  public static final long[] numbersToTest = {-10l, 0l, 1l, 2l, 3l, 4l, 9l, 11l, 3169l, 10000l};
//  public static final long[] numbersToTest = {5915587277l};
//  public static final long[] numbersToTest = {3169l};
  
  public static void main(String[] args) {
    for (int i=0; i<numbersToTest.length; i++) {
      long startTime = System.nanoTime();
      System.out.print("Is " + numbersToTest[i] + " prime? " + isPrimeTrialAndError(numbersToTest[i]));
      long endTime = System.nanoTime();
      System.out.println("  Elapsed time: " + (endTime - startTime));
    }

    System.out.println();

    for (int i=0; i<numbersToTest.length; i++) {
      long startTime = System.nanoTime();
      System.out.print("Is " + numbersToTest[i] + " prime? " + isPrimeImproved(numbersToTest[i]));
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
    public static boolean isPrimeImproved(long num) {
        // negative numbers, 0, and 1 are not considered prime by definition
        if (num <= 1)
          return false;

        List<Long> knownPrimes = new ArrayList();
        for (long potentialFactor=2; (potentialFactor*potentialFactor)<=num; potentialFactor++) {
          if (isPrimeImprovedHelper(potentialFactor, knownPrimes)) {
            knownPrimes.add(potentialFactor);
            if (num % potentialFactor == 0)
                return false;
          }
        }
        return true;
    }

  // num must be an integer greater than 1
  // knownPrimes must contain all primes up to the square root of num
  private static boolean isPrimeImprovedHelper(long num, List<Long> knownPrimes) {
    //System.out.println("calling isPrimeImprovedHelper(" + num + ", " + knownPrimes);    
    for (long primeFactor : knownPrimes) {
        if (num % primeFactor == 0)
            return false;
        if (primeFactor * primeFactor > num)
            return true;
    }
    return true;
    }
}
