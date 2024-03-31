package softuni.exam.models.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.google.gson.annotations.Expose;
import softuni.exam.models.entity.Country;
import softuni.exam.models.enums.VolcanoType;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

public class VolcanoesSeedDto implements Serializable {
@Expose
@Size(min = 2,max = 30)
    private String name;
    @Expose
    @Positive
    private int Elevation;
    @Expose
    private String volcanoType;
    @Expose
    @NotNull
    @JsonDeserialize(using = NumberDeserializers.BooleanDeserializer.class)
    private boolean isActive;
    @Expose
    private String lastEruption;
    @Expose
    private Long country;

    public VolcanoesSeedDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getElevation() {
        return Elevation;
    }

    public void setElevation(int elevation) {
        Elevation = elevation;
    }

    public String getVolcanoType() {
        return volcanoType;
    }

    public void setVolcanoType(String volcanoType) {
        this.volcanoType = volcanoType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getLastEruption() {
        return lastEruption;
    }

    public void setLastEruption(String lastEruption) {
        this.lastEruption = lastEruption;
    }

    public Long getCountry() {
        return country;
    }

    public void setCountry(Long country) {
        this.country = country;
    }
}
