package com.scripts.maple.model;

/**
 * The Class KeySettings.
 */
public class KeySettings {

	/** 攻擊鍵. */
	public char attackKey;

	/** 輔助技能. */
	public String SupportKeyString;

	/** 血水. */
	public char hpKey;

	/** 魔水. */
	public char mpKey;

	/** 經驗加倍. */
	public char expDoubleKey;

	/** 調寶加倍. */
	public char dropDoubleKey;

	/** 寵物食品. */
	public char foodKey;

	public char getAttackKey() {
		return attackKey;
	}

	public void setAttackKey(char attackKey) {
		this.attackKey = attackKey;
	}

	public String getSupportKeyString() {
		return SupportKeyString;
	}

	public void setSupportKeyString(String SupportKeyString) {
		this.SupportKeyString = SupportKeyString;
	}

	public char getHpKey() {
		return hpKey;
	}

	public void setHpKey(char hpKey) {
		this.hpKey = hpKey;
	}

	public char getMpKey() {
		return mpKey;
	}

	public void setMpKey(char mpKey) {
		this.mpKey = mpKey;
	}

	public char getExpDoubleKey() {
		return expDoubleKey;
	}

	public void setExpDoubleKey(char expDoubleKey) {
		this.expDoubleKey = expDoubleKey;
	}

	public char getDropDoubleKey() {
		return dropDoubleKey;
	}

	public void setDropDoubleKey(char dropDoubleKey) {
		this.dropDoubleKey = dropDoubleKey;
	}

	public char getFoodKey() {
		return foodKey;
	}

	public void setFoodKey(char foodKey) {
		this.foodKey = foodKey;
	}


}
