package at.jojokobi.mcutil.generation;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Bed.Part;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Door.Hinge;

public final class FurnitureGenUtil {

	private FurnitureGenUtil() {
		
	}
	
	public static void generateDoor (Location loc, Material door, BlockFace direction, boolean isOpen, boolean hinge) {
		//Bottom
		Location place = loc.clone();
		BlockState doorbottom = place.getBlock().getState();
		doorbottom.setType(door);
		Door doorbottomData = (Door) doorbottom.getBlockData();
		doorbottomData.setHalf(Half.BOTTOM);
		doorbottomData.setHinge(hinge ? Hinge.RIGHT : Hinge.LEFT);
		doorbottomData.setFacing(direction);
		doorbottom.setBlockData(doorbottomData);
		doorbottom.update(true, false);
		//Top
		place.setY(place.getY() + 1);
		BlockState doortop = place.getBlock().getState();
		doortop.setType(door);
		Door doortopData = (Door) doorbottom.getBlockData();
		doortopData.setHalf(Half.TOP);
		doortopData.setHinge(hinge ? Hinge.RIGHT : Hinge.LEFT);
		doortopData.setFacing(direction);
		doortop.setBlockData(doortopData);
		doortop.update(true, false);
	}
	
	public static void generateDoor (Location loc, Material door, BlockFace direction, boolean isOpen) {
		generateDoor(loc, door, direction, isOpen, true);
	}
	
	public static void generateBed (Location place, DyeColor color, BlockFace direction) {
		//Head
		Block bedheadBlock = place.getBlock();
		bedheadBlock.setType (Material.RED_BED);
		Bed bedheadData = (Bed) bedheadBlock.getBlockData();
		bedheadData.setFacing(direction);
		bedheadData.setPart(Part.HEAD);
		bedheadBlock.setBlockData(bedheadData);
		//Bottom
		Block bedbottomBlock = place.getBlock().getRelative(direction.getOppositeFace());
		bedbottomBlock.setType (Material.RED_BED);
		Bed bedbottomData = (Bed) bedheadBlock.getBlockData();
		bedbottomData.setFacing(direction);
		bedbottomData.setPart(Part.FOOT);
		bedbottomBlock.setBlockData(bedbottomData);
	}

}
