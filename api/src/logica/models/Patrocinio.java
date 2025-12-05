package logica.models;

import java.time.LocalDate;

import logica.enumerators.NivelPatrocinio;

public class Patrocinio  {

    private LocalDate fecha;
    private int monto;
    private String codigo;
    private int cantRegsGratis;
    private NivelPatrocinio nivelPatrocinio;
    private String tipoRegistroGratis;
    
	public Patrocinio(LocalDate fecha, int monto, String codigo, int cantRegsGratis, NivelPatrocinio nivelPatrocinio, String tipoRegistroGratis) {
		super();
		this.fecha = fecha;
		this.monto = monto;
		this.codigo = codigo;
		this.cantRegsGratis = cantRegsGratis;
		this.nivelPatrocinio = nivelPatrocinio;
        this.tipoRegistroGratis = tipoRegistroGratis;
	}

	public LocalDate getFecha() {
		return fecha;
	}



	public int getMonto() {
		return monto;
	}


	public String getCodigo() {
		return codigo;
	}



	public int getCantRegsGratis() {
		return cantRegsGratis;
	}



	public NivelPatrocinio getNivelPatrocinio() {
		return nivelPatrocinio;
	}

	

	public String getTipoRegistroGratis() {
		return tipoRegistroGratis;
	}

	
	
	
    
	
}
