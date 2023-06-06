package com.rubik.rubikinteractive.bistonapp.Entidades;

/**
 * Created by RubikInteractive on 4/4/19.
 */

public class cl_usuario {


    private int codigo;
    private String cedula;
    private String nombre;
    private String apellido;
    private String sexo;
    private String telefono;
    private String correo;
    private String direccion;
    private String nomorg;
    private Integer organizacion;
    private String usuario;
    private String contra;
    private Integer tipousuario;
    private Integer estab_activo;
    private String establecimiento;
    private String token;


    public cl_usuario(){

    }

    public cl_usuario(int codigo, String cedula, String nombre, String apellido, String sexo, String telefono, String correo, String direccion, String nomorg,Integer organizacion, String usuario, String contra, Integer tipousuario, String token) {
        this.codigo = codigo;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.sexo = sexo;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.nomorg = nomorg;
        this.organizacion = organizacion;
        this.usuario = usuario;
        this.contra = contra;
        this.tipousuario = tipousuario;
        this.estab_activo = 0;
        this.establecimiento = "";
        this.token = token;
    }


    public String getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNomorg() {
        return nomorg;
    }

    public void setNomorg(String nomorg) {
        this.nomorg = nomorg;
    }


    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(Integer organizacion) {
        this.organizacion = organizacion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public Integer getTipousuario() {
        return tipousuario;
    }

    public void setTipousuario(Integer tipousuario) {
        this.tipousuario = tipousuario;
    }

    public Integer getEstab_activo() {
        return estab_activo;
    }

    public void setEstab_activo(Integer estab_activo) {
        this.estab_activo = estab_activo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
