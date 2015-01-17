package mastersProject;

import java.awt.*;
import java.io.*;
import java.util.Collection;

public class CreateHTML {

    public static void openResult(Collection<String> arrayOfDirectories) throws InterruptedException, IOException {
        File f = new File("D:/di.html");
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        bw.write("<html>");
        bw.write("<head>");
        bw.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        bw.write("<style type=\"text/css\"> div {float: left; display: inline-block; max-width: 400px;} </style>");
        //bw.write("");
        bw.write("</head>");
        bw.write("<body>");
        bw.write("<h1>Search results</h1>");
        for (String directory : arrayOfDirectories) {
            bw.write("<div>");
            bw.write("<a href='" + directory + "'>Pass: " + directory + "</a></br>");
            bw.write("<img src='" + directory + "' height='255'/>");
            bw.write("</div>");
        }
        bw.write("</body>");
        bw.write("</html>");
        bw.close();

        String url = "D:/di.html";

        File htmlFile = new File(url);
        Desktop.getDesktop().browse(htmlFile.toURI());
    }
}
