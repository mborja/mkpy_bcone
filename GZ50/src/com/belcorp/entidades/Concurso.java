package com.belcorp.entidades;

import net.rim.device.api.util.Persistable;

public final class Concurso implements Persistable {
	private String codigo;
	private String descripcion;
	private String puntaje;
	private String resultado;
	private String nivel;
	private String premio;
	private String campanaInicio;
	private String campanaFin; 
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getPuntaje() {
		return puntaje;
	}
	public void setPuntaje(String puntaje) {
		this.puntaje = puntaje;
	}
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public String getPremio() {
		return premio;
	}
	public void setPremio(String premio) {
		this.premio = premio;
	}
	public String getCampanaInicio() {
    	return "C" + campanaInicio.substring(4) + "/" + campanaInicio.substring(0, 4);
		//return campanaInicio;
	}
	public void setCampanaInicio(String campanaInicio) {
		this.campanaInicio = campanaInicio;
	}
	public String getCampanaFin() {
    	return "C" + campanaFin.substring(4) + "/" + campanaFin.substring(0, 4);
		//return campanaFin;
	}
	public void setCampanaFin(String campanaFin) {
		this.campanaFin = campanaFin;
	}

}
