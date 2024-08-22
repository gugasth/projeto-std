package engtelecom.std.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.util.ArrayList;
@Data
public class Ambiente {
    /**
     * dispositivos cadastrados ao ambiente
     */
    private ArrayList<Dispositivo> dispositivos;

    /**
     * identificador único do ambiente
     */
    private Long id;

    /**
     * nome do ambiente
     */
    private String nome;

    public Ambiente(Long id, String nome) {
        this.id = id;
        this.nome = nome;
        this.dispositivos = new ArrayList<Dispositivo>();
    }
}
