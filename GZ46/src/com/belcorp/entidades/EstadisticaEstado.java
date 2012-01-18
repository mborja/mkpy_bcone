package com.belcorp.entidades;

import java.util.Vector;

import net.rim.device.api.util.Persistable;

public final class EstadisticaEstado implements Persistable {
	private String seccion;
	private String estado;
	private double valor;
	private Vector secciones;
	
	public String getSeccion() {
		return seccion;
	}
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public Vector getSecciones() {
		return secciones;
	}
	public void setSecciones(Vector secciones) {
		this.secciones = secciones;
	}
	
}
