package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class CountrySeedDto implements Serializable {
    @Expose
    @NotBlank
    @Size(min = 3,max = 30)
    private String name;
    @Expose
    @NotBlank
    @Size(min = 3,max = 30)
    private String capital;

    public CountrySeedDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }
}
