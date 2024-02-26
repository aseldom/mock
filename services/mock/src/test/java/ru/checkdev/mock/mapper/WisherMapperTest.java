package ru.checkdev.mock.mapper;

import org.junit.Test;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.domain.Wisher;
import ru.checkdev.mock.dto.WisherDto;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class WisherMapperTest {

    @Test
    public void whenWisherCreated() {
        var interview = new Interview(1, 1, 1, 1,
                "title", "additional",
                "contact", "30.02.2070",
                new Timestamp(System.currentTimeMillis()), 1);
        var wisherDTO = new WisherDto(1, 1, 1,
                "contact", true, 1);
        var expected = new Wisher(0, interview, 1, "contact", true, 1);
        assertEquals(new WisherMapper().getWisher(wisherDTO, interview), expected);
    }
}
