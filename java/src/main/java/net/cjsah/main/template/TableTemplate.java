package net.cjsah.main.template;

import com.alibaba.fastjson2.JSONObject;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import net.cjsah.main.doc.DocUtil;
import org.docx4j.TraversalUtil;
import org.docx4j.XmlUtils;
import org.docx4j.finders.ClassFinder;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TableTemplate {
    private static final Pattern PATTERN = Pattern.compile("\\$\\{[a-zA-Z0-9]+}");

    @SuppressWarnings("unchecked")
    public static void parseWords(Tbl table, List<JSONObject> words) throws JAXBException {
        Tr tr = (Tr) table.getContent().get(0);
        String template = XmlUtils.marshaltoString(tr);
        table.getContent().clear();
        for (JSONObject word : words) {
            tr = (Tr) XmlUtils.unmarshalString(template);

            List<Map<String, String>> translates = (List<Map<String, String>>) word.get("translates");
            ClassFinder finder = new ClassFinder(Tbl.class);
            new TraversalUtil(tr.getContent(), finder);
            Tbl translateTable = (Tbl) finder.results.get(0);
            parseWordTranslate(translateTable, translates);

            remap(tr, word);

            table.getContent().add(tr);
        }
    }

    public static void parseWordTranslate(Tbl table, List<Map<String, String>> translates) throws JAXBException {
        Tr tr = (Tr) table.getContent().get(0);
        String template = XmlUtils.marshaltoString(tr);
        table.getContent().clear();
        for (Map<String, String> translate : translates) {
            tr = (Tr) XmlUtils.unmarshallFromTemplate(template, translate);
            table.getContent().add(tr);
        }
    }

    public static void parseSpell(Tbl table, List<String> spells) throws JAXBException {
        Tr tr = (Tr) table.getContent().get(0);
        String template = XmlUtils.marshaltoString(tr);
        table.getContent().clear();

        int count = 0;
        int index = 0;
        words:
        while (true) {
            Collections.shuffle(spells);
            for (String translate : spells) {
                Map<String, Object> map = new HashMap<>();
                map.put("translate", translate);
                map.put("index", ++index);
                tr = (Tr) XmlUtils.unmarshallFromTemplate(template, map);
                table.getContent().add(tr);
                if (index > 120 && count != 0) {
                    break words;
                }
            }
            if (++count >= 4) {
                break;
            }
        }
    }

    public static void parsePassage(Tbl table, List<JSONObject> passages) throws JAXBException {
        Tr tr = (Tr) table.getContent().get(0);
        String template = XmlUtils.marshaltoString(tr);
        table.getContent().clear();
        for (JSONObject passage : passages) {
            tr = (Tr) XmlUtils.unmarshalString(template);

            ClassFinder finder = new ClassFinder(Tbl.class);
            new TraversalUtil(tr.getContent(), finder);
            Tbl passageTable = (Tbl) finder.results.get(0);
            parsePassageTable(passageTable, passage);

            remap(tr, passage);

            table.getContent().add(tr);
        }
    }

    @SuppressWarnings("unchecked")
    public static void parsePassageTable(Tbl table, Map<String, Object> passage) throws JAXBException {
        Tr tr = (Tr) table.getContent().get(0);

        ClassFinder finder = new ClassFinder(Tbl.class);
        new TraversalUtil(tr.getContent(), finder);
        Tbl passageTable = (Tbl) finder.results.get(0);
        parsePassageTranslate(passageTable, (List<JSONObject>) passage.get("words"));

        List<Object> content = getTrList(tr);
        content.clear();
        content.addAll((List<?>) passage.get("passage"));
    }

    public static void parsePassageTranslate(Tbl table, List<JSONObject> words) throws JAXBException {
        Tr tr = (Tr) table.getContent().get(0);
        String template = XmlUtils.marshaltoString(tr);
        table.getContent().clear();

        for (JSONObject word : words) {
            tr = (Tr) XmlUtils.unmarshallFromTemplate(template, word);
            table.getContent().add(tr);
        }

    }
    public static void parseAnswer(Tbl table, List<JSONObject> passages) throws JAXBException {
        Tr tr = (Tr) table.getContent().get(0);
        String template = XmlUtils.marshaltoString(tr);
        table.getContent().clear();

        for (JSONObject passage : passages) {
            tr = (Tr) XmlUtils.unmarshalString(template);
            table.getContent().add(tr);

            List<Object> trList = getTrList(tr);
            trList.clear();
            P p = DocUtil.genP("Passage " + passage.getIntValue("index"));
            DocUtil.setBold(p);

            trList.add(p);
            trList.addAll((List<?>) passage.get("answers"));
        }
    }

    public static void parseTranslate(Tbl table, List<JSONObject> passages) throws JAXBException {
        Tr tr = (Tr) table.getContent().get(0);
        String template = XmlUtils.marshaltoString(tr);
        table.getContent().clear();

        for (JSONObject passage : passages) {
            tr = (Tr) XmlUtils.unmarshalString(template);
            table.getContent().add(tr);

            List<Object> trList = getTrList(tr);
            trList.clear();
            P p = DocUtil.genP("Passage " + passage.getIntValue("index"));
            DocUtil.setBold(p);

            trList.add(p);
            trList.addAll((List<?>) passage.get("translate"));
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Object> getTrList(Tr tr) {
        return (((JAXBElement<Tc>)tr.getContent().get(0)).getValue()).getContent();
    }

    @SuppressWarnings("unchecked")
    private static void remap(Tr tr, Map<String, Object> map) {
        ((P)getTrList(tr).get(0)).getContent().stream().parallel()
                .map(it -> ((JAXBElement<Text>)((R) it).getContent().get(0)).getValue())
                .forEach(it -> {
                    Matcher matcher = PATTERN.matcher(it.getValue());
                    it.setValue(matcher.replaceAll(result -> {
                        String key = result.group();
                        key = key.substring(2, key.length() -1);
                        return map.getOrDefault(key, "{" + key + "}").toString();
                    }));
                });

    }
}
