package ru.guybydefault.restnetwork.util;

import ru.guybydefault.restnetwork.entity.CuisineCertification;

import java.util.Collection;
import java.util.stream.Collectors;

public class StringUtil {
    /**
     * Returns name of cuisines attached to <strong>cuisineCertifications</strong> delimited by comma.
     * @param cuisineCertifications
     * @return
     */
    public static final String formCookCertificationsString(Collection<CuisineCertification> cuisineCertifications) {
        return cuisineCertifications.stream().map(c -> {
            return c.getCuisine().getName();
        }).sorted().collect(Collectors.joining(", "));
    }
}
