package com.belcorp.entidades;

import net.rim.device.api.util.Persistable;

public final class EstadoConsultora implements Persistable {
	private String id;
	private String descripcion;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}
