package com.belcorp.entidades;

import java.util.Vector;

import net.rim.device.api.util.Persistable;

public final class EstadisticaFacturacion implements Persistable {
	private String seccion;
	private double pedido;
	private double peg;
	private double primerPpedido;
	private double pedidoEnviado;
	private double pedidoObservado;
	private double pedidoRechazado;
	private double pedidoFacturado;
	private double montoFacturado;
	private double activasSinPedido;
	private Vector secciones;
	
	public String getSeccion() {
		return seccion;
	}
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}
	public double getPedido() {
		return pedido;
	}
	public void setPedido(double pedido) {
		this.pedido = pedido;
	}
	public double getPeg() {
		return peg;
	}
	public void setPeg(double peg) {
		this.peg = peg;
	}
	public double getPrimerPpedido() {
		return primerPpedido;
	}
	public void setPrimerPpedido(double primerPpedido) {
		this.primerPpedido = primerPpedido;
	}
	public double getPedidoEnviado() {
		return pedidoEnviado;
	}
	public void setPedidoEnviado(double pedidoEnviado) {
		this.pedidoEnviado = pedidoEnviado;
	}
	public double getPedidoObservado() {
		return pedidoObservado;
	}
	public void setPedidoObservado(double pedidoObservado) {
		this.pedidoObservado = pedidoObservado;
	}
	public double getPedidoRechazado() {
		return pedidoRechazado;
	}
	public void setPedidoRechazado(double pedidoRechazado) {
		this.pedidoRechazado = pedidoRechazado;
	}
	public double getPedidoFacturado() {
		return pedidoFacturado;
	}
	public void setPedidoFacturado(double pedidoFacturado) {
		this.pedidoFacturado = pedidoFacturado;
	}
	public double getMontoFacturado() {
		return montoFacturado;
	}
	public void setMontoFacturado(double montoFacturado) {
		this.montoFacturado = montoFacturado;
	}
	public double getActivasSinPedido() {
		return activasSinPedido;
	}
	public void setActivasSinPedido(double activasSinPedido) {
		this.activasSinPedido = activasSinPedido;
	}
	public Vector getSecciones() {
		return secciones;
	}
	public void setSecciones(Vector secciones) {
		this.secciones = secciones;
	}
	
}
