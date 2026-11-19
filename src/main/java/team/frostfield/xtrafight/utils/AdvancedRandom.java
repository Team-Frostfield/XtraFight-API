package team.frostfield.xtrafight.utils;

import java.io.Serial;
import java.util.List;
import java.util.Random;

public class AdvancedRandom extends Random {
    @Serial
    private static final long serialVersionUID = 1L;

    // 10^0 .. 10^9 (10^9 is the largest power of ten that fits in an int)
    private static final int[] POW10 = {
            1, 10, 100, 1_000, 10_000, 100_000, 1_000_000,
            10_000_000, 100_000_000, 1_000_000_000
    };

    private static final int MAX_INT_DIGITS = 9;   // 999,999,999 fits in an int
    private static final int MAX_FLOAT_DIGITS = 7; // floats are exact for integers up to 2^24 (16,777,216)

    private int beginsFrom = 0;
    private int maxDigits = MAX_INT_DIGITS;

    public AdvancedRandom() {
        super();
    }

    public static int between(int f, int s) {
        return new AdvancedRandom().nextIntBetween(f, s);
    }

    public AdvancedRandom(long seed) {
        super(seed);
    }

    // ---- Configurable start ----

    /** Sets the minimum (inclusive) used by the "From" methods. Chainable. */
    public AdvancedRandom beginsFrom(int start) {
        this.beginsFrom = start;
        return this;
    }

    public int getBeginsFrom() {
        return this.beginsFrom;
    }

    /** Random int in [beginsFrom, endInclusive]. */
    public int nextIntFrom(int endInclusive) {
        return this.nextIntBetween(beginsFrom, endInclusive);
    }

    // ---- Configurable digits ----

    /** Sets the digit count used by the digit methods (1 to 9). Chainable. */
    public AdvancedRandom maxDigits(int digits) {
        checkDigits(digits, MAX_INT_DIGITS);
        this.maxDigits = digits;
        return this;
    }

    public int getMaxDigits() {
        return this.maxDigits;
    }

    /** Random int with exactly maxDigits digits (no leading zero). */
    public int nextWithMaxDigits() {
        return this.nextWithDigits(maxDigits);
    }

    /** Random int with exactly `digits` digits. For 1 digit, the range is 0-9. */
    public int nextWithDigits(int digits) {
        checkDigits(digits, MAX_INT_DIGITS);
        int min = digits == 1 ? 0 : POW10[digits - 1];
        return this.nextInt(min, POW10[digits]); // bound is exclusive
    }

    /** Random int from 0 up to the largest number with maxDigits digits. */
    public int nextUpToMaxDigits() {
        return this.nextInt(POW10[maxDigits]);
    }

    /** Random float holding a whole number with exactly `digits` digits (max 7, so it stays exact). */
    public float nextFloatWithDigits(int digits) {
        checkDigits(digits, MAX_FLOAT_DIGITS);
        return this.nextWithDigits(digits);
    }

    /** Random float in [0, 1) with `decimals` decimal places (max 7). */
    public float nextFloatDecimals(int decimals) {
        checkDigits(decimals, MAX_FLOAT_DIGITS);
        return this.nextInt(POW10[decimals]) / (float) POW10[decimals];
    }

    /** Digit string of exactly `digits` length, leading zeros allowed (PINs, codes). */
    public String nextDigitString(int digits) {
        if (digits < 1) {
            throw new IllegalArgumentException("digits must be >= 1");
        }
        char[] chars = new char[digits];
        for (int i = 0; i < digits; i++) {
            chars[i] = (char) ('0' + nextInt(10));
        }
        return new String(chars);
    }

    // ---- Explicit ranges ----

    /** Random int in [min, max], both inclusive. */
    public int nextIntBetween(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min (" + min + ") > max (" + max + ")");
        }
        if (max == Integer.MAX_VALUE) {
            // max + 1 would overflow, so handle the edge without long math
            if (min == Integer.MIN_VALUE) {
                return this.nextInt();
            }
            return this.nextInt(min - 1, max) + 1;
        }
        return this.nextInt(min, max + 1);
    }

    /** Random float in [min, max). */
    public float nextFloatBetween(float min, float max) {
        if (min > max) {
            throw new IllegalArgumentException("min (" + min + ") > max (" + max + ")");
        }
        return min + nextFloat() * (max - min);
    }

    /** Random double in [min, max). */
    public double nextDoubleBetween(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("min (" + min + ") > max (" + max + ")");
        }
        return min + nextDouble() * (max - min);
    }

    // ---- Extras ----

    /** Returns true with the given probability (0.0 to 1.0). */
    public boolean chance(float probability) {
        return nextFloat() < probability;
    }

    /** Picks a random element from a list. */
    public <T> T pick(List<T> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("list is empty");
        }
        return list.get(nextInt(list.size()));
    }

    /** Picks a random element from an array. */
    @SafeVarargs
    public final <T> T pick(T... options) {
        if (options.length == 0) {
            throw new IllegalArgumentException("no options provided");
        }
        return options[nextInt(options.length)];
    }

    /** Random sign: -1 or 1. */
    public int nextSign() {
        return this.nextBoolean() ? 1 : -1;
    }

    // ---- Validation ----

    private static void checkDigits(int digits, int max) {
        if (digits < 1 || digits > max) {
            throw new IllegalArgumentException("digits must be between 1 and " + max + ", got " + digits);
        }
    }
}