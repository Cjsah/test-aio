package net.cjsah.main.resolver;

import org.ddr.poi.html.HtmlRenderContext;
import org.ddr.poi.html.tag.ImageRenderer;
import org.jsoup.nodes.Element;

import java.util.regex.Pattern;

public class AIEnglishImageRenderer extends ImageRenderer {
    private static final Pattern PATTERN = Pattern.compile("^/(data|keyimages|tikuimage|tikuimages|zximages)/.*");

    @Override
    public boolean renderStart(Element element, HtmlRenderContext context) {
        String src = element.attr("src");
        if (PATTERN.matcher(src).matches()) {
            this.handleRemoteImage(element, context, "https://ai-english.shuhai777.cn" + src);
            return false;
        }
        return super.renderStart(element, context);
    }
}
