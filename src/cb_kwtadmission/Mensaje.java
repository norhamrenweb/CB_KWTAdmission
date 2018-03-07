/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cb_kwtadmission;

import java.util.ArrayList;

/**
 *
 * @author Chema
 */
class Mensaje {
    
    String texto;
    String asunto;
    ArrayList<String> destinatarios;

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public ArrayList<String> getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(ArrayList<String> destinatarios) {
        this.destinatarios = destinatarios;
    }
    
    public Mensaje(String texto, String asunto, ArrayList<String> destinatarios) {
        this.texto = texto;
        this.asunto = asunto;
        this.destinatarios = destinatarios;
    }
}
