package net.cjsah.main.doc;

import jakarta.xml.bind.JAXBElement;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.TraversalUtil;
import org.docx4j.finders.ClassFinder;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Tr;

import java.io.File;
import java.util.List;

@Slf4j
public class ParseDoc {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Docx4JException {
        String path = "./math/template/table.docx";

        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(path));

        ClassFinder finder = new ClassFinder(Tbl.class);
        MainDocumentPart document = wordMLPackage.getMainDocumentPart();
        new TraversalUtil(document.getContent(), finder);

        log.info("查找到{}个表格", finder.results.size());

        Tbl table = (Tbl) finder.results.get(finder.results.size() - 4);

        finder.results.clear();
        new TraversalUtil(table.getContent(), finder);
        log.info("查找到{}个表格", finder.results.size());

        table = (Tbl) finder.results.get(0);

        Tr tr = (Tr) (table).getContent().get(0);

        JAXBElement<Tc> element = (JAXBElement<Tc>) tr.getContent().get(0);

        List<Object> p = element.getValue().getContent();

        System.out.println(p);
    }

}
