package com.belcorp.entidades;

import net.rim.device.api.util.Persistable;

public class Estadistica implements Persistable {
	private String opcion;
	private int cantidad;
	private String fechaHora;
	
	public String getOpcion() {
		return opcion;
	}
	public void setOpcion(String opcion) {
		this.opcion = opcion;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public String getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}

}
