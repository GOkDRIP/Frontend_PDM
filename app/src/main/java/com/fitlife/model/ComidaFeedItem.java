package com.fitlife.model;

import com.google.gson.annotations.SerializedName;

public class ComidaFeedItem {
    @SerializedName("idPublicacion")
    private int idPublicacion;

    @SerializedName("usuarioNombre")
    private String usuarioNombre;

    @SerializedName("fotoURL")
    private String fotoURL;

    @SerializedName("nombreComida")
    private String nombreComida;

    @SerializedName("calorias")
    private int calorias;

    @SerializedName("carbohidratos")
    private double carbohidratos;

    @SerializedName("proteinas")
    private double proteinas;

    @SerializedName("grasas")
    private double grasas;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("fechaPublicacion")
    private String fechaPublicacion;

    @SerializedName("haDadoLike")
    private boolean haDadoLike;

    @SerializedName("likes")
    private int likes;

    // Getters and setters
    public int getIdPublicacion() { return idPublicacion; }
    public void setIdPublicacion(int idPublicacion) { this.idPublicacion = idPublicacion; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public String getFotoURL() { return fotoURL; }
    public void setFotoURL(String fotoURL) { this.fotoURL = fotoURL; }

    public String getNombreComida() { return nombreComida; }
    public void setNombreComida(String nombreComida) { this.nombreComida = nombreComida; }

    public int getCalorias() { return calorias; }
    public void setCalorias(int calorias) { this.calorias = calorias; }

    public double getCarbohidratos() { return carbohidratos; }
    public void setCarbohidratos(double carbohidratos) { this.carbohidratos = carbohidratos; }

    public double getProteinas() { return proteinas; }
    public void setProteinas(double proteinas) { this.proteinas = proteinas; }

    public double getGrasas() { return grasas; }
    public void setGrasas(double grasas) { this.grasas = grasas; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(String fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public boolean isHaDadoLike() { return haDadoLike; }
    public void setHaDadoLike(boolean haDadoLike) { this.haDadoLike = haDadoLike; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }
}
