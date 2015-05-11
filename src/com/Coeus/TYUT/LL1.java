package com.Coeus.TYUT;

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

public class LL1 {
	static String Vn[] = { "A", "B", "C" }; // 非终结符集
	static String Vt[] = { "a", "b", "c", "d", "e", "#" }; // 终结符集
	static String P[][] = new String[3][6]; // 预测分析表
	static ArrayList<String> fenxi = new ArrayList<String>();
	static String inputString = ""; // 输入的字符串
	static String action = "";// 动作
	static String top = null;// 分析栈栈顶字符
	static String topinput = null;// 余留输入串首字符

	public static void init() {
		fenxi.add("#");
		fenxi.add("A");
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 6; j++) {
				P[i][j] = "error";
			}
		}
		P[0][0] = "cB/C";
		P[1][1] = "B/C";
		P[1][3] = "ε/C";
		P[1][4] = "B/C";
		P[2][2] = "ε/C";
		System.out.println("已构建好的预测分析表:");
		System.out
				.println("----------------------------------------------------------------------");
		for (int i = 0; i < 6; i++) {
			System.out.print("          " + Vt[i]);
		}
		System.out.println();
		System.out
				.println("----------------------------------------------------------------------");
		for (int i = 0; i < 3; i++) {
			System.out.print("   " + Vn[i] + "    ");
			for (int j = 0; j < 6; j++) {
				int l = 0;
				if (j > 0) {
					l = 10 - P[i][j - 1].length();
				}
				for (int k = 0; k < l; k++) {
					System.out.print(" ");
				}
				System.out.print(P[i][j] + " ");
			}
			System.out.println();
		}
		System.out
				.println("----------------------------------------------------------------------");
	}

	public static void input() {
		System.out.println("请输入要分析的符号串并以#结束：");
		BufferedReader bfd = new BufferedReader(
				new InputStreamReader(System.in));
		try {
			inputString = bfd.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 算法主函数
	public static void judge() {
		if (fenxi.size() >= 1) {
			Iterator it = fenxi.iterator();
			while (it.hasNext()) {
				top = (String) it.next();
			}
			while (top.length() == 0) {
				fenxi.remove(fenxi.size() - 1);
				Iterator its = fenxi.iterator();
				while (its.hasNext()) {
					top = (String) its.next();
				}
			}
		}
		if (inputString.length() >= 1) {
			topinput = inputString.substring(0, 1);
		}
		// System.out.println("top=" + top + "  topinput:" + topinput);
		M(top, topinput);
		prt(fenxi, inputString, action);
		if (!(action == "succ")) {
			if (!(action == "error")) {
				String actions[] = action.split("/");
				if (actions[1].equals("C"))// 继续读下一个字符
				{
					if (actions[0].equals("ε")) {
						if (fenxi.size() > 1) {
							fenxi.remove(fenxi.size() - 1);
						}
						if (inputString.length() > 1) {
							inputString = inputString.substring(1,
									inputString.length());
						}// 余留输入串减一
						judge();
					} else {
						fenxi.remove(fenxi.size() - 1);
						String tmp[] = actions[0].split("");
						for (int i = 0; i < tmp.length; i++) {
							fenxi.add(tmp[i]);
						}
						if (inputString.length() > 1) {
							inputString = inputString.substring(1,
									inputString.length());
						}// 余留输入串减一
						judge();
					}
				} else if (actions[1].equals("R")) {
					if (actions[0].equals("ε")) {
						fenxi.remove(fenxi.size() - 1);
						if (inputString.length() > 1) {
							inputString = inputString.substring(1,
									inputString.length());
						}// 余留输入串减一
						judge();
					} else {
						fenxi.remove(fenxi.size() - 1);
						String tmp[] = actions[0].split("");
						for (int i = 0; i < tmp.length; i++) {
							fenxi.add(tmp[i]);
						}
						judge();
					}
				} else {
					System.out.print("#1出错");
				}
			} else {
				System.out.println("error 此字符串不符合该文法！");
			}
		} else {
			System.out.println("分析结束！此字符串符合该文法！");
		}
	}

	public static void prt(ArrayList<String> fen, String in, String ac) {
		int n = 0;
		Iterator iterator = fenxi.iterator();
		while (iterator.hasNext()) {
			System.out.print(iterator.next());
			n++;
		}
		System.out.println("\t\t" + in + "\t\t" + ac);
	}

	public static void M(String a, String b) {
		if (a.equals("#") && b.equals("#")) {
			action = "succ";
		} else {
			int indexvn = -1;
			int indexvt = -1;
			for (int i = 0; i < Vn.length; i++) {
				if (Vn[i].equals(a)) {
					indexvn = i;
					break;
				}
			}
			for (int i = 0; i < Vt.length; i++) {
				if (Vt[i].equals(b)) {
					indexvt = i;
					break;
				}
			}
			if (indexvn >= 0 && indexvt >= 0) {
				action = P[indexvn][indexvt];
			} else {
				boolean aa = false;// 判断a是否属于vt且a不在任何规则右部的首部
				for (int i = 0; i < Vt.length; i++) {
					if (top.equals(Vt[i])) {
						aa = true;
					}
					for (int m = 0; m < 3; m++) {
						for (int n = 0; n < 6; n++) {
							String tString = P[m][n];
							String tmp[] = tString.split("/");
							if (!tmp[0].startsWith(top)) {
								aa = true;
							}
						}
					}
				}
				if (aa) {
					action = "ε/C";
				}
			}
		}
	}

	public static void main(String[] args) {
		init();
		input();
		System.out.println("分析栈\t\t" + "余留输入串\t\t" + "动作\t\t");
		judge();
	}

}
