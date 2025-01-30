package edu.eci.arsw.threads;

/*
 * Cree una clase de tipo Thread que represente el ciclo de vida de un hilo que calcule una parte 
 * de los digitos requeridos
 */


 public class PidigitsThread extends Thread {
    
    private static final int DigitsPerSum = 8;
    private static final double Epsilon = 1e-17;
    private int start;
    private int count;
    private byte[] digitsPiArray;

    public PidigitsThread(int start, int count) {
        this.start = start;
        this.count = count;
    }

    public byte[] getDigits(int start, int count) {
        if (start < 0) {
            throw new RuntimeException("Invalid Interval");
        }
        if (count < 0) {
            throw new RuntimeException("Invalid Interval");
        }
        byte[] digits = new byte[count];
        double sum = 0;
        for (int i = 0; i < count; i++) {
            if (i % DigitsPerSum == 0) {
                sum = 4 * sum(1, start)
                        - 2 * sum(4, start)
                        - sum(5, start)
                        - sum(6, start);
                start += DigitsPerSum;
            }
            sum = 16 * (sum - Math.floor(sum));
            digits[i] = (byte) sum;
        }
        digitsPiArray = digits;
        return digits;
    }

    public byte[] getDigitsPi() {
        return digitsPiArray;
    }
   

    private static int hexExponentModulo(int p, int m) {
        int power = 1;
        while (power * 2 <= p) {
            power *= 2;
        }
        int result = 1;
        while (power > 0) {
            if (p >= power) {
                result *= 16;
                result %= m;
                p -= power;
            }
            power /= 2;
            if (power > 0) {
                result *= result;
                result %= m;
            }
        }
        return result;
    }

    private static double sum(int m, int n) {
        double sum = 0;
        int d = m;
        int power = n;
        while (true) {
            double term;
            if (power > 0) {
                term = (double) hexExponentModulo(power, d) / d;
            } else {
                term = Math.pow(16, power) / d;
                if (term < Epsilon) {
                    break;
                }
            }
            sum += term;
            power--;
            d += 8;
        }
        return sum;
    }

    public void run() {
        digitsPiArray = getDigits(this.start, this.count);
    }
}
