/**
 * Author: Yan Zhuang and Yu Deng
 * Date: 03/04/2021
 * Organisme class that is responsible for the manipulation of all organisme
 * This is a superclass for {@code Herbivore}, {@code Carnivore}, {@code Plante}
 */

public class Organisme {
    private String nomEspece;
    private double energie;
    private int age;
    private double besoinEnergie;
    private double efficaciteEnergie;
    private double resilience;
    private double fertilite;
    private int ageFertilite;
    private double energieEnfant;

    public Organisme(String nomEspece, double energie, int age, double besoinEnergie, double efficaciteEnergie, double resilience, double fertilite, int ageFertilite, double energieEnfant) {
        this.nomEspece = nomEspece;
        this.energie = energie;
        this.age = age;
        this.besoinEnergie = besoinEnergie;
        this.efficaciteEnergie = efficaciteEnergie;
        this.resilience = resilience;
        this.fertilite = fertilite;
        this.ageFertilite = ageFertilite;
        this.energieEnfant = energieEnfant;
    }

    public Organisme(Organisme organisme){
        this.nomEspece = organisme.getNomEspece();
        this.energie = organisme.getEnergie();
        this.age = organisme.getAge();
        this.besoinEnergie = organisme.getBesoinEnergie();
        this.efficaciteEnergie = organisme.getEfficaciteEnergie();
        this.resilience = organisme.getResilience();
        this.fertilite = organisme.getFertilite();
        this.ageFertilite = organisme.getAgeFertilite();
        this.energieEnfant = organisme.getEnergieEnfant();
    }

    public String getNomEspece() {
        return nomEspece;
    }

    public double getEnergie() {
        return energie;
    }

    public int getAge() {
        return age;
    }

    public double getBesoinEnergie() {
        return besoinEnergie;
    }

    public double getEfficaciteEnergie() {
        return efficaciteEnergie;
    }

    public double getResilience() {
        return resilience;
    }

    public double getFertilite() {
        return fertilite;
    }

    public int getAgeFertilite() {
        return ageFertilite;
    }

    public double getEnergieEnfant() {
        return energieEnfant;
    }

    public void removeEnergy(double missingEnergy){
        energie -= missingEnergy;
    }

    public void addEnergy(double leftoverEnergy){
        energie += leftoverEnergy;
    }

    public void increaseAge(){age++;}

    // Create a baby. Type Organisme. Facilitate the creation of babies of other organismes (Plante, Herbivore, Carnivore)
    // No need to verify parameters, thus no need to go through factory method
    public Organisme createOrganismeBaby(){
        return new Organisme(nomEspece, energieEnfant,0, besoinEnergie, efficaciteEnergie, resilience, fertilite, ageFertilite, energieEnfant);
    }

}
