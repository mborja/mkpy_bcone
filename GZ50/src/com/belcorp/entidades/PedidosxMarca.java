package com.belcorp.entidades;

import net.rim.device.api.util.Persistable;

public final class PedidosxMarca implements Persistable {
	private String codigo;
	private String campana;
	
	private double ventaEsika;
	private double porcentajeVentaEsika;

	private double ventaEbel;
	private double porcentajeVentaEbel;

	private double ventaCyzone;
	private double porcentajeVentaCyzone;
	
	private double ventaOtros;
	private double porcentajeOtros;
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public double getVentaOtros() {
		return ventaOtros;
	}
	public void setVentaOtros(double ventaOtros) {
		this.ventaOtros = ventaOtros;
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
	public void setVentaEsika(double ventaEsika) {
		this.ventaEsika = ventaEsika;
	}
	public double getVentaEsika() {
		return ventaEsika;
	}

	public void setPorcentajeVentaEsika(double porcentajeVentaEsika) {
		this.porcentajeVentaEsika = porcentajeVentaEsika;
	}

	public double getPorcentajeVentaEsika() {
		return porcentajeVentaEsika;
	}
	public void setVentaEbel(double ventaEbel) {
		this.ventaEbel = ventaEbel;
	}
	public double getVentaEbel() {
		return ventaEbel;
	}
	public void setPorcentajeVentaEbel(double porcentajeVentaEbel) {
		this.porcentajeVentaEbel = porcentajeVentaEbel;
	}
	public double getPorcentajeVentaEbel() {
		return porcentajeVentaEbel;
	}
	public void setPorcentajeVentaCyzone(double porcentajeVentaCyzone) {
		this.porcentajeVentaCyzone = porcentajeVentaCyzone;
	}
	public double getPorcentajeVentaCyzone() {
		return porcentajeVentaCyzone;
	}
	public void setVentaCyzone(double ventaCyzone) {
		this.ventaCyzone = ventaCyzone;
	}
	public double getVentaCyzone() {
		return ventaCyzone;
	}
	
	public double getPorcentajeOtros() {
		return porcentajeOtros;
	}
	public void setPorcentajeOtros(double porcentajeOtros) {
		this.porcentajeOtros = porcentajeOtros;
	}

}
