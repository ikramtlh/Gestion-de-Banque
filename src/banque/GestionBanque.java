package banque;

import java.sql.*;
import java.util.Date;

public class GestionBanque {
    private Connection conn;
    
    public GestionBanque() {
        this.conn = Connexion.getConnection();
    }

    private boolean validerRC(String rc) {
        return rc != null && rc.matches("[a-zA-Z0-9]{17}");
    }

    private boolean validerArtImp(String artImp) {
        return artImp != null && artImp.matches("\\d{11}");
    }
    
    public void ajoutClient(String nClient, String raisonSocial, String rc, String artImp) {
        if (!validerRC(rc)) {
            System.out.println("Erreur : RC doit être un numéro alphanumérique de 17 caractères.");
            return;
        }
        if (!validerArtImp(artImp)) {
            System.out.println("Erreur : Art_Imp doit être un numéro de 11 chiffres.");
            return;
        }

        try {
            String sql = "INSERT INTO Client (N_client, Raison_social, RC, Art_Imp) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nClient);
            pstmt.setString(2, raisonSocial);
            pstmt.setString(3, rc);
            pstmt.setString(4, artImp);
            pstmt.executeUpdate();
            System.out.println("Client ajouté avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du client : " + e.getMessage());
        }
    }
    
    public void modifClient(String nClient, String raisonSocial, String rc, String artImp) {
        if (!validerRC(rc)) {
            System.out.println("Erreur : RC doit être un numéro alphanumérique de 17 caractères.");
            return;
        }
        if (!validerArtImp(artImp)) {
            System.out.println("Erreur : Art_Imp doit être un numéro de 11 chiffres.");
            return;
        }

        try {
            String sql = "UPDATE Client SET Raison_social = ?, RC = ?, Art_Imp = ? WHERE N_client = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, raisonSocial);
            pstmt.setString(2, rc);
            pstmt.setString(3, artImp);
            pstmt.setString(4, nClient);
            pstmt.executeUpdate();
            System.out.println("Client modifié avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification du client : " + e.getMessage());
        }
    }
    
    public void ajoutBanque(String nBanque, String nomBanque, String adresse) {
        try {
            String sql = "INSERT INTO Banque (N_Banque, Nom_Banque, adresse) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nBanque);
            pstmt.setString(2, nomBanque);
            pstmt.setString(3, adresse);
            pstmt.executeUpdate();
            System.out.println("Banque ajoutée avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la banque : " + e.getMessage());
        }
    }
    
    public void modifBanque(String nBanque, String nomBanque, String adresse) {
        try {
            String sql = "UPDATE Banque SET Nom_Banque = ?, adresse = ? WHERE N_Banque = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nomBanque);
            pstmt.setString(2, adresse);
            pstmt.setString(3, nBanque);
            pstmt.executeUpdate();
            System.out.println("Banque modifiée avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la banque : " + e.getMessage());
        }
    }
    
    public void afficheCompte(String nBanque) {
        try {
            String sql = "SELECT * FROM Adherant WHERE N_Banque = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nBanque);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                System.out.println("N° Compte: " + rs.getString("N_compte") + 
                                 ", N° Client: " + rs.getString("N_client") +
                                 ", Solde: " + rs.getDouble("Solde"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage des comptes : " + e.getMessage());
        }
    }
    
    public void afficheCredit(String nClient, Date date) {
        try {
            String sql = "SELECT * FROM Op_Bancaire ob " +
                        "JOIN Adherant a ON ob.N_Banque = a.N_Banque AND ob.N_compte = a.N_compte " +
                        "WHERE a.N_client = ? AND ob.date = ? AND ob.Credit > 0";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nClient);
            pstmt.setDate(2, new java.sql.Date(date.getTime()));
            ResultSet rs = pstmt.executeQuery();
    
            if (!rs.isBeforeFirst()) {
                System.out.println("Aucun crédit trouvé pour ce client à la date spécifiée.");
                return;
            }
    
            while (rs.next()) {
                System.out.println("Crédit: " + rs.getDouble("Credit") + 
                                 ", N° Cheque: " + rs.getString("N_cheque") +
                                 ", Libellé: " + rs.getString("Libelle_Cheque"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage des crédits : " + e.getMessage());
        }
    }
    
    public void afficheDebit(String nClient, Date date) {
        try {
            String sql = "SELECT * FROM Op_Bancaire ob " +
                        "JOIN Adherant a ON ob.N_Banque = a.N_Banque AND ob.N_compte = a.N_compte " +
                        "WHERE a.N_client = ? AND ob.date = ? AND ob.debit > 0";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nClient);
            pstmt.setDate(2, new java.sql.Date(date.getTime()));
            ResultSet rs = pstmt.executeQuery();
    
            if (!rs.isBeforeFirst()) {
                System.out.println("Aucun débit trouvé pour ce client à la date spécifiée.");
                return;
            }
    
            while (rs.next()) {
                System.out.println("Débit: " + rs.getDouble("debit") + 
                                 ", N° Cheque: " + rs.getString("N_cheque") +
                                 ", Libellé: " + rs.getString("Libelle_Cheque"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage des débits : " + e.getMessage());
        }
    }

    public void ajoutCompte(String nCompte, String nClient, String nBanque, double soldeInitial) {
        try {
            String sql = "INSERT INTO Adherant (N_compte, N_client, N_Banque, Solde) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nCompte);
            pstmt.setString(2, nClient);
            pstmt.setString(3, nBanque);
            pstmt.setDouble(4, soldeInitial);
            pstmt.executeUpdate();
            System.out.println("Compte ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du compte : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void depot(String nCompte, double montant) {
        try {
            String sql = "UPDATE Adherant SET Solde = Solde + ? WHERE N_compte = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, montant);
            pstmt.setString(2, nCompte);
            pstmt.executeUpdate();

            String insertSql = "INSERT INTO Op_Bancaire (N_Banque, N_compte, date, N_cheque, Libelle_Cheque, debit, credit) " +
                              "SELECT N_Banque, N_compte, ?, ?, ?, ?, ? FROM Adherant WHERE N_compte = ?";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setDate(1, new java.sql.Date(new Date().getTime())); 
            insertStmt.setString(2, "CHQ" + System.currentTimeMillis()); 
            insertStmt.setString(3, "Dépôt de " + montant); 
            insertStmt.setDouble(4, 0.0); 
            insertStmt.setDouble(5, montant); 
            insertStmt.setString(6, nCompte); 
            insertStmt.executeUpdate();
    
            System.out.println("Dépôt de " + montant + " réussi !");
        } catch (SQLException e) {
            System.out.println("Erreur lors du dépôt : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void retrait(String nCompte, double montant) {
        try {
            String checkSolde = "SELECT Solde FROM Adherant WHERE N_compte = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSolde);
            checkStmt.setString(1, nCompte);
            ResultSet rs = checkStmt.executeQuery();
    
            if (rs.next()) {
                double solde = rs.getDouble("Solde");
                if (solde >= montant) {
                    String sql = "UPDATE Adherant SET Solde = Solde - ? WHERE N_compte = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setDouble(1, montant);
                    pstmt.setString(2, nCompte);
                    pstmt.executeUpdate();
    
                    String insertSql = "INSERT INTO Op_Bancaire (N_Banque, N_compte, date, N_cheque, Libelle_Cheque, debit, credit) " +
                                      "SELECT N_Banque, N_compte, ?, ?, ?, ?, ? FROM Adherant WHERE N_compte = ?";
                    PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                    insertStmt.setDate(1, new java.sql.Date(new Date().getTime())); 
                    insertStmt.setString(2, "CHQ" + System.currentTimeMillis());
                    insertStmt.setString(3, "Retrait de " + montant); 
                    insertStmt.setDouble(4, montant); 
                    insertStmt.setDouble(5, 0.0); 
                    insertStmt.setString(6, nCompte); 
                    insertStmt.executeUpdate();
    
                    System.out.println("Retrait de " + montant + " réussi !");
                } else {
                    System.out.println("Solde insuffisant !");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du retrait : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void virement(String compteSource, String compteDest, double montant) {
        try {
            conn.setAutoCommit(false); 
    
            String checkSolde = "SELECT Solde FROM Adherant WHERE N_compte = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSolde);
            checkStmt.setString(1, compteSource);
            ResultSet rs = checkStmt.executeQuery();
    
            if (rs.next()) {
                double solde = rs.getDouble("Solde");
                if (solde >= montant) {

                    String debitSql = "UPDATE Adherant SET Solde = Solde - ? WHERE N_compte = ?";
                    PreparedStatement debitStmt = conn.prepareStatement(debitSql);
                    debitStmt.setDouble(1, montant);
                    debitStmt.setString(2, compteSource);
                    debitStmt.executeUpdate();
    
                    String insertDebitSql = "INSERT INTO Op_Bancaire (N_Banque, N_compte, date, N_cheque, Libelle_Cheque, debit, credit) " +
                                           "SELECT N_Banque, N_compte, ?, ?, ?, ?, ? FROM Adherant WHERE N_compte = ?";
                    PreparedStatement insertDebitStmt = conn.prepareStatement(insertDebitSql);
                    insertDebitStmt.setDate(1, new java.sql.Date(new Date().getTime())); 
                    insertDebitStmt.setString(2, "CHQ" + System.currentTimeMillis()); 
                    insertDebitStmt.setString(3, "Virement sortant de " + montant);
                    insertDebitStmt.setDouble(4, montant); 
                    insertDebitStmt.setDouble(5, 0.0); 
                    insertDebitStmt.setString(6, compteSource); 
                    insertDebitStmt.executeUpdate();
    
                    String creditSql = "UPDATE Adherant SET Solde = Solde + ? WHERE N_compte = ?";
                    PreparedStatement creditStmt = conn.prepareStatement(creditSql);
                    creditStmt.setDouble(1, montant);
                    creditStmt.setString(2, compteDest);
                    creditStmt.executeUpdate();
    
                    String insertCreditSql = "INSERT INTO Op_Bancaire (N_Banque, N_compte, date, N_cheque, Libelle_Cheque, debit, credit) " +
                                             "SELECT N_Banque, N_compte, ?, ?, ?, ?, ? FROM Adherant WHERE N_compte = ?";
                    PreparedStatement insertCreditStmt = conn.prepareStatement(insertCreditSql);
                    insertCreditStmt.setDate(1, new java.sql.Date(new Date().getTime())); 
                    insertCreditStmt.setString(2, "CHQ" + System.currentTimeMillis()); 
                    insertCreditStmt.setString(3, "Virement entrant de " + montant); 
                    insertCreditStmt.setDouble(4, 0.0); 
                    insertCreditStmt.setDouble(5, montant); 
                    insertCreditStmt.setString(6, compteDest); 
                    insertCreditStmt.executeUpdate();
    
                    conn.commit(); 
                    System.out.println("Virement de " + montant + " réussi !");
                } else {
                    System.out.println("Solde insuffisant !");
                }
            }
        } catch (SQLException e) {
            try {
                conn.rollback(); 
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            System.out.println("Erreur lors du virement : " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}