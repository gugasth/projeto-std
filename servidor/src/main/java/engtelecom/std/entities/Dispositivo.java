package engtelecom.std.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * é um pojo, tem q ter getters e setters pra tudo
 */
@Data
public class Dispositivo {
    

    public Dispositivo(Long id, String nome, int tipo, boolean estado, float valor) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.estado = estado;
        this.valor = valor;
        this.ambiente = false;
        this.nomeAmbiente = "";
    }

    /**
     * Identificador único do dispositivo
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
     * Se o dispositivo está cadastrado a um ambiente
     */
    private boolean ambiente;

    /**
     * Nome do ambiente que o dispositivo está cadastrado
     */
    private String nomeAmbiente;

    
}