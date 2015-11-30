package edu.binghamton.my.model;

import edu.binghamton.my.constants.BRANCH_PREDICTION;

public class Selector {

	private String index;

	private TwoBitSaturatingCounter counter;

	public Selector(String index){
		this.index = index;
		this.counter = new TwoBitSaturatingCounter();
	}

	public BRANCH_PREDICTION getSelection() {
		int counterValue = counter.getCounterValue();
		if(counterValue > 1) {
			return BRANCH_PREDICTION.GLOBAL;
		}

		return BRANCH_PREDICTION.LOCAL;
	}

	public void updateSelectionCounter(BRANCH_PREDICTION local, BRANCH_PREDICTION global, BRANCH_PREDICTION actual) {
		if(local == global) {
			//do nothing
		} else if (global == actual) {
			this.counter.incrementCounter();
		} else {
			this.counter.decrementCounter();
		}
	}

	public String getIndex(){
		return this.index;
	}
}
