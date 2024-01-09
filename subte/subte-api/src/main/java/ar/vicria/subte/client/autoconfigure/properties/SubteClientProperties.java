package ar.vicria.subte.client.autoconfigure.properties;


import ar.vicria.properties.utils.AppProperties;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.util.StringUtils;

/**
 * Subte client properties.
 */
@Getter
@ConfigurationProperties("ar.vicria.subte.client")
public class SubteClientProperties extends AppProperties {
    /**
     * Is Subte client enabled.
     */
    private final boolean enabled;
    /**
     * URL of Subte service.
     */
    private final String url;

    /**
     * Constructor.
     *
     * @param enabled is Subte client enabled.
     * @param url     URL of Subte service.
     */
    @ConstructorBinding
    public SubteClientProperties(boolean enabled, String url) {
        this.enabled = enabled;
        this.url = url;
        if (enabled && !StringUtils.hasText(url)) {
            throw new IllegalArgumentException(
                    "Property ar.vicria.subte.client.url must be specified if Subte Client is enabled"
            );
        }
    }
}
