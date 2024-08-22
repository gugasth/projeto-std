package engtelecom.std;

/**
 * Classe que representa uma cortina, que é um dispositivo IOT.
 */
public class Cortina extends Dispositivo{
    /**
     * Construtor
     */
    public Cortina(String nome, boolean estado, float valor) {
        super(nome, 3, estado, valor);
    }
}
