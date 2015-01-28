public class C {
	public static void main(String args[]) {
		Runtime rt = Runtime.getRuntime();// descobre o numero de processadores
		System.out.println(rt.availableProcessors());
		System.out.println(rt.freeMemory());
		int k = 8;
		HelloThread[] arrayThread = new HelloThread[k];

		for (int i = 0; i < k; i++) {
			arrayThread[i] = new HelloThread(i);
			
		}
		for (int i = 0; i < k; i++) {
			arrayThread[i].start();
			
		}
	}
}

class HelloThread extends Thread {
	private int n;

	public HelloThread(int n){
		this.n = n;
	}

	public void run() {
		int i = 400;
		while (i > 0) {
			i--;
			System.out.println(n);
		}

	}
}