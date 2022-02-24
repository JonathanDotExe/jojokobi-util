package at.jojokobi.mcutil;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public final class PotionEffectUtil {
	
	private static final PotionEffectType[] negativePotionEffects = {PotionEffectType.BLINDNESS, PotionEffectType.CONFUSION, PotionEffectType.HARM, PotionEffectType.HUNGER, PotionEffectType.POISON, PotionEffectType.SLOW, PotionEffectType.SLOW_DIGGING, PotionEffectType.UNLUCK, PotionEffectType.WITHER, PotionEffectType.WEAKNESS};
	
	private PotionEffectUtil() {
		
	}
	
	public static boolean hasNegativePotionEffect (Player player) {
		boolean negative = false;
		for (int i = 0; i < negativePotionEffects.length && !negative; i++) {
			if (player.getPotionEffect(negativePotionEffects[i]) != null) {
				negative = true;
			}
		}
		return negative;
	}
	

}
