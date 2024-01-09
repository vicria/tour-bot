package ar.vicria.subte.client.autoconfigure;

import ar.vicria.subte.client.autoconfigure.properties.SubteClientProperties;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.subte.resources.StationResource;
import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collections;
import java.util.List;


/**
 * Subte client auto-configuration.
 */
@Configuration
@EnableConfigurationProperties(SubteClientProperties.class)
@Import(FeignClientsConfiguration.class)
@RequiredArgsConstructor
public class SubteClientAutoConfig {
    private final SubteClientProperties subteClientProperties;

    /**
     * StationResource Feign client bean.
     *
     * @param decoder  Feign decoder.
     * @param encoder  Feign encoder.
     * @param contract Feign contract.
     *
     * @return StationResource Feign client bean.
     */
    @Bean
    @ConditionalOnProperty(value = "ar.vicria.subte.client.enabled", havingValue = "true")
    public StationResource stationResourceClient(Decoder decoder, Encoder encoder, Contract contract) {
        return Feign.builder()
                .client(new Client.Default(null, null))
                .encoder(encoder)
                .decoder(decoder)
                .contract(contract)
                .target(StationResource.class, subteClientProperties.getUrl());
    }

    /**
     * No operation StationResource client bean used if Feign client is disabled.
     *
     * @return No operation StationResource client bean.
     */
    @Bean
    @ConditionalOnProperty(value = "ar.vicria.subte.client.enabled", havingValue = "false", matchIfMissing = true)
    public StationResource stationResourceNoOpClient() {
        return new StationResource() {
            @Override
            public StationDto getOne(String id) {
                return null;
            }

            @Override
            public List<StationDto> getAll() {
                return Collections.emptyList();
            }

            @Override
            public StationDto create(StationDto dto) {
                return null;
            }

            @Override
            public StationDto update(StationDto dto) {
                return null;
            }
        };
    }
}
