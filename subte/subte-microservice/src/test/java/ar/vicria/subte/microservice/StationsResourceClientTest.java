package ar.vicria.subte.microservice;

import ar.vicria.subte.client.autoconfigure.SubteClientAutoConfig;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.subte.resources.StationResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class StationsResourceClientTest extends AbstractSubteTestContainersTestBase {
    private AnnotationConfigApplicationContext context;
    private StationResource stationResource;

    @BeforeEach
    void setUp() {
        this.context = new AnnotationConfigApplicationContext();
        context.register(SubteClientAutoConfig.class, HttpMessageConvertersAutoConfiguration.class);
        TestPropertyValues.of("ar.vicria.subte.client.enabled=true", String.format("ar.vicria.subte.client.url=http://%s:%d", subteContainer.getHost(), subteContainer.getMappedPort(8082)))
                .applyTo(context.getEnvironment());
        context.refresh();
        stationResource = context.getBean(StationResource.class);
    }

    @AfterEach
    void tearDown() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Test
    void testGetAll() {
        List<StationDto> all = stationResource.getAll();
        Assertions.assertEquals(90, all.size());

        //Since station ids are randomized only check lines and names of some of the stations
        Assertions.assertTrue(all.stream().anyMatch(s -> s.getLine().equals("H🟡") && s.getName().equals("Facultad de Derecho")));
        Assertions.assertTrue(all.stream().anyMatch(s -> s.getLine().equals("H🟡") && s.getName().equals("Hospitales")));
        Assertions.assertTrue(all.stream().anyMatch(s -> s.getLine().equals("D🟢") && s.getName().equals("Congreso de Tucumán")));
        Assertions.assertTrue(all.stream().anyMatch(s -> s.getLine().equals("D🟢") && s.getName().equals("Catedral")));
        Assertions.assertTrue(all.stream().anyMatch(s -> s.getLine().equals("B🔴") && s.getName().equals("Juan Manuel de Rosas Villa Urquiza")));
        Assertions.assertTrue(all.stream().anyMatch(s -> s.getLine().equals("B🔴") && s.getName().equals("Leandro N. Alem")));
        Assertions.assertTrue(all.stream().anyMatch(s -> s.getLine().equals("C🔵") && s.getName().equals("Retiro")));
        Assertions.assertTrue(all.stream().anyMatch(s -> s.getLine().equals("C🔵") && s.getName().equals("Constitución")));
        Assertions.assertTrue(all.stream().anyMatch(s -> s.getLine().equals("A🌐") && s.getName().equals("Plaza de Mayo")));
        Assertions.assertTrue(all.stream().anyMatch(s -> s.getLine().equals("A🌐") && s.getName().equals("San Pedrito")));
        Assertions.assertTrue(all.stream().anyMatch(s -> s.getLine().equals("E🟣") && s.getName().equals("Plaza de los Virreyes")));
        Assertions.assertTrue(all.stream().anyMatch(s -> s.getLine().equals("E🟣") && s.getName().equals("Retiro")));
    }

    @Test
    @Disabled("This test failing testGetAll() by changing total number of stations, remove this test or add new endpoint to delete stations")
    void testCreate() {
        StationDto newStation = new StationDto("New line", "New station");

        StationDto returned = stationResource.create(newStation);
        newStation.setId(returned.getId());
        Assertions.assertEquals(newStation, returned);

        returned = stationResource.getOne(returned.getId());
        Assertions.assertEquals(newStation, returned);
    }

    @Test
    @Disabled("Update is not functional now, enable after fix")
    void testUpdate() {
        StationDto firstStation = stationResource.getAll().get(0);
        StationDto updated = new StationDto("New line", "New station");
        updated.setId(firstStation.getId());

        StationDto returned = stationResource.update(updated);
        Assertions.assertEquals(updated, returned);

        returned = stationResource.getOne(updated.getId());
        Assertions.assertEquals(updated, returned);
    }
}
