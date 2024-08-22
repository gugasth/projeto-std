package engtelecom.std.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import engtelecom.std.entities.Ambiente;
import engtelecom.std.entities.Dispositivo;
import engtelecom.std.exceptions.AmbienteNaoEncontradoException;
import engtelecom.std.exceptions.DispositivoNaoEncontradoException;
import engtelecom.std.service.DispositivoService;

@RestController
@RequestMapping("/dispositivos")
public class DispositivoController {

    @Autowired
    private DispositivoService dispositivoService;

    /**
     * Obtém todos os dispositivos
     * @return todos os dispositivos cadastrados no db
     */
    @GetMapping
    public List<Dispositivo> buscarTodas() {
        return this.dispositivoService.buscarDispositivos();
    }

    /**
     * Obtém um dispositivo pelo id
     * @param id do dispositivo a ser procurado
     * @return o dispositivo encontrado
     */
    @GetMapping("/{id}")
    public Dispositivo buscaDispositivoPorId(@PathVariable Long id) {
        Dispositivo d = dispositivoService.buscarPorId(id);
        if (d != null) {
            return d;
        }
        throw new DispositivoNaoEncontradoException(id);
    }

    /**
     * Cadastra um dispositivo
     * @param dispositivo a ser cadastrado
     * @return o dispositivo cadastrado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Dispositivo adicionarDispositivo(@RequestBody Dispositivo dispositivo) {
        return this.dispositivoService.cadastrarDispositivo(dispositivo);
    }

    
    /**
     * Altera o valor de um dispositivo
     * @param id do dispositivo a ser alterado
     * @param dispositivo com o novo valor
     * @return
     * @throws TimeoutException
     * @throws IOException
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Dispositivo alterarValorDispositivo(@PathVariable Long id, @RequestBody Map<String, Object> requestBody) throws IOException, TimeoutException {
        float valor = Float.parseFloat(requestBody.get("valor").toString());
        boolean estado = (boolean) requestBody.get("estado");

        Dispositivo d = this.dispositivoService.alterarValorDispositivo(id, valor, estado);
        if (d != null) {
            return d;
        }
        throw new DispositivoNaoEncontradoException(id);
    }


    /**
     * Exclui um dispositivo
     * @param id do dispositivo a ser excluído
     * @throws TimeoutException
     * @throws IOException
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirDispositivo(@PathVariable long id) throws IOException, TimeoutException {
        if (!this.dispositivoService.excluirDispositivo(id)) {
            throw new DispositivoNaoEncontradoException(id);
        }
    }
}
