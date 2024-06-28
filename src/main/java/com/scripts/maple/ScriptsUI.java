/****************************************************************************************
 * 																						*
 * 																						*
 * 																						*
 * 																						*
 * 																						*
 * **************************************************************************************
 * */

package com.scripts.maple;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Font;

import com.scripts.maple.job.ScriptsJob;
import com.scripts.maple.model.KeySettings;

// TODO: Auto-generated Javadoc
/**
 * The Class ScriptsUI.
 */
public class ScriptsUI {

	/** The key. */
	public static KeySettings key = new KeySettings();

	public static boolean runnable = false;

	/** The scripts job. */
	public static ScriptsJob scriptsJob;

	/** The timer. */
	private static Timer timer;					// 計時器

	/** The frame. */
	private static JFrame frame;				// 主框架

	/** The timer label. */
	private static JLabel timerLabel; 			// 計時器Label

	/** The panel. */
	private static JPanel panel;	  			// 畫面

	/** The output text area. */
	private static JTextArea outputTextArea; 	//TextArea


	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		showView();
	}

	/**
	 * Show view.
	 */
	private static void showView() {
		// 主框架 初始化
		frame = new JFrame("腳本測試");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 600);

		// 畫面 初始化與constraints基本設定
		panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		// 設定間距
		constraints.insets = new Insets(10, 10, 10, 10);
		//-----------------畫面物件設定-----------------
		//timer
		timerLabel = new JLabel("00:00:00");
		JLabel timerTitle = new JLabel("執行時間: ");
		// 攻擊鍵
		JLabel attackLabel = new JLabel("攻擊鍵:");
		final JTextField attackText = new JTextField(5);
		// 輔助技能
		JLabel supportLabel = new JLabel("輔助技能:");
		final JTextField supportText = new JTextField(5);
		// 經驗加倍
		JLabel expDoubleLabel = new JLabel("經驗加倍位置:");
		final JTextField expDoubleText = new JTextField(5);
		// 掉落加倍
		JLabel dropDoubleLabel = new JLabel("掉落加倍位置:");
		final JTextField dropDoubleText = new JTextField(5);
		// 寵物食品
		JLabel foodLabel = new JLabel("寵物食品:");
		final JTextField foodText = new JTextField(5);
		//開始結束按鈕
		final JButton startButton = new JButton("開始");
		JButton stopButton = new JButton("結束");
		JButton cleanLogButton = new JButton("清除log");
	    // Logger Text
	    outputTextArea = new JTextArea(10, 50);
	    outputTextArea.setEditable(false); 								// 設置為不可編輯
	    outputTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16)); // 調整字體大小
	    JScrollPane scrollPane = new JScrollPane(outputTextArea);

		// -----------------放入畫面開始-----------------
		addPanel(panel, attackLabel, 0, 0, constraints);
		addPanel(panel, attackText, 1, 0, constraints);
		addPanel(panel, supportLabel, 0, 2, constraints);
		addPanel(panel, supportText, 1, 2, constraints);
		addPanel(panel, expDoubleLabel, 0, 10, constraints);
		addPanel(panel, expDoubleText, 1, 10, constraints);
		addPanel(panel, dropDoubleLabel, 0, 12, constraints);
		addPanel(panel, dropDoubleText, 1, 12, constraints);
		addPanel(panel, foodLabel, 0, 14, constraints);
		addPanel(panel, foodText, 1, 14, constraints);

		// 放入計時器
		addPanel(panel, timerTitle, 2, 0, constraints);
		addPanel(panel, timerLabel, 3, 0, constraints);

		// 放入按鈕
		constraints.gridwidth = 2;
		addPanel(panel, startButton, 0, 16, constraints);
		addPanel(panel, stopButton, 0, 18, constraints);
		addPanel(panel, cleanLogButton, 0, 20, constraints);

        // logger
        constraints.gridwidth = 4; 						 // 佔據4列
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridheight = 20; 					 // 佔據20行
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0; 						 // 佔滿水平方向剩餘空間
        constraints.weighty = 1.0; 						 // 佔滿垂直方向剩餘空間
		addPanel(panel, scrollPane, 20, 0, constraints); // 調整 y 座標和佔據的列數

		//畫面放完後初始化
		constraints = new GridBagConstraints();
		// -----------------放入畫結束-----------------

		// 開始
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 設置按鍵
				String attackKey = attackText.getText();
				String supportKey = supportText.getText();
				String expDoubleKey = expDoubleText.getText();
				String dropDoubleKey = dropDoubleText.getText();
				String foodKey = foodText.getText();
				//attackKey必須當地一個參數 剩下按鍵檢查隨便
				if (checkInput(attackKey,expDoubleKey,dropDoubleKey,foodKey) && !runnable) {
					// 按鍵設定實體
					key.setAttackKey(string2Char(attackKey));
					key.setSupportKeyString(supportKey);
					key.setExpDoubleKey(string2Char(expDoubleKey));
					key.setDropDoubleKey(string2Char(dropDoubleKey));
					key.setFoodKey(string2Char(foodKey));

					// 另開執行序執行scriptsJob送入按鍵設定
					scriptsJob = new ScriptsJob(key, outputTextArea);
					Thread thread = new Thread(scriptsJob);
					thread.start();
					System.out.println("執行續正在執行?: "+runnable);
					startTimer();
					runnable = true;

					//關閉按鍵
					startButton.setEnabled(false);
				}

			}
		});

		// 結束
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (scriptsJob != null) {
					scriptsJob.stop();
					scriptsJob = null;
					startButton.setEnabled(true);
				}
				System.out.println("執行續正在執行?: "+runnable);
				stopTimer();
			}
		});

		cleanLogButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				outputTextArea.setText("");
			}

		});

		// 面板靠上
		frame.add(panel, BorderLayout.NORTH);
		frame.setLocationRelativeTo(null);
		// 顯示畫面
		frame.setVisible(true);
	}

	/**
	 * Adds the panel.
	 *
	 * @param panel            the panel
	 * @param component            the component
	 * @param x the x
	 * @param y the y
	 * @param constraints            the constraints
	 */
	// add
	private static void addPanel(JPanel panel, JComponent component, int x, int y, GridBagConstraints constraints) {
		constraints.gridx = x;
		constraints.gridy = y;
		panel.add(component, constraints);
	}

	/**
	 * Start timer.
	 */
	// 開始計時
	private static void startTimer() {
		timer = new Timer(1000, new ActionListener() {
			int secondsPassed = 0;

			public void actionPerformed(ActionEvent e) {
				secondsPassed++;
				updateTimerLabel(secondsPassed);
			}
		});
		timer.start();
	}

	/**
	 * Update timer label.
	 *
	 * @param secondsPassed the seconds passed
	 */
	// 更新timer
	private static void updateTimerLabel(int secondsPassed) {
		int hours = secondsPassed / 3600;
		int minutes = (secondsPassed % 3600) / 60;
		int seconds = secondsPassed % 60;
		timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
	}

	/**
	 * Stop timer.
	 */
	// 停止計時器
	private static void stopTimer() {
		if (timer != null) {
			timer.stop();
			timer = null;
		}
		timerLabel.setText("00:00:00");
	}

	public static boolean checkInput(String... s) {
		boolean result = true;
		if (s[0].length() < 1){
			outputTextArea.append("攻擊鍵未輸入" + "\n");
			result = false;
		}

		for (String tmp : s){
			if (tmp.length() > 1) {
				outputTextArea.append("請檢查按鍵設定" + "\n");
				result = false;
				break;
			}
		}
		return result;
	}

	/**
	 * String2 char.
	 *
	 * @param s the s
	 * @return the char
	 */
	public static char string2Char(String s) {
		char c = ' ';
		// 空的直接回傳
		if (s.isEmpty()) {
			return c;
		}
		if (s.length() == 1) {
			c = s.charAt(0);
		}
		return c;
	}

}
