package org.example;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        XMLParser parseador = new XMLParser();

        //convertimos nuestro xml a una bbdd
        parseador.leerXML();

        try {
            parseador.escribirXML();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}
