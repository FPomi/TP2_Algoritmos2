package aed;

public class MaxHeap {

    private DHondt[] heap;
    private int size;
    private int proximo;

    public MaxHeap(int[] s) { // Array2Heap

        this.proximo = 0;

        // conteo de votos
        int votosTotales = 0;
        for (int i=0; i<s.length; i++)
            votosTotales += s[i];

        // conteo de distritos que superan el umbral
        int cantidadDistritos = 0;
        for (int i=0; i<s.length - 1; i++){
            if (s[i] > votosTotales*0.03)
                cantidadDistritos++;
        }

        // inicializo size y heap con los distritos que superan el umbral
        this.size = cantidadDistritos;
        this.heap = new DHondt[cantidadDistritos];

        // Agrego los nodos al heap (por ahora array comun desordenado)
        for (int i = 0; i < s.length - 1; i++) {
            if (s[i] > votosTotales*0.03) {
                this.heap[this.proximo] = new DHondt(i, s[i]);
                this.proximo++; 
            }
        }

        // Algoritmo de Floyd
        for (int i = (this.proximo / 2) - 1; i >= 0; i--) {
            bajar(i);
        }
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

    public void bajar(int indice) {
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