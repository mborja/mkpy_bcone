package com.belcorp.entidades;

import java.util.Vector;

import net.rim.device.api.util.Persistable;

public final class Configuracion implements Persistable {
	private String tipoDocIdentidad;
	private int maxDocIdentidad;
	private int minDocIdentidad;
	private int minBuscaNombre;
	private int minBuscaCodigo;
	private String simboloMoneda;
	private String simboloDecimal;
	private String simboloMiles;
	private int cantidadDecimales;
	private Vector modulo;
	private Vector subModulo;
	private Vector opcion;
	private String mostrarDocIdentidad;
	private String mostrarAsistencia;
	
	public String getMostrarDocIdentidad() {
		return mostrarDocIdentidad;
	}
	public void setMostrarDocIdentidad(String mostrarDocIdentidad) {
		this.mostrarDocIdentidad = mostrarDocIdentidad;
	}
	public String getMostrarAsistencia() {
		return mostrarAsistencia;
	}
	public void setMostrarAsistencia(String mostrarAsistencia) {
		this.mostrarAsistencia = mostrarAsistencia;
	}
	public Vector getModulo() {
        if ( modulo == null ) 
        	modulo = new Vector();
		return modulo;
	}
	public void setModulo(Vector modulo) {
		this.modulo = modulo;
	}
	public Vector getSubModulo() {
        if ( subModulo == null ) 
        	subModulo = new Vector();
		return subModulo;
	}
	public void setSubModulo(Vector subModulo) {
		this.subModulo = subModulo;
	}
	public Vector getOpcion() {
        if ( opcion == null ) 
        	opcion = new Vector();
		return opcion;
	}
	public void setOpcion(Vector opcion) {
		this.opcion = opcion;
	}
	public int getMinBuscaCodigo() {
		return minBuscaCodigo;
	}
	public String getSimboloMoneda() {
		return simboloMoneda;
	}
	public void setSimboloMoneda(String simboloMoneda) {
		this.simboloMoneda = simboloMoneda;
	}
	public String getSimboloDecimal() {
		return simboloDecimal;
	}
	public void setSimboloDecimal(String simboloDecimal) {
		this.simboloDecimal = simboloDecimal;
	}
	public String getSimboloMiles() {
		return simboloMiles;
	}
	public void setSimboloMiles(String simboloMiles) {
		this.simboloMiles = simboloMiles;
	}
	public int getCantidadDecimales() {
		return cantidadDecimales;
	}
	public void setCantidadDecimales(int cantidadDecimales) {
		this.cantidadDecimales = cantidadDecimales;
	}
	public void setMinBuscaCodigo(int minBuscaCodigo) {
		this.minBuscaCodigo = minBuscaCodigo;
	}
	public String getTipoDocIdentidad() {
		return tipoDocIdentidad;
	}
	public void setTipoDocIdentidad(String tipoDocIdentidad) {
		this.tipoDocIdentidad = tipoDocIdentidad;
	}
	public int getMaxDocIdentidad() {
		return maxDocIdentidad;
	}
	public void setMaxDocIdentidad(int maxDocIdentidad) {
		this.maxDocIdentidad = maxDocIdentidad;
	}
	public int getMinDocIdentidad() {
		return minDocIdentidad;
	}
	public void setMinDocIdentidad(int minDocIdentidad) {
		this.minDocIdentidad = minDocIdentidad;
	}
	public int getMinBuscaNombre() {
		return minBuscaNombre;
	}
	public void setMinBuscaNombre(int minBuscaNombre) {
		this.minBuscaNombre = minBuscaNombre;
	}
}
