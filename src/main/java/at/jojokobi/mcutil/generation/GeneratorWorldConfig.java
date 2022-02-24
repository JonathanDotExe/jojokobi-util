package at.jojokobi.mcutil.generation;

import java.util.ArrayList;
import java.util.List;

public class GeneratorWorldConfig {

	private boolean generateStructures = true;
	private List<String> dontGenerate = new ArrayList<>();
	
	
	public boolean isGenerateStructures() {
		return generateStructures;
	}
	public void setGenerateStructures(boolean generateStructures) {
		this.generateStructures = generateStructures;
	}
	public List<String> getDontGenerate() {
		return dontGenerate;
	}
	public void setDontGenerate(List<String> dontGenerate) {
		this.dontGenerate = dontGenerate;
	}

}
