package util;

import model.NhanVien;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class FileExportHelper {
    public static void exportBangLuongToXML(List<NhanVien> list, String filePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("BangLuong");
        doc.appendChild(root);

        DecimalFormat df = new DecimalFormat("#,### VNĐ");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator('.'); 
        df.setDecimalFormatSymbols(dfs);

        for (NhanVien nv : list) {
            Element emp = doc.createElement("NhanVien");
            root.appendChild(emp);
            
            emp.appendChild(createNode(doc, "MaNV", String.valueOf(nv.getMaNV())));
            emp.appendChild(createNode(doc, "TenNV", nv.getTenNV()));
            
            emp.appendChild(createNode(doc, "Luong", df.format(nv.getLuong())));
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(new File(filePath)));
    }

    private static Node createNode(Document doc, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
}