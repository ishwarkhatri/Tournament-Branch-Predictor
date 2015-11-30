package edu.binghamton.my.constants;

public enum BRANCH_PREDICTION {

	BRANCH_TAKEN("t"),
	BRANCH_NOT_TAKEN("n"),
	LOCAL("l"),
	GLOBAL("g");

	private String value;
	private BRANCH_PREDICTION(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static BRANCH_PREDICTION getType(String value) {
		switch (value) {
		case "t":
			return BRANCH_TAKEN;
		case "n":
			return BRANCH_NOT_TAKEN;
		case "g":
			return GLOBAL;
		case "l":
			return LOCAL;
		default:
			break;
		}
		return null;
	}
}
