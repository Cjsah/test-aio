package net.cjsah.main.doc;

import lombok.extern.slf4j.Slf4j;
import org.docx4j.TraversalUtil;
import org.docx4j.finders.ClassFinder;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Tbl;

import java.io.File;

@Slf4j
public class ParseDoc {
    public static void main(String[] args) throws Docx4JException {
        String path = "./math/template/result.docx";

        while (true) {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(path));

            ClassFinder finder = new ClassFinder(Tbl.class);
            MainDocumentPart document = wordMLPackage.getMainDocumentPart();
            new TraversalUtil(document.getContent(), finder);

            log.info("查找到{}个表格", finder.results.size());

            Tbl table = (Tbl) finder.results.get(22);
            finder.results.clear();
            new TraversalUtil(table.getContent(), finder);
            log.info("查找到{}个表格", finder.results.size());
            table = (Tbl) finder.results.get(0);


            System.out.println(table);

        }
    }

}
