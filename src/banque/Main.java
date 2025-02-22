package banque;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        GestionBanque gestionBanque = new GestionBanque();

        // Ajout de clients
        System.out.println("\n=== Ajout de clients ===");
        gestionBanque.ajoutClient("C005", "Client 1", "RC001123456789010", "12345678901");
        gestionBanque.ajoutClient("C006", "Client 2", "RC002123456789020", "12345678902");
        // Modification d'un client
        System.out.println("\n=== Modification de client ===");
        gestionBanque.modifClient("C005", "Client 1 Modifié", "RC001123456789013", "98765432109");

        // Ajout de banques
        System.out.println("\n=== Ajout de banques ===");
        gestionBanque.ajoutBanque("B005", "Banque 1", "Adresse 1");
        gestionBanque.ajoutBanque("B006", "Banque 2", "Adresse 2");

        // Modification d'une banque
        System.out.println("\n=== Modification de banque ===");
        gestionBanque.modifBanque("B005", "Banque 1 Modifiée", "Adresse 1 Modifiée");

        // Ajout de comptes
        System.out.println("\n=== Ajout de comptes ===");
        gestionBanque.ajoutCompte("A001", "C005", "B005", 1000.0);
        gestionBanque.ajoutCompte("A002", "C006", "B006", 2000.0);

        // Dépôt sur un compte
        System.out.println("\n=== Dépôt sur un compte ===");
        gestionBanque.depot("A001", 500.0);

        // Retrait d'un compte
        System.out.println("\n=== Retrait d'un compte ===");
        gestionBanque.retrait("A001", 200.0);

        // Virement entre comptes
        System.out.println("\n=== Virement entre comptes ===");
        gestionBanque.virement("A001", "A002", 300.0);

        // Affichage des comptes d'une banque
        System.out.println("\n=== Affichage des comptes d'une banque ===");
        gestionBanque.afficheCompte("B005");

        // Affichage des crédits d'un client à une date donnée
        System.out.println("\n=== Affichage des crédits d'un client ===");
        gestionBanque.afficheCredit("C005", new Date());

        // Affichage des débits d'un client à une date donnée
        System.out.println("\n=== Affichage des débits d'un client ===");
        gestionBanque.afficheDebit("C005", new Date());
    }
}