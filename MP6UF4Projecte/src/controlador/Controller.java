/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import execpcions.Excepcio;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import model.EquipF1;
import model.Model;
import model.Pilot;
import utilscontroller.Utils;
import view.View;

/**
 *
 * @author alumne
 */
public class Controller {

    private static Model model;
    private static View view;
    
    private int filaSel = -1; // variable que agafara la fila de la taula que seleccione
    private int filaSelPilots = -1;
    private TableColumn tc; // variable on guardarem el contingut de una fila de la taula
    private TableColumn tp;

    public Controller(Model m, View v) {
        model = m;
        view = v;
        controlador();
        view.setVisible(true);
    }
    
    private void itemsEnBlanc() {
        view.getjTextEquip().setText("");
        view.getjTextNumMecanics().setText("");
        view.getjTextPressupost().setText("");
        view.getjTextDorsal().setText("");
        view.getjTextNomPilot().setText("");
        view.getjTextEmailPilot().setText("");
    }
    
    private void reomplirComboboxOrdenar() {
        view.getjComboBoxOrdenar().removeAllItems();
        view.getjComboBoxOrdenar().addItem("Ordenar per dorsal");
        view.getjComboBoxOrdenar().addItem("Ordenar per nom");
    }
    
    //per carregar un JComboBox a partir d'un Tresset que conté les dades 
    private static void loadCombobox(Collection resultSet, JComboBox combo) {
        combo.setModel(new DefaultComboBoxModel((resultSet != null ? resultSet.toArray() : new Object[]{})));
    }
    
    private void omplirCombobox() {
        loadCombobox(model.getEquips(), view.getjComboBox1());
    }
    
    private void carregarTaula() {
        model.mosrtrarDades();
        tc = Utils.<EquipF1>loadTable(model.getEquips(), view.getjTable1(), EquipF1.class, true, model.getResultat());
        tp = Utils.<Pilot>loadTable(model.getPilots(), view.getjTable2(), Pilot.class, true, model.getResultatPilots());
        omplirCombobox();
    }
    
    private EquipF1 obtenirEquipSeleccionat() {
        // recuperem el tableColumnModel de la taula
        TableColumnModel tcm = view.getjTable1().getColumnModel();
        // a la variable tcm li afegim la columna
        tcm.addColumn(tc);
        // recuperem l'objecte
        EquipF1 obj = (EquipF1)view.getjTable1().getValueAt(filaSel, tcm.getColumnCount()-1);
        // Tornem a amagar la columna ocultable d'objectes
        tcm.removeColumn(tc);
        return obj;
    }
    
    private Pilot obtenirPilotSeleccionat() {
        TableColumnModel tcm = view.getjTable2().getColumnModel();
        tcm.addColumn(tp);
        Pilot obj = (Pilot)view.getjTable2().getValueAt(filaSelPilots, tcm.getColumnCount()-1);
        tcm.removeColumn(tp);
        return obj;
    }

    private void controlador() {  
        model.comprobarBd();
        model.conexion();
        
        itemsEnBlanc();
        carregarTaula();
        view.getjComboBoxOrdenar().removeAllItems();
        view.getjComboBoxOrdenar().addItem("Ordenar per dorsal");
        view.getjComboBoxOrdenar().addItem("Ordenar per nom");
        
        // Averiguar quina fila hi ha seleccionada a la taula equips
        view.getjTable1().addMouseListener(
            new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    filaSel = view.getjTable1().getSelectedRow();
                }
            }
        );

        // Boto insertar equips bd
        view.getjButtonInsertar().addActionListener(e -> {
            try {
                model.insertarEquipsBd(
                    view.getjTextEquip().getText().trim(),
                    Integer.parseInt(view.getjTextNumMecanics().getText()),
                    Double.parseDouble(view.getjTextPressupost().getText()),
                    view.getjCheckBoxCampio().isSelected()
                );
                
                carregarTaula();             
                itemsEnBlanc();
                filaSel = -1;

            } catch (Excepcio er) {
                JOptionPane.showMessageDialog(view, er.retornaMissatge());
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Hi ha alguna dada incorrecta. Revisa-ho");
            }
        });
        
        // Boto borrar equips bd
        view.getjButtonBorrar().addActionListener(
            e -> {
                if(filaSel!=-1) {  
                    EquipF1 obj = obtenirEquipSeleccionat();
                    String nom_antic = obj.get1_nom();

                    model.eliminarEquipBd(obj, nom_antic);
                    // Recarreguem la taula despres d'esborrar l'objecte
                    carregarTaula();
                    itemsEnBlanc();
                    // Reiniciem filaSel a -1 per a tornar a seleccionar
                    filaSel = -1;
                    filaSelPilots = -1;
                }else JOptionPane.showMessageDialog(view, "Has de seleccionar una fila per borrar-la");
        });
                    
        // Boto modificar equips BD
        view.getjButtonModificar().addActionListener(e -> {
            if(filaSel!=-1) {
                EquipF1 obj = obtenirEquipSeleccionat();
                String nom_antic = obj.get1_nom();

                try {
                    model.modificarEquipsBd(
                        obj,
                        nom_antic,
                        view.getjTextEquip().getText().trim(),
                        Integer.parseInt(view.getjTextNumMecanics().getText()),
                        Double.parseDouble(view.getjTextPressupost().getText()),
                        view.getjCheckBoxCampio().isSelected()
                    );

                    carregarTaula();
                    itemsEnBlanc();
                } catch (Excepcio er) {
                    JOptionPane.showMessageDialog(view, er.retornaMissatge());
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(view, "Hi ha alguna dada incorrecta. Revisa-ho");
                }
                filaSel = -1;
                filaSelPilots = -1;
            }else JOptionPane.showMessageDialog(view, "Has de seleccionar una fila per modificar-la");

        });
        
        // Boto insertar pilots bd
        view.getjButtonInsertPilot().addActionListener(e -> {
            EquipF1 obj = (EquipF1)view.getjComboBox1().getSelectedItem();
            String nomEquip = obj.get1_nom();
            try {
                model.insertarPilotBd(
                    Integer.parseInt(view.getjTextDorsal().getText()),
                    view.getjTextNomPilot().getText().trim(),
                    view.getjTextEmailPilot().getText().trim(),
                    nomEquip,
                    (EquipF1)view.getjComboBox1().getSelectedItem()
                );

                carregarTaula();               
                itemsEnBlanc();
                reomplirComboboxOrdenar();
            } catch (Excepcio er) {
                JOptionPane.showMessageDialog(view, er.retornaMissatge());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Hi ha alguna dada incorrecta. Revisa-ho");
            }
        });
        
        // Boto borrar pilots bd
        view.getjButtonBorraPilot().addActionListener(
            e -> {
                if(filaSelPilots!=-1) {
                    Pilot obj = obtenirPilotSeleccionat();                 

                    model.eliminarPilotBd(obj, obj.get1_dorsal());
                    // Recarreguem la taula despres d'esborrar l'objecte
                    carregarTaula();
                    itemsEnBlanc();
                    // Reiniciem filaSel a -1 per a tornar a seleccionar
                    filaSel = -1;
                    filaSelPilots = -1;
                }else JOptionPane.showMessageDialog(view, "Has de seleccionar una fila per borrar-la");
        });
        
        // Accio al seleccionar una fila de la taula pilots
        view.getjTable2().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                filaSelPilots = view.getjTable2().getSelectedRow();

                if(filaSelPilots!=-1) {
                    view.getjTextDorsal().setText(view.getjTable2().getValueAt(filaSelPilots, 0).toString());
                    view.getjTextNomPilot().setText(view.getjTable2().getValueAt(filaSelPilots, 1).toString());
                    view.getjTextEmailPilot().setText(view.getjTable2().getValueAt(filaSelPilots, 2).toString());
                    
                    Pilot obj = obtenirPilotSeleccionat();
                    
                    view.getjTable1().clearSelection();
                    
                    for (int i = 0; i < view.getjTable1().getRowCount(); i++) {
                        if (view.getjTable1().getValueAt(i, 0).toString().equalsIgnoreCase(obj.get4_equipPilot())) {
                            view.getjTable1().addRowSelectionInterval(i, i);
                        }
                    }               
                } else {
                    carregarTaula();
                    itemsEnBlanc();
                }
            }
        });
        
        // Boto modificar pilots bd
        view.getjButtonModificaPilot().addActionListener(e -> {
            if(filaSelPilots!=-1) {
                Pilot obj = obtenirPilotSeleccionat();
                int dorsal_antic = obj.get1_dorsal();
                EquipF1 obj3 = (EquipF1)view.getjComboBox1().getSelectedItem();
                String nomEquip = obj3.get1_nom();
                    try {
                        model.modificarPilotBd(
                                obj,
                                dorsal_antic,
                                Integer.parseInt(view.getjTextDorsal().getText()),
                                view.getjTextNomPilot().getText().trim(),
                                view.getjTextEmailPilot().getText().trim(),
                                nomEquip
                        );
                        carregarTaula();
                        itemsEnBlanc();
                        filaSel = -1;
                    } catch (Excepcio ex) {
                        JOptionPane.showMessageDialog(view, ex.retornaMissatge());
                    
                    } catch (java.lang.ClassFormatError er) {
                        JOptionPane.showMessageDialog(view, "Email no vàlid");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(view, "Hi ha alguna dada incorrecta. Revisa-ho");
                    }
            }else JOptionPane.showMessageDialog(view, "Has de seleccionar una fila per modificar-la");

        });
        
        // Boto tornar a mostrar tots els equips
        view.getjButtonMostrarTotsEquips().addActionListener(e -> {
            carregarTaula();
            itemsEnBlanc();
            filaSel = -1;
            reomplirComboboxOrdenar();
        });

        // Accio al seleccionar una fila de la taula equips
        view.getjTable1().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                filaSel = view.getjTable1().getSelectedRow();

                if(filaSel!=-1) {
                    view.getjTextEquip().setText(view.getjTable1().getValueAt(filaSel, 0).toString());
                    view.getjTextNumMecanics().setText(view.getjTable1().getValueAt(filaSel, 1).toString());
                    view.getjTextPressupost().setText(view.getjTable1().getValueAt(filaSel, 2).toString());
                    TableColumnModel tcm = view.getjTable1().getColumnModel();
                    tcm.addColumn(tc);
                    EquipF1 obj = (EquipF1)view.getjTable1().getValueAt(filaSel, tcm.getColumnCount()-1);
                    tcm.removeColumn(tc);
                    
                    view.getjTable2().clearSelection();
                    
                    for (int i = 0; i < view.getjTable2().getRowCount(); i++) {
//                        System.out.println(view.getjTable2().getValueAt(i, 3).toString());
                        try {
                            if (view.getjTable2().getValueAt(i, 3).toString().equalsIgnoreCase(obj.get1_nom())) {
                                view.getjTable2().addRowSelectionInterval(i, i);
                            }
                        } catch (java.lang.NullPointerException ex) {
                            System.out.println("L'equip seleccionat no te cap pilot");
                        }
                    
                    }
                } else {
                    carregarTaula();
                    itemsEnBlanc();
                }
            }
        });


        // Combobox per filtrar equips
        view.getjComboBoxOrdenar().addItemListener(e -> {
                if(view.getjComboBoxOrdenar().getSelectedIndex() == 0) {
                    tp = Utils.<Pilot>loadTable(model.getPilots(), view.getjTable2(), Pilot.class, true, model.getResultatPilots());
                }
                if(view.getjComboBoxOrdenar().getSelectedIndex() == 1) {
                    model.getPilotsOrd().addAll(model.getPilots());
                    tp = Utils.<Pilot>loadTable(model.getPilotsOrd(), view.getjTable2(), Pilot.class, true, model.getResultatPilots());                 
                }
        });
        
       view.addWindowListener(new java.awt.event.WindowAdapter() {
           public void windowClosing(java.awt.event.WindowEvent evt) {
               model.tancarConexio();
           }
       });   
    }
}
