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

    private static List<PidigitsThread> ListThread = new ArrayList<PidigitsThread>();
    private static byte[] ListByte;

    /**
     * Returns a range of hexadecimal digits of pi.
     * 
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @return An array containing the hexadecimal digits.
     */
    public byte[] getDigits(int start, int count, int n) {

        // Reiniciar la lista de hilos eliminando cualquier hilo que este previamente guardado


        // Se calcula la cantidad de digitos que cada hilo debe procesar
        int ThreadNumber = count / n;
        ListByte = new byte[count];
        int auxiliar = 0;

        //Crear el primer hilo con la mayor carga de trabajo adoptando el excendente
        if (count % n != 0)
            auxiliar = count % n;
        ListThread.add(new PidigitsThread(start, ThreadNumber + auxiliar));
        n--;
        start += auxiliar;
        
        // Se crean los demas hilos
        for (int i = 0; i < n; i++) {
            ListThread.add(new PidigitsThread(start + ThreadNumber, ThreadNumber));
            start += ThreadNumber;
        }

         // Aqui arrancan todos los hilos
        for (Thread T : ListThread) {
            T.start();

        }
        // Sincronizar los hilos y se utiliza la funcion join para que el hilo
        // principal espere que cada hilo termine su ejecucion
        try {
            int i = 0;
            for (PidigitsThread P : ListThread) {
                P.join();
                for (byte B : P.getDigitsPi()) {
                    ListByte[i] = B;
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("Error en el Thread");
        }
        byte[] digits = ListByte;
        return digits;

    }

}