package com.belcorp.entidades;

import net.rim.device.api.util.Persistable;

public final class Postulante implements Persistable {
	private String documento;
	private String resultado;
	
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

}
