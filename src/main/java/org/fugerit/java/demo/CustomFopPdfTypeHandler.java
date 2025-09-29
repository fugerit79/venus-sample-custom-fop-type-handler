package org.fugerit.java.demo;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;
import org.w3c.dom.Element;

@Slf4j
public class CustomFopPdfTypeHandler extends PdfFopTypeHandler {

    public final static String SYS_PROP_OVERRIDE_FOP_SUPPRESS_EVENTS = "override-for-suppress-events";

    protected void handleConfigTag(Element config) throws ConfigException {
        String overrideFopSuppressEvents = System.getProperty(SYS_PROP_OVERRIDE_FOP_SUPPRESS_EVENTS);
        if (StringUtils.isNotEmpty(overrideFopSuppressEvents)) {
            log.info("Override for suppressed events: {} -> {}",
                    config.getAttribute( PdfFopTypeHandler.ATT_FOP_SUPPRESS_EVENTS ),
                    overrideFopSuppressEvents);
            config.setAttribute(PdfFopTypeHandler.ATT_FOP_SUPPRESS_EVENTS, overrideFopSuppressEvents);
        }
        super.handleConfigTag(config);
    }

}
