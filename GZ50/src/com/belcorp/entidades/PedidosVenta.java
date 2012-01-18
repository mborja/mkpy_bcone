package com.belcorp.entidades;

import net.rim.device.api.util.Persistable;

public final class PedidosVenta implements Persistable {
	private String codigo;
	private String campana;
	private double venta;
	private double ganancia;
	private String asistencia;
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getCampana() {
    	return campana;
	}
	public String getCampanaFormato() {
    	return "C" + campana.substring(4) + "/" + campana.substring(0, 4);
	}
	public void setCampana(String campana) {
		this.campana = campana;
	}
	public double getVenta() {
		return venta;
	}
	public void setVenta(double venta) {
		this.venta = venta;
	}
	public double getGanancia() {
		return ganancia;
	}
	public void setGanancia(double ganancia) {
		this.ganancia = ganancia;
	}
	public String getAsistencia() {
		if ( asistencia.equals("1") ) {
			return "Si";
		} else {
			return "No";
		}
	}
	public void setAsistencia(String asistencia) {
		this.asistencia = asistencia;
	}
	
}
