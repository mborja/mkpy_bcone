package com.belcorp.entidades;

import net.rim.device.api.util.Persistable;

public final class Pais implements Persistable {
	private String id;
	private String descripcion;
	private String fuerzaVenta;
	
	
	public String getFuerzaVenta() {
		return fuerzaVenta;
	}
	public void setFuerzaVenta(String fuerzaVenta) {
		this.fuerzaVenta = fuerzaVenta;
	}
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
