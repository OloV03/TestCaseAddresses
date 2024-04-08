package com.IlyaJukov.XML;

import com.IlyaJukov.Address.AddressPart;
import com.IlyaJukov.Address.AddressComponent;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class ParserXML {

    /**
     * <b><p>Все элементы адреса (с историей действия каждого)</p></b>
     * @param fileName Имя файла с данными
     * @return HashMap, где <b>key</b> - id элемента адреса,
     *                      <b>value</b> - значение элемента
     */
    public HashMap<String, AddressPart> getAddressParts(String fileName) {

        HashMap<String, AddressPart> parts = new HashMap<>();

        Document document = getDocument(fileName);
        assert document != null;
        NodeList nodes = null;

        try {
            nodes = document.getElementsByTagName("OBJECT");
        } catch (Exception e) {
            System.out.println("\n-----\n\tIlya balbes, kotoriy ne znaet chto za fail nujen\n-----\n");
            return null;
        }


        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            try {
                AddressPart part;
                String objectId = getAttribute(node, "OBJECTID");

                if (parts.containsKey(objectId))
                    part = parts.get(objectId);
                else {
                    part = new AddressPart(objectId);
                    parts.put(objectId, part);
                }

                AddressComponent component = new AddressComponent(
                        objectId,
                        getAttribute(node, "NAME"),
                        getAttribute(node, "TYPENAME"),
                        new SimpleDateFormat("yyyy-MM-dd").parse(
                                getAttribute(node, "STARTDATE")
                        ),
                        new SimpleDateFormat("yyyy-MM-dd").parse(
                                getAttribute(node, "ENDDATE")
                        ),
                        getAttribute(node, "ISACTUAL").equals("1"),
                        getAttribute(node, "ISACTIVE").equals("1")
                );

                part.addComponent(component);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return parts;
    }

    /**
     * <b><p>Установка связей "предок - потомок" для переданных элементов</p></b>
     * @param fileName Имя файла с данными иерархии
     * @param parts Список элементов для установки связей
     */
    public void setAddressHierarchy(String fileName, HashMap<String, AddressPart> parts) {

        Document doc = getDocument(fileName);
        assert doc != null;
        NodeList nodes = null;

        try {
            nodes = doc.getElementsByTagName("ITEM");
        } catch (Exception e) {
            System.out.println("\n-----\n\tIlya balbes, kotoriy ne znaet chto za fail nujen\n-----\n");
            return;
        }
        
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            AddressPart parent = parts.get(getAttribute(node, "PARENTOBJID"));
            AddressPart child = parts.get(getAttribute(node, "OBJECTID"));

            if (parent == null || child == null) continue;
            if (getAttribute(node, "ISACTIVE").equals("0")) continue;

            parent.addChild(child);
            child.setParent(parent);
        }
    }

    private Document getDocument(String fileName) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder();

            return documentBuilder.parse(fileName);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getAttribute(Node node, String attribute) {
        return node
                .getAttributes()
                .getNamedItem(attribute)
                .getNodeValue();
    }
}
