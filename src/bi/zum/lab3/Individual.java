package bi.zum.lab3;

import cz.cvut.fit.zum.api.Node;
import cz.cvut.fit.zum.api.ga.AbstractEvolution;
import cz.cvut.fit.zum.api.ga.AbstractIndividual;
import cz.cvut.fit.zum.data.Edge;
import cz.cvut.fit.zum.data.StateSpace;
import cz.cvut.fit.zum.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * @author Your name
 */
public class Individual extends AbstractIndividual {

    private double fitness = Double.NaN;
    private AbstractEvolution evolution;
    
    List<Boolean> genome = new ArrayList<Boolean>(StateSpace.nodesCount());
    
    /**
     * Creates a new individual
     * 
     * @param evolution The evolution object
     * @param randomInit <code>true</code> if the individual should be
     * initialized randomly (we do wish to initialize if we copy the individual)
     */
    public Individual(AbstractEvolution evolution, boolean randomInit) {
        this.evolution = evolution;
        
        if(randomInit) {
            
            Random r = new Random();
            for (int i = 0; i < StateSpace.nodesCount(); i++)
                genome.add(r.nextBoolean());
            
        }
    }

    @Override
    public boolean isNodeSelected(int j) {
        
        return genome.get(j);
    
    }

    /**
     * Evaluate the value of the fitness function for the individual. After
     * the fitness is computed, the <code>getFitness</code> may be called
     * repeatedly, saving computation time.
     */
    @Override
    public void computeFitness() {
        
        repair();
        Double count = 0.0;
        for (int i = 0; i < StateSpace.nodesCount(); i++)
            if (genome.get(i))
                count++;
        
        this.fitness = StateSpace.nodesCount() / count;
    }

    /**
     * Only return the computed fitness value
     *
     * @return value of fitness function
     */
    @Override
    public double getFitness() {
        return this.fitness;
    }

    /**
     * Does random changes in the individual's genotype, taking mutation
     * probability into account.
     * 
     * @param mutationRate Probability of a bit being inverted, i.e. a node
     * being added to/removed from the vertex cover.
     */
    @Override
    public void mutate(double mutationRate) {
        
        Random r = new Random();
        for (int i = 0; i < StateSpace.nodesCount(); i++)
            if (r.nextDouble() <= mutationRate)
                genome.set(i, !genome.get(i));
        
    }
    
    /**
     * Crosses the current individual over with other individual given as a
     * parameter, yielding a pair of offsprings.
     * 
     * @param other The other individual to be crossed over with
     * @return A couple of offspring individuals
     */
    @Override
    public Pair crossover(AbstractIndividual other) {

        Pair<Individual,Individual> result = new Pair();

        List<Boolean> g1 = new ArrayList<Boolean>(StateSpace.nodesCount());
        List<Boolean> g2 = new ArrayList<Boolean>(StateSpace.nodesCount());
        
        Random r = new Random();
        int divPoint = r.nextInt(StateSpace.nodesCount());
        
        for (int i = 0; i < divPoint; i++) {
            g1.set(i, genome.get(i));
            g2.set(i, other.isNodeSelected(i));
        }
        
        for (int i = divPoint; i < StateSpace.nodesCount(); i++) {
            g1.set(i, other.isNodeSelected(i));
            g2.set(i, genome.get(i));
        }
        
        result.a = new Individual(evolution, false);
        result.b = new Individual(evolution, false);
        
        result.a.genome = g1;
        result.b.genome = g2;
        
        return result;
    }

    /**
    * Repairs the genotype to make it valid, i.e. ensures all the edges
    * are in the vertex cover.
    */
    private void repair() {

        /* We iterate over all the edges */
        for(Edge e : StateSpace.getEdges()) {
            if (!genome.get(e.getFromId()) && !genome.get(e.getToId())) {
                Random r = new Random();
                genome.set(r.nextBoolean() ? e.getFromId() : e.getToId(), Boolean.TRUE);
            }
        }
    }
    
    /**
     * When you are changing an individual (eg. at crossover) you probably don't
     * want to affect the old one (you don't want to destruct it). So you have
     * to implement "deep copy" of this object.
     *
     * @return identical individual
     */
    @Override
    public Individual deepCopy() {
        Individual newOne = new Individual(evolution, false);
        
        newOne.fitness = this.fitness;
        for (int i = 0; i < StateSpace.nodesCount(); i++)
            newOne.genome.add(this.genome.get(i));

        newOne.fitness = this.fitness;
        return newOne;
    }

    /**
     * Return a string representation of the individual.
     *
     * @return The string representing this object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        boolean empty = true;
        for (int i = 0; i < genome.size(); i++) {
            if (genome.get(i)) {
                if (!empty)
                    sb.append(",");
                sb.append(Integer.toString(i));
                empty = false;
            }
        }

        return sb.toString();
    }
}
