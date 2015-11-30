package edu.binghamton.my.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.binghamton.my.constants.BRANCH_PREDICTION;
import edu.binghamton.my.constants.Constants;
import edu.binghamton.my.model.GlobalPredictor;
import edu.binghamton.my.model.LocalPredictor;
import edu.binghamton.my.model.Selector;

public class TournamentPredictor {

	private static Map<String, LocalPredictor> localPredictorMap = new HashMap<>();
	private static Map<String, GlobalPredictor> globalPredictorMap = new HashMap<>();
	private static Map<String, Selector> selectorMap = new HashMap<>();
	private static List<String> outputDataList = new LinkedList<>();
	private static int correctLocalPredictions;
	private static int correctGlobalPredictions;
	private static int correctTournamentPredictions;

	public static void main(String[] args) {
		if(args.length < 2) {
			echoError("Invalid command line arguments!");
			System.exit(1);
		}

		echo("\n**** Tournament Branch Predictor ****\n");
		String inputFileName = args[0];
		String outputFileName = args[1];

		File inputFile = new File(inputFileName);
		File outputFile = new File(outputFileName);

		Scanner scan = null;
		LocalPredictor localPredictor = null;
		GlobalPredictor globalPredictor =null;
		Selector selectorPredictor = null;
		BRANCH_PREDICTION localPrediction;
		BRANCH_PREDICTION globalPrediction;
		BRANCH_PREDICTION selectorPrediction;
		String globalIndex = Constants.INITIAL_GLOBAL_INDEX;
		String finalPrediction;
		BufferedWriter writer = null;
		try {
			scan = new Scanner(inputFile);
			writer = new BufferedWriter(new FileWriter(outputFile));

			echo("Reading input file: " + inputFileName);
			echo("\nStarting prediction analysis...");
			int lineCount = 1;
			while(scan.hasNextLine()) {
				String input = scan.nextLine();
				if(input == null || "".equals(input.trim()) || input.length() < 3) {
					continue;
				}

				String branchPC = input.substring(0, 1);
				String branchResolution = input.substring(1, 2);
				//String targetAddress = input.substring(2);
	
				//Local Prediction
				localPredictor = localPredictorMap.get(branchPC);
				if(localPredictor == null) {
					localPredictor = new LocalPredictor(branchPC);
					localPredictorMap.put(branchPC, localPredictor);
				}
	
				localPrediction = localPredictor.getLocalPrediction();
				localPredictor.updateCounter(branchResolution); 
				if(localPrediction.getValue().equalsIgnoreCase(branchResolution)) {
					correctLocalPredictions++;
				}
	
				//Global Prediction
				globalPredictor = globalPredictorMap.get(globalIndex);
				if(globalPredictor == null) {
					globalPredictor = new GlobalPredictor(globalIndex);
					globalPredictorMap.put(globalIndex, globalPredictor);
				}
	
				globalPrediction = globalPredictor.getGlobalPrediction();
				globalPredictor.updateCounter(branchResolution);
				if(globalPrediction.getValue().equalsIgnoreCase(branchResolution)) {
					correctGlobalPredictions++;
				}
	
				//Selection Logic
				selectorPredictor = selectorMap.get(branchPC);
				if(selectorPredictor == null) {
					selectorPredictor = new Selector(branchPC);
					selectorMap.put(branchPC, selectorPredictor);
				}
	
				selectorPrediction = selectorPredictor.getSelection();
				selectorPredictor.updateSelectionCounter(localPrediction, globalPrediction, BRANCH_PREDICTION.getType(branchResolution.toLowerCase()));
	
				finalPrediction = getFinalPrediction(localPrediction, globalPrediction, selectorPrediction);
				if(finalPrediction.equalsIgnoreCase(branchResolution)) {
					correctTournamentPredictions++;
				}
	
				generateOutput(branchPC, localPrediction, globalPrediction, selectorPrediction, finalPrediction, branchResolution);
				globalIndex = updateGlobalIndex(globalIndex, branchResolution);
				lineCount++;
			}

			echo("Prediction completed");
			echo("Total lines: " + --lineCount);

			echo("\nWriting data to output file: " + outputFileName);
			writeOutputToFile(outputFile, writer);
			echo("Output written successfully");

			echo("\n---- Prediction statistics ----\n");
			displayStats();
			echo("\n-------------------------------\n");

			echo("\n**** End of program ****");

		} catch (IOException e) {
			echoError("Error in tournament predictor: " + e.getMessage());
		} finally {
			try {
				writer.close();
			} catch(Exception e) {
				echoError("Unable to close bufferred writer: " + e.getMessage());
			}
		}
	}

	private static void writeOutputToFile(File outputFile, BufferedWriter writer) {
		try {
			for(int i=0; i < outputDataList.size(); i++) {
				if(i == (outputDataList.size() - 1))
					writer.write(outputDataList.get(i));
				else
					writer.write(outputDataList.get(i) + "\n");
			}
		} catch(Exception e) {
			echoError("Error to write in outfile: " + e.getMessage());
		}
	}

	private static void displayStats() {
		echo("Correct Local Predictions:\n" + correctLocalPredictions);
		echo("Correct Global Predictions:\n" + correctGlobalPredictions);
		echo("Correction Tournament Predictions:\n" + correctTournamentPredictions);
	}

	private static void generateOutput(String branchPC, BRANCH_PREDICTION localPrediction,
			BRANCH_PREDICTION globalPrediction, BRANCH_PREDICTION selectorPrediction, String finalPrediction,
			String branchResolution) {
		String data = branchPC + localPrediction.getValue() + globalPrediction.getValue() + selectorPrediction.getValue() + finalPrediction + branchResolution;
		outputDataList.add(data);
	}

	private static String getFinalPrediction(BRANCH_PREDICTION localPrediction, BRANCH_PREDICTION globalPrediction,
			BRANCH_PREDICTION selectorPrediction) {
		if(Constants.LOCAL.equalsIgnoreCase(selectorPrediction.getValue())) {
			return localPrediction.getValue();
		} else {
			return globalPrediction.getValue();
		}
	}

	private static String updateGlobalIndex(String globalIndex, String branchResolution) {
		return globalIndex = globalIndex.substring(1).toLowerCase() + branchResolution.toLowerCase();
	}

	private static void echoError(String data) {
		System.err.println(data);
	}
	private static void echo(String data) {
		System.out.println(data);
	}
}
