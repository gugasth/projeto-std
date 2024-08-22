package engtelecom.std;

/**
 * Classe que representa uma caixa de som, que é um dispositivo IOT.
 */
public class CaixaDeSom extends Dispositivo{
    
    /**
     * Construtor
     */
    public CaixaDeSom(String nome, boolean estado, float valor) {
        super(nome, 2, estado, valor);
    }
}
