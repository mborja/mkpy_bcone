package com.belcorp.entidades;

import com.belcorp.utilidades.Sistema;

import net.rim.device.api.util.Persistable;

public final class Usuario implements Persistable {
	private String codigo;
	private String clave;
	private String Nombre;
	private String zonaRegionPais;
	private String campana;
	private boolean puedeAutoValidar;
//	private String rol;
	private String idPais;
	private String idEmpresa; // es la fuerza de ventas
	//private Configuracion configuracion;
//	private int pin;
//	private String imei;
	private String imsi;
//	private String fv;
//	private String idapp;
	private boolean autovalidar;
	private String versionApp;
	private String urlWS;
	private String fechaHoraServidor;
	private String fechaHoraLocal;
	private long tiempoAutoValidacion;
	private long tiempoDatosConsultora;
	private long tiempoSaldosConsultora;
	private long tiempoTotales;

	private String fechaHoraUltimaSincronizacionConsultora;	
	private String fechaHoraUltimaSincronizacionSaldos;	
	private String fechaHoraUltimaSincronizacionTotales;	
	private String fechaHoraUltimaSincronizacionOtros;	
	private boolean estadoUltimaSincronizacionConsultora;	
	private boolean estadoHoraUltimaSincronizacionSaldos;	
	private boolean estadoHoraUltimaSincronizacionTotales;	
	private boolean estadoHoraUltimaSincronizacionOtros;	
	private boolean sincronizado;
	private String usuarioVersion;
	
	public String getUsuarioVersion() {
		if ( usuarioVersion == null ) {
			return Sistema.getVersion();
		} else {
			return usuarioVersion;
		}
	}
	public void setUsuarioVersion(String usuarioVersion) {
		this.usuarioVersion = usuarioVersion;
	}
	public String getIdPais() {
		return idPais;
	}
	public void setIdPais(String idPais) {
		this.idPais = idPais;
	}
	public String getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public void setFechaHoraUltimaSincronizacionConsultora(
			String fechaHoraUltimaSincronizacionConsultora) {
		this.fechaHoraUltimaSincronizacionConsultora = fechaHoraUltimaSincronizacionConsultora;
	}
	public String getFechaHoraUltimaSincronizacionSaldos() {
		return fechaHoraUltimaSincronizacionSaldos;
	}
	public void setFechaHoraUltimaSincronizacionSaldos(
			String fechaHoraUltimaSincronizacionSaldos) {
		this.fechaHoraUltimaSincronizacionSaldos = fechaHoraUltimaSincronizacionSaldos;
	}
	public String getFechaHoraUltimaSincronizacionTotales() {
		return fechaHoraUltimaSincronizacionTotales;
	}
	public void setFechaHoraUltimaSincronizacionTotales(
			String fechaHoraUltimaSincronizacionTotales) {
		this.fechaHoraUltimaSincronizacionTotales = fechaHoraUltimaSincronizacionTotales;
	}
	public String getFechaHoraUltimaSincronizacionOtros() {
		return fechaHoraUltimaSincronizacionOtros;
	}
	public void setFechaHoraUltimaSincronizacionOtros(
			String fechaHoraUltimaSincronizacionOtros) {
		this.fechaHoraUltimaSincronizacionOtros = fechaHoraUltimaSincronizacionOtros;
	}
	public String getFechaHoraUltimaSincronizacionConsultora() {
		return fechaHoraUltimaSincronizacionConsultora; 
	}
	public String getFechaHoraUltimaSincronizacionConsultoraFormato() {
		String anio = fechaHoraUltimaSincronizacionConsultora.substring(0, 4);
		String mes = fechaHoraUltimaSincronizacionConsultora.substring(4, 6);
		String dia = fechaHoraUltimaSincronizacionConsultora.substring(6, 8);
		String hora = fechaHoraUltimaSincronizacionConsultora.substring(8, 10);
		String min = fechaHoraUltimaSincronizacionConsultora.substring(10, 12);
		//return fechaHoraUltimaSincronizacionConsultora;
		String formato;
		if(Integer.parseInt(hora)>12)
			formato=" P.M.";
		else
			formato=" A.M.";
		
		return anio + "/" + mes + "/" + dia + " " + hora + ":" + min+formato; 
	}
	public boolean isEstadoHoraUltimaSincronizacionOtros() {
		return estadoHoraUltimaSincronizacionOtros;
	}
	public void setEstadoHoraUltimaSincronizacionOtros(
			boolean estadoHoraUltimaSincronizacionOtros) {
		this.estadoHoraUltimaSincronizacionOtros = estadoHoraUltimaSincronizacionOtros;
	}
	public boolean isEstadoUltimaSincronizacionConsultora() {
		return estadoUltimaSincronizacionConsultora;
	}
	public void setEstadoUltimaSincronizacionConsultora(
			boolean estadoUltimaSincronizacionConsultora) {
		this.estadoUltimaSincronizacionConsultora = estadoUltimaSincronizacionConsultora;
	}
	public boolean isEstadoHoraUltimaSincronizacionSaldos() {
		return estadoHoraUltimaSincronizacionSaldos;
	}
	public void setEstadoHoraUltimaSincronizacionSaldos(
			boolean estadoHoraUltimaSincronizacionSaldos) {
		this.estadoHoraUltimaSincronizacionSaldos = estadoHoraUltimaSincronizacionSaldos;
	}
	public boolean isEstadoHoraUltimaSincronizacionTotales() {
		return estadoHoraUltimaSincronizacionTotales;
	}
	public void setEstadoHoraUltimaSincronizacionTotales(
			boolean estadoHoraUltimaSincronizacionTotales) {
		this.estadoHoraUltimaSincronizacionTotales = estadoHoraUltimaSincronizacionTotales;
	}
	public void setSincronizado(boolean sincronizado) {
		this.sincronizado = sincronizado;
	}
	public boolean isSincronizado() {
		return sincronizado;
	}
	
	public long getTiempoTotales() {
		return tiempoTotales;
	}
	public void setTiempoTotales(long tiempoTotales) {
		this.tiempoTotales = tiempoTotales;
	}
	public long getTiempoDatosConsultora() {
		return tiempoDatosConsultora;
	}
	public void setTiempoDatosConsultora(long tiempoDatosConsultora) {
		this.tiempoDatosConsultora = tiempoDatosConsultora;
	}
	public long getTiempoSaldosConsultora() {
		return tiempoSaldosConsultora;
	}
	public void setTiempoSaldosConsultora(long tiempoSaldosConsultora) {
		this.tiempoSaldosConsultora = tiempoSaldosConsultora;
	}
	
	public long getTiempoAutoValidacion() {
		return tiempoAutoValidacion;
	}
	public void setTiempoAutoValidacion(long tiempoAutoValidacion) {
		this.tiempoAutoValidacion = tiempoAutoValidacion;
	}
	
	public String getFechaHoraServidor() {
		return fechaHoraServidor;
	}
	public void setFechaHoraServidor(String fechaHoraServidor) {
		this.fechaHoraServidor = fechaHoraServidor;
	}
	public String getFechaHoraLocal() {
		return fechaHoraLocal;
	}
	public void setFechaHoraLocal(String fechaHoraLocal) {
		this.fechaHoraLocal = fechaHoraLocal;
	}	
	public String getVersionApp() {
		return versionApp;
	}
	public void setVersionApp(String versionApp) {
		this.versionApp = versionApp;
	}
	public String getUrlWS() {
		return urlWS;
	}
	public void setUrlWS(String url) {
		this.urlWS = url;
	}
	
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
//	public Configuracion getConfiguracion() {
//		return configuracion;
//	}
//	public void setConfiguracion(Configuracion configuracion) {
//		this.configuracion = configuracion;
//	}
	public String getNombre() {
		return Nombre;
	}
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getZonaRegionPais() {
		return zonaRegionPais;
	}
	public void setZonaRegionPais(String zonaRegionPais) {
		this.zonaRegionPais = zonaRegionPais;
	}
	public String getCampanaFormato() {
		if ( campana.length() > 4 ) {
	    	return "C" + campana.substring(4) + "/" + campana.substring(0, 4);
		} else {
			//TODO: cambiar valor en duro
			return "C07/2011";
		}
		//return campana;
	}
	public String getCampana() {
		if ( campana.equals("") ) {
			//TODO: cambiar valor en duro
			return "201107";
		} else {
			return campana;
		}
	}
	public void setCampana(String campana) {
		this.campana = campana;
	}
	public boolean getPuedeAutoValidar() {
		return puedeAutoValidar; 
	}
	public void setPuedeAutoValidar(boolean puedeAutoValidar) {
		this.puedeAutoValidar = puedeAutoValidar;
	}
	public void setAutovalidar(boolean autovalidar) {
		this.autovalidar = autovalidar;
	}
	public boolean getAutovalidar() {
		return autovalidar;
	}
}
