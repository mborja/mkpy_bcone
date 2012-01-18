package com.belcorp.entidades;

import java.util.Vector;

import net.rim.device.api.util.Persistable;

public final class EstadisticaIndicador implements Persistable {
	private String seccion;
	private String categoria;
	private double meta;
	private double facturacion;
	private double logro;
	private Vector secciones; // Indicador
	
	public String getSeccion() {
		return seccion;
	}
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public double getMeta() {
		return meta;
	}
	public void setMeta(double meta) {
		this.meta = meta;
	}
	public double getFacturacion() {
		return facturacion;
	}
	public void setFacturacion(double facturacion) {
		this.facturacion = facturacion;
	}
	public double getLogro() {
		return logro;
	}
	public void setLogro(double logro) {
		this.logro = logro;
	}
	public Vector getSecciones() {
		return secciones;
	}
	public void setSecciones(Vector secciones) {
		this.secciones = secciones;
	}
	
}
