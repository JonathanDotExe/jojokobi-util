package at.jojokobi.mcutil.item;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

public class PersistentUUIDDataType implements PersistentDataType<byte[], UUID>{
	
	private static PersistentUUIDDataType instance;
	
	public static PersistentUUIDDataType getInstance () {
		if (instance == null) {
			instance = new PersistentUUIDDataType();
		}
		return instance;
	}
	
	private PersistentUUIDDataType() {
		
	}

	@Override
	public UUID fromPrimitive(byte[] bytes, PersistentDataAdapterContext ctx) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		return new UUID(buffer.getLong(), buffer.getLong());
	}

	@Override
	public Class<UUID> getComplexType() {
		return UUID.class;
	}

	@Override
	public Class<byte[]> getPrimitiveType() {
		return byte[].class;
	}

	@Override
	public byte[] toPrimitive(UUID uuid, PersistentDataAdapterContext ctx) {
		ByteBuffer buffer = ByteBuffer.allocate(16);
		buffer.putLong(uuid.getMostSignificantBits());
		buffer.putLong(uuid.getLeastSignificantBits());
		return buffer.array();
	}

}
