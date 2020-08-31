package com.survey.innovation;

/**
 * Created by omar.valdez on 14/01/2017.
 */

public class SurveyData {

    private String marca;
    private String modelo;
    private String auto;
    private String kilom;
    private String comments;
    private String autonvo;
    private String idDevice;

    public SurveyData(String marca, String modelo, String auto, String kilom, String comments, String autonvo, String idDevice) {
        this.marca = marca;
        this.modelo = modelo;
        this.auto = auto;
        this.kilom = kilom;
        this.comments = comments;
        this.autonvo = autonvo;
        this.idDevice = idDevice;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    public String getKilom() {
        return kilom;
    }

    public void setKilom(String kilom) {
        this.kilom = kilom;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAutonvo() {
        return autonvo;
    }

    public void setAutonvo(String autonvo) {
        this.autonvo = autonvo;
    }


}
