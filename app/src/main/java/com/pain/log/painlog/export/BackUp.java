package com.pain.log.painlog.export;

import android.app.Activity;
import android.os.Environment;
import android.widget.Toast;

import com.pain.log.painlog.BD.Consultas;
import com.pain.log.painlog.BD.MyDatabase;
import com.pain.log.painlog.R;
import com.pain.log.painlog.negocio.Diarios;
import com.pain.log.painlog.negocio.Logs;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.pain.log.painlog.negocio.LogUtils.LOGI;

/**
 * Created by rulo on 19/03/15.
 */
public class BackUp {

    public static final String root = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PainLog/backup";

    public static String fileName = "/NAMEFILE.xml";
    public static String fileNameTemp = "fileNameTemp";

    public static int numErr = 0;
    public static final int OPBACKUP = 0;
    public static final int OPRESTORE = 1;


    public static void dump(Activity act) {

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH.mm");
        String name = "PL " +  formatter.format(today);


        MyDatabase db = new MyDatabase(act);

        File sd = new File(path);
        String pathFile = sd + fileName.replace("NAMEFILE", name);

        DatabaseDump databaseDump = new DatabaseDump(db.getReadableDatabase(), pathFile);
        databaseDump.exportData();
        Toast.makeText(act, act.getResources().getString(R.string.BackupoK),Toast.LENGTH_SHORT).show();

    }

    public static void dumpTemp(Activity act) {

        MyDatabase db = new MyDatabase(act);

        File sd = new File(path);
        String pathFile = sd + fileName.replace("NAMEFILE", fileNameTemp);

        DatabaseDump databaseDump = new DatabaseDump(db.getReadableDatabase(), pathFile);
        databaseDump.exportData();

    }

    public static void ReadXMLFile(File fXmlFile, Activity act, boolean err) {

        Diarios diario = new Diarios();
        Logs log = new Logs();
        Consultas consultas = new Consultas(act);

        if (!err) {//no es error
            dumpTemp(act);
            numErr = 0;

         }
        consultas.deleteBBDD();

        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            LOGI("ReadXMLFile", doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("table");


            for (int temp = 0; temp < nList.getLength(); temp++) { //TABLA

                Node nNode = nList.item(temp);
                Element eElement = (Element) nNode;
                LOGI("ReadXMLFile", "Table name: " + eElement.getAttribute("name"));


                NodeList nListRows = eElement.getElementsByTagName("row");

                for (int i = 0; i < nListRows.getLength(); i++) { //RESGITROS

                    switch (eElement.getAttribute("name")){
                        case "diarios":
                            diario = new Diarios();
                            break;
                        case "registros":
                            log = new Logs();
                            break;
                        default:
                            break;
                    }


                    Node nNodeRow = nListRows.item(i);
                    Element eElementRow = (Element) nNodeRow;
                    LOGI("ReadXMLFile", "Row name: " + eElement.getAttribute("name"));

                    NodeList nListCol = eElementRow.getElementsByTagName("col");
                    for (int j = 0; j < nListCol.getLength(); j++) { //COLUMNAs

                        Node nNodeCol = nListCol.item(j);
                        Element eElementCol = (Element) nNodeCol;
                        LOGI("ReadXMLFile", "Col name: " + eElementCol.getAttribute("name"));

                       switch (eElementCol.getAttribute("name")){
                           case "clave":

                               diario.setClave(Integer.parseInt(eElementCol.getTextContent()));
                               break;
                           case "nombre":

                               diario.setNombre(eElementCol.getTextContent());
                               break;
                           case "clave_r":

                               log.setClave(Integer.parseInt(eElementCol.getTextContent()));
                               break;
                           case "fecha":

                               log.setFecha(eElementCol.getTextContent());
                               break;
                           case "intensidad":

                               log.setIntensidad(Integer.parseInt(eElementCol.getTextContent()));
                               break;
                           case "notas":

                               log.setNotas(eElementCol.getTextContent());
                               break;
                           case "diarios_clave":

                               log.setClave_d(Integer.parseInt(eElementCol.getTextContent()));
                               break;
                       }
                    }

                    switch (eElement.getAttribute("name")){
                        case "diarios":
                            consultas.addDiario(diario);
                            break;
                        case "registros":
                            consultas.addRegistros(log);
                            break;
                        default:
                            break;
                    }
                }
            }
            if (!err)
                Toast.makeText(act, act.getResources().getString(R.string.RestoreOK), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

            numErr = numErr +1;
            System.out.println(numErr);
            if (numErr <= 1) {

                Toast.makeText(act, act.getResources().getString(R.string.RestoreNotOK), Toast.LENGTH_SHORT).show();
                ReadXMLFile(new File(BackUp.path + BackUp.fileNameTemp), act, true);
            }
            e.printStackTrace();
        }

        removeItem(BackUp.fileNameTemp + ".xml"); // delete file temp
    }


    public static void removeItem(String nombre) {
        File archivo;
        archivo = new File(path + "/" + nombre);
        if (archivo.exists())
            archivo.delete();

    }

}


