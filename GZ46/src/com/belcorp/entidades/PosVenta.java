package com.belcorp.entidades;

import net.rim.device.api.util.Persistable;

public final class PosVenta implements Persistable {
	private String codigo;
	private String campanaOrigen;
	private String campanaProceso;
	private String producto; //TODO: es codigoventa?
	private String unidad;
	private String operacion;
	private String resultado;
	private String motivo;
	
	public String getCodigo(){
		return codigo;
	}
	public void setCodigo(String codigo){
		this.codigo=codigo;
	}
	public String getCampanaOrigen() {
    	return "C" + campanaOrigen.substring(4) + "/" + campanaOrigen.substring(0, 4);
		//return campanaOrigen;
	}
	public void setCampanaOrigen(String campanaOrigen) {
		this.campanaOrigen = campanaOrigen;
	}
	public String getCampanaProceso() {
    	return "C" + campanaProceso.substring(4) + "/" + campanaProceso.substring(0, 4);
		//return campanaProceso;
	}
	public void setCampanaProceso(String campanaProceso) {
		this.campanaProceso = campanaProceso;
	}
	public String getProducto() {
		return producto;
	}
	public void setProducto(String producto) {
		this.producto = producto;
	}
	public String getUnidad() {
		return unidad;
	}
	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}
	public String getOperacion() {
		return operacion;
	}
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
}
