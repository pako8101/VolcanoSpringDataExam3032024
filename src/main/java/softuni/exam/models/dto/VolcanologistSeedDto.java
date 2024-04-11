package softuni.exam.models.dto;

import softuni.exam.models.entity.Volcano;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.LocalDate;

@XmlRootElement(name = "volcanologist")
@XmlAccessorType(XmlAccessType.FIELD)
public class VolcanologistSeedDto implements Serializable {
    @XmlElement(name = "first_name")
    @NotNull
    @Size(min = 2, max = 30)
    private String firstName;
    @XmlElement(name = "last_name")
    @NotNull
    @Size(min = 2, max = 30)
    private String lastName;
    @XmlElement
    @Positive
    private double salary;
    @XmlElement
    @Min(18)
    @Max(80)
    private int age;

    @XmlElement(name = "exploring_from")
    @NotNull
    private String exploringFrom;
    @XmlElement(name = "exploring_volcano_id")
    private Long exploringVolcano;

    public VolcanologistSeedDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getExploringFrom() {
        return exploringFrom;
    }

    public void setExploringFrom(String exploringFrom) {
        this.exploringFrom = exploringFrom;
    }

    public Long getExploringVolcano() {
        return exploringVolcano;
    }

    public void setExploringVolcano(Long exploringVolcano) {
        this.exploringVolcano = exploringVolcano;
    }
}
