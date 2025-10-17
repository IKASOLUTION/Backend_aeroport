package aeroport.bf.domain.enums;
import lombok.Getter;

@Getter
public enum Etat {

    VALIDER ("Valider"),
    ANNULER ("Annuler");

    private final String label;

    Etat(String label) {
        this.label = label;
    }

}
