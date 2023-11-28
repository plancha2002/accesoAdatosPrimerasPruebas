package org.example;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class XMLParser {

    private static final String URL="jdbc:mysql://localhost:3306/accesoDatos";
    private static final String USERNAME="root";
    private static final String PASSWORD="";


    public void leerXML() {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("./datos.xml");

            NodeList rowList = document.getElementsByTagName("Row");

            for (int i = 1; i < rowList.getLength(); i++) {
                Node rowNode = rowList.item(i);
                if (rowNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element rowElement = (Element) rowNode;

                    NodeList cellList = rowElement.getElementsByTagName("Data");
                    empresas empresa = new empresas();

                    for (int j = 0; j < cellList.getLength(); j++) {
                        Node cellNode = cellList.item(j);
                        if (cellNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element cellElement = (Element) cellNode;
                            String contenido = cellElement.getTextContent();

                            // Asignar contenido a los campos de la empresa
                            switch (j) {
                                case 0:
                                    empresa.setNIF(contenido);
                                    break;
                                case 1:
                                    empresa.setAdjudicatario(contenido);
                                    break;
                                case 2:
                                    empresa.setObjetoGenerico(contenido);
                                    break;
                                case 3:
                                    empresa.setObjeto(contenido);
                                    break;
                                case 4:
                                    //debido al problema de diferentes formatos de fecha nos vemos en la obligacion de
                                    //controlar todos los casos, lo hara parseDate
                                    Date fecha = parseDate(contenido);
                                    empresa.setFechaAdjudicacion(fecha);

                                    break;
                                case 5:
                                    //buscamos borrar posibles espacios, simbolos, comas, etc...
                                    System.out.println(contenido);
                                    String importeStr = contenido
                                            .replace(".", "")
                                            .replace(",", ".")
                                            .replace("€", "")
                                            .replace(" ", "");
                                    System.out.println(importeStr);
                                    //convertimos nuestro string del importe a float
                                    float importe = Float.parseFloat(importeStr);
                                    empresa.setImporte(importe);
                                    break;
                                    case 6:
                                    empresa.setProveedoresConsultados(contenido);
                                    break;
                                case 7:
                                    empresa.setTipoContrato(contenido);
                                    break;
                            }
                        }
                    }

                    // Aquí puedes almacenar el objeto 'empresa' en tu base de datos o hacer lo que necesites con él
                    System.out.println(empresa.toString());

                    // Creamos un obejto DataBaseManager para instanciar la conexion
                    DatabaseManager databaseManager = new DatabaseManager(URL, USERNAME, PASSWORD);
                    //insertamos el obejto extraido mediante nuestro aplicativo en databaseManager
                    databaseManager.insertData(empresa);
                    //cerramos conexion
                    databaseManager.closeConnection();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void escribirXML() throws SQLException, ParserConfigurationException, TransformerException {
        DatabaseManager databaseManager = new DatabaseManager(URL, USERNAME, PASSWORD);
        ArrayList<empresas> emp = databaseManager.selectData();
        databaseManager.closeConnection();



            // Crea una nueva instancia de DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // Crea un nuevo objeto DocumentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Crea un nuevo documento XML
            Document doc = builder.newDocument();

            // Crea un elemento raíz
            Element root = doc.createElement("contratos");
            doc.appendChild(root);
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        for (empresas empresas : emp){
            System.out.println(empresas);
                // le ponemos contrato a nuestra raiz
            Element contrato = doc.createElement("contrato");
                root.appendChild(contrato);

                //le ponemos valor
            Element nif = doc.createElement("nif");
                nif.setTextContent(empresas.getNIF());
                contrato.appendChild(nif);

            Element adjudicatario = doc.createElement("Adjudicatario");
                adjudicatario.setTextContent(empresas.getAdjudicatario());
                contrato.appendChild(adjudicatario);

            Element objetoGenerico = doc.createElement("ObjetoGenerico");
                objetoGenerico.setTextContent(empresas.getObjetoGenerico());
                contrato.appendChild(objetoGenerico);

            Element objeto = doc.createElement("Objeto");
                objeto.setTextContent(empresas.getObjeto());
                contrato.appendChild(objeto);

            Element fecha_adjudicacion = doc.createElement("FechaAdjudicacion");
                Date fecha = empresas.getFechaAdjudicacion();
                fecha_adjudicacion.setTextContent(formato.format(fecha));
                contrato.appendChild(fecha_adjudicacion);

            Element importe = doc.createElement("Importe");
                importe.setTextContent(Float.toString(empresas.getImporte()));
                contrato.appendChild(importe);

            Element proveedoresConsultados = doc.createElement("ProveedoresConsultados");
                proveedoresConsultados.setTextContent(empresas.getProveedoresConsultados());
                contrato.appendChild(proveedoresConsultados);


            }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        // Indica la ruta donde deseas guardar el archivo XML
        String rutaArchivo = "./resultado.xml";
        File archivo = new File(rutaArchivo);

        // Convierte el contenido del documento en un archivo XML
        transformer.transform(new DOMSource(doc), new StreamResult(archivo));





        }
    private static Date parseDate(String fechaStr) {

        SimpleDateFormat[] dateFormats = {
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"),
                new SimpleDateFormat("dd.MM.yyyy") // Agrega aquí otros formatos si es necesario
        };

        for (SimpleDateFormat format : dateFormats) {
            try {
                return format.parse(fechaStr);
            } catch (ParseException e) {

            }
        }
        try {
            int dias = Integer.parseInt(fechaStr);
            Calendar cal = Calendar.getInstance();
            cal.set(1900, 0, 1); // 1 de enero de 1900
            cal.add(Calendar.DAY_OF_YEAR, dias); // Sumar la cantidad de días
            return cal.getTime();
        } catch (NumberFormatException e) {
            // Si no se pudo analizar en ningún formato, usar la fecha actual
            return new Date();
        }
    }
}
