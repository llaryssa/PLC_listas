import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Q3 {

	Guerreiro[] guerreiros;
	Enemy[] enemys;
	Lock enemyLifeLock = new ReentrantLock();
	int fontEnergy = 100;
	int[] turns;
	final int TURN_GUERREIRO = 0;
	final int TURN_ENEMY = 1;
	Random rand;
	final int minFontEnergy = 100;
	final int EnergyForWizard = 1000000;
	final int numGuerreiros = 10;
	final int minGuerreiroLife = 100;
	final int GuerreiroLifeRange = 2;
	final int minGuerreiroPower = 5;
	final int GuerreiroPowerRange = 6;
	final int minEnemyLife = 500;
	final int enemyLifeRange = 1200;
	int minEnemyPower = fontEnergy * minGuerreiroPower;
	int enemyPowerRange = fontEnergy * GuerreiroPowerRange;
	final int minMagoLife = 500;
	final int MagoLifeRange = 300;
	final int minMagoPower = 100;
	final int MagoPowerRange = 50;
	PriorityQueue<Enemy> availableEnemys;
	final int initAvailableEnemies = // numGuerreiros*
	(2 * numGuerreiros);
	Lock[] warriorEnemyLocks;
	Object fontLock = new Object();
	int recoveryTime = 100;
	Object giveMeAnEnemyLock = new Object();

	public static void main(String[] args) {
		new Q3().go();
	}

	public void go() {
		fontEnergy = 100;
		enemys = new Enemy[numGuerreiros];
		turns = new int[numGuerreiros];
		rand = new Random();
		availableEnemys = new PriorityQueue<Enemy>();
		deliveryEnemies();
		warriorEnemyLocks = new ReentrantLock[numGuerreiros];
		guerreiros = new Guerreiro[numGuerreiros];
		for (int i = 0; i < numGuerreiros; i++) {
			turns[i] = TURN_GUERREIRO;
			warriorEnemyLocks[i] = new ReentrantLock(true);
			guerreiros[i] = new Guerreiro(i);
			guerreiros[i].start();
		}
		

	}

	public void deliveryEnemies() {// ok
		minEnemyPower = Math.max(0,fontEnergy * minGuerreiroPower);
		enemyPowerRange = fontEnergy * GuerreiroPowerRange;
		for (int i = 0; i < initAvailableEnemies; i++) {
			Enemy e = new Enemy();
			availableEnemys.add(e);
		}
	}

	class Guerreiro extends Thread {
		int maxLife = minGuerreiroLife + rand.nextInt(GuerreiroLifeRange);
		int power = minGuerreiroPower + rand.nextInt(GuerreiroPowerRange);
		int life;
		int index;
		boolean alive;

		public Guerreiro(int indice) {
			this.index = indice;
			life = maxLife;
			alive = true;
			setName("Guerreiro " + indice);
		}

		public void giveMeAnEnemy(int index) {
			synchronized (giveMeAnEnemyLock) {

				Enemy e = enemys[index];
				if (e == null || (!e.alive)) {
					e = availableEnemys.poll();
					if (e == null) {
						deliveryEnemies();
					}
					e = availableEnemys.poll();
					e.start();
				}
				e.set(index);
				enemys[index] = e;
			}
			/*
			 * if (e == null) { e = availableEnemys.poll(); if (e == null) {
			 * deliveryEnemies(); } e = availableEnemys.poll(); } else if
			 * (!e.alive) { e = availableEnemys.poll(); if (e == null) {
			 * deliveryEnemies(); } e = availableEnemys.poll(); }
			 */

		}

		public void goForIt() {

		}

		@Override
		public void run() {
			while (alive & fontEnergy < EnergyForWizard) {
				// System.out.println(turns[index]);

				while (!warriorEnemyLocks[index].tryLock()) {
					try {
						Thread.sleep(0, rand.nextInt(10));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (turns[index] == TURN_GUERREIRO) {// Se for minha vez
					System.out.println("Vez de " + getName());
					giveMeAnEnemy(index);
					int energy;
					synchronized (fontLock) {
						energy = (power * fontEnergy);
					}
					if (energy > 0) {
						synchronized (fontLock) {
							fontEnergy += energy;
						}
					} else {
						turns[index] = TURN_ENEMY;

					}
				}
				warriorEnemyLocks[index].unlock();

			}
			if (alive) {

			}
		}

		public int atack(int atack) {
			life -= atack;
			if (life <= 0) {
				alive = false;
				System.out.println(getName() + " morreu");
			}
			return 0;
		}
	}

	class Enemy extends Thread implements Comparable<Enemy> {
		double life;
		int power;
		boolean alive;
		int energy;
		int index;

		@Override
		public void run() {
			System.out
					.println(getName() + " vs " + guerreiros[index].getName());
			while (alive & guerreiros[index].alive) {
				while (!warriorEnemyLocks[index].tryLock()) {
					try {
						Thread.sleep(0, rand.nextInt(10));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("Enemy");
				if (turns[index] == TURN_ENEMY) {// Se for minha vez
					System.out.println("Vez de " + getName());
					guerreiros[index].atack(power);
					turns[index] = TURN_GUERREIRO;
				}
				warriorEnemyLocks[index].unlock();
			}
			if (!alive) {
				//System.out.println(getName() + " morreu");
				return;
			} else {
				availableEnemys.add(this);
				// enemys[index] = null;
			}

		}

		public Enemy() {
			try {
				life = minEnemyLife + rand.nextInt(Math.max(1, enemyLifeRange));
				power = minEnemyPower
						+  rand.nextInt(Math.max(1,enemyPowerRange));
				alive = false;
				energy = (int) (life * rand.nextDouble());
			} catch (Exception e) {
			e.printStackTrace();
				System.out.println(enemyPowerRange + " enemy power range");
			}
		}

		public int atack(int atack) {
			System.out.println(getName() + " atacked");
			life -= atack;
			if (life <= 0) {
				alive = false;
				return energy;
			}
			return 0;
		}

		public void set(int index) {
			this.index = index;
			setName("Enemy " + index);
		}

		@Override
		public int compareTo(Enemy o) {// ok
			return Integer.valueOf(o.energy).compareTo(energy);
		}
	}

	class Mago extends Thread {

		@Override
		public void run() {

		}
	}
}
