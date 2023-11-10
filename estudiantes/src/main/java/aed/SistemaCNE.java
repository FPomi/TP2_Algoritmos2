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
    int[] _diputadosPorDistritos_copia; 
    int[] _rangoMesasDistritos; // Tienen que estar ordenados (para que punto 5 sea de orden log (D))

    int[] _votosPresidenciales; // Mantener como variable a las dos mayores cantidades de votos
    int[][] _votosDiputados;    // Heap aparte que lo ordene
    int[][] _bancasPorDistrito;

    MaxHeap[] _resultadosPorDistritos;
    int[] _ballotage; //Almacena los dos partidos que tienen mas votos para presidente
    int _votosTotales;

    //-----------------------------------------------------------------------------------------------------------------------------------------------------//

    public class VotosPartido{
        private int presidente;
        private int diputados;
        VotosPartido(int presidente, int diputados){this.presidente = presidente; this.diputados = diputados;}
        public int votosPresidente(){return presidente;}
        public int votosDiputados(){return diputados;}
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------//

    public SistemaCNE(String[] nombresDistritos, int[] diputadosPorDistrito, String[] nombresPartidos, int[] ultimasMesasDistritos) {
        
        P = nombresPartidos.length;
        D = nombresDistritos.length;

        _nombresPartidos = new String[P];
        _nombresDistritos = new String[D]; 
    
        _diputadosPorDistritos = new int[D];
        _rangoMesasDistritos = new int[D]; 

        _votosPresidenciales = new int[P];
        _votosDiputados = new int[D][P];

        for (int partido = 0; partido < P; partido++ ){
            _nombresPartidos[partido] = nombresPartidos[partido];
        }

        for (int distrito = 0; distrito < D; distrito++ ){
            _nombresDistritos[distrito] = nombresDistritos[distrito];
            _diputadosPorDistritos[distrito] = diputadosPorDistrito[distrito];
            _rangoMesasDistritos[distrito] = ultimasMesasDistritos[distrito];
        } 

        _diputadosPorDistritos_copia = _diputadosPorDistritos.clone();
        _resultadosPorDistritos = new MaxHeap[D];

        for (int i = 0; i < D; i++) {
            _resultadosPorDistritos[i] = new MaxHeap(P-1);
        }

        _bancasPorDistrito = new int[D][P];
        _ballotage = new int[]{0,1};
        _votosTotales = 0;

    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------//

    public String nombrePartido(int idPartido) {
        return _nombresPartidos[idPartido];
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------//

    public String nombreDistrito(int idDistrito) {
        return _nombresDistritos[idDistrito];
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------//

    public int diputadosEnDisputa(int idDistrito) {
        return _diputadosPorDistritos[idDistrito];
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------//

    public int idDistritoDeMesa(int idMesa) {
        
        int izq = 0;
        int der = D - 1;

        // Comprobación especial para el caso donde idMesa es mayor o igual al último elemento
        if (idMesa >= _rangoMesasDistritos[der]) {
            return der;
        }

        while (izq <= der) {
            // Evitar desbordamiento al calcular el medio
            int medio = izq + (der - izq) / 2;

            if (idMesa < _rangoMesasDistritos[medio]) {
                der = medio - 1;
            } else {
                izq = medio + 1;
            }
        }

        return der + 1;

    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------//

    public String distritoDeMesa(int idMesa) { 
        return _nombresDistritos[idDistritoDeMesa(idMesa)];
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------//

    public void registrarMesa(int idMesa, VotosPartido[] actaMesa) {
        
        int idDistrito = idDistritoDeMesa(idMesa); // O(log D)

        for (int partido = 0; partido < P; partido++) { // O(P)

            _votosTotales += actaMesa[partido].votosPresidente();
            _votosPresidenciales[partido] += actaMesa[partido].votosPresidente();
            _votosDiputados[idDistrito][partido] += actaMesa[partido].votosDiputados();
        
            if (_ballotage[0] != partido) { 
                
                if (_votosPresidenciales[partido] >= _votosPresidenciales[_ballotage[0]]){
                
                    _ballotage[1] = _ballotage[0];
                    _ballotage[0] = partido;
                
                }else if (_votosPresidenciales[partido] >= _votosPresidenciales[_ballotage[1]]){
                    _ballotage[1] = partido;
                }

            }  

        }

        if (_votosPresidenciales[_ballotage[1]] > _votosPresidenciales[_ballotage[0]]){
            int aux = _ballotage [1];
            _ballotage [1] = _ballotage[0];
            _ballotage [0] = aux; 
        }

        int[] votosDistrito = this._votosDiputados[idDistrito].clone(); // O(P)

        _resultadosPorDistritos[idDistrito] = new MaxHeap(votosDistrito); // O(P)
    
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------//

    public int votosPresidenciales(int idPartido) {
        return _votosPresidenciales[idPartido];
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------//

    public int votosDiputados(int idPartido, int idDistrito) {
        return _votosDiputados[idDistrito][idPartido];
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------//

    public int[] resultadosDiputados(int idDistrito){

        DHondt partidoMasVotos;
        MaxHeap resultadosDelDistrito = _resultadosPorDistritos[idDistrito];

        while (_diputadosPorDistritos_copia[idDistrito] > 0){ // O(Dd * (log P + 1 + 1 + log P)) = O(Dd * (log P)) 
            
            partidoMasVotos = resultadosDelDistrito.desencolarRaiz(); // O (log P)
            
            _bancasPorDistrito[idDistrito][partidoMasVotos.idPartido] ++; 
            partidoMasVotos.dividendo ++;
            partidoMasVotos.cociente = partidoMasVotos.votos / partidoMasVotos.dividendo;
            
            resultadosDelDistrito.encolar(partidoMasVotos); // O (log P)

            _diputadosPorDistritos_copia[idDistrito] --;

        }

        return _bancasPorDistrito[idDistrito];

    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------//

    public boolean hayBallotage(){

        boolean hayBallotaje = true;
        
        float porcentajeMasVotado = (votosPresidenciales(_ballotage[0]) * 100) / _votosTotales;  
        float porcentajeSegundoMasVotado = (votosPresidenciales(_ballotage[1]) * 100) / _votosTotales;

        if (porcentajeMasVotado >= 45){
            hayBallotaje = false;
        }

        if (porcentajeMasVotado >= 40 && (porcentajeMasVotado - porcentajeSegundoMasVotado > 10)){
            hayBallotaje = false;
        }

        return hayBallotaje;
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------//

}

