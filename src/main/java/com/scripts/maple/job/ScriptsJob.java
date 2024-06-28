package com.scripts.maple.job;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.scripts.maple.properties.Constants;
import com.scripts.maple.model.KeySettings;
import com.scripts.maple.ScriptsUI;

public class ScriptsJob implements Runnable {
	private static final Logger logger = Logger.getLogger(ScriptsJob.class.getName());
	private JTextArea outputArea;
	private KeySettings key = new KeySettings();

	// 判斷執行
	public volatile boolean doScriptsCtrl = false;

	// 計時器計數
	private long startTime;

	public ScriptsJob(KeySettings key, JTextArea outputArea) {
		this.key = key;
		this.outputArea = outputArea;
	}

	// 覆寫Runnable run()
	public void run() {
		try {
			doScriptsCtrl = true;
			startTime = System.currentTimeMillis();
			logToUI("********按鍵設定********");
			logToUI("攻擊: " + key.getAttackKey());
			logToUI("掉落加倍: " + key.getDropDoubleKey());
			logToUI("經驗加倍: " + key.getExpDoubleKey());
			logToUI("寵物食品: " + key.getFoodKey());
			logToUI("輔助技能: " + key.getSupportKeyString());
			logToUI("********腳本開始********");
			while (doScriptsCtrl) {
				doAttack();
				doMoveLeft();
				doAttack();
				doMoveRight();
				// 每經過30分鐘吃加倍
				if (System.currentTimeMillis() - startTime >= TimeUnit.MINUTES.toMillis(Constants.DOUBLE_TIME_FOR_TIMER)) {
					doDouble();
					startTime = System.currentTimeMillis(); // 重置計時器
				}
			}
			logToUI("********腳本暫停********");
			ScriptsUI.runnable = false;

		} catch (Exception e) {
			logToUI("發生錯誤" + e);
			e.printStackTrace();
		}

	}

	// 控制停止腳本
	public void stop() {
		doScriptsCtrl = false;
	}

	// 攻擊秒數由attackTime定義
	public void doAttack() {
		if (key.getAttackKey() != ' ') {
			logToUI("正在使用攻擊....");
			try {
				Robot robot = new Robot();
				for (int i = 0; i < Constants.ATTACK_ROUND; i++) {
					if (!doScriptsCtrl) {
						break;
					}
					int keycode = KeyEvent.getExtendedKeyCodeForChar(key.getAttackKey());
					robot.keyPress(keycode);
					robot.keyRelease(keycode);
					// 設定延遲
					Thread.sleep(Constants.DELAY);
				}
			} catch (Exception e) {
				logToUI("發生錯誤" + e);
				return;
			}

		} else {
			logToUI("攻擊按鍵未設定");
			return;
		}
	}

	// 往左走半秒
	public void doMoveLeft() {
		try {
			logToUI("往左移動....");
			Robot robot = new Robot();
			int keycode = KeyEvent.VK_LEFT;
			robot.keyPress(keycode);
			// 設定延遲
			Thread.sleep(Constants.MOVE_TIME);
			robot.keyRelease(keycode);
		} catch (Exception e) {
			logToUI("發生錯誤" + e);
			return;
		}

	}

	// 往右走半秒
	public void doMoveRight() {
		try {
			logToUI("往右移動....");
			Robot robot = new Robot();
			int keycode = KeyEvent.VK_RIGHT;
			robot.keyPress(keycode);
			// 設定延遲
			Thread.sleep(Constants.MOVE_TIME);
			robot.keyRelease(keycode);
		} catch (Exception e) {
			logToUI("發生錯誤" + e);
			return;
		}

	}

	public void doSupport() {

	}

	public void doDouble() {
		try {
			Robot robot = new Robot();
			if (key.getDropDoubleKey() != ' ') {
				logToUI("正在使用掉落加倍....");
				Thread.sleep(Constants.DELAY);
				//觸發按鍵
				int keycode = KeyEvent.getExtendedKeyCodeForChar(key.getDropDoubleKey());
				robot.keyPress(keycode);
				robot.keyRelease(keycode);
			} else {
				logToUI("未設定掉落加倍");
				Thread.sleep(Constants.DELAY);
			}

			if (key.getExpDoubleKey() != ' ') {
				logToUI("正在使用掉落加倍....");
				Thread.sleep(Constants.DELAY);
				//觸發按鍵
				int keycode = KeyEvent.getExtendedKeyCodeForChar(key.getExpDoubleKey());
				robot.keyPress(keycode);
				robot.keyRelease(keycode);
			} else {
				logToUI("未設定經驗加倍");
				Thread.sleep(Constants.DELAY);
			}
		} catch (Exception e) {
			logToUI("發生錯誤");
			return;
		}
	}

	//設定訊息給Swing畫面
    private void logToUI(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                outputArea.append(message + "\n"); // 將訊息添加到 JTextArea
            }
        });
    }

}
