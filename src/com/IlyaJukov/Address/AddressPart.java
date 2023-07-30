package com.IlyaJukov.Address;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddressPart {

    private String objectId;
    private AddressPart parent;
    private List<AddressPart> children;
    private List<AddressComponent> components;

    public AddressPart(String objectId) {
        this.objectId = objectId;
        this.components = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public List<AddressPart> getChildren() {
        return children;
    }
    public String getObjectId() {
        return objectId;
    }
    public AddressPart getParent() {
        return parent;
    }

    public void setParent(AddressPart parent) {
        this.parent = parent;
    }

    public void addChild(AddressPart child) {
        this.children.add(child);
    }
    public boolean addComponent(AddressComponent part) {
        return components.add(part);
    }

    /**
     * <b><p>Актуальное значение элемента на дату</p></b>
     * @param date Дата получения
     * @return Актуальное значение элемента
     */
    public AddressComponent getComponentToDate(Date date) {
        return components.stream()
                .filter(x -> x.getStartDates().before(date) && x.getEndDates().after(date))
                .findFirst().get();
    }

    @Override
    public String toString() {
        return getComponentToDate(new Date()).toString();
    }
}
