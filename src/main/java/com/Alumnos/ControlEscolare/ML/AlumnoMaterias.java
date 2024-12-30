/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Alumnos.ControlEscolare.ML;

/**
 *
 * @author Alien 9
 */

public class AlumnoMaterias {
    
    
    private int idAlumnoMaterias;
    
    public Alumno alumno;
    
    public Materia materia;
    
    private double TotalCosto;

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public int getIdAlumnoMaterias() {
        return idAlumnoMaterias;
    }

    public void setIdAlumnoMaterias(int idAlumnoMaterias) {
        this.idAlumnoMaterias = idAlumnoMaterias;
    }

    public double getTotalCosto() {
        return TotalCosto;
    }

    public void setTotalCosto(double TotalCosto) {
        this.TotalCosto = TotalCosto;
    }       
    
}
