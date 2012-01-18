package com.belcorp.entidades;

import java.util.Vector;

import net.rim.device.api.util.Persistable;

public final class Facturacion implements Persistable {
	private String ubicacion;
	private String campana;
	private double pedidos;
	private double peg;
	private double primerpedido;
	private double enviado;
	private double observado;
	private double rechazado;
	private double facturado;
	private double monto;
	private double activassinpedido;
	private Vector facturacion;
	
	public Vector getFacturacion() {
		if ( facturacion == null )
			facturacion = new Vector();
		return facturacion;
	}
	public void setFacturacion(Vector facturacion) {
		this.facturacion = facturacion;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	public String getCampana() {
    	return "C" + campana.substring(4) + "/" + campana.substring(0, 4);
		//return campana;
	}
	public void setCampana(String campana) {
		this.campana = campana;
	}	
	public double getPedidos() {
		return pedidos;
	}
	public void setPedidos(double pedidos) {
		this.pedidos = pedidos;
	}
	public double getPeg() {
		return peg;
	}
	public void setPeg(double peg) {
		this.peg = peg;
	}
	public double getPrimerpedido() {
		return primerpedido;
	}
	public void setPrimerpedido(double primerpedido) {
		this.primerpedido = primerpedido;
	}
	public double getEnviado() {
		return enviado;
	}
	public void setEnviado(double enviado) {
		this.enviado = enviado;
	}
	public double getObservado() {
		return observado;
	}
	public void setObservado(double observado) {
		this.observado = observado;
	}
	public double getRechazado() {
		return rechazado;
	}
	public void setRechazado(double rechazado) {
		this.rechazado = rechazado;
	}
	public double getFacturado() {
		return facturado;
	}
	public void setFacturado(double facturado) {
		this.facturado = facturado;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
	public double getActivassinpedido() {
		return activassinpedido;
	}
	public void setActivassinpedido(double activassinpedido) {
		this.activassinpedido = activassinpedido;
	}

}
