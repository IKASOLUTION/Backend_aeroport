package aeroport.bf.config.audit;
/**
 * Enum for the different audit actions
 */
public enum EntityAuditAction {
    CREATE("Création"),
    UPDATE("Modification"),
    DELETE("Suppression"),
    DECONNEXION("Déconnexion"),
    REUNITIALISATION("Réunitialisation"),
    ACTIVATION("Activation"),
    DESACTIVATION("Désactivation"),
    VOL("Vol"),
    CONNEXION("Connexion");

    private String value;

    EntityAuditAction(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return this.value();
    }
}
