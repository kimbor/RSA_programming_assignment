import java.util.*;

class Main {
//  public static final long[] numbersToTest = {-10, 0, 1, 2, 3, 4, 9, 11, 3169, 10000};
  public static final long[] numbersToTest = {5915587277l};

  public static void main(String[] args) {
    long startTime;
    long endTime;

    System.out.println();

    // for (int i=0; i<numbersToTest.length; i++) {
    //   startTime = System.nanoTime();
    //   System.out.println("Is " + numbersToTest[i] + " prime? " + isPrimeTrialAndError(numbersToTest[i]));
    //   endTime = System.nanoTime();
    //   System.out.println("Elapsed time: " + (endTime - startTime));
    //   System.out.println();
    // }

    for (int i=0; i<numbersToTest.length; i++) {
      startTime = System.nanoTime();
      System.out.println("Is " + numbersToTest[i] + " prime? " + isPrimeImproved(numbersToTest[i], new TreeSet()));
      endTime = System.nanoTime();
      System.out.println("Elapsed time: " + (endTime - startTime));
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
  // assumption is that all primes up to the square root of num are in the knownPrimes list
  private static boolean isPrimeImproved(long num, SortedSet<Long> knownPrimes) {
System.out.println("calling isPrimeImproved(" + num + ", " + knownPrimes);    
    // negative numbers, 0, and 1 are not considered prime by definition
    if (num <= 1)
      return false;

    long highestKnownPrime = (knownPrimes.size() == 0 ? 2 : knownPrimes.last());
    if (num < highestKnownPrime)
      return false;

    // iterate from 2 to the square root of our number
    for (long potentialFactor=2; (potentialFactor*potentialFactor)<=num; potentialFactor++) {
      if (!knownPrimes.contains(potentialFactor) && isPrimeImproved(potentialFactor, knownPrimes)) {
//        System.out.println("Adding " + potentialFactor + " to list of known primes");
        knownPrimes.add(potentialFactor);
//        System.out.println("Known primes: " + knownPrimes);
      }

      if (knownPrimes.contains(potentialFactor)) {
        System.out.println("Checking " + num + " modulo " + potentialFactor);
        if (num % potentialFactor == 0)
          return false;
      }
    }
    return true;


  //     for (int i=0; i < knownPrimes.size(); i++) {
  //       int curPrime = knownPrimes.get(i);

  //       System.out.println("Checking " + potentialFactor + " mod " + curPrime);
  //       if ((curPrime * curPrime > potentialFactor) || (potentialFactor % curPrime == 0)) {
  //         isPotentialFactorPrime = false;
  //         continue;   // hop out of this for loop
  //       }
  //     }
  //     if (isPotentialFactorPrime) {
  //       knownPrimes.add(potentialFactor);
  //       System.out.println("Adding " + potentialFactor + " to primes list");
  //     }
  //   if (num % potentialFactor == 0) // if num modulo potentialFactor is zero
  //     return false;                 // then potentialFactor is an actual factor and this num is not prime      
  //   // no factors have been found, so this number must be prime
  //   return true;
  }
}
