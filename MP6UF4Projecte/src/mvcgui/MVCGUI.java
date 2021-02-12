/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvcgui;

import controlador.Controller;
import model.Model;
import view.View;

/**
 *
 * @author alumne
 */
public class MVCGUI {
    public static void main(String[] args) {
        new Controller(new Model(), new View());
    }
}
