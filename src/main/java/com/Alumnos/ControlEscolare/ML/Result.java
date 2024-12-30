/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Alumnos.ControlEscolare.ML;

import java.util.List;

/**
 *
 * @author Alien 9
 */
public class Result<T>{
    
    public boolean correct;
    public String errorMessage;
    public Exception ex;
    public T object;
    public List<T> objects;
    
    public double totalCosto;

    public double getTotalCosto() {
        return totalCosto;
    }

    public void setTotalCosto(double totalCosto) {
        this.totalCosto = totalCosto;
    }
}
