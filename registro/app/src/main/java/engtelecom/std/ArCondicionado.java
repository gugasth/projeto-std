package engtelecom.std;

/**
 * Classe que representa um ar condicionado, que é um dispositivo IOT.
 */
public class ArCondicionado extends Dispositivo{
    
    /**
     * Construtor
     */
    public ArCondicionado(String nome, boolean estado, float valor) {
        super(nome, 1, estado, valor);
    }
    
    
}
