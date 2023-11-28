package org.example;



import java.util.Date;


public class empresas {

    private String NIF;
    private String adjudicatario;

    private String objetoGenerico;
    private String objeto;

    private Date fechaAdjudicacion;
    private float importe;

    private String proveedoresConsultados;

    private String tipoContrato;

    public empresas() {
    }

    public String getNIF() {
        return NIF;
    }

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    public String getAdjudicatario() {
        return adjudicatario;
    }

    public void setAdjudicatario(String adjudicatario) {
        this.adjudicatario = adjudicatario;
    }

    public String getObjetoGenerico() {
        return objetoGenerico;
    }

    public void setObjetoGenerico(String objetoGenerico) {
        this.objetoGenerico = objetoGenerico;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public Date getFechaAdjudicacion() {
        return fechaAdjudicacion;
    }

    public void setFechaAdjudicacion(Date fechaAdjudicacion) {
        this.fechaAdjudicacion = fechaAdjudicacion;
    }

    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe = importe;
    }

    public String getProveedoresConsultados() {
        return proveedoresConsultados;
    }

    public void setProveedoresConsultados(String proveedoresConsultados) {
        this.proveedoresConsultados = proveedoresConsultados;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    @Override
    public String toString() {
        return "empresas{" +
                "NIF='" + NIF + '\'' +
                ", adjudicatario='" + adjudicatario + '\'' +
                ", objetoGenerico='" + objetoGenerico + '\'' +
                ", objeto='" + objeto + '\'' +
                ", fechaAdjudicacion=" + fechaAdjudicacion +
                ", importe=" + importe +
                ", proveedoresConsultados='" + proveedoresConsultados + '\'' +
                ", tipoContrato='" + tipoContrato + '\'' +
                '}';
    }
}
