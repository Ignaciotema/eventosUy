package excepciones;

public class TipoRegistroExistenteExcepcion extends Exception {
    private static final long serialVersionUID = 1L;

    public TipoRegistroExistenteExcepcion() {
        super("El tipo de registro ya existe");
    }

    public TipoRegistroExistenteExcepcion(String message) {
        super(message);
    }

    public TipoRegistroExistenteExcepcion(String message, Throwable cause) {
        super(message, cause);
    }

    public TipoRegistroExistenteExcepcion(Throwable cause) {
        super(cause);
    }
}