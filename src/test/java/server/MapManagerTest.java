package server;

import static org.junit.Assert.*;
import map.Cell;
import map.MapManager;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dinosaur.Dinosaur;

/**
 * @author  gas12n
 */
public class MapManagerTest {
	/**
	 * @uml.property  name="mapMan"
	 * @uml.associationEnd  
	 */
	static MapManager mapMan;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mapMan = new MapManager();
	}

	@Before
	public void setUp() throws Exception {
		InitDB.main(null);
	}

	@Test
	public void carrionTest() {
		int carrion = 0;
		Cell[][] globMap = mapMan.getGlobalMap();
		for (int i = 0; i < globMap.length; i++) {
			for (int j = 0; j < globMap.length; j++) {
				if (globMap[i][j].isCarrion())
					carrion++;
			}
		}
		int newcarrion = 0;
		mapMan.putNewCarrion();
		System.out.println(carrion);
		for (int i = 0; i < globMap.length; i++) {
			for (int j = 0; j < globMap.length; j++) {
				if (globMap[i][j].isCarrion())
					newcarrion++;
			}
		}
		System.out.println("after " + newcarrion);
		assertTrue(newcarrion == carrion + 1);
		int i, j=0;
		boolean stop = false;
		for (i = 0; i < globMap.length && !stop; i++) {
			for (j = 0; j < globMap.length && !stop; j++) {
				if (globMap[i][j].isCarrion()) {
					globMap[i][j].setCurrentEnergyCarrion(0);
					mapMan.growUpMap();
					stop = true;
				}
			}
		}
		assertFalse(globMap[i][j].isCarrion());
	}

	@Test
	public void PutNewCarrionTest() {
		int i = 0;
		while (i < 50) {
			int[] coord = mapMan.putNewCarrion();
			assertEquals(true, coord[0] > 0);
			assertEquals(true, coord[1] > 0);
			assertEquals(true, coord[0] < mapMan.getGlobalMap().length);
			assertEquals(true, coord[1] < mapMan.getGlobalMap().length);
			assertFalse(mapMan.getGlobalMap()[coord[0]][coord[1]].isWater());
			assertFalse(mapMan.getGlobalMap()[coord[0]][coord[1]].getThereIsDinosaur());

			i++;
		}

	}

	@Test
	public void randDinosaurTest() {
		int i = 0;
		while (i < 50) {
			int[] coord = mapMan.randomPositioner();
			assertEquals(true, coord[0] > 0);
			assertEquals(true, coord[1] > 0);
			assertEquals(true, coord[0] < mapMan.getGlobalMap().length);
			assertEquals(true, coord[1] < mapMan.getGlobalMap().length);
			
			i++;
		}

	}

	@Test
	public void putInMapTest() {
		int i = 0, j = 0;
		Dinosaur denver = new Dinosaur("notA", "notA", 5, 5);
		mapMan.putInMap(denver);
		Dinosaur dino = new Dinosaur("notA", "notA", 5, 5);
		mapMan.putInMap(dino);
		assertEquals(true,
				(dino.getColPosition() != denver.getColPosition() || dino
						.getRowPosition() != dino.getColPosition()));
		Cell[][] globMap = mapMan.getGlobalMap();
		while (!globMap[i][j].isWater()) {
			i++;
			j++;
		}
		denver = new Dinosaur("notA", "notA", i, j);
		assertFalse(globMap[i][j].getThereIsDinosaur());
	}

	@Test
	public void GrowUpMapTest() {
		int i = 0, j = 0;
		Cell[][] globMap = mapMan.getGlobalMap();
		while (!globMap[i][j].isVegetation()) {
			i++;
			j++;
		}
		int currentV = globMap[i][j].getCurrentEnergyCarrion();
		mapMan.growUpMap();
		assertTrue(currentV < globMap[i][j].getCurrentEnergyVegetation());
		i = 0;
		j = 0;
		while (!globMap[i][j].isCarrion()) {
			i++;
			j++;
		}
		int currentC = globMap[i][j].getCurrentEnergyCarrion();
		mapMan.growUpMap();
		assertTrue(currentC > globMap[i][j].getCurrentEnergyCarrion());

		int loop = 0;
		while (loop < 35) {
			mapMan.growUpMap();
			assertTrue(globMap[i][j].getCurrentEnergyCarrion() >= 0);
			assertTrue(globMap[i][j].getCurrentEnergyVegetation() <= globMap[i][j]
					.getEnergyVegetation());
			loop++;
		}
	}

}