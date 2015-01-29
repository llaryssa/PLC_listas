import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Q1 {
	long N;
	long X1;
	boolean primo;
	long threadsNotFinished = 0;
	long nextNum;

	Object primoSync = new Object();
	Object finishedSync = new Object();
	Object counterSync = new Object();
	Lock primoLock = new ReentrantLock(true);
	Lock finishedLock = new ReentrantLock(true);;
	Lock counterLock = new ReentrantLock(true);
	Lock turnLock = new ReentrantLock();
	Random rand = new Random();
	int sleepTime = 1;

	public static void main(String[] args) {
		new Q1().go();

	}

	
	
	public void go() {

		Arquivo arq = new Arquivo("Q1.in", "trash.out");
		N = arq.readLong();
		//X1 = arq.readInt();
		System.out.println("N: " + N + ", X: " + Runtime.getRuntime().availableProcessors());
		serial();
		staticSync(Runtime.getRuntime().availableProcessors());
		dinamicSync(Runtime.getRuntime().availableProcessors());
		staticLocs(Runtime.getRuntime().availableProcessors());
		dinamicLocks(Runtime.getRuntime().availableProcessors());
		while(!arq.isEndOfFile()){
			X1 =arq.readLong(); 		
			System.out.println("X: " + X1);	
			serial();
			staticSync(X1);
			//dinamicSync(X1);
			staticLocs(X1);
			dinamicLocks(X1);
		}

	}

	public void print(String methodName, long time,long threads ,  boolean primo){
		System.out.println(time+" "+methodName+": "+threads+" Primo: "+primo);
	}
	public void serial() {
		while (!turnLock.tryLock()) {
			try {
				Thread.sleep(0, sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long start = System.nanoTime();
		primo = true;
		for (int i = 2; primo && i < N; i++) {
			if (N % i == 0) {
				primo = false;
			}
		}
		System.out.println((System.nanoTime() - start) + " Serial: Primo: "
				+ primo);
		turnLock.unlock();
	}

	public void staticSync(long X) {
		while (!turnLock.tryLock()) {
			try {
				Thread.sleep(0, sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long start = System.nanoTime();
		primo = true;
		long K = (N - 2) / X;
		threadsNotFinished = X;
		for (long x = 0; x < X; x++) {
			final long in = 2 + x * K;
			long aux = 2 + (x + 1) * K - 1;
			if (aux == N - 2)
				aux++;
			final long fm = aux;
			(new Thread("thread " + x) {
				@Override
				public void run() {
					for (long i = in; primo && i <= fm; i++) {

						// System.out.println(getName() + " testando " + i);
						if (N % i == 0) {
							synchronized (primoSync) {
								primo = false;
							}
						}
					}
					synchronized (finishedSync) {
						// System.out.println(threadsNotFinished);
						if (threadsNotFinished != 0) {
							threadsNotFinished--;

						}

					}
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
		
		long time = System.nanoTime()-start;
		print("StaticSync",time, X,primo);
		turnLock.unlock();

	}

	public void dinamicSync(long X) {
		while (!turnLock.tryLock()) {
			try {
				Thread.sleep(0, sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long start = System.nanoTime();
		primo = true;
		threadsNotFinished = X;
		nextNum = 2;
		for (long x = 0; x < X; x++) {
			(new Thread() {
				long i = 2;

				@Override
				public void run() {
					while (true) {
						synchronized (counterSync) {
							if (nextNum < N) {
								i = nextNum;
								nextNum++;
							} else {
								break;
							}
						}
						// System.out.println("testando " + i);
						if (N % i == 0) {
							synchronized (primoSync) {
								primo = false;
								break;
							}
						}
					}
					synchronized (finishedSync) {
						if (threadsNotFinished != 0) {
							threadsNotFinished--;

						}
					}
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
		long time = System.nanoTime()-start;
		print("DynamicSync",time, X,primo);
		turnLock.unlock();

	}

	public void staticLocs(long X) {
		while (!turnLock.tryLock()) {
			try {
				Thread.sleep(0, sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long start = System.nanoTime();
		primo = true;
		long K = (N - 2) / X;
		threadsNotFinished = X;
		for (long x = 0; x < X; x++) {
			final long in = 2 + x * K;
			long aux = 2 + (x + 1) * K - 1;
			if (aux == N - 2)
				aux++;
			final long fm = aux;
			(new Thread("thread " + x) {
				@Override
				public void run() {
					for (long i = in; primo && i <= fm; i++) {

						// System.out.println(getName() + " testando " + i);
						if (N % i == 0) {
							if (N % i == 0) {//nao entendi pq i consegue ficar igual a N
								while (!primoLock.tryLock()) {
									try {
										sleep(0, rand.nextInt(sleepTime));
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								primo = false;
								System.out.println(i);
								primoLock.unlock();
							}
						}
					}
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
		long time = System.nanoTime()-start;
		print("StaticLocs",time, X,primo);
		turnLock.unlock();

	}
	
	public void dinamicLocks(long X) {
		while (!turnLock.tryLock()) {
			try {
				Thread.sleep(0, sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long start = System.nanoTime();
		primo = true;
		threadsNotFinished = X;
		nextNum = 2;
		sleepTime = (int) X;
		for (long x = 0; x < X; x++) {
			(new Thread("Therad " + x) {
				long i = nextNum;

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
						i = nextNum;
						nextNum++;
						counterLock.unlock();
						if (i<N&N % i == 0) {//nao entendi pq i consegue ficar igual a N
							while (!primoLock.tryLock()) {
								try {
									sleep(0, rand.nextInt(sleepTime));
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							primo = false;
							System.out.println(i);
							primoLock.unlock();
						}
					} while (primo && nextNum < N);
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
		long time = System.nanoTime()-start;
		print("DynamicLocs",time, X,primo);
		turnLock.unlock();
	}
}
