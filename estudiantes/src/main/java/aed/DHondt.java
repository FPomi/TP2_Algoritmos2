package aed;

public class DHondt{
    public int idPartido;
    public int votos;
    public int cociente;
    public int dividendo;

    public DHondt(int idPartido, int votos){
        this.idPartido = idPartido;
        this.votos = votos;
        this.cociente = votos;
        this.dividendo = 1;
    }

}
