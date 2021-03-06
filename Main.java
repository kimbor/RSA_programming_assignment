
/******************************************************************************
Kim Rader
COMP-530 Cryptographic Systems Security
University of Nicosia
March 6, 2019
Assignment 1 - The RSA Algorithm
On GitHub as https://github.com/kimbor/RSA_programming_assignment
*******************************************************************************/

import java.util.*;
import java.math.BigInteger;

class Main {
	public static void main(String[] args) {
		// Numbers used in tests for Questions 1, 2, 3.
		// note: last character of 5915587277l is the letter 'l' to cast it
		// as a long since the number is too big to fit in an int
	    long[] numbersToTest = { -3, 2, 5, 9, 12, 3169, 10000, 5915587277l };

	    // Test for Question 1
		System.out.println("\nQuestion 1: Test for primeness using trial and error method.");
		for (int i = 0; i < numbersToTest.length; i++) {
			long startTime = System.nanoTime();
			System.out.print("Is " + numbersToTest[i] + " prime? ");
			System.out.print(isPrimeTrialAndError(numbersToTest[i]));
			long endTime = System.nanoTime();
			System.out.println("  Elapsed time: " + (endTime - startTime));
		}

		// Test for Question 2
		System.out.println("\nQuestion 2: Test for primeness using improved algorithm.");
		for (int i = 0; i < numbersToTest.length; i++) {
			long startTime = System.nanoTime();
			System.out.print("Is " + numbersToTest[i] + " prime? " + isPrime(numbersToTest[i]));
			long endTime = System.nanoTime();
			System.out.println("  Elapsed time: " + (endTime - startTime));
		}

		// Test for Question 3
		System.out.println("\nQuestion 3: Find prime factorization of any composite number.");
		for (int i = 0; i < numbersToTest.length; i++) {
			long startTime = System.nanoTime();
			System.out.print("Prime factorization of " + numbersToTest[i] + " is: ");
			System.out.print(getPrimeFactorization(numbersToTest[i]));
			long endTime = System.nanoTime();
			System.out.println("  Elapsed time: " + (endTime - startTime));
		}

		// Numbers used in tests for Questions 4 and 5
		// note: last character of larger numbers is the letter 'l' to cast them as longs
		long[][] pairsOfNumbersToTest = {
			{15, 26}, {1, 5}, {6, 9}, {342952340l, 4230493243l}, 
			{499017086208l, 676126714752l}, {5988737349l, 578354589l} };
		
		// Test for Question 4
		System.out.println("\nQuestion 4: Implement Euclid’s algorithm "
				+ " and compute hcf(499017086208, 676126714752), hcf(5988737349, 578354589)");
		for (int i = 0; i < pairsOfNumbersToTest.length; i++) {
			long startTime = System.nanoTime();
			System.out.print("Highest common factor of (" + pairsOfNumbersToTest[i][0] + ", "
					+ pairsOfNumbersToTest[i][1] + ") = ");
			System.out.print(getHighestCommonFactor(pairsOfNumbersToTest[i][0], pairsOfNumbersToTest[i][1]));
			long endTime = System.nanoTime();
			System.out.println("  Elapsed time: " + (endTime - startTime));
		}

		// Test for Question 5
		System.out.println(
				"\nQuestion 5: Explain how Euclid’s algorithm might help you to find multiplicative inverses and implement it. "
						+ "Solve for x the linear congruence 342952340x=1 mod 4230493243");
		for (int i = 0; i < pairsOfNumbersToTest.length; i++) {
			long startTime = System.nanoTime();
			System.out.print(pairsOfNumbersToTest[i][0] + "^(-1) mod " + pairsOfNumbersToTest[i][1]);
			long modInverse = getModularInverse(pairsOfNumbersToTest[i][0], pairsOfNumbersToTest[i][1]);
			if (modInverse == 0)
				System.out.print(" does not exist");
			else
				System.out.print(" = " + modInverse);
			long endTime = System.nanoTime();
			System.out.println("  Elapsed time: " + (endTime - startTime));
		}

		// data used in tests for Question 6
		class RsaKey {
			public long publicKeyN;
			public int publicKeyE;
			RsaKey(long publicKeyN, int publicKeyE) {this.publicKeyN=publicKeyN; this.publicKeyE=publicKeyE;}
		}
		RsaKey[] keysToTest = { new RsaKey(2773, 17), new RsaKey(937513, 638471) };
		long[] messagesToTest = {31, 32};
		
		// Test for Question 6
		System.out.println("\nQuestion 6: Write a program to convert an encrypted number c = m^e (mod n) "
				+ "into the original m =c^d (mod n), where 0 < m < n is some integer. ");
		System.out.println("Pick any plaintext you would like to encrypt it using the public key (937513, 638471) "
				+ "and then check correctness of the algorithm.");
		for (int i = 0; i < keysToTest.length; i++) {
			for (int j=0; j < messagesToTest.length; j++) {
				System.out.print("Cyphertext of " + messagesToTest[j] 
						+ " using public key (" + keysToTest[i].publicKeyN + ", " + keysToTest[i].publicKeyE + ") = ");
				long startTime = System.nanoTime();
				long cypherText = rsaEncrypt(messagesToTest[j], keysToTest[i].publicKeyN, keysToTest[i].publicKeyE);
				long endTime = System.nanoTime();
				System.out.print(cypherText);
				System.out.println(" Elapsed time: " + (endTime - startTime));
	
				startTime = System.nanoTime();
				System.out.print("Decryption of " + cypherText 
						+ " using public key (" + keysToTest[i].publicKeyN + ", " + keysToTest[i].publicKeyE + ") = ");
				endTime = System.nanoTime();
				System.out.print(rsaDecryptWithPublicKey(cypherText, keysToTest[i].publicKeyN, keysToTest[i].publicKeyE));
				System.out.println(" Elapsed time: " + (endTime - startTime));
			}
		}
	}

	/**
	 * Question 1 
	 * A number p is defined as prime if and only if it is divisible by itself (and 1). 
	 * Implement a method from scratch that allows to test primality of a number using trial and error method. 
	 */
	public static boolean isPrimeTrialAndError(long num) {
		// negative numbers, 0, and 1 are not considered prime by definition
		if (num <= 1)
			return false;

		// iterate from 2 to one less than the number to test
		// check to see if the number is divisible by each of these potential factors
		for (long potentialFactor = 2; potentialFactor < num; potentialFactor++) {
			if (num % potentialFactor == 0) // if num modulo potentialFactor is zero
				return false; // then potentialFactor is an actual factor and this num is not prime
		}

		// no factors have been found, so this number must be prime
		return true;
	}

	/**
	 * Question 2 
	 * The technique that you used in the previous question is a bit a slow 
	 * if you consider a big enough number (e.g. 10-digits). 
	 * Can you think of any other way to improve it? Implement the new one. 
	 * 
	 * This method improves the efficiency of the isPrimeTrialAndError function in two ways:
	 * -only check potential factors up to the square root of the number
	 * -only check potential factors that are themselves prime
	 * This method is based on the Sieve of Erastothenes algorithm.
	 */
	public static boolean isPrime(long num) {
		// negative numbers, 0, and 1 are not considered prime by definition
		if (num <= 1)
			return false;

		// iterate from 2 to the square root of num
		// keep track of primes we discover along the ways
		// only test if num is divisible by potential factors which are prime
		List<Long> knownPrimes = new ArrayList<Long>();
		for (long potentialFactor = 2; (potentialFactor * potentialFactor) <= num; potentialFactor++) {
			if (isPrimeHelper(potentialFactor, knownPrimes)) {
				knownPrimes.add(potentialFactor);
				if (num % potentialFactor == 0)		// % is modulus operator
					return false;
			}
		}
		return true;
	}

	/**
	 * Helper method for isPrime(long), written for Question 2.
	 * num must be an integer greater than 1
	 * knownPrimes must contain all primes up to the square root of num
	 */
	private static boolean isPrimeHelper(long num, List<Long> knownPrimes) {
		for (long primeFactor : knownPrimes) {
			if (num % primeFactor == 0)		// % is modulus operator
				return false;
			if (primeFactor * primeFactor > num)
				return true;
		}
		return true;
	}

	/**
   * Question 3 
   * Based on the function built in Question 2, implement a function that returns 
   * the factorisation of any composite number.
   * Factorisation means of the form p1^a1 * p2^a2 *....*pn^an etc.
	 * 
   * This method calls getPrimeFactors(long) to find the list of factors, 
   * then formats them into a pretty string.
	 */
	public static String getPrimeFactorization(long num) {
		List<Long> factors = getPrimeFactors(num);
		if (factors.size() == 0) // if no factors found (because num <=1)
			return ""; // return empty string

		// we get each factor in the factors List
		// we find its first index and its last index
		// and use those to calculate the number of times the factor appears
		// then append "[factor]^[factorCount] * " to the result string
		StringBuilder result = new StringBuilder();
		int firstIndexOfFactor = 0;
		while (firstIndexOfFactor < factors.size()) {
			Long curFactor = factors.get(firstIndexOfFactor);
			int lastIndexOfFactor = factors.lastIndexOf(curFactor);
			int curFactorCount = lastIndexOfFactor - firstIndexOfFactor + 1;
			result.append(curFactor + "^" + curFactorCount + " * ");
			firstIndexOfFactor = lastIndexOfFactor + 1;
		}
		result = result.delete(result.length() - 3, result.length()); // trim trailing " * "
		return result.toString();
	}

	/**
	 * Helper method for Question 3, also used by getPrivateKey(long,long) below.
	 * Finds the prime factors of num;
	 * If a factor occurs more than once, it will be returned more than once.
	 * e.g. getPrimeFactors(12) => [2, 2, 3];
	 * If num is less than 2, returns an empty list.
	 */
	private static List<Long> getPrimeFactors(long num) {
		List<Long> knownFactors = new ArrayList<Long>();

		// negative numbers, 0, and 1 are not considered composite by definition
		if (num <= 1)
			return knownFactors; // return empty list

		long curNum = num;
		long factor;
		while ((factor = getFirstPrimeFactor(curNum)) != 0) {
			knownFactors.add(factor);
			curNum = curNum / factor;
		}
		knownFactors.add(curNum);

		return knownFactors;
	}

	/**
     * Helper method used by getPrimeFactor(long), above.
     * Finds the smallest prime factor of num.
	 */
	private static long getFirstPrimeFactor(long num) {
		assert num > 1 : "Found: " + num + ". Expected an integer greater than 1.";

		// Iterate from 2 up to the square root of num.
		// For each potential factor, check if it's prime. If it is, check if num is
		// evenly divisible by it.
		// If so, return it.
		for (long potentialFactor = 2; (potentialFactor * potentialFactor) <= num; potentialFactor++) {
			if (isPrime(potentialFactor) && (num % potentialFactor == 0))	// % is modulus operator
				return potentialFactor;
		}
		return 0; // This should never happen, since every number greater than 1 has at least one
					// prime factor.
	}

	/**
	 * Question 4
	 * Implement Euclid’s algorithm.
	 *
	 * We calculate the highest common factor (aka the greatest common divisor) of two numbers
	 * by successively calculating one number modulo the other number until the result is zero.
	 * Each input number must be an integer greater than 0;
	 */
	public static long getHighestCommonFactor(long num1, long num2) {
		assert (num1 > 0 && num2 > 0) :	"Found: (" + num1 + ", " + num2 + "). Expected both numbers to be greater than zero.";

		long r0 = num1;
		long r1 = num2;

		while (r1 != 0) {
			long r = r0 % r1;	// % is modulus operator
			r0 = r1;
			r1 = r;
		}
		return r0;
	}

	/**
	 * Question 5
	 * Explain how Euclid’s algorithm might help you to find multiplicative inverses and implement it.
	 *
	 * Returns the multiplicative inverse of [num] modulo [modulus], 
	 * or returns 0 if num does not have an inverse.
	 * 
	 * This is essentially the same algorithm as Euclid's algorithm as implemented in getHighestCommonFactor(long, long) above
	 * except that instead of just keeping track of remainders we also keep track of quotients.
	 */
	public static long getModularInverse(long num, long modulus) {
		// num and modulus must be co-prime
		if (getHighestCommonFactor(num, modulus) != 1)
			return 0;	// if they are not co-prime, there is no multiplicative inverse, so return zero

		long r0 = modulus; 
		long r1 = num;
		long t0 = 0;
		long t1 = 1;

		long inverseMod = t1;
		long remainder = r1;

		while (remainder != 1) {
			long quotient = r0 / r1; 	// r0 divided by r1, then truncated to whole number
			remainder = r0 % r1;		// % is modulus operator
			inverseMod = t0 - (quotient * t1);

			r0 = r1;
			r1 = remainder;
			t0 = t1;
			t1 = inverseMod;
		}
		
		// if result is less than zero, add modulus to it to make positive and return
		// otherwise just return result
		return (inverseMod >= 0 ? inverseMod : inverseMod + modulus);
	}

	/**
	 * Question 6
	 * Write a program to convert an encrypted number c = m^e (mod n) 
	 * into the original m = c^d (mod n), where 0 < m < n is some integer. 
	 */
	public static long rsaEncrypt(long message, long publicKeyN, int publicKeyE) {
		assert(publicKeyE > 0);
		assert(publicKeyN > publicKeyE);
		
		// we use BigInteger instead of long here in order to avoid rounding of large numbers
		// during some of the math operations
		BigInteger messageBI = BigInteger.valueOf(message);
		BigInteger publicKeyNBI = BigInteger.valueOf(publicKeyN);

		return messageBI.pow(publicKeyE).mod(publicKeyNBI).longValue();	// message^publicKeyE mod publicKeyN
	}

	public static long rsaDecryptWithPrivateKey(long cypherText, long publicKeyN, long privateKeyD) {
		// we use BigInteger instead of long here in order to avoid rounding of large numbers
		// during some of the math operations
		BigInteger cypherBI = BigInteger.valueOf(cypherText);
		BigInteger publicKeyNBI = BigInteger.valueOf(publicKeyN);

		return cypherBI.pow((int) privateKeyD).mod(publicKeyNBI).longValue();
	}

	public static long rsaDecryptWithPublicKey(long cypherText, long publicKeyN, long publicKeyE) {
		return rsaDecryptWithPrivateKey(cypherText, publicKeyN, getPrivateKey(publicKeyN, publicKeyE));
	}

	/**
	 * Question 7
	 * Can you describe how question 5 might be used to break RSA algorithm 
	 * and recover the secret key?
	 * 
	 * This method uses the getModularInverse method from question 5 
	 * to calculate the RSA private key from an RSA public key pair.
	 * It successfully breaks RSA encryption for smaller public key values.
	 * However, for larger public key values it would take too long to execute to be practical,
	 * at least using currently available hardware.
	 */
	public static long getPrivateKey(long publicKeyN, long publicKeyE) {
		List<Long> primeFactors = getPrimeFactors(publicKeyN);
		assert primeFactors.size() == 2 : "publicKeyN " + publicKeyN + " must be a product of 2 primes";

		long p = primeFactors.get(0).longValue();
		long q = primeFactors.get(1).longValue();
		return getModularInverse(publicKeyE, (p - 1) * (q - 1));
	}
}
