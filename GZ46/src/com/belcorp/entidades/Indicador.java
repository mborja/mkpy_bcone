package com.belcorp.entidades;

import java.util.Vector;

import net.rim.device.api.util.Persistable;

public final class Indicador implements Persistable {
	private String ubicacion;
	private String campana;
	private double cantidadPedidosVenta;
	private double cantidadPediosFacturado;
	private double porcentajeCantidadPedidoLogrado;
	private double montoPedidoVenta;
	private double montoPedidoFacturado;
	private double porcentajeMontoPedidoLogrado;
	private double ingresosestimados;//fuent d ventas
	private double ingresosreal;//facturado
	private double porcentajPrimerPedidoLogrado; //fac vs fuent d vntas
	private double montoVentaNeta;
	private double cantidadConsultorasActivas;
	private double porcentajeActividad;
	private double cantidadConsultorasReingreso;
	private double cantidadConsultorasEgreso;
	private double capitalizacion;
	private double porcentajeRotacion;
	private double porcentajeRetencion;//retencion de nuevas.
	private Vector indicador;
	
	public String getCampana() {
    	return "C" + campana.substring(4) + "/" + campana.substring(0, 4);
		//return campana;
	}
	public void setCampana(String campana) {
		this.campana = campana;
	}
	public Vector getIndicador() {
		if ( indicador == null ) 
			indicador = new Vector();
		return indicador;
	}
	public void setIndicador(Vector indicador) {
		this.indicador = indicador;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	public double getCantidadPedidosVenta() {
		return cantidadPedidosVenta;
	}
	public void setCantidadPedidosVenta(double cantidadPedidosVenta) {
		this.cantidadPedidosVenta = cantidadPedidosVenta;
	}
	public double getCantidadPediosFacturado() {
		return cantidadPediosFacturado;
	}
	public void setCantidadPediosFacturado(double cantidadPediosFacturado) {
		this.cantidadPediosFacturado = cantidadPediosFacturado;
	}
	public double getPorcentajeCantidadPedidoLogrado() {
		return porcentajeCantidadPedidoLogrado;
	}
	public void setPorcentajeCantidadPedidoLogrado(
			double porcentajeCantidadPedidoLogrado) {
		this.porcentajeCantidadPedidoLogrado = porcentajeCantidadPedidoLogrado;
	}
	public double getMontoPedidoVenta() {
		return montoPedidoVenta;
	}
	public void setMontoPedidoVenta(double montoPedidoVenta) {
		this.montoPedidoVenta = montoPedidoVenta;
	}
	public double getMontoPedidoFacturado() {
		return montoPedidoFacturado;
	}
	public void setMontoPedidoFacturado(double montoPedidoFacturado) {
		this.montoPedidoFacturado = montoPedidoFacturado;
	}
	public double getPorcentajeMontoPedidoLogrado() {
		return porcentajeMontoPedidoLogrado;
	}
	public void setPorcentajeMontoPedidoLogrado(double porcentajeMontoPedidoLogrado) {
		this.porcentajeMontoPedidoLogrado = porcentajeMontoPedidoLogrado;
	}
	public double getIngresosestimados() {
		return ingresosestimados;
	}
	public void setIngresosestimados(double ingresosestimados) {
		this.ingresosestimados = ingresosestimados;
	}
	public double getIngresosreal() {
		return ingresosreal;
	}
	public void setIngresosreal(double ingresosreal) {
		this.ingresosreal = ingresosreal;
	}
	public double getPorcentajPrimerPedidoLogrado() {
		return porcentajPrimerPedidoLogrado;
	}
	public void setPorcentajPrimerPedidoLogrado(double porcentajPrimerPedidoLogrado) {
		this.porcentajPrimerPedidoLogrado = porcentajPrimerPedidoLogrado;
	}
	public double getMontoVentaNeta() {
		return montoVentaNeta;
	}
	public void setMontoVentaNeta(double montoVentaNeta) {
		this.montoVentaNeta = montoVentaNeta;
	}
	public double getCantidadConsultorasActivas() {
		return cantidadConsultorasActivas;
	}
	public void setCantidadConsultorasActivas(double cantidadConsultorasActivas) {
		this.cantidadConsultorasActivas = cantidadConsultorasActivas;
	}
	public double getPorcentajeActividad() {
		return porcentajeActividad;
	}
	public void setPorcentajeActividad(double porcentajeActividad) {
		this.porcentajeActividad = porcentajeActividad;
	}
	public double getCantidadConsultorasReingreso() {
		return cantidadConsultorasReingreso;
	}
	public void setCantidadConsultorasReingreso(double cantidadConsultorasReingreso) {
		this.cantidadConsultorasReingreso = cantidadConsultorasReingreso;
	}
	public double getCantidadConsultorasEgreso() {
		return cantidadConsultorasEgreso;
	}
	public void setCantidadConsultorasEgreso(double cantidadConsultorasEgreso) {
		this.cantidadConsultorasEgreso = cantidadConsultorasEgreso;
	}
	public double getCapitalizacion() {
		return capitalizacion;
	}
	public void setCapitalizacion(double capitalizacion) {
		this.capitalizacion = capitalizacion;
	}
	public double getPorcentajeRotacion() {
		return porcentajeRotacion;
	}
	public void setPorcentajeRotacion(double porcentajeRotacion) {
		this.porcentajeRotacion = porcentajeRotacion;
	}
	public double getPorcentajeRetencion() {
		return porcentajeRetencion;
	}
	public void setPorcentajeRetencion(double porcentajeRetencion) {
		this.porcentajeRetencion = porcentajeRetencion;
	}
}
