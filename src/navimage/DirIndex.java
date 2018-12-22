package navimage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirIndex {
    public static Path stylesheet;
    private PrintWriter writer;
    
    public DirIndex (Path p) throws IOException {
        p = Paths.get(p.toString(), "index.html");
        
        writer = new PrintWriter(
                 new BufferedWriter(
                 new FileWriter(p.toString())));
        
        writer.println("<!DOCTYPE html>");
        writer.println("<html>");
        writer.format("<head><link href=\"%s\" rel=\"stylesheet\"></head>\n",
                       p.relativize(stylesheet).toString());
        writer.println("<body>");
        writer.println("<table>");
        writer.println("<tr><th>Name</th></tr>");
    }
    
    public void addDir (Path p) {
        writer.println("<tr>");
        writer.format("<td><a href=\"%s\"> %s </a></td>\n",
                      Paths.get(p.getFileName().toString(), "index.html"),
                      p.getFileName());
        writer.println("</tr>");
    }
    
    public void addFile (Path p) {
        writer.println("<tr>");
        writer.format("<td>%s</td>", p.getFileName());
        writer.println("</tr>");
    }
    
    public void close () {
        writer.println("</table>");
        writer.println("</body>");
        writer.println("</html>");
        
        writer.flush();
        writer.close();
    }
}
