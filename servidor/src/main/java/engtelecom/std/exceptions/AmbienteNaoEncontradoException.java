package engtelecom.std.exceptions;

public class AmbienteNaoEncontradoException extends RuntimeException {

    public AmbienteNaoEncontradoException(Long id){

        super("Ambiente não encontrado com o id: " + id.toString());
    }

    
}