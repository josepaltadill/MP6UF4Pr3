/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author alumne
 */
public class Pilot implements Comparable<Pilot>, Serializable {
    private int _1_dorsal;
    private String _2_nom;
    private String _3_email;
    private String _4_equipPilot;

    public Pilot(int _1_dorsal, String _2_nom, String _3_email, String _4_equipPilot) {
        this._1_dorsal = _1_dorsal;
        this._2_nom = _2_nom;
        this._3_email = _3_email;
        this._4_equipPilot = _4_equipPilot;
    }

    public int get1_dorsal() {
        return _1_dorsal;
    }

    public void set1_dorsal(int _1_dorsal) {
        this._1_dorsal = _1_dorsal;
    }

    public String get2_nom() {
        return _2_nom;
    }

    public void set2_nom(String _2_nom) {
        this._2_nom = _2_nom;
    }

    public String get3_email() {
        return _3_email;
    }

    public void set3_email(String _3_email) {
        this._3_email = _3_email;
    }

    public String get4_equipPilot() {
        return _4_equipPilot;
    }

    public void set4_equipPilot(String _4_equipPilot) {
        this._4_equipPilot = _4_equipPilot;
    }

    @Override
    public String toString() {
        return _2_nom.toString();
    }


    @Override
    public int compareTo(Pilot o) {
        return this._1_dorsal - o._1_dorsal;
    }
    
} 
