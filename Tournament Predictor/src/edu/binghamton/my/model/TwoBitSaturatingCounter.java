package edu.binghamton.my.model;

public class TwoBitSaturatingCounter {

	private int counter;

	public void incrementCounter(){
		if(this.counter < 3) {
			this.counter++;
		}
	}

	public void decrementCounter(){
		if(this.counter > 0) {
			this.counter--;
		}
	}

	public int getCounterValue() {
		return this.counter;
	}
}
