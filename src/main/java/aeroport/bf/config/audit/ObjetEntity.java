package aeroport.bf.config.audit;
/**
 * Enum for the different audit actions
 */
public enum ObjetEntity {
    AGENCE("Agence"),
    FILIALE("Filiale"),
    USER("Utilisateur"),
    PRODUIT("Produit"),
    Mailling("Envoie mail"),
    CLIENT("Client"),
    MOTIF("Motif"),
    MATERIEL("Materiel"),
    TYPECLIENT("TypeClient"),
    TYPEMATERIEL("TypeMateriel"),
    SOUSCRIPTION("Souscription"),
    FOURNISSEUR("Fournisseur"),
    PROFIL("Profil"),
    PROFORMA("Proforma"),
    COMPAGNIE("Compagine"),
    Aeroport("Aeroport"),
    PAYS("Pays"),
    EXERCICE("Exercice"),
    VOL("Vol"),
    LISTE_NOIRE("Liste noire"),
    RESPONSABLE("Responsable"),
    NOTIFICATION("Notification"),
    ENTETE("Entete");
    



    private String value;

    ObjetEntity(final String value) {
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
