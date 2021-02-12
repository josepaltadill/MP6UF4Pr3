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
public class EquipF1 implements Comparable<EquipF1>, Serializable {
    private String _1_nom;
    private int _2_numMecanics;
    private double _3_pressupost;
    private boolean _5_campioMundial;

    public EquipF1(String _1_nom, int _2_numMecanics, double _3_pressupost, boolean _5_campioMundial) {
        this._1_nom = _1_nom;
        this._2_numMecanics = _2_numMecanics;
        this._3_pressupost = _3_pressupost;
        this._5_campioMundial = _5_campioMundial;
    }

    public String get1_nom() {
        return _1_nom;
    }

    public void set1_nom(String _1_nom) {
        this._1_nom = _1_nom;
    }

    public int get2_numMecanics() {
        return _2_numMecanics;
    }

    public void set2_numMecanics(int _2_numMecanics) {
        this._2_numMecanics = _2_numMecanics;
    }

    public double get3_pressupost() {
        return _3_pressupost;
    }

    public void set3_pressupost(double _3_pressupost) {
        this._3_pressupost = _3_pressupost;
    }

    public boolean is5_campioMundial() {
        return _5_campioMundial;
    }

    public void set5_campioMundial(boolean _5_campioMundial) {
        this._5_campioMundial = _5_campioMundial;
    }

    @Override
    public String toString() {
        return _1_nom;
    }
    
    @Override
    public int compareTo(EquipF1 o) {
        return this._1_nom.compareTo(o.get1_nom());
    }
}
