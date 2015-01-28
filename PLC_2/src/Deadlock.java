import java.util.concurrent.locks.ReentrantLock;

public class Deadlock {
	ReentrantLock lock1 = new ReentrantLock();
	ReentrantLock lock2 = new ReentrantLock();
	long numPositivo = 3500000000l;
	long numNegativo = -3500000000l;

	public Deadlock() {
		Thread t1 = new Thread() {
			public void run() {
				executar(lock1, lock2);
			}
		};
		Thread t2 = new Thread() {
			public void run() {
				executar(lock2, lock1);
			}
		};
		try {
			t1.start();
			t2.start();
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
		}
	}

	public void executar(ReentrantLock fst, ReentrantLock snd) {
		for (int i = 0; i < 50; i++) {
			fst.lock();
			try {
				for (int w = 0; w < 35000000; w++) {
					if (fst == lock1)
						numPositivo--;
					else {
						numNegativo++;
					}
				}
				snd.lock();
				try {
					for (int j = 0; j < 35000000; j++) {
						if (fst == lock1)
							numNegativo++;
						else {
							numPositivo--;
						}
					}
				} finally {
					snd.unlock();
				}
			} finally {
				fst.unlock();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public static void main(String[] args) {
		Deadlock dead = new Deadlock();
		System.out.println("Número positivo se tornou : " + dead.numPositivo);
		System.out.println("Número negativo se tornou : " + dead.numNegativo);
	}
}