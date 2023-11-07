package aed;

import java.util.Arrays;

public class MaxHeap {

    private DHondt[] heap;
    private int size;

    public MaxHeap(int[] s) { // Array2Heap

    }

    public MaxHeap(int P) { // constructor
        heap = new DHondt[P];
        size = 0;
    }

    public void encolar(DHondt nodo) {
        heap[size] = nodo;
        size++;
        subir();
    }

    public void subir() {
        int indice = size - 1;
        while (indice > 0 && heap[indice].cociente > heap[posPadre(indice)].cociente) {
            intercambiar(indice, posPadre(indice));
            indice = posPadre(indice);
        }
    }

    public void bajar() {
        int indice = 0;
        while (!esHoja(indice)) {
            int indiceHijoIzquierdo = posHijoIzquierdo(indice);
            int indiceHijoDerecho = posHijoDerecho(indice);
            int indiceMayor = indiceHijoIzquierdo;

            if (posHijoDerecho(indice) < size && heap[indiceHijoDerecho].cociente > heap[indiceHijoIzquierdo].cociente) {
                indiceMayor = indiceHijoDerecho;
            }

            if (heap[indice].cociente > heap[indiceMayor].cociente) {
                break;
            } else {
                intercambiar(indice, indiceMayor);
            }

            indice = indiceMayor;
        }
    }

    public void intercambiar(int indice1, int indice2) {
        DHondt temp = this.heap[indice1];
        heap[indice1] = heap[indice2];
        heap[indice2] = temp;
    }

    public boolean esHoja(int indice) {return posHijoIzquierdo(indice) >= size;}

    public int posPadre(int indice) {return (indice - 1) / 2;}

    public int posHijoDerecho(int indice) {return 2*indice + 2;}

    public int posHijoIzquierdo(int indice) {return 2*indice + 1;}
}