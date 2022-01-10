package com.vetapp.data.models.medication;

public class Medicine {

    private String name;
    private String manufacturer;
    private String prospect;

    public Medicine() {
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getProspect() {
        return prospect;
    }

    @Override
    public String toString() {
        return name;
    }
}
