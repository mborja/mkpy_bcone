package com.belcorp.entidades;

import net.rim.device.api.util.Persistable;

public class Atraccion implements Persistable {
	private String docIdentidad;
	private String resultado;
	private String fechaConsulta;
	
	public String getDocIdentidad() {
		return docIdentidad;
	}
	public void setDocIdentidad(String docIdentidad) {
		this.docIdentidad = docIdentidad;
	}
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	public void setFechaConsulta(String fechaConsulta) {
		this.fechaConsulta = fechaConsulta;
	}
	public String getFechaConsulta() {
		return fechaConsulta;
	}
	
}
