/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formulabean;

import java.beans.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;
import java.sql.*;
import java.sql.Statement;

/**
 *
 * @author alumne
 */
public class FormulaBean implements Serializable, VetoableChangeListener, PropertyChangeListener {
    
    public static final String PROP_SAMPLE_PROPERTY = "sampleProperty";
    public static final String PROP_DADES = "dades";
    public static final String PROP_CONSULTA = "consulta";
    public static final String PROP_INSERT = "insert";
    public static final String PROP_TANCAR = "tancar";
    public static final String PROP_COMPROBARBD = "comprobarBd";
    
    private String sampleProperty;
    private PropertyChangeSupport propertySupport;
    
    // Propietats simples
    private Connection con = null;
    private ResultSet resultat;
    Properties datos;
    
    // Propietats lligades
    private String tancar;
    
    // Propietats restringides
    private String dades;    
    private String consulta;   
    private String insert;        
    private String comprobarBd;
    
    // Constructor
    public FormulaBean() {
        propertySupport = new PropertyChangeSupport(this);
        this.addVetoableChangeListener(this);
    }
    
    // Getter propietats simples
    public Connection getCon() {
        return con;
    }
    
    public ResultSet getResultat() {
        return resultat;
    }
    
    // Getters i setters propietats lligades
    public String getTancar() {
        return tancar;
    }

    public void setTancar(String tancar) {
        String oldTancar = this.tancar;
        this.tancar = tancar;
        propertySupport.firePropertyChange(PROP_TANCAR, oldTancar, tancar);
    }
    
    // Getters i setters propietats restringides
    public String getDades() {
        return dades;
    }

    public void setDades(String dades) throws PropertyVetoException {
        String oldDades = this.dades;
        vetoableChangeSupport.fireVetoableChange(PROP_DADES, oldDades, dades);
        this.dades = dades;
        propertySupport.firePropertyChange(PROP_DADES, oldDades, dades);
    }
    
     public String getConsulta() {
        return consulta;
    }

    public void setConsulta(String consulta) throws PropertyVetoException {
        String oldConsulta = this.consulta;
        vetoableChangeSupport.fireVetoableChange(PROP_CONSULTA, oldConsulta, consulta);
        this.consulta = consulta;
        propertySupport.firePropertyChange(PROP_CONSULTA, oldConsulta, consulta);
    }
    
    public String getInsert() {
        return insert;
    }

    public void setInsert(String insert) throws PropertyVetoException {
        String oldInsert = this.insert;
        vetoableChangeSupport.fireVetoableChange(PROP_INSERT, oldInsert, insert);
        this.insert = insert;
        propertySupport.firePropertyChange(PROP_INSERT, oldInsert, insert);
    }

    public String getComprobarBd() {
        return comprobarBd;
    }

    public void setComprobarBd(String comprobarBd) throws PropertyVetoException {
        String oldComprobarBd = this.comprobarBd;
        vetoableChangeSupport.fireVetoableChange(PROP_COMPROBARBD, oldComprobarBd, comprobarBd);
        this.comprobarBd = comprobarBd;
        propertySupport.firePropertyChange(PROP_COMPROBARBD, oldComprobarBd, comprobarBd);
    }

    private transient final VetoableChangeSupport vetoableChangeSupport = new VetoableChangeSupport(this);

    public void addVetoableChangeListener(VetoableChangeListener listener) {
        vetoableChangeSupport.addVetoableChangeListener(listener);
    }

    public void removeVetoableChangeListener(VetoableChangeListener listener) {
        vetoableChangeSupport.removeVetoableChangeListener(listener);
    }
    
    public String getSampleProperty() {
        return sampleProperty;
    }
    
    public void setSampleProperty(String value) {
        String oldValue = sampleProperty;
        sampleProperty = value;
        propertySupport.firePropertyChange(PROP_SAMPLE_PROPERTY, oldValue, sampleProperty);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        switch (evt.getPropertyName()) {
            case FormulaBean.PROP_COMPROBARBD:
                datos = new Properties();
                try {
                    datos.load(new FileInputStream((String) evt.getNewValue()));
                    System.out.println("Nom del properties correcte");
                    String url1 = (String)datos.get("url1");
                    String url2 = (String)datos.get("url2");
                    String user = (String)datos.get("user");
                    String password = (String)datos.get("password");
                    
                    con = DriverManager.getConnection(url2, user, password);
                    System.out.println("Conectat en exit");
                    
                } catch (IOException | SQLException ex) {
                    throw new PropertyVetoException("Error", evt);
                }
                break;
            case FormulaBean.PROP_DADES:
                datos = new Properties();
                try {
                    datos.load(new FileInputStream((String) evt.getNewValue()));
                    System.out.println("Nom del properties correcte");
                    String url1 = (String)datos.get("url1");
                    String url2 = (String)datos.get("url2");
                    String user = (String)datos.get("user");
                    String password = (String)datos.get("password");
                    
                    con = DriverManager.getConnection(url1, user, password);
                    System.out.println("Conectat en exit");
                    
                } catch (IOException | SQLException ex) {
                    throw new PropertyVetoException("Error", evt);
                }
                break;
            case FormulaBean.PROP_CONSULTA:
                try {
                    String SQL = evt.getNewValue().toString();
                    System.out.println(con);
                    System.out.println(SQL);
                    Statement st;
                    st = con.createStatement();
                    ResultSet rs = st.executeQuery(SQL);
                    resultat = rs;
                } catch (SQLException ex) {
                    throw new PropertyVetoException("Error", evt);
                }
                break;
            case FormulaBean.PROP_INSERT:
                try {
                    String SQL = evt.getNewValue().toString();
                    System.out.println(con);
                    System.out.println(SQL);
                    Statement st;
                    st = con.createStatement();
                    st.executeUpdate(SQL);
                } catch (SQLException ex) {
                    if (ex.getErrorCode() == 1064) {
                        throw new PropertyVetoException("Hi ha un error d'escriptura en la consulta SQL", evt);
                    } else if (ex.getErrorCode() == 1062) {
                        throw new PropertyVetoException("Clau primària duplicada", evt);
                    } else {
                        throw new PropertyVetoException(ex.toString(), evt);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case FormulaBean.PROP_TANCAR:
                if ((boolean) evt.getNewValue() && con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        
                    }
                    
                }
                break;
        }
    }
    
    
    
}