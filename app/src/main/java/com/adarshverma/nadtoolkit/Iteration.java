package com.adarshverma.nadtoolkit;

public class Iteration {

    private int iteration;
    private double aValue;
    private double bValue;
    private double midValue;
    private double fMidValue;

    public Iteration(int iteration, double aValue, double bValue, double midValue, double fMidValue) {
        this.iteration = iteration;
        this.aValue = aValue;
        this.bValue = bValue;
        this.midValue = midValue;
        this.fMidValue = fMidValue;
    }

    public Iteration(int iteration, double xValue, double fValue) {
        this.iteration = iteration;
        this.aValue = xValue;
        this.midValue = fValue;

    }


    public int getIteration() {
        return iteration;
    }


    public double getaValue() {
        return aValue;
    }

    public double getbValue() {
        return bValue;
    }


    public double getMidValue() {
        return midValue;
    }


    public double getfMidValue() {
        return fMidValue;
    }

}
