package br.com.frazao.neuralnetwork;

import java.util.Date;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Ygor
 */
public class CobrancaPOJO {
    
    private Date dataEnvio;
    private int matricula;
    private double docValid;
    private double perfil;
    private double categoria;
    private double abastecimento;
    private double localidade;
    private double sitAgua;
    private double sitEsg;
    private double hd;
    private double enviadas;
    private int pagas;
    private double propPag;

    public CobrancaPOJO() {
    }

    public CobrancaPOJO(Date dataEnvio, int matricula, double docValid, double perfil, double categoria, double abastecimento, double localidade, double sitAgua, double sitEsg, double hd, double enviadas, int pagas, double propPag) {
        this.dataEnvio = dataEnvio;
        this.matricula = matricula;
        this.docValid = docValid;
        this.perfil = perfil;
        this.categoria = categoria;
        this.abastecimento = abastecimento;
        this.localidade = localidade;
        this.sitAgua = sitAgua;
        this.sitEsg = sitEsg;
        this.hd = hd;
        this.enviadas = enviadas;
        this.pagas = pagas;
        this.propPag = propPag;
    }

    public Date getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(Date dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public double getDocValid() {
        return docValid;
    }

    public void setDocValid(double docValid) {
        this.docValid = docValid;
    }

    public double getPerfil() {
        return perfil;
    }

    public void setPerfil(double perfil) {
        this.perfil = perfil;
    }

    public double getCategoria() {
        return categoria;
    }

    public void setCategoria(double categoria) {
        this.categoria = categoria;
    }

    public double getAbastecimento() {
        return abastecimento;
    }

    public void setAbastecimento(double abastecimento) {
        this.abastecimento = abastecimento;
    }

    public double getLocalidade() {
        return localidade;
    }

    public void setLocalidade(double localidade) {
        this.localidade = localidade;
    }

    public double getSitAgua() {
        return sitAgua;
    }

    public void setSitAgua(double sitAgua) {
        this.sitAgua = sitAgua;
    }

    public double getSitEsg() {
        return sitEsg;
    }

    public void setSitEsg(double sitEsg) {
        this.sitEsg = sitEsg;
    }

    public double getHd() {
        return hd;
    }

    public void setHd(double hd) {
        this.hd = hd;
    }

    public double getEnviadas() {
        return enviadas;
    }

    public void setEnviadas(double enviadas) {
        this.enviadas = enviadas;
    }

    public int getPagas() {
        return pagas;
    }

    public void setPagas(int pagas) {
        this.pagas = pagas;
    }

    public double getPropPag() {
        return propPag;
    }

    public void setPropPag(double propPag) {
        this.propPag = propPag;
    }

    

    
    
}
