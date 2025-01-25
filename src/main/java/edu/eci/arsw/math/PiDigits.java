package edu.eci.arsw.math;

import java.util.ArrayList;
import java.util.List;

import edu.eci.arsw.threads.PidigitsThread;

public class PiDigits {

    public byte[] getDigits(int start, int count, int n) {
        // Lista de hilos y resultados locales para evitar problemas con variables estáticas
        List<PidigitsThread> threadList = new ArrayList<>();
        byte[] result = new byte[count];

        // Cantidad de dígitos por hilo y excedente
        int digitsPerThread = count / n;
        int remainder = count % n;

        // Crear y asignar trabajo a los hilos
        int currentStart = start;
        for (int i = 0; i < n; i++) {
            int workLoad = digitsPerThread + (i == 0 ? remainder : 0); // El primer hilo toma el excedente
            threadList.add(new PidigitsThread(currentStart, workLoad));
            currentStart += workLoad;
        }

        // Iniciar todos los hilos
        for (Thread thread : threadList) {
            thread.start();
        }

        // Sincronizar los hilos y recopilar resultados
        try {
            int resultIndex = 0;
            for (PidigitsThread thread : threadList) {
                thread.join();
                byte[] partialResult = thread.getDigitsPi();
                System.arraycopy(partialResult, 0, result, resultIndex, partialResult.length);
                resultIndex += partialResult.length;
            }
        } catch (InterruptedException e) {
            System.err.println("Error al sincronizar los hilos: " + e.getMessage());
        }

        return result;
    }
}
