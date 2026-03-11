class SequenceThread extends Thread {
    private final int threadId;
    private final int step;
    private volatile boolean canStop = false;

    public SequenceThread(int id, int step) {
        this.threadId = id;
        this.step = step;
    }

    public void stopThread() {
        canStop = true;
    }

    @Override
    public void run() {
        long sum = 0;
        long count = 0;
        long current = 0;

        // Потік працює, поки керуючий потік не змінить canStop
        while (!canStop) {
            sum += current;
            current += step;
            count++;
        }
        System.out.println("Потік " + threadId + ": Сума = " + sum + ", Кількість доданків = " + count);
    }
}

public class Main {
    static void main() {
        int numThreads = 4; // Кількість потоків можна змінювати
        SequenceThread[] threads = new SequenceThread[numThreads];

        // Запуск потоків
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new SequenceThread(i + 1, i + 1); // Крок для прикладу дорівнює номеру потоку
            threads[i].start();
        }

        // Керуючий потік чекає заданий проміжок часу (наприклад, 1 секунда)
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.getLogger(Main.class.getName()).log(System.Logger.Level.ERROR, "Сон головного потоку перервано", e);
        }

        // Генерування дозволу на завершення роботи потоків
        for (int i = 0; i < numThreads; i++) {
            threads[i].stopThread();
        }
    }
}