package engtelecom.std;

/**
 * Classe que representa uma televisão, que é um dispositivo IOT.
 */
public class Televisao extends Dispositivo{
    /**
     * Construtor
     */
    public Televisao(String nome, boolean estado, float valor) {
        super(nome, 5, estado, valor);
    }
}