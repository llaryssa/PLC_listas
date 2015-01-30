package tests;

import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SulmanQ2 implements Runnable {
	Lock trava = new ReentrantLock();
	static int n, posicaoI = 0, posicaoJ = 0;
	static int[][] matriz1, matriz2, resultado;

	public static void main(String args[]) {

		Scanner in = new Scanner(System.in);
		System.out.println("Digite o tamanho da matriz");
		n = in.nextInt();
		matriz1 = new int[n][n];
		matriz2 = new int[n][n];
		resultado = new int[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.println("Digite o elemento " + i + "x" + j
						+ " da matriz 1");
				matriz1[i][j] = in.nextInt();
			}
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.println("Digite o elemento" + i + "x" + j
						+ " da matriz 2");
				matriz2[i][j] = in.nextInt();
			}
		}
		for (int i = 1; i <= n; i++) {

			Thread[] threads = new Thread[i];

			for (int j = 0; j < i; j++) {
				threads[j] = new Thread(new SulmanQ2());
			}
			for (int j = 0; j < i; j++) {
				threads[j].start();
			}
		}
	}

	public void run() {
		int soma = 0, i1, j1 = 0, i2 = 0, j2;
		int posicaoAtualI, posicaoAtualJ;
		trava.lock();
		System.out.println();
		posicaoAtualI = posicaoI++;
		posicaoAtualJ = posicaoJ++;
		i1 = posicaoAtualI;
		j2 = posicaoAtualI;
		
		

		trava.unlock();

		for (int i = 0; i < n; i++) {
			soma = matriz1[i1][j1] * matriz2[i2][j2];
			j1++;
			i2++;
			System.out.print("i1: " + i1);
			System.out.print(" j1: " + j1);
			System.out.print(" i2: " + i2);
			System.out.println(" i2: " + i2);
		}
		resultado[posicaoAtualI][posicaoAtualJ] = soma;
	}
}
