package aed;

import java.util.Dictionary;
import java.util.Hashtable;

public class SistemaCNE {
    // Completar atributos privados

    String[] _nombresPartidos;
    String[] _nombresDistritos; 
    
    int[] _diputadosPorDistritos;
    int[] _rangoMesasDistritos; // Tienen que estar ordenados (para que punto 5 sea de orden log (D))

    int[] _votosPresidenciales; // Mantener como variable a las dos mayores cantidades de votos
    int[][] _votosDiputados;    // Heap aparte que lo ordene
    boolean[] _mesasRegistradas; // True = mesa se registro - False / Null = mesa no se registro


    public class VotosPartido{
        private int presidente;
        private int diputados;
        VotosPartido(int presidente, int diputados){this.presidente = presidente; this.diputados = diputados;}
        public int votosPresidente(){return presidente;}
        public int votosDiputados(){return diputados;}
    }

    public SistemaCNE(String[] nombresDistritos, int[] diputadosPorDistrito, String[] nombresPartidos, int[] ultimasMesasDistritos) {
        
        _nombresPartidos = new String[nombresPartidos.length];
        _nombresDistritos = new String[nombresDistritos.length]; 
    
        _diputadosPorDistritos = new int[nombresDistritos.length];
        _rangoMesasDistritos = new int[nombresDistritos.length]; 

        _votosPresidenciales = new int[nombresPartidos.length];
        _votosDiputados = new int[nombresDistritos.length][nombresPartidos.length];

        _mesasRegistradas = new boolean[ultimasMesasDistritos[ultimasMesasDistritos.length - 1]]; 

        for (int i = 0; i < nombresPartidos.length; i++ ){
            _nombresPartidos[i] = nombresPartidos[i];
            _votosPresidenciales[i] = 0; 

            for (int j = 0; j < nombresDistritos.length; j++ ){
                _nombresDistritos[j] = nombresDistritos[j];
                _diputadosPorDistritos[j] = diputadosPorDistrito[j];
                _rangoMesasDistritos[j] = ultimasMesasDistritos[j];
                _votosDiputados [j][i] = 0;
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

    public String distritoDeMesa(int idMesa) { //REVISAR, NO ANDAN LAS PRUEBAS
        
        int izq = 0;
        int medio = 0;
        int der = _nombresDistritos.length - 1;

        if (der % 2 == 0){
            medio = der / 2;
        }else{
            medio = (der + 1) / 2;
        }

        while(izq != medio || der != medio){
            
            if (_rangoMesasDistritos[medio - 1] < idMesa && _rangoMesasDistritos[medio + 1] > idMesa){
            
                izq = medio;
                der = medio;
            
            }else if (_rangoMesasDistritos[medio - 1] < idMesa && _rangoMesasDistritos[medio + 1] < idMesa) {
                
                izq = medio;

                if ((der - medio) % 2 == 0){
                    medio = medio + (der - medio) / 2;
                }else{
                    medio = medio + (der - medio + 1) / 2;
                }

            } else {

                der = medio;

                if ((medio - izq) % 2 == 0){
                    medio = (medio - izq) / 2;
                }else{
                    medio = (medio + 1 - izq) / 2;
                }

            }
        }

        return _nombresDistritos[medio];

    }

    public void registrarMesa(int idMesa, VotosPartido[] actaMesa) {
        throw new UnsupportedOperationException("No implementada aun");
    }

    public int votosPresidenciales(int idPartido) {
        throw new UnsupportedOperationException("No implementada aun");
    }

    public int votosDiputados(int idPartido, int idDistrito) {
        throw new UnsupportedOperationException("No implementada aun");
    }

    public int[] resultadosDiputados(int idDistrito){
        throw new UnsupportedOperationException("No implementada aun");
    }

    public boolean hayBallotage(){
        throw new UnsupportedOperationException("No implementada aun");
    }
}

