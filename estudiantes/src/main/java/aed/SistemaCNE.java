package aed;
import aed.MaxHeap;

import java.util.Dictionary;
import java.util.Hashtable;

public class SistemaCNE {
    // Completar atributos privados

    int P;
    int D;

    String[] _nombresPartidos;
    String[] _nombresDistritos; 
    
    int[] _diputadosPorDistritos; 
    int[] _rangoMesasDistritos; // Tienen que estar ordenados (para que punto 5 sea de orden log (D))

    int[] _votosPresidenciales; // Mantener como variable a las dos mayores cantidades de votos
    int[][] _votosDiputados;    // Heap aparte que lo ordene
    boolean[] _mesasRegistradas; // True = mesa se registro - False / Null = mesa no se registro

    MaxHeap[] _resultadosPorDistritos;
    int[][] _bancasPorDistrito;
    int[] _ballotage; //Almacena los dos partidos que tienen mas votos para presidente

    public class VotosPartido{
        private int presidente;
        private int diputados;
        VotosPartido(int presidente, int diputados){this.presidente = presidente; this.diputados = diputados;}
        public int votosPresidente(){return presidente;}
        public int votosDiputados(){return diputados;}
    }

    public SistemaCNE(String[] nombresDistritos, int[] diputadosPorDistrito, String[] nombresPartidos, int[] ultimasMesasDistritos) {
        
        P = nombresPartidos.length;
        D = nombresDistritos.length;

        _nombresPartidos = new String[P];
        _nombresDistritos = new String[D]; 
    
        _diputadosPorDistritos = new int[D];
        _rangoMesasDistritos = new int[D]; 

        _votosPresidenciales = new int[P];
        _votosDiputados = new int[D][P];

        _mesasRegistradas = new boolean[ultimasMesasDistritos[ultimasMesasDistritos.length - 1]]; 

        for (int partido = 0; partido < P; partido++ ){
            _nombresPartidos[partido] = nombresPartidos[partido];
            _votosPresidenciales[partido] = 0; 

            for (int distrito = 0; distrito < D; distrito++ ){
                _nombresDistritos[distrito] = nombresDistritos[distrito];
                _diputadosPorDistritos[distrito] = diputadosPorDistrito[distrito];
                _rangoMesasDistritos[distrito] = ultimasMesasDistritos[distrito];
                _votosDiputados [distrito][partido] = 0;
            } 
        }

        _resultadosPorDistritos = new MaxHeap[D];

        for (int i = 0; i < D; i++) {
            _resultadosPorDistritos[i] = new MaxHeap(P-1);
        }

        _bancasPorDistrito = new int [D][P];        

        for (int distrito = 0; distrito < D; distrito++ ){

            for(int partido = 0; partido < P; partido++){
                _bancasPorDistrito[distrito][partido] = 0;
            }

        }

    }

    public String nombrePartido(int idPartido) {
        return _nombresPartidos[idPartido];
    }

    public String nombreDistrito(int idDistrito) {
        return _nombresDistritos[idDistrito];
    }

    public int diputadosEnDisputa(int idDistrito) {
        return _diputadosPorDistritos[idDistrito];
    }

    public int idDistritoDeMesa(int idMesa) {
        
        // Busqueda binaria en lista de D elementos. O(log D)

        int izq = 0;
        int der = D - 1;
        
        if (idMesa >= _rangoMesasDistritos[der]) return der;
        
        while (izq <= der){
            int medio = (izq + der) / 2;
    
            if (idMesa < _rangoMesasDistritos[medio]){
                der = medio - 1;
            } else {
                izq = medio + 1;
            }
        }
        return der+1;

    }

    public String distritoDeMesa(int idMesa) { 
        return _nombresDistritos[idDistritoDeMesa(idMesa)];
    }

    public void registrarMesa(int idMesa, VotosPartido[] actaMesa) {
        
        int idDistrito = idDistritoDeMesa(idMesa); // O(log D)

        for (int i=0; i < P; i++) { // O(P)
            _votosPresidenciales[i] += actaMesa[i].votosPresidente();
            _votosDiputados[idDistrito][i] += actaMesa[i].votosDiputados();
        }

        int[] votosDistrito = this._votosDiputados[idDistrito].clone(); // O(P)

        _resultadosPorDistritos[idDistrito] = new MaxHeap(votosDistrito); // O(P)
    
    }

    public int votosPresidenciales(int idPartido) {
        return _votosPresidenciales[idPartido];
    }

    public int votosDiputados(int idPartido, int idDistrito) {
        return _votosDiputados[idDistrito][idPartido];
    }

    public int[] resultadosDiputados(int idDistrito){

        DHondt partidoMasVotos;

        while (_diputadosPorDistritos[idDistrito] > 0){ // O(Dd * (log P + 1 + 1 + log P)) = O(Dd * (log P)) 
            
            partidoMasVotos = _resultadosPorDistritos[idDistrito].desencolarRaiz(); // O (log P)
            
            _bancasPorDistrito[idDistrito][partidoMasVotos.idPartido] ++; 
            partidoMasVotos.dividendo ++;
            partidoMasVotos.cociente = partidoMasVotos.votos / partidoMasVotos.dividendo;
            
            _resultadosPorDistritos[idDistrito].encolar(partidoMasVotos); // O (log P)

            _diputadosPorDistritos[idDistrito] --; //CONSULTAR TEMA DE IN / INOUT

        }

        return _bancasPorDistrito[idDistrito];

    }

    public boolean hayBallotage(){
        throw new UnsupportedOperationException("No implementada aun");
    }
}

