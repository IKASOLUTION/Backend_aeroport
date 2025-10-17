package aeroport.bf.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * Générateur de codes avec préfixes personnalisés
 * Exemple: PR-SBMP-09-2025-001
 */
public class GenerateurCodes {
    
    // Enumération pour les différents formats de code
    public enum FormatCode {
        MMJJAAAA_NNN("MMJJAAAA-NNN", "Format date classique"),
        JJMMAAAA_NNN("JJMMAAAA-NNN", "Format européen"),
        AAAAMMJJ_NNN("AAAAMMJJ-NNN", "Format ISO"),
        PREFIXE_PERSONNALISE("PREFIXE-MOIS-ANNEE-INCREMENT", "Format avec préfixes personnalisés");
        
        private final String format;
        private final String description;
        
        FormatCode(String format, String description) {
            this.format = format;
            this.description = description;
        }
        
        public String getDescription() { return description; }
        
        @Override
        public String toString() {
            return format;
        }
    }
    
    // Classe pour stocker les préfixes personnalisés
    public static class PrefixesPersonnalises {
        private String prefixe1;
        private String prefixe2;
        
        public PrefixesPersonnalises() {
            this.prefixe1 = "PR";
            this.prefixe2 = "SBMP";
        }
        
        public PrefixesPersonnalises(String prefixe1, String prefixe2) {
            this.prefixe1 = prefixe1;
            this.prefixe2 = prefixe2;
        }
        
        // Getters et Setters
        public String getPrefixe1() { return prefixe1; }
        public void setPrefixe1(String prefixe1) { this.prefixe1 = prefixe1; }
        
        public String getPrefixe2() { return prefixe2; }
        public void setPrefixe2(String prefixe2) { this.prefixe2 = prefixe2; }
        
        @Override
        public String toString() {
            return prefixe1 + "-" + prefixe2;
        }
    }
    
    // Classe pour stocker les informations d'un code généré
    public static class CodeGenere {
        private final String code;
        private final LocalDate date;
        private final int sequence;
        private final FormatCode format;
        private final String dateCreation;
        private final PrefixesPersonnalises prefixes;
        
        public CodeGenere(String code, LocalDate date, int sequence, FormatCode format, PrefixesPersonnalises prefixes) {
            this.code = code;
            this.date = date;
            this.sequence = sequence;
            this.format = format;
            this.prefixes = prefixes;
            this.dateCreation = LocalDate.now().toString() + " " + 
                              java.time.LocalTime.now().toString().substring(0, 8);
        }
        
        // Getters
        public String getCode() { return code; }
        public LocalDate getDate() { return date; }
        public int getSequence() { return sequence; }
        public FormatCode getFormat() { return format; }
        public String getDateCreation() { return dateCreation; }
        public PrefixesPersonnalises getPrefixes() { return prefixes; }
        
        @Override
        public String toString() {
            if (format == FormatCode.PREFIXE_PERSONNALISE) {
                return String.format("Code: %s | Date: %s | Préfixes: %s | Séq: #%03d | Créé: %s", 
                                   code, date.toString(), prefixes.toString(), sequence, dateCreation);
            } else {
                return String.format("Code: %s | Date: %s | Séquence: #%03d | Créé: %s", 
                                   code, date.toString(), sequence, dateCreation);
            }
        }
    }
    
    // Stockage des compteurs par mois-année et par préfixe
    private Map<String, Integer> compteurs;
    
    // Historique des codes générés
    private List<CodeGenere> historique;
    
    // Format par défaut
    private FormatCode formatActuel;
    
    // Préfixes personnalisés
    private PrefixesPersonnalises prefixes;
    
    // Scanner pour les entrées utilisateur
    private Scanner scanner;
    
    /**
     * Constructeur
     */
    public GenerateurCodes() {
        this.compteurs = new HashMap<>();
        this.historique = new ArrayList<>();
        this.formatActuel = FormatCode.PREFIXE_PERSONNALISE; // Format par défaut
        this.prefixes = new PrefixesPersonnalises(); // PR-SBMP par défaut
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Génère un code pour une date donnée
     * @param date La date pour laquelle générer le code
     * @return Le code généré
     */
    public CodeGenere genererCode(LocalDate date) {
        // Extraire les composants de la date
        int mois = date.getMonthValue();
        int jour = date.getDayOfMonth();
        int annee = date.getYear();
        
        // Créer la clé pour le compteur selon le format
        String cleCompteur;
        if (formatActuel == FormatCode.PREFIXE_PERSONNALISE) {
            // Pour le format personnalisé, la clé inclut les préfixes
            cleCompteur = String.format("%s-%s-%02d-%04d", 
                                      prefixes.getPrefixe1(), 
                                      prefixes.getPrefixe2(), 
                                      mois, annee);
        } else {
            // Pour les autres formats, clé classique
            cleCompteur = String.format("%02d-%04d", mois, annee);
        }
        
        // Incrémenter le compteur
        int sequence = compteurs.getOrDefault(cleCompteur, 0) + 1;
        compteurs.put(cleCompteur, sequence);
        
        // Générer le code selon le format sélectionné
        String code = genererCodeSelonFormat(mois, jour, annee, sequence);
        
        // Créer l'objet CodeGenere
        CodeGenere codeGenere = new CodeGenere(code, date, sequence, formatActuel, 
                                             new PrefixesPersonnalises(prefixes.getPrefixe1(), 
                                                                     prefixes.getPrefixe2()));
        
        // Ajouter à l'historique
        historique.add(codeGenere);
        
        return codeGenere;
    }
    
    /**
     * Génère le code selon le format spécifié
     */
    public String genererCodeSelonFormat(int mois, int jour, int annee, int sequence) {
        String numeroSequence = String.format("%03d", sequence);
        
        switch (formatActuel) {
            case MMJJAAAA_NNN:
                return String.format("%02d%02d%04d-%s", mois, jour, annee, numeroSequence);
            case JJMMAAAA_NNN:
                return String.format("%02d%02d%04d-%s", jour, mois, annee, numeroSequence);
            case AAAAMMJJ_NNN:
                return String.format("%04d%02d%02d-%s", annee, mois, jour, numeroSequence);
            case PREFIXE_PERSONNALISE:
                return String.format("%s-%s-%02d-%04d-%s", 
                                   prefixes.getPrefixe1(), 
                                   prefixes.getPrefixe2(), 
                                   mois, annee, numeroSequence);
            default:
                return String.format("%02d%02d%04d-%s", mois, jour, annee, numeroSequence);
        }
    }
    
    /**
     * Génère un code pour la date actuelle
     */
    public CodeGenere genererCodeAujourdhui() {
        return genererCode(LocalDate.now());
    }
    
    /**
     * Change le format de code
     */
    public void changerFormat(FormatCode nouveauFormat) {
        this.formatActuel = nouveauFormat;
        System.out.println("✅ Format changé vers: " + nouveauFormat);
        if (nouveauFormat == FormatCode.PREFIXE_PERSONNALISE) {
            System.out.println("   Préfixes actuels: " + prefixes.toString());
        }
    }
    
    /**
     * Modifie les préfixes personnalisés
     */
    public void modifierPrefixes(String prefixe1, String prefixe2) {
        this.prefixes.setPrefixe1(prefixe1.toUpperCase());
        this.prefixes.setPrefixe2(prefixe2.toUpperCase());
        System.out.println("✅ Préfixes modifiés: " + prefixes.toString());
    }
    
    /**
     * Affiche l'historique des codes générés
     */
    public void afficherHistorique() {
        System.out.println("\n" + "=".repeat(90));
        System.out.println("📚 HISTORIQUE DES CODES GÉNÉRÉS");
        System.out.println("=".repeat(90));
        
        if (historique.isEmpty()) {
            System.out.println("Aucun code généré pour le moment.");
        } else {
            for (int i = historique.size() - 1; i >= 0; i--) {
                System.out.printf("[%02d] %s%n", historique.size() - i, historique.get(i));
            }
        }
        System.out.println("=".repeat(90));
    }
    
    /**
     * Affiche les statistiques des compteurs
     */
    public void afficherStatistiques() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("📊 STATISTIQUES DES COMPTEURS");
        System.out.println("=".repeat(80));
        
        if (compteurs.isEmpty()) {
            System.out.println("Aucune donnée disponible.");
        } else {
            compteurs.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByKey().reversed())
                .forEach(entry -> {
                    System.out.printf("Clé [%s]: %d codes générés%n", 
                                    entry.getKey(), entry.getValue());
                });
        }
        System.out.println("=".repeat(80));
    }
    
    /**
     * Réinitialise tous les compteurs
     */
    public void reinitialiserCompteurs() {
        compteurs.clear();
        historique.clear();
        System.out.println("✅ Tous les compteurs ont été réinitialisés.");
    }
    
    /**
     * Parse une date depuis une chaîne
     */
    private LocalDate parseDate(String dateStr) throws DateTimeParseException {
        DateTimeFormatter[] formats = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd")
        };
        
        for (DateTimeFormatter format : formats) {
            try {
                return LocalDate.parse(dateStr, format);
            } catch (DateTimeParseException e) {
                // Continuer avec le prochain format
            }
        }
        
        throw new DateTimeParseException("Format de date non reconnu", dateStr, 0);
    }
    
    /**
     * Interface en ligne de commande
     */
    public void lancerInterface() {
        System.out.println("🎯 GÉNÉRATEUR DE CODES JAVA AVANCÉ");
        System.out.println("=".repeat(60));
        
        while (true) {
            afficherMenu();
            
            try {
                int choix = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choix) {
                    case 1:
                        genererCodePourAujourdhui();
                        break;
                    case 2:
                        genererCodePourDateCustom();
                        break;
                    case 3:
                        changerFormatCode();
                        break;
                    case 4:
                        modifierPrefixesPersonnalises();
                        break;
                    case 5:
                        afficherHistorique();
                        break;
                    case 6:
                        afficherStatistiques();
                        break;
                    case 7:
                        genererPlusieursCodesDemo();
                        break;
                    case 8:
                        reinitialiserTout();
                        break;
                    case 0:
                        System.out.println("👋 Au revoir !");
                        return;
                    default:
                        System.out.println("❌ Choix invalide. Veuillez réessayer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Veuillez entrer un nombre valide.");
            }
            
            System.out.println("\nAppuyez sur Entrée pour continuer...");
            scanner.nextLine();
        }
    }
    
    private void afficherMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 MENU PRINCIPAL");
        System.out.println("=".repeat(60));
        System.out.println("Format actuel: " + formatActuel);
        if (formatActuel == FormatCode.PREFIXE_PERSONNALISE) {
            System.out.println("Préfixes: " + prefixes.toString());
        }
        System.out.println("Total codes générés: " + historique.size());
        System.out.println("-".repeat(60));
        System.out.println("1. 🆕 Générer code pour aujourd'hui");
        System.out.println("2. 📅 Générer code pour une date spécifique");
        System.out.println("3. 🔧 Changer format de code");
        System.out.println("4. 🏷️  Modifier préfixes personnalisés");
        System.out.println("5. 📚 Voir l'historique");
        System.out.println("6. 📊 Voir les statistiques");
        System.out.println("7. 🚀 Démonstration (plusieurs codes)");
        System.out.println("8. 🔄 Réinitialiser tout");
        System.out.println("0. ❌ Quitter");
        System.out.println("=".repeat(60));
        System.out.print("Votre choix: ");
    }
    
    private void genererCodePourAujourdhui() {
        CodeGenere code = genererCodeAujourdhui();
        afficherCodeGenere(code);
    }
    
    private void genererCodePourDateCustom() {
        System.out.print("Entrez la date (yyyy-MM-dd, dd/MM/yyyy, etc.): ");
        String dateStr = scanner.nextLine().trim();
        
        try {
            LocalDate date = parseDate(dateStr);
            CodeGenere code = genererCode(date);
            afficherCodeGenere(code);
        } catch (DateTimeParseException e) {
            System.out.println("❌ Format de date invalide. Formats acceptés:");
            System.out.println("   - yyyy-MM-dd (ex: 2024-12-25)");
            System.out.println("   - dd/MM/yyyy (ex: 25/12/2024)");
            System.out.println("   - dd-MM-yyyy (ex: 25-12-2024)");
        }
    }
    
    private void changerFormatCode() {
        System.out.println("\n📋 FORMATS DISPONIBLES:");
        FormatCode[] formats = FormatCode.values();
        for (int i = 0; i < formats.length; i++) {
            String indicateur = formats[i] == formatActuel ? " ✓ (actuel)" : "";
            System.out.printf("%d. %s - %s%s%n", 
                            i + 1, formats[i], formats[i].getDescription(), indicateur);
        }
        
        System.out.print("Choisissez un format (1-" + formats.length + "): ");
        try {
            int choix = Integer.parseInt(scanner.nextLine().trim());
            if (choix >= 1 && choix <= formats.length) {
                changerFormat(formats[choix - 1]);
            } else {
                System.out.println("❌ Choix invalide.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Veuillez entrer un nombre valide.");
        }
    }
    
    private void modifierPrefixesPersonnalises() {
        System.out.println("\n🏷️ MODIFICATION DES PRÉFIXES");
        System.out.println("Préfixes actuels: " + prefixes.toString());
        
        System.out.print("Nouveau préfixe 1 (actuellement: " + prefixes.getPrefixe1() + "): ");
        String nouveauPrefixe1 = scanner.nextLine().trim();
        if (nouveauPrefixe1.isEmpty()) {
            nouveauPrefixe1 = prefixes.getPrefixe1();
        }
        
        System.out.print("Nouveau préfixe 2 (actuellement: " + prefixes.getPrefixe2() + "): ");
        String nouveauPrefixe2 = scanner.nextLine().trim();
        if (nouveauPrefixe2.isEmpty()) {
            nouveauPrefixe2 = prefixes.getPrefixe2();
        }
        
        modifierPrefixes(nouveauPrefixe1, nouveauPrefixe2);
    }
    
    private void genererPlusieursCodesDemo() {
        System.out.println("\n🚀 GÉNÉRATION DE CODES DE DÉMONSTRATION");
        System.out.println("Génération de 5 codes avec le format actuel...\n");
        
        LocalDate dateBase = LocalDate.now();
        for (int i = 0; i < 5; i++) {
            LocalDate date = dateBase.plusDays(i);
            CodeGenere code = genererCode(date);
            System.out.printf("[%d] %s%n", i + 1, code.getCode());
        }
    }
    
    private void afficherCodeGenere(CodeGenere code) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🎉 CODE GÉNÉRÉ AVEC SUCCÈS");
        System.out.println("=".repeat(70));
        System.out.println("📋 Code: " + code.getCode());
        System.out.println("📅 Date: " + code.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("🔢 Séquence: #" + String.format("%03d", code.getSequence()));
        System.out.println("📝 Format: " + code.getFormat());
        if (code.getFormat() == FormatCode.PREFIXE_PERSONNALISE) {
            System.out.println("🏷️ Préfixes: " + code.getPrefixes().toString());
        }
        System.out.println("⏰ Créé le: " + code.getDateCreation());
        System.out.println("=".repeat(70));
    }
    
    private void reinitialiserTout() {
        System.out.print("⚠️  Êtes-vous sûr de vouloir tout réinitialiser? (oui/non): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        
        if (confirmation.equals("oui") || confirmation.equals("o") || confirmation.equals("yes")) {
            reinitialiserCompteurs();
        } else {
            System.out.println("❌ Opération annulée.");
        }
    }
    
    /**
     * Méthode principale
     */
    public static void main(String[] args) {
        GenerateurCodes generateur = new GenerateurCodes();
        
        // Démonstration rapide
        if (args.length > 0 && args[0].equals("demo")) {
            demonstrationRapide(generateur);
        } else {
            // Lancer l'interface interactive
            generateur.lancerInterface();
        }
    }
    
    /**
     * Démonstration rapide des fonctionnalités
     */
    private static void demonstrationRapide(GenerateurCodes generateur) {
        System.out.println("🚀 DÉMONSTRATION RAPIDE - FORMAT PERSONNALISÉ");
        System.out.println("=".repeat(60));
        
        // Générer avec le format par défaut PR-SBMP
        CodeGenere code1 = generateur.genererCodeAujourdhui();
        System.out.println("Code 1 (PR-SBMP): " + code1.getCode());
        
        CodeGenere code2 = generateur.genererCodeAujourdhui();
        System.out.println("Code 2 (PR-SBMP): " + code2.getCode());
        
        // Changer les préfixes
        generateur.modifierPrefixes("ADM", "TECH");
        CodeGenere code3 = generateur.genererCodeAujourdhui();
        System.out.println("Code 3 (ADM-TECH): " + code3.getCode());
        
        // Générer pour une date spécifique
        CodeGenere code4 = generateur.genererCode(LocalDate.of(2024, 12, 25));
        System.out.println("Code 4 (25/12/2024): " + code4.getCode());
        
        // Changer vers format classique
        generateur.changerFormat(FormatCode.MMJJAAAA_NNN);
        CodeGenere code5 = generateur.genererCodeAujourdhui();
        System.out.println("Code 5 (classique): " + code5.getCode());
        
        // Afficher statistiques
        generateur.afficherStatistiques();
    }
}
