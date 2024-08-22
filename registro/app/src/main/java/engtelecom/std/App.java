/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package engtelecom.std;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        // lê o primeiro argumento como inteiro
        int arg = Integer.parseInt(args[0]);

        // verifica se é algum número entre 1 a 5
        switch (arg) {
            case 1:
                ArCondicionado a = new ArCondicionado("Ar condicionado", false, 21);
                a.registraDispositivo();
                break;
            case 2:
                CaixaDeSom b = new CaixaDeSom("Caixa de som", false, 0);
                b.registraDispositivo();
                break;
            case 3:
                Cortina c = new Cortina("Cortina", false, 0);
                c.registraDispositivo();
                break;
            case 4:
                Lampada d = new Lampada("Lâmpada", false, 0);
                d.registraDispositivo();
                break;
            case 5:
                Televisao e = new Televisao("Televisão", false, 0);
                e.registraDispositivo();
                break;
            default:
                System.out.println("Número inválido, você deve digitar um número entre 1 e 5.");
        }
    }
}
