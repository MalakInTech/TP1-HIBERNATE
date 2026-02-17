package org.example;

import com.example.model.Produit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // Création de l'EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate-demo");

        // Insertion de produits
        insererProduits(emf);

        // Lecture des produits
        lireProduits(emf);

        //Mise a jour du prix d'un produit
        mettreAJourPrix(emf, 2L, new BigDecimal("549.99"));

        //Suppression d'un produit
        supprimerProduit(emf, 3L);

        //Recherche par prix
        rechercheParPrix(emf, new BigDecimal("300"), new BigDecimal("1000"));

        // Fermeture de l'EntityManagerFactory
        emf.close();
    }

    private static void insererProduits(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Création de quelques produits
            Produit p1 = new Produit("Laptop", new BigDecimal("999.99"));
            Produit p2 = new Produit("Smartphone", new BigDecimal("499.99"));
            Produit p3 = new Produit("Tablette", new BigDecimal("299.99"));

            // Persistance des produits
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);

            em.getTransaction().commit();
            System.out.println("Produits insérés avec succès !");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void lireProduits(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            // Requête JPQL pour récupérer tous les produits
            List<Produit> produits = em.createQuery("SELECT p FROM Produit p", Produit.class)
                    .getResultList();

            System.out.println("\nListe des produits :");
            for (Produit produit : produits) {
                System.out.println(produit);
            }

            // Recherche d'un produit par ID
            System.out.println("\nRecherche du produit avec ID=2 :");
            Produit produit = em.find(Produit.class, 2L);
            if (produit != null) {
                System.out.println(produit);
            } else {
                System.out.println("Produit non trouvé");
            }
        } finally {
            em.close();
        }
    }

    private static void mettreAJourPrix(EntityManagerFactory emf, Long produitId, BigDecimal nouveauPrix) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // On récupère le produit par ID
            Produit produit = em.find(Produit.class, produitId);
            if (produit != null) {
                produit.setPrix(nouveauPrix); // on change le prix
                System.out.println("Prix mis à jour pour : " + produit);
            } else {
                System.out.println("Produit non trouvé");
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void supprimerProduit(EntityManagerFactory emf, Long produitId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Produit produit = em.find(Produit.class, produitId);
            if (produit != null) {
                em.remove(produit);
                System.out.println("Produit supprimé : " + produit);
            } else {
                System.out.println("Produit non trouvé");
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void rechercheParPrix(EntityManagerFactory emf, BigDecimal min, BigDecimal max) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Produit> produits = em.createQuery(
                            "SELECT p FROM Produit p WHERE p.prix BETWEEN :min AND :max", Produit.class)
                    .setParameter("min", min)
                    .setParameter("max", max)
                    .getResultList();

            System.out.println("\nProduits avec prix entre " + min + " et " + max + " :");
            for (Produit p : produits) {
                System.out.println(p);
            }
        } finally {
            em.close();
        }
    }




}
