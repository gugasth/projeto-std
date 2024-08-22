package engtelecom.std;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.UUID;

/**
 * Classe abstrata que representa um dispositivo IOT.
 */
public abstract class Dispositivo {
    public Dispositivo(String nome, int tipo, boolean estado, float valor) {
        this.nome = nome;
        this.tipo = tipo;
        this.estado = estado;
        this.valor = valor;
        this.uid = UUID.randomUUID();
    }

    /**
     * Identificador único do dispositivo no servidor
     */
    private Long id;

    /**
     * Nome do dispositivo
     */
    private String nome;

    /**
     * Tipo do dispositivo
     */
    private int tipo;

    /**
     * Estado do dispositivo (ligado/desligado)
     */
    private boolean estado;

    /**
     * Valor do dispositivo (temperatura, luminosidade, etc)
     */
    private float valor;

    /**
     * Se o ambiente está cadastrado a um ambiente
     */
    private boolean ambiente;

    /**
     * Nome do ambiente que o dispositivo está cadastrado
     */
    private String nomeAmbiente;

    /**
     * Identificador único do dispositivo antes do registro no sistema
     */
    private UUID uid; 

    // Getter
    public String getNome() {
        return this.nome;
    }


    /**
     * Método que liga ou desliga o dispositivo
     */
    public void ligaDesliga() {
        // se está ligado
        if (this.estado) {
            // então desliga
            this.estado = false;
            // se está desligado
        } else {
            // então liga
            this.estado = true;
        }
    }

    /**
     * Método que retorna o estado do dispositivo
     * @return true se está ligado, false se está desligado
     */
    public boolean getEstado() {
        return this.estado;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Método que faz o registro do dispositivo
     * enviando informações sobre o dispositivo para o servidor através do RabbitMQ
     * após enviar essas informações, se torna o consumidor de mensagens do servidor
     * @throws IOException
     */
        public void registraDispositivo() throws java.io.IOException, java.lang.InterruptedException, TimeoutException {
                // Informações sobre a conexão com o sistema de filas
                ConnectionFactory factory =  Conexao.getConnectionFactory();

                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();

                channel.queueDeclare("registro", false, false, false, null);

                // Inicialmente envia a mensagem referente ao registro
                String mensagemRegistro = this.toString();
                channel.basicPublish("", "registro", null, mensagemRegistro.getBytes());
                System.out.println(" [x] Sent '" + mensagemRegistro + "'");

                // agora aguardo a confirmação de registro
                channel.queueDeclare("apresentacao", false, false, false, null);
                Consumer apresentacaoConsumer = new DefaultConsumer(channel){
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException{
                        String message = new String(body, "UTF-8");
                        // se a primeira string da mensagem for o uid, então faz a ultima string virar o novo id
                        String[] messageSplit = message.split(",");
                        
                        // verifica se a confirmação de registro é para esse dispositivo
                        if (messageSplit[0].equals(uid.toString())){
                            Long novoId = Long.parseLong(messageSplit[2]);
                            setId(novoId); // atualiza id
                            System.out.println("O dispositivo " + messageSplit[0] + " foi registrado com sucesso, seu identificador único para o servidor é " + novoId + ".");
                        }
                    }
                };
                channel.basicConsume("apresentacao", true, apresentacaoConsumer);

                // Agora que o dispositivo está registrado, ele se torna o consumidor de mensagens do servidor
                channel.queueDeclare(String.valueOf(this.getId()), false, false, false, null);
                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                               byte[] body) throws IOException {
                        String message = new String(body, "UTF-8");
                        System.out.println(" [x] Received '" + message + "'");
                    }
                };
                
                channel.queueDeclare(String.valueOf(this.getId()), false, false, false, null);
                channel.basicConsume(String.valueOf(this.getId()), true, consumer);
                // Se torna o consumidor e fica eternamente consumindo por mensagens
                System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            }



    @Override
    public String toString() {
        return uid + "," + nome + "," + tipo + "," + estado + "," + valor;
    }



    public Long getId() {
        return id;
    }







    public void setNome(String nome) {
        this.nome = nome;
    }



    public int getTipo() {
        return tipo;
    }



    public void setTipo(int tipo) {
        this.tipo = tipo;
    }



    public void setEstado(boolean estado) {
        this.estado = estado;
    }



    public float getValor() {
        return valor;
    }



    public void setValor(float valor) {
        this.valor = valor;
    }



    public boolean isAmbiente() {
        return ambiente;
    }



    public void setAmbiente(boolean ambiente) {
        this.ambiente = ambiente;
    }



    public String getNomeAmbiente() {
        return nomeAmbiente;
    }



    public void setNomeAmbiente(String nomeAmbiente) {
        this.nomeAmbiente = nomeAmbiente;
    } 
        
            



}
