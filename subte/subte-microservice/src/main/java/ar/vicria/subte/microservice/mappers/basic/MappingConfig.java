package ar.vicria.subte.microservice.mappers.basic;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;

/**
 * Config for mapstruct.
 */
@MapperConfig(injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        disableSubMappingMethodsGeneration = true,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface MappingConfig {
}
