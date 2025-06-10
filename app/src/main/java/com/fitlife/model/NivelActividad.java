package com.fitlife.model;

public enum NivelActividad {
    SEDENTARIO("Sedentario"),
    LIGERAMENTE_ACTIVO("Ligeramente activo"),
    MODERADAMENTE_ACTIVO("Moderadamente activo"),
    MUY_ACTIVO("Muy activo"),
    EXTRA_ACTIVO("Extra activo");

    private final String label;
    NivelActividad(String label) {
        this.label = label;
    }
    @Override
    public String toString() {
        return label;
    }
    /** Para enviar al backend: */
    public String toApiValue() {
        return name();  // devuelve el nombre del enum, p.ej. "MEDIO_BAJO"
    }
}
//dfadad