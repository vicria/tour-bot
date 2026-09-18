package ar.vicria.telegram.microservice.stations;

import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.services.RestToSubte;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StationCatalogServiceTest {

    @Mock
    private RestToSubte restToSubte;

    private static final String LINE_E = "E\uD83D\uDFE3";

    @Test
    public void refreshUpdatesCatalog() {
        //given
        List<StationDto> stations = List.of(new StationDto(LINE_E, "Station1"));
        when(restToSubte.get()).thenReturn(stations);
        StationCatalogService service = new StationCatalogService(new StationCatalogFactory(), restToSubte);

        service.init();

        //when
        service.refresh();

        // then
        assertEquals(1, service.getCatalog().getStations().size());

    }

    @Test
    public void refreshUpdatesCatalogWithException() {
        //given
        List<StationDto> stations = List.of(
                new StationDto(LINE_E, "Station1"),
                new StationDto(LINE_E, "Station2"),
                new StationDto(LINE_E, "Station3")
        );

        when(restToSubte.get())
                .thenReturn(stations)
                .thenThrow(new RuntimeException());

        StationCatalogService service = new StationCatalogService(new StationCatalogFactory(), restToSubte);

        service.init();

        //when then
        StationCatalog beforeRefresh = service.getCatalog();
        service.refresh();
        assertSame(beforeRefresh, service.getCatalog());
    }

}
