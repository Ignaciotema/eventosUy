package logica.data_types;

import java.time.LocalDate;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import logica.enumerators.NivelPatrocinio;
import webservices.LocalDateAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class DTPatrocinio {
	private String institucion;
	
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	@XmlSchemaType(name = "date")
	private LocalDate fecha;
	
	private float monto;
	private String codigo;
	private NivelPatrocinio nivelPatrocinio;
    private String tipoRegistroGratis;   // NUEVO
    private  int cantRegsGratis;
	public DTPatrocinio(String institucion, LocalDate fecha, float monto, String codigo, NivelPatrocinio nivelPatrocinio, String tipoRegistroGratis, int cantRegsGratis) {
		super();
		this.institucion = institucion;
		this.fecha = fecha;
		this.monto = monto;
		this.codigo = codigo;
		this.nivelPatrocinio = nivelPatrocinio;
		this.tipoRegistroGratis = tipoRegistroGratis;
		this.cantRegsGratis = cantRegsGratis;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public float getMonto() {
		return monto;
	}
	public String getCodigo() {
		return codigo;
	}
	public NivelPatrocinio getNivelPatrocinio() {
		return nivelPatrocinio;
	}
	public String getTipoRegistroGratis() {
		return tipoRegistroGratis;
	}
	public int getCantRegsGratis() {
		return cantRegsGratis;
	}
	/*public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public void setMonto(float monto) {
		this.monto = monto;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public void setNivelPatrocinio(NivelPatrocinio nivelPatrocinio) {
		this.nivelPatrocinio = nivelPatrocinio;
	}*/
	public String getInstitucion() {
		return institucion;
	}
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public void setMonto(float monto) {
		this.monto = monto;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public void setNivelPatrocinio(NivelPatrocinio nivelPatrocinio) {
		this.nivelPatrocinio = nivelPatrocinio;
	}
	public void setTipoRegistroGratis(String tipoRegistroGratis) {
		this.tipoRegistroGratis = tipoRegistroGratis;
	}
	public void setCantRegsGratis(int cantRegsGratis) {
		this.cantRegsGratis = cantRegsGratis;
	}
	public DTPatrocinio() {
		this.tipoRegistroGratis = "";
		this.cantRegsGratis = 0;
	}
	
	
	
}