package com.bruce.service;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.springframework.stereotype.Service;

@Service
public class MarkdownService {

    // 创建Markdown解析器和HTML渲染器
    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();

    // 方法：将Markdown转换为HTML
    public String renderMarkdownToHtml(String markdown) {
        return renderer.render(parser.parse(markdown));
    }
}
