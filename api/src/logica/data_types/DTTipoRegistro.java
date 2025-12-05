package logica.data_types;

public class DTTipoRegistro {
	private String nombre;
	private String descripcion;
	private float costo;
	private int cupo;
	
	
	public DTTipoRegistro(String nombre, String descripcion, float cost, int cupo) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.costo = cost;
		this.cupo = cupo;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public DTTipoRegistro() {
	}

	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public float getCosto() {
		return costo;
	}
	public void setCosto(float costo) {
		this.costo = costo;
	}
	public int getCupo() {
		return cupo;
	}
	public void setCupo(int cupo) {
		this.cupo = cupo;
	}
	
	public boolean verificarCupo() {
		return !(this.cupo == 0);
	}
	
	public DTTipoRegistro infoTipoRegistro() {
		
		DTTipoRegistro dtTipoRegistro = new DTTipoRegistro(nombre, descripcion, costo, cupo);
		return dtTipoRegistro;
		
	}
}
