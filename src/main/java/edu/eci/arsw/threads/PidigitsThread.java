package edu.eci.arsw.threads;

/*
 * Cree una clase de tipo Thread que represente el ciclo de vida de un hilo que calcule una parte 
 * de los digitos requeridos
 */


public class PidigitsThread extends Thread{
    private static int DigitsPerSum = 8;
    private static double Epsilon = 1e-17;
    private int A;
    private int B;
    private byte[] digitsPiArray;

    public PidigitsThread(){

    }


    public PidigitsThread(int A, int B){
        this.A = A;
        this.B = B;
    }

    public byte[] getDigits(int start, int count) {
        if (start < 0) {
            throw new RuntimeException("Ingrese un intervalo que sea valido");
        }  
        if (count < 0) {
            throw new RuntimeException("Ingrese un intervalo que sea valido");
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

    public byte[] getDigitsPi(){
        return digitsPiArray;
    }


    private static int hexadecimalExponentModule(int r, int h) {
        int potencia = 1;
        while (potencia * 2 <= r){
            potencia *= 2;
        }
        int finalresult = 1;
        while (potencia > 0){
            if (r >= potencia){
                finalresult *=16;
                finalresult %= h;
            }
            potencia /= 2;
            if (potencia > 0){
                finalresult *= finalresult;
                finalresult %= h;

            }
        }
        return finalresult;
    }

    private double sum(int e, int f) {
        double Sum = 0;
        int d = e;
        int power = f;
        while (true) {
            double term;
            if (power > 0) {
                term = (double) hexadecimalExponentModule(power, d) / d;
            } else {
                term = Math.pow(16, power) / d;
                if (term < Epsilon) {
                    break;
                }
            }
            d += 8;
            power--;
            Sum += term;
        }
        return Sum;
    }
    

    public void run(){
        getDigits(this.A,this.B);
    }
}
