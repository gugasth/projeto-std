package engtelecom.std.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import engtelecom.std.entities.Ambiente;
import engtelecom.std.entities.Conexao;
import engtelecom.std.entities.Dispositivo;


/**
* DispositivoService é uma classe que simula um banco de dados.
*
* A anotação @Component indica que a classe DispositivoService é um componente do
* Spring. Isso significa que o Spring irá gerenciar as instâncias dessa classe
* e irá injetá-las onde for necessário.
*
* Classes anotadas com @Component são chamadas de beans. E são singleton por
* padrão, ou seja, o Spring irá criar apenas uma instância dessa classe e irá
* compartilhá-la entre todos os componentes que a utilizarem.
*
* https://java-design-patterns.com/patterns/singleton/
*
*/
@Component
public class DispositivoService {

    private ArrayList<Dispositivo> listaDispositivos;
    private AtomicLong contadorDispositivos;
    private AtomicLong contadorAmbientes;
    private ArrayList<Ambiente> listaAmbientes;
    

    public DispositivoService(){
        this.listaDispositivos = new ArrayList<>();
        this.contadorDispositivos = new AtomicLong();   
        this.listaAmbientes = new ArrayList<>(); 
        this.contadorAmbientes = new AtomicLong();

        this.cadastrarDispositivo(new Dispositivo(null, "Lâmpada", 1, false, 0));
        this.cadastrarDispositivo(new Dispositivo(null, "Ar condicionado", 2, false, 0));
    }

    
    /**
     * Busca todos os dispositivos
     * @return todos os dispositivos cadastrados no db
     */
    public List<Dispositivo> buscarDispositivos(){
        return this.listaDispositivos;
    }

    /**
     * Cadastra um dispositivo no db
     * @param d dispositivo a ser cadastrado no db
     * @return o dispositivo cadastrado.
     */
    public Dispositivo cadastrarDispositivo(Dispositivo d){

        d.setId(this.contadorDispositivos.incrementAndGet());
        this.listaDispositivos.add(d);
        return d;
    }
    

    /**
     * Busca um dispositivo específico
     * verifica se algum dispositivo na agenda tem o mesmo id do id fornecido como parametro, caso sim, retorna o Dispositivo.
     * @param id do dispositivo a ser buscado
     * @return o id do dispositivo, caso possível, senão retorna null. 
     */
    public Dispositivo buscarPorId(Long id){
        for (Dispositivo d : listaDispositivos){
            if (d.getId() == id){
                return d;
            }
        }
        return null;
    }

    /**
     * Atualiza um dispositivo
     * @param dispositivo dispositivo a ser atualizado
     * @return o dispositivo atualizado
     * @throws TimeoutException
     * @throws IOException
     */
    public Dispositivo ligarDesligarDispositivo(Long id, boolean estado) throws IOException, TimeoutException {
        Dispositivo d = buscarPorId(id);
        String msg;
        if (d != null) {
            d.setEstado(estado); // liga ou desliga
            if (estado == true){
                msg = "Ligando dispositivo";
            } else {
                msg = "Desligando dispositivo";
            }
            enviarMensagem(msg, id);
        }
        return d;
    }

    /**
     * Altera o valor de um dispositivo, seja temperatura, luminosidade, volume, etc...
     * @param dispositivo dispositivo a ser alterado
     * @return o dispositivo com o valor atualizado
     * @throws TimeoutException
     * @throws IOException
     */
    public Dispositivo alterarValorDispositivo(Long id, float valor, boolean estado) throws IOException, TimeoutException {
        Dispositivo d = buscarPorId(id);
        if (d != null) {
            d.setValor(valor);
            d.setEstado(estado);
            enviarMensagem("Alterando para: " + valor , id);
            enviarMensagem("Alterando para: " + estado, id);
        }
        return d;
    }

    /**
     * Exclui um dispositivo
     * @param id identificador único do dispositivo a ser excluido
     * @return true caso ele tenha sido excluido, false caso contrário, ele será excluído caso ele exista.
     * @throws TimeoutException
     * @throws IOException
     */
    public boolean excluirDispositivo(Long id) throws IOException, TimeoutException {
        Dispositivo d = buscarPorId(id);
        if (d != null) {
            listaDispositivos.remove(d);
            enviarMensagem("Dispositivo removido do servidor", id);
        return true;
        }
        return false;
    }

    /**
     * Busca todos os ambientes
     */
    public List<Ambiente> buscarAmbientes(){
        return this.listaAmbientes;
    }

    /**
     * Cadastra um ambiente
     */
    public Ambiente cadastrarAmbiente(Ambiente a){
        a.setId(this.contadorAmbientes.incrementAndGet());
        this.listaAmbientes.add(a);
        return a;
    }

    /**
     * Busca um ambiente específico
     */
    public Ambiente buscarAmbientePorId(Long id){
        for (Ambiente a : listaAmbientes){
            if (a.getId() == id){
                return a;
            }
        }
        return null;
    }

    /**
     * Liga ou desliga todos os dispositivos que estão cadastrados no ambiente
     * @param ambiente ambiente a ser ligado ou desligado
     * @return o ambiente com o estado atualizado
     * @throws TimeoutException
     * @throws IOException
     */
    public Ambiente ligarDesligarAmbiente(Long id, boolean estado) throws IOException, TimeoutException {
        Ambiente a = buscarAmbientePorId(id);
   
        if (a != null) {
            for (Dispositivo dispositivo : a.getDispositivos()) {
                ligarDesligarDispositivo(dispositivo.getId(), estado);
            }
            return a;
        } else {
            return null;
        }

    }

    /**
     * Exclui um ambiente
     */
    public boolean excluirAmbiente(Long id) {
        Ambiente a = buscarAmbientePorId(id);
        if (a != null) {
            listaAmbientes.remove(a);
        return true;
        }
        return false;
    }

    /**
     * Adiciona um dispositivo ao ambiente
     * @param idAmbiente identificador único do ambiente
     * @param idDispositivo identificador único do dispositivo
     * @return o ambiente com o dispositivo adicionado
     */
    public Ambiente adicionarDispositivoAoAmbiente(Long idAmbiente, Long idDispositivo) {
        for (Ambiente ambiente : listaAmbientes) {
            if (ambiente.getId() == idAmbiente) {
                for (Dispositivo dispositivo : listaDispositivos) {
                    if (dispositivo.getId() == idDispositivo) {
                        // adiciona o dispositivo ao ambiente
                        ambiente.getDispositivos().add(dispositivo);
                        return ambiente;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Método que envia uma mensagem para um dispositivo específico
     * @param mensagem mensagem a ser enviada
     * @param idLong identificador único do dispositivo (nome da fila)
     */
    public void enviarMensagem(String mensagem, Long idLong) throws IOException, TimeoutException {
        ConnectionFactory factory =  Conexao.getConnectionFactory();

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(String.valueOf(idLong), false, false, false, null);

        channel.basicPublish("", String.valueOf(idLong), null, mensagem.getBytes());
        System.out.println(" [x] Sent '" + mensagem + "'");
    }
    
}