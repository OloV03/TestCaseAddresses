package com.IlyaJukov;

import com.IlyaJukov.Address.AddressComponent;
import com.IlyaJukov.Controllers.AddressController;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        AddressController controller = new AddressController();

        System.out.println("#1 Task");
        String[] ids = new String[] {"1422396", "1450759", "1449192", "1451562"};
        HashMap<String, AddressComponent> parts = controller.getAddressParts(ids, "2010-01-01");
        for (String id : ids) {
            String result = id + ": " + (parts.containsKey(id) ? parts.get(id).toString() : " not found");
            System.out.println(result);
        }

        System.out.println("===================");

        System.out.println("#2 Task");
        controller.getActualAddresses("проезд")
                .forEach(System.out::println);
    }
}
