package edu.binghamton.my.model;

import edu.binghamton.my.constants.BRANCH_PREDICTION;
import edu.binghamton.my.constants.Constants;

public class LocalPredictor {

	private String index;

	private TwoBitSaturatingCounter counter ;

	public LocalPredictor(String index){
		this.index = index;
		this.counter = new TwoBitSaturatingCounter();
	}

	public BRANCH_PREDICTION getLocalPrediction() {
		int counterValue = counter.getCounterValue();
		if(counterValue > 1) {
			return BRANCH_PREDICTION.BRANCH_TAKEN;
		}

		return BRANCH_PREDICTION.BRANCH_NOT_TAKEN;
	}

	private void updateBranchTaken() {
		this.counter.incrementCounter();
	}

	private void updateBranchNotTaken() {
		this.counter.decrementCounter();
	}

	public String getIndex(){
		return this.index;
	}

	public void updateCounter(String branchResolution) {
		if(Constants.TAKEN.equalsIgnoreCase(branchResolution)) {
			this.updateBranchTaken();
		} else {
			this.updateBranchNotTaken();
		}
	}

	
}
