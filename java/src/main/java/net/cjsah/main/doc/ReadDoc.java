package net.cjsah.main.doc;

import jakarta.xml.bind.JAXBElement;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.TraversalUtil;
import org.docx4j.finders.ClassFinder;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;

import java.io.File;
import java.util.List;

@Slf4j
public class ReadDoc {

    public static void main(String[] args) throws Docx4JException {
        String path = "./result.docx";

        while (true) {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(path));

            ClassFinder finder = new ClassFinder(Tbl.class);
            MainDocumentPart document = wordMLPackage.getMainDocumentPart();
            new TraversalUtil(document.getContent(), finder);

            System.out.println(finder.results);

            Tbl tbl = (Tbl) finder.results.get(2);

            P line1 = getContent(tbl, 0);
            P line2 = getContent(tbl, 1);

            System.out.println(line1);
            System.out.println(line2);

        }
    }

    @SuppressWarnings("unchecked")
    private static P getContent(Tbl table, int index) {
        Tr line = (Tr) table.getContent().get(index);
        Tc node = ((JAXBElement<Tc>) line.getContent().get(0)).getValue();
        return (P) node.getContent().get(0);
    }

}
