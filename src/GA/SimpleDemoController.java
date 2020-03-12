/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

import Include.Individual;
import Include.Population;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 *
 * @author user
 */
public class SimpleDemoController implements Initializable {
    
    @FXML public JFXSlider numberOfGenes;
    @FXML public JFXSlider numberOfIndividuals;
    @FXML public JFXButton generator;
    @FXML public JFXTextArea result;
    @FXML public Label nbrIndividuals,nbrGenes;
    
    private Population population;
    private Individual fittest,secondFittest;
    private int generationCount;

	//Selection
	void selection() {

		//Select the most fittest individual
		fittest = population.selectFittest();

		//Select the second most fittest individual
		secondFittest = population.selectSecondFittest();
	}

	//Crossover
	void crossover() {
            
		Random rn = new Random();

                int crossOverPoint = rn.nextInt((int)numberOfGenes.getValue());  // throws NPE if rn is null 

		//Swap values among parents
		for (int i = 0; i < crossOverPoint; i++) {
			int temp = fittest.getGenes()[i];
			fittest.getGenes()[i] = secondFittest.getGenes()[i];
			secondFittest.getGenes()[i] = temp;
		}

	}

	//Mutation
	void mutation() {
		Random rn = new Random();

		//Select a random mutation point
		int mutationPoint = rn.nextInt((int)numberOfGenes.getValue());

		//Flip values at the mutation point
		if (fittest.getGenes()[mutationPoint] == 0) {
			fittest.getGenes()[mutationPoint] = 1;
		} else {
			fittest.getGenes()[mutationPoint] = 0;
		}

		mutationPoint = rn.nextInt((int)numberOfGenes.getValue());

		if (secondFittest.getGenes()[mutationPoint] == 0) {
			secondFittest.getGenes()[mutationPoint] = 1;
		} else {
			secondFittest.getGenes()[mutationPoint] = 0;
		}
	}

	//Get fittest offspring
	Individual getFittestOffspring() {
		if (fittest.getFitness() > secondFittest.getFitness()) {
			return fittest;
		}
		return secondFittest;
	}


	//Replace least fittest individual from most fittest offspring
	void addFittestOffspring() {

		//Update fitness values of offspring
		fittest.calcFitness();
		secondFittest.calcFitness();

		//Get index of least fit individual
		int leastFittestIndex = population.getLeastFittestIndex();

		//Replace least fittest individual from most fittest offspring
		population.getIndividuals()[leastFittestIndex] = getFittestOffspring();
	}
    
	//show genetic state of the population pool
	static String showGeneticPool(Individual[] individuals) {
            
                String geneticPool = "";
            
		geneticPool += "\n==Genetic Pool==\n";
		int increment=0;
		for (Individual individual:individuals) {
			geneticPool += "> Individual  "+increment+" | "+ individual.genesToString()+" |\n";
			increment++;
		}
		geneticPool += "\n================\n";
                
                return geneticPool;
	}    
    
    
        /*public SimpleDemoController() {
		this.population = new Population((int)numberOfIndividuals.getValue(), (int)numberOfGenes.getValue());
		this.generationCount = 0;
    }  */  
    
        public void generateResults(){
            
        result.setText("");    
        
        population = new Population((int)numberOfIndividuals.getValue(), (int)numberOfGenes.getValue());
        generationCount = 0;
        
		result.appendText("Population of " + population.getPopSize() + " individual(s), each Individual with : " + numberOfGenes.getValue() + " gene(s).\n");

		//Calculate fitness of each individual
		population.calculateFitness();

		result.appendText("\nGeneration: " + generationCount + " Fittest: " + population.getFittestScore());
		//show genetic pool
		result.appendText(showGeneticPool(population.getIndividuals()));

		//While population gets an individual with maximum fitness
		while (population.getFittestScore() < numberOfGenes.getValue()) {
			++generationCount;

			//Do selection
			selection();

			//Do crossover
			crossover();
                        
                        
			//Do mutation under a random probability
                        Random rn = new Random();
			if (rn.nextInt()%7 < 5) {
				mutation();
			}

			//Add fittest offspring to population
			addFittestOffspring();

			//Calculate new fitness value
			population.calculateFitness();

			result.appendText("\nGeneration: " + generationCount + " Fittest score: " + population.getFittestScore());
            
			//show genetic pool
			result.appendText(showGeneticPool(population.getIndividuals()));
		}

		result.appendText("\nSolution found in generation " + generationCount);
		result.appendText("\nIndex of winner Individual: "+population.getFittestIndex());
		result.appendText("\nFitness: "+population.getFittestScore());
		result.appendText("\nGenes: ");
		for (int i = 0; i < numberOfGenes.getValue(); i++) {
			result.appendText(String.valueOf(population.selectFittest().getGenes()[i]));
		}

        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        numberOfGenes.valueProperty().addListener((obs,oldVal,newVal)->{
            numberOfGenes.setValue(newVal.intValue());
        });
        numberOfIndividuals.valueProperty().addListener((obs,oldVal,newVal)->{
            numberOfIndividuals.setValue(newVal.intValue());
        });
            
        generator.setOnAction(Action -> {
            generateResults();
        });
        
    }    
    
}
