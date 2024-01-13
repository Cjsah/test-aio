package net.cjsah.main.doc;

import jakarta.xml.bind.JAXBElement;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.TraversalUtil;
import org.docx4j.finders.ClassFinder;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Tr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class Main {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        PassageNode passage = new PassageNode("a abc def gh ijklm n opq rst' uv'w xyz");
        List<String> bold = new ArrayList<>() {{
            this.add("abc");
            this.add("rst");
            this.add("klm");
            this.add("cjsah");
        }};
        List<String> italic = new ArrayList<>() {{
            this.add("a");
            this.add("xyz");
            this.add("rst");
            this.add("cjsah");
        }};

        List<PassageNode> results = parsePassage(new ArrayList<>() {{
            this.add(passage);
        }}, bold, node -> node.bold = true);
        results = parsePassage(results, italic, node -> node.italic = true);
        System.out.println(results);

        String path = "test.docx";
        String resultPath = "result.docx";

        try {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(path));
            ClassFinder finder = new ClassFinder(Tbl.class);
            MainDocumentPart document = wordMLPackage.getMainDocumentPart();
            new TraversalUtil(document.getContent(), finder);

            log.info("查找到{}个表格", finder.results.size());

            Tbl table = (Tbl) finder.results.get(0);
            Tr tr = (Tr) (table).getContent().get(0);
            JAXBElement<Tc> element = (JAXBElement<Tc>) tr.getContent().get(0);
            P p = (P) element.getValue().getContent().get(0);
            p.getContent().clear();

            for (PassageNode node : results) {
                R value = DocUtil.genR(node.value, node.bold, node.italic);
                p.getContent().add(value);
            }

            try (
                    FileOutputStream fos = new FileOutputStream(resultPath)
            ) {
                wordMLPackage.save(fos);
            }

        } catch (Docx4JException | IOException e) {
            log.error("读取失败", e);
        }

    }

    private static List<PassageNode> parsePassage(List<PassageNode> nodes, List<String> words, Consumer<PassageNode> consumer) {
        List<PassageNode> results = new ArrayList<>();
        while (!nodes.isEmpty()) {
            PassageNode node = nodes.get(0);
            if (node.parsed) {
                results.add(node);
                nodes.remove(0);
                continue;
            }
            boolean noMatch = true;
            for (String word : words) {
                int index = node.value.indexOf(word);
                if (index != -1 && notLetter(node.value, index - 1) && notLetter(node.value, index + word.length())) {
                    nodes.remove(0);
                    node.substring(word.length() + index, node.value.length(), nodes);
                    node.substring(index, word.length() + index, nodes, part -> {
                        consumer.accept(part);
                        part.parsed = true;
                    });
                    node.substring(0, index, nodes);

                    noMatch = false;
                    break;
                }

            }
            if (noMatch) {
                nodes.remove(0);
                results.add(node);
            }

        }
        return results;
    }

    private static boolean notLetter(String value, int index) {
        if (index < 0 || index >= value.length()) return true;
        char c = value.charAt(index);
        return (c < 'a' || c > 'z') && (c < 'A' || c > 'Z');
    }

    @Data
    static class PassageNode {
        String value;
        boolean bold;
        boolean italic;
        boolean parsed;

        public PassageNode(String value) {
            this.value = value;
            this.bold = false;
            this.italic = false;
            this.parsed = false;
        }

        public void substring(int from, int to, List<PassageNode> append) {
            this.substring(from, to, append, node -> {});
        }

        public void substring(int from, int to, List<PassageNode> append, Consumer<PassageNode> consumer) {
            if (from == to) return;
            PassageNode node = new PassageNode(this.value.substring(from, to));
            node.bold = this.bold;
            node.italic = this.italic;
            consumer.accept(node);
            append.add(0, node);
        }

    }

}
