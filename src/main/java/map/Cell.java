package map;

import java.io.Serializable;

import dinosaur.Dinosaur;

/**
 * @author  gas12n
 */
public class Cell implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2536848468008125515L;
	private boolean isVegetation;
	private boolean isEarth;
	private boolean isCarrion;
	private boolean isWater;
	private boolean isVisible;
	private String type; /* contiene a, v, t, c in base alle tipologia di casella */
	private int energyVegetation;
	private int energyCarrion;
	private int currentEnergyVegetation;
	private int currentEnergyCarrion;
	private boolean thereIsDinosaur;
	private String idDinosaur;
	/**
	 * @uml.property  name="localDinosaur"
	 * @uml.associationEnd  
	 */
	private Dinosaur localDinosaur;

	/**
	 * @return
	 * @uml.property  name="idDinosaur"
	 */
	public final String getIdDinosaur() {
		return idDinosaur;
	}

	/**
	 * @param idDinosaur
	 * @uml.property  name="idDinosaur"
	 */
	public final void setIdDinosaur(String idDinosaur) {
		this.idDinosaur = idDinosaur;
	}

	/**
	 * @return
	 * @uml.property  name="isVisible"
	 */
	public final boolean isVisible() {
		return isVisible;
	}

	/**
	 * @param isVisible
	 * @uml.property  name="isVisible"
	 */
	public final void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	/**
	 * @return
	 * @uml.property  name="currentEnergyVegetation"
	 */
	public int getCurrentEnergyVegetation() {
		return currentEnergyVegetation;
	}

	/**
	 * @param currentEnergyVegetation
	 * @uml.property  name="currentEnergyVegetation"
	 */
	public void setCurrentEnergyVegetation(int currentEnergyVegetation) {
		this.currentEnergyVegetation = currentEnergyVegetation;
	}

	/**
	 * @return
	 * @uml.property  name="currentEnergyCarrion"
	 */
	public int getCurrentEnergyCarrion() {
		return currentEnergyCarrion;
	}

	/**
	 * @param currentEnergyCarrion
	 * @uml.property  name="currentEnergyCarrion"
	 */
	public void setCurrentEnergyCarrion(int currentEnergyCarrion) {
		this.currentEnergyCarrion = currentEnergyCarrion;
	}

	/**
	 * @param isVegetation
	 * @uml.property  name="isVegetation"
	 */
	public final void setVegetation(boolean isVegetation) {
		this.isVegetation = isVegetation;
	}

	/**
	 * @param isEarth
	 * @uml.property  name="isEarth"
	 */
	public final void setEarth(boolean isEarth) {
		this.isEarth = isEarth;
	}

	/**
	 * @param isCarrion
	 * @uml.property  name="isCarrion"
	 */
	public final void setCarrion(boolean isCarrion) {
		this.isCarrion = isCarrion;
	}

	/**
	 * @param isWater
	 * @uml.property  name="isWater"
	 */
	public final void setWater(boolean isWater) {
		this.isWater = isWater;
	}

	/**
	 * @return
	 * @uml.property  name="isVegetation"
	 */
	public final boolean isVegetation() {
		return isVegetation;
	}

	/**
	 * @return
	 * @uml.property  name="isEarth"
	 */
	public final boolean isEarth() {
		return isEarth;
	}

	/**
	 * @return
	 * @uml.property  name="isCarrion"
	 */
	public final boolean isCarrion() {
		return isCarrion;
	}

	/**
	 * @return
	 * @uml.property  name="isWater"
	 */
	public final boolean isWater() {
		return isWater;
	}

	/**
	 * @param type
	 * @uml.property  name="type"
	 */
	public final void setType(String type) {
		this.type = type;
	}

	/**
	 * @return
	 * @uml.property  name="type"
	 */
	public final String getType() {
		return type;
	}

	/**
	 * @return
	 * @uml.property  name="energyVegetation"
	 */
	public int getEnergyVegetation() {
		return energyVegetation;
	}

	/**
	 * @param energyVegetation
	 * @uml.property  name="energyVegetation"
	 */
	public void setEnergyVegetation(int energyVegetation) {
		this.energyVegetation = energyVegetation;
	}

	/**
	 * @return
	 * @uml.property  name="energyCarrion"
	 */
	public final int getEnergyCarrion() {
		return energyCarrion;
	}

	/**
	 * @param energyCarrion
	 * @uml.property  name="energyCarrion"
	 */
	public final void setEnergyCarrion(int energyCarrion) {
		this.energyCarrion = energyCarrion;
	}

	/**
	 * @return
	 * @uml.property  name="thereIsDinosaur"
	 */
	public final boolean getThereIsDinosaur() {
		return thereIsDinosaur;
	}

	/**
	 * @param thereIsDinosaur
	 * @uml.property  name="thereIsDinosaur"
	 */
	public final void setThereIsDinosaur(boolean thereIsDinosaur) {
		this.thereIsDinosaur = thereIsDinosaur;
	}

	/**
	 * @return
	 * @uml.property  name="localDinosaur"
	 */
	public Dinosaur getLocalDinosaur() {
		return localDinosaur;
	}

	/**
	 * @param localDinosaur
	 * @uml.property  name="localDinosaur"
	 */
	public void setLocalDinosaur(Dinosaur localDinosaur) {
		this.localDinosaur = localDinosaur;
	}

}
