import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Q2 {

	long[][] m1, m2;
	int n;
	Lock turn = new ReentrantLock();
	int nextLine;
	int sleepTime = 2;
	Random rand = new Random();
	int threadsNotFinished;
	Lock counterLock = new ReentrantLock(true);
	Lock scoreLock = new ReentrantLock();
	Lock finishedLock = new ReentrantLock(true);
	long bestThreads = 1;
	long bestTime = Long.MAX_VALUE;
	int finishedTurns;

	public static void main(String[] args) {
		new Q2().go();
	}

	public void go() {
		Arquivo arq = new Arquivo("Q2.in", "trash.out");
		n = arq.readInt();
		m1 = new long[n][n];
		m2 = new long[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				m1[i][j] = arq.readLong();
			}
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				m2[i][j] = arq.readLong();
			}
		}
finishedTurns=0;
		
		for (int i = 1; i <= n; i++) {
			mult(i);
		}
		while(finishedTurns!=n){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Best Time: "+ bestTime +": "+bestThreads+" Threads" );

	}

	private void mult(int X) {
		while (!turn.tryLock()) {

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long start = System.nanoTime();
		long[][] resp = new long[n][n];
		threadsNotFinished = X;
		nextLine = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				resp[i][j] = 0;
			}
		}
		for (long x = 0; x < X; x++) {
			(new Thread("Thread " + x) {
				int i = nextLine;

				@Override
				public void run() {
					do {
						while (!counterLock.tryLock()) {
							try {
								sleep(0, rand.nextInt(sleepTime));
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						i = nextLine;
						nextLine++;
						counterLock.unlock();

						for (int j = 0; j < n && i < n; j++) {
							resp[i][j] += m1[i][j] * m2[j][i];

						}

					} while (nextLine < n);
					while (!finishedLock.tryLock()) {
						try {
							sleep(0, rand.nextInt(sleepTime));
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (threadsNotFinished != 0) {
						threadsNotFinished--;

					}
					finishedLock.unlock();

				}

			}).start();

		}
		while (threadsNotFinished != 0) {
			try {
				Thread.sleep(0, sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long time = System.nanoTime() - start;
		System.out.println(X + " Threads: Time: " + time);
		// printMatrix(resp);
		if (time < bestTime) {
			bestTime = time;
			bestThreads = X;
		}
		finishedTurns++;
		turn.unlock();
	}

	public void printMatrix(long[][] m) {
		for (int i = 0; i < m[0].length; i++) {
			for (int j = 0; j < m.length; j++) {
				System.out.print(m[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

}
