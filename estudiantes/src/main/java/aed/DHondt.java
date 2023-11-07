package aed;

public class DHondt{
    public int idPartido;
    public int votos;
    public int cociente;
    public int bancasAsignadas;

    public DHondt(int idPartido, int votos){
        this.idPartido = idPartido;
        this.votos = votos;
        this.cociente = 0;
        this.bancasAsignadas = 0;
    }
}
