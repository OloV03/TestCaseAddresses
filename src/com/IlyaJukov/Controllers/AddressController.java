package com.IlyaJukov.Controllers;

import com.IlyaJukov.Address.AddressComponent;
import com.IlyaJukov.Address.AddressPart;
import com.IlyaJukov.XML.ParserXML;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AddressController {

    private HashMap<String, AddressPart> parts;
    private ParserXML parserXML;

    public HashMap<String, AddressComponent> getAddressParts(String[] ids, String dateInString) {

        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Incorrect date format");
            return null;
        }

        HashMap<String, AddressComponent> result = new HashMap<>();
        for (String id : ids) {
            if (!parts.containsKey(id)) {
                System.out.println("Key" + id + " not found");
                continue;
            }

            AddressPart foundPart = parts.get(id);
            result.put(id, foundPart.getComponentToDate(date));
        }

        return result;
    }

    public AddressController() {
        this.parserXML = new ParserXML();
        this.parts = parserXML.getAddressParts("AS_ADDR_OBJ.XML");
        parserXML.setAddressHierarchy("AS_ADM_HIERARCHY.XML", parts);
    }

    public List<String> getActualAddresses() {

        List<String> addresses = new ArrayList<>();
        parts.values().stream()
                .filter(x -> x.getParent() == null)
                .forEach(x -> addresses.addAll(getAddress(x)));

        return addresses;
    }

    public List<String> getActualAddresses(String requiredPart) {

        List<String> addresses = new ArrayList<>();
        parts.values().stream()
                .filter(x -> x.getParent() == null)
                .forEach(x -> addresses.addAll(getAddress(x)));

        return addresses.stream()
                .filter(x -> x.contains(requiredPart))
                .toList();
    }

    private List<String> getAddress(AddressPart root) {

        if (root.getChildren().isEmpty())
            return List.of(root.toString());

        List<String> result = new ArrayList<>();
        for (AddressPart child : root.getChildren()) {
            result.addAll(getAddress(child));
        }

        for (int i = 0; i < result.size(); i++) {
            String part = result.get(i);
            result.set(i, root.toString() + ", " + part);
        }
        return result;
    }
}
