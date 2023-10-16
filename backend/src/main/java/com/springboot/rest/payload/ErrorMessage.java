package com.springboot.rest.payload;

import java.util.Date;

public class ErrorMessage {

    private final String error;
    private final String detail;
    private final Date timestamp;

    public ErrorMessage(String resource, String ... id) {
        switch (resource) {
            case "usuario":
                this.error = "El usuario con ID " + id[0] + " no se encontró en el sistema.";
                break;
            case "orden":
                this.error = "La orden con ID " + id[0] + " no se encontró en el sistema.";
                break;
            default:
                this.error = "La orden con ID " + id[0] + " no se encontró en el sistema o no existe el user con el id " + id[1] + ".";
                break;
        }
        this.detail = "Asegúrese de que la URL esté escrita correctamente o pruebe con un ID de recurso diferente.";
        this.timestamp = new Date();
    }

    public String getError() {
        return error;
    }

    public String getDetail() {
        return detail;
    }

    public Date getTimestamp() {
        return timestamp;
    }

}
