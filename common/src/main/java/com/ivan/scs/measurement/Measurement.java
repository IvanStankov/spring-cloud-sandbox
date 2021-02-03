package com.ivan.scs.measurement;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Measurement {

    private int temperature;
    private int humidity;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime date;

}
