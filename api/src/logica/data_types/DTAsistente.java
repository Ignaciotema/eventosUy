package logica.data_types;

import java.time.LocalDate;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import webservices.LocalDateAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class DTAsistente extends DataUsuario {
	
	private String apellido;
	
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	@XmlSchemaType(name = "date")
	private LocalDate fechaNacimiento;
	
	private String institucion;

	public DTAsistente(String nickname, String nombre, String email, String apellido, LocalDate fechaNacimiento, String institucion) {
		super(nickname, nombre, email, TipoUsuario.ASISTENTE);
		this.apellido = apellido;
		this.fechaNacimiento = fechaNacimiento;
		this.institucion = institucion;
	}

	public DTAsistente() {
		super();
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getInstitucion() {
		return institucion;
	}

	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}
}