/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package execpcions;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author alumne
 */
public class Excepcio extends Exception {
    private Map<Integer,String> missatges = new TreeMap<>();
    private int valor;
    
    private Excepcio() {
        missatges.put(1, "El nom no pot estar buit");
        missatges.put(2, "El nom esta repetit. Introdueix-ne un altre");
        missatges.put(3, "Email no vàlid");
        missatges.put(4, "El dorsal esta repetit. Introdueix-ne un altre");
    }
    
    public Excepcio(int valor) {
        this();
        this.valor = valor;
    }
    
    public String retornaMissatge() {
        return missatges.get(this.valor);
    }
}
