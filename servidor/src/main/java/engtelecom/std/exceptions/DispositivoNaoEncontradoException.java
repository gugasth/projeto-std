package engtelecom.std.exceptions;

public class DispositivoNaoEncontradoException extends RuntimeException {

    public DispositivoNaoEncontradoException(Long id){

        super("Dispositivo nao encontrado com o id: " + id.toString());
    }

    
}