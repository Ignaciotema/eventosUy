package excepciones;

public class EmailRepetido extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailRepetido(String mensaje) {
		super(mensaje);
	}
}