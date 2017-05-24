# Cryptography

A Java package which currently contains only one library: the `uk.org.bobulous.java.crypto.keccak` package which provides an implementation of the [Keccak sponge function family of hash functions](http://keccak.noekeon.org/index.html), and the standardised SHA-3 instances (both SHA3 hash functions and SHAKE extendable-output functions).

## WARNING

**This project should be considered to be in beta.**

I've been working on this Java package for the last six weeks, and the code has been extensively tested in development, so I believe it's finally ready for other people to play with. However, there are bound to be errors, oversights, misunderstandings, and sneaky traps, so the advice is simple: do not use this package for important purposes without applying your own rigorous testing. Please raise an issue here on GitHub if you find any bugs.

## Features

This implementation supports the following features:

* All of the SHA-3 functions defined in [NIST FIPS PUB 202](http://nvlpubs.nist.gov/nistpubs/FIPS/NIST.FIPS.202.pdf).
* Any Keccak sponge configuration with a width from 200 to 1600 (inclusive), and a bitrate which is exactly divisible by eight.
* Arbitrary output hash lengths.
* Arbitrary input message lengths when using byte array message input.
* Arbitrary whole-byte input message lengths when using `InputStream` message input.
* Input can be provided as a byte array or as an `InputStream`. Output is provided as a byte array, which will be padded with zero bits if the output length (in bits) is not exactly divisible by eight.
* The "lane complementing transform" optimisation (suggested by the Keccak team in their implementation guide). This is enabled with a hard switch in the `KeccakState` class, but can be disabled if it harms rather than benefits performance. (On my desktop machine I see a 22% reduction in runtime with this optimisation enabled, but other platforms may differ. This package has been primarily written on the assumption that 64-bit machines will be using it.)

## Examples of use

Using the SHA-3 functions is made very simple by the `uk.org.bobulous.java.crypto.keccak.FIPS202` utility class. This utility class contains a `HashFunction` class which offers the SHA3 hash functions SHA3-224, SHA3-256, SHA3-384, and SHA3-512, and an `ExtendableOutputFunction` class which holds definitions for the SHAKE extendable-output functions SHAKE128, SHAKE256, RawSHAKE128, and RawSHAKE256.

### SHA3 hash functions

Suppose you want to calculate the SHA3-224 hash for an empty `String` (and thus a message of length zero bits). You can do so like this:

```java
byte[] messageBytes = new byte[] {};
byte[] hashBytes = FIPS202.HashFunction.SHA3_224.apply(messageBytes);
```

If you wanted something a bit more useful, you could calculate the SHA3-512 hash of a password with the following (remembering that the character encoding used to convert the `String` into bytes is a critically important factor):

```java
byte[] passwordBytes = secretPassword.getBytes(Charset.forName(
		"UTF-8"));
byte[] passwordHash = FIPS202.HashFunction.SHA3_512.apply(passwordBytes);
```

### SHAKE extendable-output functions

Each SHAKE extendable-output function (XOF) requires that you choose an output length for the generated hash. You can create a reusable `KeccakSponge` object which represents your chosen XOF with your chosen output length, and then apply it to our empty message like this:

```java
byte[] messageBytes = new byte[] {};
KeccakSponge sponge = FIPS202.ExtendableOutputFunction.SHAKE256.withOutputLength(4096);
byte[] hashBytes = sponge.apply(messageBytes);
```

### Defining a custom Keccak sponge function

If you're sure you know what you're doing and you want to create a Keccak sponge function with custom parameters then you can do so using the `KeccakSponge` constructor `KeccakSponge(int bitrate, int capacity, String domainSuffix, int outputLength)`, and then apply it to our empty message like this:

```java
KeccakSponge spongeFunction = new KeccakSponge(40, 160, "", 4096);
byte[] message = new byte[]{};
byte[] hash = spongeFunction.apply(5, message);
```

The created `KeccakSponge` object is immutable and re-usable, and is safe to share.

### Applying the hash functions to an `InputStream`

The hash functions are all able to read from an `InputStream` instead of a byte array, which should be more convenient if you're trying to hash the content of files. For example, suppose you want to calculate the SHA3-512 hash for a file called "AOL-data.tgz" in your home directory:

```java
File oopsData = new File("/home/bob/AOL-data.tgz");
InputStream fileStream = new FileInputStream(oopsData);
byte[] fileHash = FIPS202.HashFunction.SHA3_512.apply(fileStream);
```

(Though bear in mind that the `apply(InputStream)` method signature declares that it `throws IOException` in case anything goes wrong while working with the `InputStream`.)

## More to do

I intend to implement further optimisations to the Keccak permutation code. More immediately, though, I realise that this package does not yet offer a convenient way to convert a `byte[]` hash result into an easily printable hexadecimal string, so I'll be working on that very soon.
