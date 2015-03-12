package com.pain.log.painlog.negocio;

/**
 * Created by Rulo on 08/03/2015.
 */
public class MenuDrawer {


    public static final int TYPE_GRAN_HEADER = 0;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_ITEM = 2;


    private int titulo;
    private int icono;
    private int type;

    public MenuDrawer(int type ,int titulo, int icono) {
        this.titulo = titulo;
        this.icono = icono;
        this.type = type;
    }

    public int getTitulo() {
        return titulo;
    }

    public void setTitulo(int titulo) {
        this.titulo = titulo;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
