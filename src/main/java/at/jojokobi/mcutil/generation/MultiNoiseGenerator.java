package at.jojokobi.mcutil.generation;

import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class MultiNoiseGenerator extends NoiseGenerator {
	
	private NoiseGenerator[] generators;
	
	public MultiNoiseGenerator(long seed, int amount) {
		generators = new NoiseGenerator[amount];
		for (int i = 0; i < generators.length; i++) {
			generators[i] = new SimplexNoiseGenerator(seed + i * (i % 2 == 0 ? 57 : -82));
		}
	}

	@Override
	public double noise(double x, double y, double z) {
		double noise = 0;
		double multipliers = 0;
		for (int i = 0; i < generators.length; i++) {
			double multiplier = (1.0/(1 + i));
			multipliers += multiplier;
			noise += generators[i].noise(x*(1+i), y*(1+i), z*(1+i)) * multiplier;
		}
		
		return noise/multipliers;
	}
	
}
