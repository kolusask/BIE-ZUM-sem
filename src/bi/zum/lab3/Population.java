package bi.zum.lab3;

import cz.cvut.fit.zum.api.ga.AbstractEvolution;
import cz.cvut.fit.zum.api.ga.AbstractIndividual;
import cz.cvut.fit.zum.api.ga.AbstractPopulation;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Your name
 */
public class Population extends AbstractPopulation {

    public Population(AbstractEvolution evolution, int size) {
        individuals = new Individual[size];
        for (int i = 0; i < individuals.length; i++) {
            individuals[i] = new Individual(evolution, true);
            individuals[i].computeFitness();
        }
    }

    /**
     * Method to select individuals from population
     *
     * @param count The number of individuals to be selected
     * @return List of selected individuals
     */
    public List<AbstractIndividual> selectIndividuals(int count) {
        ArrayList<AbstractIndividual> selected = new ArrayList<AbstractIndividual>();

        List<Double> fitnesses = new ArrayList<Double>(individuals.length);
        
        double fitnessSum = 0;
        for (int i = 0; i < individuals.length; i++) {
            fitnesses.set(i, individuals[i].getFitness());
            fitnessSum += individuals[i].getFitness();
        }
       
        Random r = new Random();
        for (int i = 0; i < count; i++) {
            Integer ind = 0;
            while (selected.contains(individuals[ind])) {
                double selection = r.nextDouble();
                while (selection > fitnessSum)
                    selection = r.nextDouble();
                double sum = 0;
                for (int j = 0; j < fitnesses.size(); j++) {
                    sum += fitnesses.get(j);
                    if (sum >= selection) {
                        ind = j;
                        break;
                    }
                }    
            }
            selected.add(individuals[ind]);
        }
        
        return selected;
    }
}
