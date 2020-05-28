package com.example.projet.ui;

public class Etudiant  {
    private String nom;
    private String prenom;
    private String classe;

    public Etudiant(String nom,String prenom,String classe) {

        this.nom = nom;
        this.prenom = prenom;
        this.classe = classe;

        }

    public String getClasse() {
        return classe;
    }
    public String getNom() {
        return nom;
    }
    public String getPrenom() {
        return prenom;
    }

}
