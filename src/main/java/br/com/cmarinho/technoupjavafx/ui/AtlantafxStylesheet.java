package br.com.cmarinho.technoupjavafx.ui;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;

public class AtlantafxStylesheet {
    public static void applyTheme(boolean requireDarkMode) {
        if (requireDarkMode)
            Application.setUserAgentStylesheet(
                    new PrimerDark()
                            .getUserAgentStylesheet()
                        );
        else
            Application.setUserAgentStylesheet(
                    new PrimerLight()
                            .getUserAgentStylesheet()
                        );
    }
}
