package engtelecom.std.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import engtelecom.std.entities.Ambiente;
import engtelecom.std.exceptions.AmbienteNaoEncontradoException;
import engtelecom.std.service.DispositivoService;

@RestController
@RequestMapping("/ambientes")
public class AmbienteController {

    @Autowired
    private DispositivoService dispositivoService;

    /**
     * Obtém todos os ambientes
     * @return todos os ambientes cadastrados no db
     */
    @GetMapping
    public List<Ambiente> buscarAmbientes() {
        return this.dispositivoService.buscarAmbientes();
    }

    /**
     * Obtém um ambiente pelo id
     * @param id do ambiente a ser procurado
     * @return o ambiente encontrado
     */
    @GetMapping("/{id}")
    public Ambiente buscaAmbientePorId(@PathVariable Long id) {
        Ambiente a = dispositivoService.buscarAmbientePorId(id);
        if (a != null) {
            return a;
        }
        throw new AmbienteNaoEncontradoException(id);
    }

    /**
     * Cadastra um ambiente
     * @param idLong id do ambiente
     * @param nome do ambiente
     * @return o ambiente cadastrado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ambiente adicionarAmbiente(@RequestBody Ambiente a) {
        return this.dispositivoService.cadastrarAmbiente(a);
    }

    /**
     * Adiciona dispositivo ao ambiente
     * @param id do dispositivo a ser adicionado
     * @return o ambiente com o dispositivo adicionado
     */
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Ambiente adicionarDispositivo(@PathVariable Long id, @RequestBody Long idDispositivo) {
        Ambiente a = this.dispositivoService.buscarAmbientePorId(id);
        if (a != null) {
            this.dispositivoService.adicionarDispositivoAoAmbiente(id, idDispositivo);
            return a;
        }
        throw new AmbienteNaoEncontradoException(id);
    }

    /**
     * Liga ou desliga todos os dispositivos que estão cadastrados no ambiente
     * @param id do ambiente a ser ligado/desligado
     * @param estado novo estado (ligado ou desligado)
     * @return o ambiente com o estado atualizado
     * @throws TimeoutException
     * @throws IOException
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Ambiente ligarDesligarAmbiente(@PathVariable Long id, @RequestBody boolean estado) throws IOException, TimeoutException {
        Ambiente a = this.dispositivoService.ligarDesligarAmbiente(id, estado);
        if (a != null) {
            return a;
        }
        throw new AmbienteNaoEncontradoException(id);
    }

    /**
     * Exclui um ambiente
     * @param id do ambiente a ser excluído
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirAmbiente(@PathVariable long id) {
        if (!this.dispositivoService.excluirAmbiente(id)) {
            throw new AmbienteNaoEncontradoException(id);
        }
    }
}
