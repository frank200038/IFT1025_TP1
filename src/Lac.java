import java.io.PrintStream;

import java.util.*;

import static java.util.stream.Collectors.*;

/**
 * Author: Yan Zhuang and Yu Deng
 *
 * Lac, where the simulation happens. Contains the main method tick() that handles the simulation, and other
 * helper methods
 *
 * Date: 03/04/2021
 */
public final class Lac {
    private final int energieSolaire;
    private final List<Plante> plantes;
    private final List<Herbivore> herbivores;
    private final List<Carnivore> carnivores;

    public Lac(int energieSolaire, List<Plante> plantes,List<Herbivore> herbivores,List<Carnivore> carnivores) {
        this.energieSolaire = energieSolaire;
        this.plantes = plantes;
        this.herbivores = herbivores;
        this.carnivores = carnivores;
    }

    /**
     * Avance la simulation d'un cycle.
     */
    public void tick() {
        List<Organisme> babyArray = new ArrayList<>(); // Create a new array that will contain babies made from an organisme

        for (Plante plante: plantes){
            double absorbedEnergy = absorbedEnergyForPlante(plante); // Calculate the energy absorbed by the plant
            double neededEnergy = plante.getBesoinEnergie(); // Energy needed to survive

            if (neededEnergy > absorbedEnergy){
                verifySurvivalChance(neededEnergy,absorbedEnergy,plante);
            }
            else {
                double leftoverEnergy = absorbedEnergy - neededEnergy;
                if (plante.getAge() >= plante.getAgeFertilite()) {
                    leftoverEnergy = makeBaby(leftoverEnergy,plante,babyArray);
                }

                if (leftoverEnergy > 0) {
                    plante.addEnergy(leftoverEnergy * plante.getEfficaciteEnergie());
                }
            }
        }

        verifyOrganismState(); //Remove all the dead organisme (Organismes that have negative or 0 unit of energy)

        // Add new Plant babies to Plante array
        for (Organisme baby : babyArray){
            if (baby instanceof Plante) {
                plantes.add((Plante) baby);
            }
        }

        //Clear baby array for herbivores to store their babies
        babyArray.clear();

        for (Herbivore herbivore: herbivores){
            double debrouillardise = herbivore.getDebrouillardise();
            int eatTimes = 0; // Times that the herbivore can feed himself
            double absorbedEnergy = 0;
            double neededEnergy = herbivore.getBesoinEnergie(); // Energy needed to survive

            while (generateRandom() <= debrouillardise){
                eatTimes++;
            }

            while (eatTimes > 0){
                var foodToEat = findFood(herbivore);
                if ( foodToEat != null ) {
                    double energyMin = herbivore.getVoraciteMin() * foodToEat.getEnergie();
                    double energyMax = herbivore.getVoraciteMax() * foodToEat.getEnergie();
                    double obtainedEnergy = generateRandom(energyMax, energyMin);

                    absorbedEnergy += obtainedEnergy;
                    foodToEat.removeEnergy(obtainedEnergy);
                    eatTimes--;
                }
                // There is no more food that can be eaten.
                else{
                    break;
                }
            }

            if (neededEnergy > absorbedEnergy){
                verifySurvivalChance(neededEnergy,absorbedEnergy,herbivore);
            }
            else {
                double leftoverEnergy = absorbedEnergy - neededEnergy;
                if (herbivore.getAge() >= herbivore.getAgeFertilite()) {
                    leftoverEnergy = makeBaby(leftoverEnergy,herbivore,babyArray);
                }

                if (leftoverEnergy > 0) {
                    herbivore.addEnergy(leftoverEnergy * herbivore.getEfficaciteEnergie());
                }
            }
        }

        // Remove all the dead organismes at this point (Organismes that have negative or 0 unit of energy)
        verifyOrganismState();

        // Add all the herbivore babies into Herbivore array
        for (Organisme baby : babyArray){
            if (baby instanceof Herbivore) {
                herbivores.add((Herbivore) baby);
            }
        }

        // Clear the array again for Carnivores to store their babies
        babyArray.clear();

        for (Carnivore carnivore: carnivores){
            double debrouillardise = carnivore.getDebrouillardise();
            int eatTimes = 0;// Times that the carnivore can feed himself
            double absorbedEnergy = 0;
            double neededEnergy = carnivore.getBesoinEnergie(); // Energy needed to survive

            while (generateRandom() <= debrouillardise){
                eatTimes++;
            }

            while (eatTimes > 0){
                var foodToEat = findFood(carnivore);

                if (foodToEat != null ) {
                    double obtainedEnergy = foodToEat.getEnergie();
                    absorbedEnergy += obtainedEnergy;

                    // Remove all the energies from the organism being eaten.(= dead) Organisme will be removed at the end
                    foodToEat.removeEnergy(obtainedEnergy);

                    eatTimes--;
                }
                // There is no more food that can be eaten
                else{
                    break;
                }
            }

            if (neededEnergy > absorbedEnergy){
                verifySurvivalChance(neededEnergy,absorbedEnergy,carnivore);
            }
            else {
                double leftoverEnergy = absorbedEnergy - neededEnergy;
                if (carnivore.getAge() >= carnivore.getAgeFertilite()) {
                    leftoverEnergy = makeBaby(leftoverEnergy,carnivore,babyArray);
                }

                if (leftoverEnergy > 0) {
                    carnivore.addEnergy(leftoverEnergy * carnivore.getEfficaciteEnergie());
                }
            }
        }

        // Remove all the dead organisme (Organismes that have negative or 0 unit of energy)
        verifyOrganismState();

        // Add all the carnivore babies
        for (Organisme baby : babyArray){
            if (baby instanceof Carnivore) {
                carnivores.add((Carnivore) baby);
            }
        }

        // Increase all the organisme ages by one, for next cycle
        increaseAgeForAll();


    }

    public void imprimeRapport(PrintStream out) {
        var especes = this.plantes.stream().collect(groupingBy(
                Plante::getNomEspece,
                summarizingDouble(Plante::getEnergie)));
        out.println("Il reste " + especes.size() + " espèces de plantes.");
        for (var entry : especes.entrySet()) {
            var value = entry.getValue();
            out.printf(
                    "%s: %d individus qui contiennent en tout %.2f unités d'énergie.\n",
                    entry.getKey(),
                    value.getCount(),
                    value.getSum());
        }

        out.println();

        var herbivores = this.herbivores.stream().collect(groupingBy(
                Herbivore::getNomEspece,
                summarizingDouble(Herbivore::getEnergie)));
        out.println("Il reste " + herbivores.size() + " espèces d'herbivores.");
        for (var entry : herbivores.entrySet()) {
            var value = entry.getValue();
            out.printf(
                    "%s: %d individus qui contiennent en tout %.2f unités d'énergie.\n",
                    entry.getKey(),
                    value.getCount(),
                    value.getSum());
        }
        out.println();
        var carnivores = this.carnivores.stream().collect(groupingBy(
                Carnivore::getNomEspece,
                summarizingDouble(Carnivore::getEnergie)));
        out.println("Il reste " + carnivores.size() + " espèces de caranivores.");
        for (var entry : carnivores.entrySet()) {
            var value = entry.getValue();
            out.printf(
                    "%s: %d individus qui contiennent en tout %.10f unités d'énergie.\n",
                    entry.getKey(),
                    value.getCount(),
                    value.getSum());
        }

    }


    /**
     * Calculate the total energy that all the plants in the environment contains.
     * @return  Total energy of all plants
     */
    private double totalEnergyOfPlante(){
        double totalEnergy = 0;
        for (Plante plante: plantes){
            totalEnergy += plante.getEnergie();
        }
        return totalEnergy;
    }

    /**
     * Calculate the amount of energy absorbed by a particular plant.
     * @param plante The plant that absorbs the energy
     * @return The amount of energy the {@code Plante} suppose to absorb
     */
    private double absorbedEnergyForPlante(Plante plante){
        double totalEnergy = totalEnergyOfPlante();
        double currentEnergy = plante.getEnergie();
        return energieSolaire * currentEnergy / totalEnergy;
    }

    /**
     * Calculate the survival chance of an organism
     * @param missingEnergy Unit of energy that is not sufficient for the organism to grow
     * @param resilience The resilience of the organism
     * @return The survival rate that this organism has, based on the formula
     */
    private double calculateSurvivalRate(double missingEnergy, double resilience){
        return Math.pow(resilience,missingEnergy) ;
    }

    /**
     * Verify if an organisme survives or not, based on the randomly generated number and its survival possibility
     * If the survival possibility is lower than the randomly generate number, the organism dies.
     * Otherwise, it lives and the missing energy is subtracted from the organisme
     *
     * @param neededEnergy Amount of energy that the organisme needs to survive
     * @param absorbedEnergy Amount of energy that the organisme absorbed (Either from sun or from eating)
     * @param organisme The organism being treated
     */
    private void verifySurvivalChance(double neededEnergy, double absorbedEnergy, Organisme organisme) {

        double missingEnergy = neededEnergy - absorbedEnergy;
        double chanceSurvive = calculateSurvivalRate(missingEnergy, organisme.getResilience());

        if (chanceSurvive >= generateRandom()) {
            organisme.removeEnergy(missingEnergy);
        } else {
            // Remove all the energy (= dead), will be eliminated at the end.
            organisme.removeEnergy(organisme.getEnergie());
        }
    }

    /**
     * Find the food for a herbivore or for a carnivore
     * @param organisme The Organisme (Herbivore or Carnivore) that needs to eat
     * @return The food (Organisme), randomly chosen, to be eaten by the animal
     */
    private Organisme findFood(Organisme organisme){
        var rightFood = new ArrayList<Organisme>();

        // Herbivores can only eat plants
        if(organisme instanceof Herbivore) {
            var herbivore = (Herbivore)organisme;
            var aliment = herbivore.getAliments();

            for (Plante plante:plantes){
                if (aliment.contains(plante.getNomEspece()) && plante.getEnergie() > 0){
                    rightFood.add(plante);
                }
            }
        }
        // Carnivores can eat herbivores and other carnivores too ( But not their own species)
        else if (organisme instanceof Carnivore) {
            var carnivore = (Carnivore) organisme;
            var aliment = carnivore.getAliments();
            var tailleMaximum = carnivore.getTailleMaximum();
            var name = carnivore.getNomEspece();

            for (Herbivore herbivore:herbivores){
                // Make sure we find the right herbivore, and the herbivore is smaller than the carnivore.
                if (aliment.contains(herbivore.getNomEspece()) && herbivore.getEnergie() > 0){
                    if (herbivore.getTailleMaximum() <= tailleMaximum) {
                        rightFood.add(herbivore);
                    }
                }
            }

            for (Carnivore carnivoreToEat : carnivores){
                // Make sure we find the right carnivore to be eaten, and that the carnivore is not eating its own kind. Also
                // make sure that the size of the carnivore to be eaten is smaller.
                if (aliment.contains(carnivoreToEat.getNomEspece()) && carnivoreToEat.getEnergie() > 0){
                    if (carnivoreToEat.getTailleMaximum() <= tailleMaximum  && !name.equals(carnivoreToEat.getNomEspece())) {
                        rightFood.add(carnivoreToEat);
                    }
                }
            }
        }

        // To make sure we are not just eating always the same organisme
        // Randomly chose one organisme among all the organisme that can be eaten
        if (rightFood.size() != 0){
            var randomIndex = generateRandomInt(rightFood.size(),0);
            return rightFood.get(randomIndex);
        }else{
            return null;
        }
    }

    /**
     * Process to make baby. A baby can be made only if the organisme reaches the age of maturity, and its fertility is
     * smaller than the random generated number. For each unit of {@code leftOverEnergy}, there will be one opportuninity
     * to make a baby. The number of tentatives will be subtracted, based on the energy needed for a baby, each time after a
     * baby is made
     *
     * @param leftoverEnergy Amount of energy left
     * @param organisme The orangsime from which a baby can be made
     * @param babyArray The array that will be used to store the babies made
     * @return Amount of energy left after making babies
     */
    private double makeBaby(double leftoverEnergy, Organisme organisme, List<Organisme> babyArray){
        int rollTimes = (int)leftoverEnergy;
        List<Boolean> makeBaby = new ArrayList<>();
        while (rollTimes > 0) {
            double chance = generateRandom();

            // If the organisme is mature
            if (organisme.getFertilite() >= chance){

                // If we don't have enough energy to make a baby directly
                if (leftoverEnergy - organisme.getEnergieEnfant() < 0){

                    // The amount of energy we need to take from the parent
                    double needMoreEnergy = organisme.getEnergieEnfant() - leftoverEnergy;

                    // If the parent has enough energy to be taken away to make a baby
                    if (needMoreEnergy <= organisme.getEnergie()){
                        makeBaby.add(true);
                        organisme.removeEnergy(needMoreEnergy);
                        leftoverEnergy = 0;
                        break;
                    }

                // If we have enough energy to make a baby
                }else{
                    makeBaby.add(true);

                    // We have less leftOverEnergy, and the times left to roll will be decreased accordingly based
                    // on the energy needed to make a baby
                    leftoverEnergy -= organisme.getEnergieEnfant();
                    rollTimes -= (int)organisme.getEnergieEnfant();
                }
            }else{
                rollTimes--;
            }
        }

        for (int i=0;i<=makeBaby.size();i++){
            if (organisme instanceof Plante){
                Plante planteParent = (Plante)organisme;
                babyArray.add(planteParent.createPlanteBaby());
            }
            else if (organisme instanceof Herbivore){
                Herbivore herbivoreParent = (Herbivore) organisme;
                babyArray.add(herbivoreParent.createHerbivoreBaby());
            }
            else{
                Carnivore carnivoreParent = (Carnivore) organisme;
                babyArray.add(carnivoreParent.createCarnivoreBaby());
            }
        }
        return leftoverEnergy;
    }

    /**
     * Verify if an organism still have energies. If the energy is 0 or negative, remove the organism
     * as it is dead.
     */
    private void verifyOrganismState(){
        for(Iterator<Plante> iterator = plantes.iterator();iterator.hasNext();){
            Plante plante = iterator.next();
            if (plante.getEnergie() <= 0){
                iterator.remove();
            }
        }

        for( Iterator<Herbivore> iterator = herbivores.iterator();iterator.hasNext();){
            Herbivore herbivore = iterator.next();
            if (herbivore.getEnergie() <= 0){
                iterator.remove();
            }
        }

        for( Iterator<Carnivore> iterator = carnivores.iterator();iterator.hasNext();){
            Carnivore carnivore = iterator.next();
            if (carnivore.getEnergie() <= 0){
                iterator.remove();
            }
        }
    }

    /**
     * Increase the age of all organisme by 1
     */
    private void increaseAgeForAll(){
        var allOrganisme = new ArrayList<Organisme>(plantes);
        allOrganisme.addAll(herbivores);
        allOrganisme.addAll(carnivores);

        for (Organisme organisme:allOrganisme){
            organisme.increaseAge();
        }
    }


    /**
     * Generate a random number, double
     * @return  A random number, double. [0,1)
     */
    private double generateRandom(){

        return Math.random() ;
    }

    /**
     * Generate a random double number [min,max)
     * @param max upper bound of the random number
     * @param min lower bound of the random number
     * @return A random double number [min,max)
     */
    private double generateRandom(double max,double min){
        Random random = new Random();
        return min + (max-min) * random.nextDouble();
    }

    /**
     * Generate a random integer [min,max)
     * @param max upper bound of the random number
     * @param min lower bound of the random number
     * @return A random integer [min,max)
     */
    private int generateRandomInt(int max,int min){
        Random random = new Random();
        return random.nextInt(max-min)+min;
    }

}
