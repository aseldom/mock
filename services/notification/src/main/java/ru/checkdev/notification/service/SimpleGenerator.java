package ru.checkdev.notification.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.StringWriter;
import java.util.Map;

/**
 * Class SimpleGenerator for replacing keys in line with values from keyMap.
 *
 * @author Karetko Victor
 * @version 1.00
 * @since 10.12.2016
 */
public class SimpleGenerator {
    /**
     * Method search keys in line and replace them with values.
     *
     * @param line  line to search keys.
     * @param model map with keys and their values.
     * @return line with replaced keys.
     */
    public String generate(String line, Map<String, ?> model) {
        StringWriter out = new StringWriter();
        try {
            Configuration cfg = new Configuration(Configuration.getVersion());
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            new Template("template", line, cfg).process(model, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toString();
    }
}
