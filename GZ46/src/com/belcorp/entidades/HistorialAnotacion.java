package com.belcorp.entidades;

import net.rim.device.api.util.Persistable;

public class HistorialAnotacion implements Persistable {
	private String fechaHora;
	private String anotacion;

	public String getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}
	public String getAnotacion() {
		return anotacion;
	}
	public void setAnotacion(String anotacion) {
		this.anotacion = anotacion;
	}
	
	

}
