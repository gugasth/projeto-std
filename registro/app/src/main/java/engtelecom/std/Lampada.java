package engtelecom.std;

/**
 * Classe que representa uma lampada, que é um dispositivo IOT.
 */
public class Lampada extends Dispositivo{
    // Construtor
    public Lampada(String nome, boolean estado, float valor) {
        super(nome, 4, estado, valor);
    }
}
