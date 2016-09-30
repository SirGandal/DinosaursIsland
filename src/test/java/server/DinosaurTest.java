package server;

import static org.junit.Assert.*;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import dinosaur.Dinosaur;
import exception.NotEnoughEnergy;

public class DinosaurTest {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	private int oldDimension;

	@Test(expected = NotEnoughEnergy.class)
	public void testGrowDinosaur() throws NotEnoughEnergy {
		Dinosaur denver = new Dinosaur("pippo", "pipporace", 5, 5);
		denver.incrementDimension();
		denver.setMaxEnergy(denver.getDimension());
		try {
			oldDimension = denver.getDimension();
			denver.growDinosaur(denver);
		} catch (NotEnoughEnergy e) {
			Assume.assumeNoException(e);
		}
		assertEquals(oldDimension+1,denver.getDimension());

		denver.setCurrentEnergy(300);
			oldDimension = denver.getDimension();
			denver.growDinosaur(denver);
		
		assertEquals(oldDimension,denver.getDimension() );

	}

	@Test
	public void testVisualDino() {
		//fail("Not yet implemented");
	}

}
