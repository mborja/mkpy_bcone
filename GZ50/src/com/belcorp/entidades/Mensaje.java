package com.belcorp.entidades;

import net.rim.device.api.util.Persistable;

public final class Mensaje implements Persistable {
	private String id;
	private String mensaje;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
}
