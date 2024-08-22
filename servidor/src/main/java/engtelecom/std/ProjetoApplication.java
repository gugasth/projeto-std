package engtelecom.std;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import engtelecom.std.entities.Conexao;
import engtelecom.std.entities.Dispositivo;
import engtelecom.std.service.DispositivoService;

@SpringBootApplication
public class ProjetoApplication implements Runnable {

    private final DispositivoService dispositivoService;

    @Autowired
    public ProjetoApplication(DispositivoService dispositivoService) {
        this.dispositivoService = dispositivoService;
    }

    private Channel channelMessage;

    // Método que vai ser executado pela thread.
    // Fica aguardando registros dos dispositivos
    @Override
    public void run() {
        ConnectionFactory factory = Conexao.getConnectionFactory();
        try (Connection connection = factory.newConnection()){
            channelMessage = connection.createChannel();
            channelMessage.queueDeclare("registro", false, false, false, null);
            channelMessage.basicConsume("registro", true, new DefaultConsumer(channelMessage) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String receivedMessage = new String(body, "UTF-8");
                    // Registra o dispositivo quando ele se apresenta.
                    System.out.println("-----------------");
                    System.out.println("O dispositivo " + receivedMessage + " se apresentou e será registrado em breve.");

                    // Separa a received message usando vírgulas como delimitador
                    String[] receivedMessageSplit = receivedMessage.split(",");

                    System.out.println("-----------------");

                    // Atribui id, nome, tipo, estado e valor
                    String idAntigo = receivedMessageSplit[0]; // recebe uid
                    UUID idAntigoUUID = UUID.fromString(idAntigo);

                    String nome = receivedMessageSplit[1];

                    int tipo = Integer.parseInt(receivedMessageSplit[2]);

                    boolean estado = Boolean.parseBoolean(receivedMessageSplit[3]);

                    float valor = Float.parseFloat(receivedMessageSplit[4]);

                    Dispositivo d = new Dispositivo(1L, nome, tipo, estado, valor);
                    
                    d = dispositivoService.cadastrarDispositivo(d);
                    Long idNovo = d.getId();
                    System.out.println("O dispositivo " + receivedMessage + " foi registrado com sucesso, seu identificador único para o servidor é " + idNovo + ".");
                    
                    // Envia mensagem de confirmação de registro, com o id novo
                    String mensagemConfirmacao = idAntigoUUID + ", " + "você foi registrado como," + idNovo;
                    channelMessage.queueDeclare("apresentacao", false, false, false, null);
                    channelMessage.basicPublish("", "apresentacao", null, mensagemConfirmacao.getBytes());
                    System.out.println(" [x] Sent '" + mensagemConfirmacao + "'");
                }
            });

            while (true) {
                // Espera 1 segundo antes de verificar se mais algum dispositivo se registrou
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("Ocorreu um erro na thread\n");
        }
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ProjetoApplication.class);
        ConfigurableApplicationContext context = app.run(args);

        // Obtendo a instância do db do contexto do Spring
        DispositivoService dispositivoService = context.getBean(DispositivoService.class);

        ProjetoApplication projetoApplication = new ProjetoApplication(dispositivoService);
        
        // Cria e inicia a thread
        Thread registroThread = new Thread(projetoApplication);
        registroThread.start();
    }
}
