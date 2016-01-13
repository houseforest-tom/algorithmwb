package de.tuhh.swp;

import java.util.ArrayList;

/**
 * Created by Tom on 13.01.2016.
 */
public class AlgorithmResult {

    private int attempts;
    private int correct;
    private LearningData learnset;
    private ArrayList<AlgorithmFailure> failures;

    public AlgorithmResult(int attempts, LearningData learnset) {
        this.attempts = attempts;
        this.correct = 0;
        this.learnset = learnset;
        this.failures = new ArrayList<>();
    }

    public void setCorrectAttemptCount(int correct) {
        this.correct = correct;
    }

    public void addFailure(AlgorithmFailure failure) {
        failures.add(failure);
    }

    public int getAttemptCount() {
        return attempts;
    }

    public int getCorrectAttempts() {
        return correct;
    }

    public int getIncorrectAttempts() {
        return attempts - correct;
    }

    public LearningData getLearnset() {
        return learnset;
    }

    public ArrayList<AlgorithmFailure> getFailures() {
        return failures;
    }
}
