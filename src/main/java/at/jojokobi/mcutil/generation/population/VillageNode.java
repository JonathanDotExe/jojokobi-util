package at.jojokobi.mcutil.generation.population;

public class VillageNode {
	
	private Structure house;
	private boolean left;
	private boolean top;
	private boolean right;
	private boolean bottom;
	
	public VillageNode(Structure house, boolean left, boolean top, boolean right, boolean bottom) {
		super();
		this.house = house;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public VillageNode(Structure house) {
		super();
		this.house = house;
	}
	
	public VillageNode() {
		
	}

	public Structure getHouse() {
		return house;
	}

	public void setHouse(Structure house) {
		this.house = house;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isTop() {
		return top;
	}

	public void setTop(boolean top) {
		this.top = top;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isBottom() {
		return bottom;
	}

	public void setBottom(boolean bottom) {
		this.bottom = bottom;
	}
	
//
//	public VillageNode getLeft() {
//		return left;
//	}
//
//	public void setLeft(VillageNode left) {
//		this.left = left;
//	}
//
//	public VillageNode getTop() {
//		return top;
//	}
//
//	public void setTop(VillageNode top) {
//		this.top = top;
//	}
//
//	public VillageNode getRight() {
//		return right;
//	}
//
//	public void setRight(VillageNode right) {
//		this.right = right;
//	}
//
//	public VillageNode getBottom() {
//		return bottom;
//	}
//
//	public void setBottom(VillageNode bottom) {
//		this.bottom = bottom;
//	}

}
