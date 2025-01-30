package edu.eci.arsw.math;

import java.util.ArrayList;
import java.util.List;

import edu.eci.arsw.threads.*;

///  <summary>
///  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
///  digits of pi.
///  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
///  *** Translated from C# code: https://github.com/mmoroney/DigitsOfPi ***
///  </summary>



    public class PiDigits {
        private List<PidigitsThread> ListThread= new ArrayList<PidigitsThread>();
        private byte[] byteList;
        /**
         * Returns a range of hexadecimal digits of pi.
         * @param start The starting location of the range.
         * @param count The number of digits to return
         * @return An array containing the hexadecimal digits.
         */
        public byte[] getDigits(int start, int count, int N) {
            int numberPerThread = count / N;
            byteList = new byte[count];
            int aux = 0;
            if (count % N != 0) aux =count % N ;
            ListThread.add(new PidigitsThread(start,numberPerThread + aux));
            N--;
            start += aux;
            // System.out.println("VALOR start: " + start);
            for (int i = 0; i < N; i++) {
                ListThread.add(new PidigitsThread(start + numberPerThread,numberPerThread));
                start +=numberPerThread;
                // System.out.println("VALOR start: " + start + ", VALOR PER THREAD: " + (numberPerThread + aux) );
            }
            for (Thread t : ListThread) {
                t.start();
            }
            try {
                int i = 0;
                for (PidigitsThread t : ListThread) {
                    t.join();
                    for (byte b : t.getDigitsPi()) {
                        byteList[i]= b;
                        i++;
                    }
                }
            } catch (Exception e) {
                System.out.println("Error in thread");
            }
            byte[] digits = byteList;
            return digits;
        }
    }
