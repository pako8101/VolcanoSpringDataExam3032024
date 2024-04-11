package softuni.exam.models.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "countries")
public class Country extends BaseEntity{

    @Column(nullable = false,unique = true)
    private String name;
    @Column(nullable = false)
     private String capital;
  @OneToMany(mappedBy = "country",fetch = FetchType.EAGER)
//@OneToMany
    private Set<Volcano> volcanoes;

    public Country() {
    }

    public Set<Volcano> getVolcanoes() {
        return volcanoes;
    }

    public void setVolcanoes(Set<Volcano> volcanoes) {
        this.volcanoes = volcanoes;
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
