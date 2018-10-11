package Model.Unitats.Unit_Training;


/**
 * Contiene toda la informacion necesaria para poder entrenar qualquier objeto
 *
 * @param <T> tipo del objeto a entrenar
 */

public class TrainTask<T extends Trainable> {
    private long trainingTime, lastTrain;
    private T nextEntityToTrain;

    public TrainTask(T nextEntityToTrain) {
        this.nextEntityToTrain = nextEntityToTrain;
        this.trainingTime = nextEntityToTrain.getTrainingTime();
        lastTrain = System.currentTimeMillis();
    }

    public long getTrainingTime() {
        return trainingTime;
    }

    public T getTrainResult() {
        return nextEntityToTrain;
    }

    public long getLastTrain() {
        return lastTrain;
    }

    public void initTrain() {
        lastTrain = System.currentTimeMillis();
    }
}