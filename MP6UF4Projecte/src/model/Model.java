/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import execpcions.Excepcio;
import formulabean.FormulaBean;
import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.*;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author alumne
 */
public class Model {
    private Collection <EquipF1> equips = new TreeSet<>();
    private Collection <Pilot> pilots = new TreeSet<>();
    private Collection <Pilot> pilotsOrd = new TreeSet<>(new PilotOrdenatNom());
    private String regex = "[\\w!#$%&'*+/=`{|}~]+(?:\\.[\\w!#$%&'*+/=`{|}~]+)*@(?:[a-zA-z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    Pattern pattern = Pattern.compile(regex);
    private ResultSet resultat;
    private ResultSet resultatPilots;
    private ResultSet pilotsEquip;
    private ResultSet equipPilot;

    Properties dades = new Properties();
    InputStream entrada = null;
    Connection con = null;
    
    FormulaBean bean = new FormulaBean();

    public Collection<EquipF1> getEquips() {
        return equips;
    }

    public Collection<Pilot> getPilots() {
        return pilots;
    }

    public Collection<Pilot> getPilotsOrd() {
        return pilotsOrd;
    }
    
    // getter per pasar la consulta de la bd per carregar dades
    public ResultSet getResultat() {
        return resultat;
    }

    public ResultSet getResultatPilots() {
        return resultatPilots;
    }

    public ResultSet getPilotsEquip() {
        return pilotsEquip;
    }

    public ResultSet getEquipPilot() {
        return equipPilot;
    }    

    public void setCon(Connection con) {
        this.con = con;
    }
    
    public void comprobarBd() {
        try {
            bean.setComprobarBd("dades.properties");
            bean.setConsulta("show databases");
            boolean existeix = false;
            ResultSet rs = bean.getResultat();
            try {
                while (rs.next()) {
                    if (rs.getString(1).equalsIgnoreCase("formula1")) {
                        existeix = true;
                        System.out.println("La base de dades " + rs.getString(1) + " existeix ");
                    }
                }
                if (!existeix) {
                    System.out.println("creo base dades");
                    bean.setInsert("CREATE DATABASE IF NOT EXISTS formula1");
                    bean.setInsert("USE formula1;");
                    bean.setInsert("CREATE TABLE IF NOT EXISTS equip("
                            + "nom varchar(40) NOT NULL,"
                            + "numMecanics int DEFAULT NULL,"
                            + "pressupost double DEFAULT NULL,"
                            + "campioMundial tinyint(1) DEFAULT NULL,"
                            + "PRIMARY KEY (nom));");
                    bean.setInsert("CREATE TABLE IF NOT EXISTS pilot("
                            + "dorsal int NOT NULL,"
                            + "nom varchar(40) DEFAULT NULL,"
                            + "email varchar(40) DEFAULT NULL,"
                            + "nom_equipf1 varchar(40) DEFAULT NULL,"
                            + "PRIMARY KEY (dorsal),"
                            + "CONSTRAINT pilot_ibfk_1 FOREIGN KEY (nom_equipf1) REFERENCES equip (nom) ON DELETE SET NULL ON UPDATE CASCADE);");
                    bean.setInsert("INSERT INTO equip VALUES ('Ferrari',28,1234567,0),('Mercedes',13,1234556,0),('Renault',25,1234568,0);");
                    bean.setInsert("INSERT INTO pilot VALUES (1,'pilot4','email4@emai.com','Ferrari'),(2,'pilot32','email@emai.com','Ferrari'),(3,'pilot3','emai3@email.com','Mercedes'),(4,'pilot1','emai1@email.com','Renault');");
                } 
            } catch (SQLException ex) {

            }
        } catch (PropertyVetoException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.exit(0);
        }
    }

    public void conexion() {
        try {
            bean.setDades("dades.properties");
            JOptionPane.showMessageDialog(null, "Acces en un nom de fitxer correcte");
        } catch (PropertyVetoException ex) {
            JOptionPane.showMessageDialog(null, "Hi ha alguna dada incorrecta. Torna a accedir a l'aplicació");
            System.exit(0);
        }
        con = bean.getCon();
    }


    // Metode generic insertar
    public <T> void insertar(T obj, Collection<T> c) {
        c.add(obj);
    }
    
    // Metodes insertar bd
    public void insertarEquipsBd(String nom, int numMecanics, double pressupost, boolean campioMundial) throws Excepcio {
        if (nom.trim().isEmpty()) {
            throw new Excepcio(1);
        }
        
        try {
            bean.setInsert("insert into equip (nom, numMecanics, pressupost, campioMundial) values ('" + nom + "'," + numMecanics + ",'" + pressupost + "'," + campioMundial + ")");
        } catch (PropertyVetoException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }   
            
//        try {
//            String SQL = "insert into equip (nom, numMecanics, pressupost, campioMundial) values (?,?,?,?)";
//            PreparedStatement pst = con.prepareStatement(SQL);
//            
//            pst.setString(1, nom);
//            pst.setInt(2, numMecanics);
//            pst.setDouble(3, pressupost);
//            pst.setBoolean(4, campioMundial);
//            
//            pst.execute();
//        } catch (SQLException ex) {
////            System.out.println("El nom de l'equip està repetit, trian un altre");
//            throw new Excepcio(2);
//        }
    }
    
    public void insertarPilotBd(int dorsal, String nom, String email, String nom_equipf1, EquipF1 equip) throws Excepcio {
        if (nom.trim().isEmpty()) {
            throw new Excepcio(1);
        }
        for(Pilot pilot : pilots) {
            if (pilot.get2_nom().equalsIgnoreCase(nom)) {
                throw new Excepcio(2);
            }
        }
        Matcher matcher = pattern.matcher(email);
        if(matcher.matches()) {
            try {
                bean.setInsert("insert into pilot (dorsal, nom, email, nom_equipf1) values (" + dorsal + ",'" + nom + "','" + email + "','" + nom_equipf1 + "')");
//                String SQL = "insert into pilot (dorsal, nom, email, nom_equipf1) values (?,?,?,?)";
//                PreparedStatement pst = con.prepareStatement(SQL);
//                
//                pst.setInt(1, dorsal);
//                pst.setString(2, nom);
//                pst.setString(3, email);
//                pst.setString(4, nom_equipf1);
//                
//                pst.execute();
            } catch (PropertyVetoException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        } else {
            throw new Excepcio(3);
        }
    }
    
    // Metode generic borrar
    public <T> void borrar(T obj, Collection<T> c) {
        c.remove(obj);
    }
    
    // Metodes borrar bd
    public void eliminarEquipBd(EquipF1 equip, String nom) {
        try {
            bean.setInsert("delete from equip where nom = '" + nom + "'");
            this.<EquipF1>borrar(equip, equips);
        } catch (PropertyVetoException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
//        try {
//            String SQL = "delete from equip where nom = ?";
//            PreparedStatement pst = con.prepareStatement(SQL);
//            
//            pst.setString(1, nom);
//            pst.execute();
//            
//            this.<EquipF1>borrar(equip, equips);
//            
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Error a l'eliminar registre" + ex.getMessage());
//        }
    }
    
    public void eliminarPilotBd(Pilot pilot, int dorsal) {
        try {
            bean.setInsert("delete from pilot where dorsal = " + dorsal);
            this.<Pilot>borrar(pilot, pilots);
        } catch (PropertyVetoException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
//        try {
//            String SQL = "delete from pilot where dorsal = ?";
//            PreparedStatement pst = con.prepareStatement(SQL);
//            
//            pst.setInt(1, dorsal);
//            pst.execute();
//            
//            this.<Pilot>borrar(pilot, pilots);
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Error a l'eliminar registre" + ex.getMessage());
//        }
    }
    
    // Metodes modificar bd
    public void modificarEquipsBd(EquipF1 equip, String nom_antic, String nom, int numMecanics, double pressupost, boolean campioMundial) throws Excepcio {
        if (nom.trim().isEmpty()) {
            throw new Excepcio(1);
        }
        try {
            bean.setInsert("update equip set nom = '" + nom + "', numMecanics = " + numMecanics + ", pressupost = '" + pressupost + "', campioMundial = " + campioMundial + " where nom = '"+ nom_antic + "'");
            this.<EquipF1>borrar(equip, equips);
        } catch (PropertyVetoException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
//        try {
//            String SQL = "update equip set nom=?, numMecanics=?, pressupost=?, campioMundial=? where nom=?";
//            PreparedStatement pst = con.prepareStatement(SQL);
//            
//            pst.setString(1, nom);
//            pst.setInt(2, numMecanics);
//            pst.setDouble(3, pressupost);
//            pst.setBoolean(4, campioMundial);
//            pst.setString(5, nom_antic);
//            
//            pst.execute();
//            
//            this.<EquipF1>borrar(equip, equips);
//        } catch (SQLException ex) {
//            throw new Excepcio(2);
//        }
    }
    
    public void modificarPilotBd(Pilot pilot, int dorsal_antic, int dorsal, String nom, String email, String nom_equipf1) throws Excepcio {
        this.<Pilot>borrar(pilot, pilots);
        if (nom.trim().isEmpty()) {
            throw new Excepcio(1);
        }
        for(Pilot pilots : pilots) {
            if (pilots.get2_nom().equalsIgnoreCase(nom)) {
                throw new Excepcio(2);
            }
        }        
        Matcher matcher = pattern.matcher(email);
        if(matcher.matches()) {
            try {
                bean.setInsert("update pilot set dorsal = " + dorsal + ", nom = '" + nom + "', email = '" + email + "', nom_equipf1 = '" + nom_equipf1 + "' where dorsal = " + dorsal_antic);
            } catch (PropertyVetoException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
//            try {
//                String SQL = "update pilot set dorsal=?, nom=?, email=?, nom_equipf1=? where dorsal=?";
//                PreparedStatement pst = con.prepareStatement(SQL);
//
//                pst.setInt(1, dorsal);
//                pst.setString(2, nom);
//                pst.setString(3, email);
//                pst.setNString(4, nom_equipf1);
//                pst.setInt(5, dorsal_antic);
//
//                pst.execute();
//            } catch (SQLException ex) {
//                throw new Excepcio(4);
//            }
        } else {
            throw new Excepcio(3);
        }
    }
    
    public void mosrtrarDades() {
        pilots.removeAll(pilots);
        equips.removeAll(equips);
        
        try {
            bean.setConsulta("select * from equip;");
            ResultSet rs = bean.getResultat();
            
            while (rs.next()) {
                String nom = rs.getNString("nom");
                int numMecanics = rs.getInt("numMecanics");
                double pressupost = rs.getDouble("pressupost");
                boolean campioMundial = rs.getBoolean("campioMundial");
                EquipF1 e = new EquipF1(nom, numMecanics, pressupost, campioMundial);
                this.<EquipF1>insertar(e, equips);
            }
            
            bean.setConsulta("select * from pilot;");
            ResultSet rsEquip = bean.getResultat();
            
            while (rsEquip.next()) {
                int dorsal = rsEquip.getInt("dorsal");
                String nom = rsEquip.getNString("nom");
                String email = rsEquip.getNString("email");
                String nom_equipf1 = rsEquip.getNString("nom_equipf1");

                Pilot a = new Pilot(dorsal, nom, email, nom_equipf1);
                this.<Pilot>insertar(a, pilots);
            }
//        try {
//            con.setAutoCommit(false);
//            try {
//                String SQL = "select * from equip";
//                Statement st = con.createStatement();
//                ResultSet rs = st.executeQuery(SQL);
//                resultat = rs;
//
//                while (rs.next()) {
//                    String nom = rs.getNString("nom");
//                    int numMecanics = rs.getInt("numMecanics");
//                    double pressupost = rs.getDouble("pressupost");
//                    boolean campioMundial = rs.getBoolean("campioMundial");
//                    EquipF1 e = new EquipF1(nom, numMecanics, pressupost, campioMundial);
//                    this.<EquipF1>insertar(e, equips);
//                }
//            } catch (SQLException ex) {
//                JOptionPane.showMessageDialog(null, "Error al mostrar -carregar- dades" + ex.getMessage());
//            }
//
//            try {
//                String SQLPilot = "select * from pilot";
//                Statement st = con.createStatement();
//                ResultSet rsEquip = st.executeQuery(SQLPilot);
//                resultatPilots = rsEquip;
//
//                while (rsEquip.next()) {
//                    int dorsal = rsEquip.getInt("dorsal");
//                    String nom = rsEquip.getNString("nom");
//                    String email = rsEquip.getNString("email");
//                    String nom_equipf1 = rsEquip.getNString("nom_equipf1");
//
//                    Pilot a = new Pilot(dorsal, nom, email, nom_equipf1);
//                    this.<Pilot>insertar(a, pilots);
//                }
//            } catch (SQLException ex) {
//                JOptionPane.showMessageDialog(null, "Error al mostrar -carregar- dades" + ex.getMessage());
//            }
//        } catch (Exception e) {
//            try {
//                con.rollback();
//            } catch (SQLException ex) {
//                System.out.println(ex.toString());
//            }
//        } finally {
//            try {
//                con.setAutoCommit(true);
//            } catch (SQLException ex) {
//                System.out.println(ex.toString());
//            }
//        }
        } catch (PropertyVetoException ex) {
            JOptionPane.showMessageDialog(null, "Error en la consulta SQL");
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }
    
    public void tancarConexio() {
        try {
            bean.setTancar("Tanca");
        } catch (PropertyVetoException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
//        try {
//            con.close();
//        } catch (SQLException ex) {
//            System.out.println(ex.toString());
//        }
    }
   
}

class PilotOrdenatNom implements Comparator<Pilot> {

    @Override
    public int compare(Pilot t, Pilot t1) {
        return t.get2_nom().compareTo(t1.get2_nom());
    }
    
}
