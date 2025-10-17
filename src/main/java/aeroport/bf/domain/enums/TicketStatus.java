package aeroport.bf.domain.enums;
import lombok.Getter;

@Getter
public enum TicketStatus {

    READY ("Prêt"),
    WIP ("En cours"),
    DONE ("Terminé"),
    CANCElED ("Annulé");

    private final String label;

    TicketStatus(String label) {
        this.label = label;
    }

}
