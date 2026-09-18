package ar.vicria.telegram.microservice.stations;

import ar.vicria.subte.dto.StationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StationCatalogFactoryTest {

    private static final String LINE_E = "E\uD83D\uDFE3";
    private static final String LINE_B = "B\uD83D\uDD34";

    private StationCatalog catalog;

    @BeforeEach
    public void setUp() {
        catalog = new StationCatalogFactory().create(List.of(
                new StationDto(LINE_E, "Station1"),
                new StationDto(LINE_E, "Station2"),
                new StationDto(LINE_B, "Station3"))
        );
    }

    @Test
    public void catalogIsImmutable() {
        //given
        StationDto stationDto = new StationDto(LINE_E, "Station4");
        String newLine = "D\uD83D\uDFE2";

        // when then
        assertThrows(UnsupportedOperationException.class,
                () -> catalog.getStations().add(stationDto));
        assertThrows(UnsupportedOperationException.class,
                () -> catalog.getLines().add(newLine));
        assertThrows(UnsupportedOperationException.class,
                () -> catalog.getByLine().put(newLine, List.of(stationDto)));
        assertThrows(UnsupportedOperationException.class,
                () -> catalog.getByLine().get(LINE_E).add(stationDto));
        assertThrows(UnsupportedOperationException.class,
                () -> catalog.getByNameAndLine().put(stationDto.toString(), stationDto));

    }

    @Test
    public void catalogElementsEqualsStations() {
        //given
        StationDto stationDto = catalog.getStations().get(0);
        String keyByToStringMethod = String.join(" ", stationDto.getName(), stationDto.getLine());

        // when then
        assertSame(stationDto, catalog.getByNameAndLine().get(keyByToStringMethod));
    }

    @Test
    public void catalogElementsWithoutDuplicated() {
        //given
        StationDto stationDto = new StationDto(LINE_E, "Station1");
        StationDto duplicatedStationDto = new StationDto(LINE_E, "Station1");
        List<StationDto> stationsWithDuplicate = List.of(stationDto, duplicatedStationDto);

        // when then
        assertThrows(IllegalStateException.class, () ->
                new StationCatalogFactory().create(stationsWithDuplicate));
    }

}