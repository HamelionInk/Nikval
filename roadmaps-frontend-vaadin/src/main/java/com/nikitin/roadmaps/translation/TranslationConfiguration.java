package com.nikitin.roadmaps.translation;

import com.vaadin.flow.i18n.I18NProvider;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

@Component
public class TranslationConfiguration implements I18NProvider {

    private static final String BUNDLE_PREFIX = "translate";

    public final Locale LOCALE_RU = new Locale("ru", "RU");
    public final Locale LOCALE_EN = new Locale("en", "GB");

    private final List<Locale> locales = List.of(LOCALE_RU, LOCALE_EN);

    @Override
    public List<Locale> getProvidedLocales() {
        return locales;
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        if (Objects.isNull(key)) {
            LoggerFactory.getLogger(TranslationConfiguration.class.getName()).warn("Выбранная локаль не найдена");
        }

        final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_PREFIX, locale);

        String value;

        try {
            value = bundle.getString(key);
            if (params.length > 0) {
                value = MessageFormat.format(value, params);
            }
            return value;
        } catch (MissingResourceException exception) {
            LoggerFactory.getLogger(TranslationConfiguration.class.getName()).warn("Ресурс не найден");
            return "|" + locale.getLanguage() + ": " + key;
        }
    }
}
