package com.IlyaJukov.Address;

import java.util.Date;

public class AddressComponent {

    private String id;
    private String name;
    private String type;
    private Date startDates;
    private Date endDates;
    private boolean isActual;
    private boolean isActive;

    public AddressComponent(String id, String name, String type, Date startDates, Date endDates, boolean isActual, boolean isActive) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.startDates = startDates;
        this.endDates = endDates;
        this.isActual = isActual;
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }

    public Date getStartDates() { return startDates; }
    public Date getEndDates() { return endDates; }
}
