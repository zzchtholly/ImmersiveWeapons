package com.anonymoushacker1279.immersiveweapons;

import com.anonymoushacker1279.immersiveweapons.init.DeferredRegistryHandler;
import com.anonymoushacker1279.immersiveweapons.init.DispenserBehaviorRegistry;
import com.anonymoushacker1279.immersiveweapons.init.OreGeneratorHandler;
import com.anonymoushacker1279.immersiveweapons.util.*;
import com.anonymoushacker1279.immersiveweapons.world.gen.feature.structure.BattlefieldVillagePools;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.MobSpawnInfo.Spawners;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ImmersiveWeapons.MOD_ID)
public class ImmersiveWeapons {

	public static final String MOD_ID = "immersiveweapons";

	// Setup logger
	public static final Logger LOGGER = LogManager.getLogger();

	// Mod setup begins here
	public ImmersiveWeapons() {
		// Load configuration
		Config.setup(FMLPaths.CONFIGDIR.get().resolve(MOD_ID + ".toml"));

		ConfiguredSurfaceBuilders.register();
		DeferredRegistryHandler.init();

		MinecraftForge.EVENT_BUS.register(this);

		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		Structures.DEFERRED_REGISTRY_STRUCTURE.register(modEventBus);
		modEventBus.addListener(this::setup);

		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		forgeBus.addListener(EventPriority.NORMAL, this::worldLoadEvent);
		forgeBus.addListener(EventPriority.HIGH, this::onBiomeLoading);

		// Register packet handlers
		PacketHandler.registerPackets();
	}

	/**
	 * Sets up a custom biome. Adds the types to the BiomeDictionary
	 *      and adds the biome to the BiomeManager.
	 * @param biome the <code>Biome</code> being setup
	 * @param biomeType the <code>BiomeType</code> for the biome
	 * @param weight weight to generate biomes
	 * @param types the dimension type: leave null for a modded dimension
	 */
	private static void setupBiome(final Biome biome, final BiomeType biomeType, final int weight, final BiomeDictionary.Type... types) {
		BiomeDictionary.addTypes(key(biome), types);
		BiomeManager.addBiome(biomeType, new BiomeManager.BiomeEntry(key(biome), weight));
	}

	/**
	 * Create a RegistryKey for Biomes.
	 * @param biome the <code>Biome</code> being registered
	 * @return RegistryKey
	 */
	private static RegistryKey<Biome> key(final Biome biome) {
		return RegistryKey.create(ForgeRegistries.Keys.BIOMES, Objects.requireNonNull(ForgeRegistries.BIOMES.getKey(biome), "Biome registry name was null"));
	}

	/**
	 * Event handler for the FMLCommonSetupEvent.
	 * Most of this is registry related.
	 * @param event the <code>FMLCommonSetupEvent</code> instance
	 */
	@SubscribeEvent
	public void setup(final FMLCommonSetupEvent event) {
		OreGeneratorHandler.init(event);
		DispenserBehaviorRegistry.init();
		BattlefieldVillagePools.init();
		event.enqueueWork(() -> {
			setupBiome(DeferredRegistryHandler.BATTLEFIELD.get(), BiomeManager.BiomeType.WARM, 3, Type.PLAINS, Type.OVERWORLD);
			Structures.setupStructures();
			Structures.registerAllPieces();
			ConfiguredStructures.registerConfiguredStructures();
			Structures.init();
		});
		AddAttributesAfterSetup.init();
	}

	/**
	 * Event handler for the BiomeLoadingEvent.
	 * Configures custom ores, carvers, spawns, structures, etc.
	 * @param event the <code>BiomeLoadingEvent</code> instance
	 */
	@SubscribeEvent
	public void onBiomeLoading(final BiomeLoadingEvent event) {
		// Biome modifications
		BiomeGenerationSettingsBuilder generation = event.getGeneration();
		if (event.getCategory() != Biome.Category.NETHER || event.getCategory() != Biome.Category.THEEND) {
			event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES)
					.add(() -> OreGeneratorHandler.ORE_COPPER_CONFIG);
			event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES)
					.add(() -> OreGeneratorHandler.ORE_COBALT_CONFIG);

			if (event.getCategory() != Category.OCEAN && event.getCategory() != Category.RIVER) {
				event.getSpawns().addSpawn(EntityClassification.MONSTER, new Spawners(DeferredRegistryHandler.WANDERING_WARRIOR_ENTITY.get(), 15, 1, 1));
				event.getSpawns().addSpawn(EntityClassification.MONSTER, new Spawners(DeferredRegistryHandler.HANS_ENTITY.get(), 1, 1, 1));
			}
		}
		if (event.getCategory() == Biome.Category.NETHER) {
			event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES)
					.add(() -> OreGeneratorHandler.ORE_MOLTEN_CONFIG);
		}

		if (event.getCategory() == Category.FOREST) {
			generation.addStructureStart(ConfiguredStructures.CONFIGURED_ABANDONED_FACTORY);
			generation.addStructureStart(ConfiguredStructures.CONFIGURED_UNDERGROUND_BUNKER);
			generation.addStructureStart(ConfiguredStructures.CONFIGURED_BEAR_TRAP);
		}
		if (event.getCategory() == Category.PLAINS) {
			generation.addStructureStart(ConfiguredStructures.CONFIGURED_ABANDONED_FACTORY);
			generation.addStructureStart(ConfiguredStructures.CONFIGURED_CAMPSITE);
		}
		if (event.getCategory() == Category.JUNGLE) {
			generation.addStructureStart(ConfiguredStructures.CONFIGURED_PITFALL_TRAP);
		}
		if (event.getCategory() == Category.DESERT) {
			generation.addStructureStart(ConfiguredStructures.CONFIGURED_LANDMINE_TRAP);
			generation.addStructureStart(ConfiguredStructures.CONFIGURED_CAMPSITE);
		}
		if (event.getCategory() == Category.TAIGA) {
			generation.addStructureStart(ConfiguredStructures.CONFIGURED_CLOUD_ISLAND);
		}
		if (Objects.requireNonNull(event.getName()).toString().equals(Objects.requireNonNull(DeferredRegistryHandler.BATTLEFIELD.get().getRegistryName()).toString())) {
			generation.addStructureStart(ConfiguredStructures.CONFIGURED_BATTLEFIELD_CAMP);
			generation.addStructureStart(ConfiguredStructures.CONFIGURED_UNDERGROUND_BUNKER);
			generation.addStructureStart(ConfiguredStructures.CONFIGURED_BEAR_TRAP);
			generation.addStructureStart(ConfiguredStructures.CONFIGURED_BATTLEFIELD_VILLAGE);
			generation.addFeature(Decoration.VEGETAL_DECORATION, ConfiguredStructures.CONFIGURED_WOODEN_SPIKES);
			generation.addCarver(GenerationStage.Carving.AIR, new ConfiguredCarver<>(DeferredRegistryHandler.TRENCH_WORLD_CARVER.get(), new ProbabilityConfig(0.115f)));
		}
	}

	/**
	 * Event handler for the WorldEvent.Load event.
	 * Most importantly, we are building a Map
	 *     to contain our structures.
	 * @param event the <code>WorldEvent.Load</code> instance
	 */
	public void worldLoadEvent(final WorldEvent.Load event) {
		if (event.getWorld() instanceof ServerWorld) {
			ServerWorld serverWorld = (ServerWorld) event.getWorld();

			if (serverWorld.getChunkSource().getGenerator() instanceof FlatChunkGenerator &&
					serverWorld.dimension().equals(World.OVERWORLD)) {
				return;
			}

			Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
			tempMap.put(Structures.ABANDONED_FACTORY.get(), DimensionStructuresSettings.DEFAULTS.get(Structures.ABANDONED_FACTORY.get()));
			tempMap.put(Structures.PITFALL_TRAP.get(), DimensionStructuresSettings.DEFAULTS.get(Structures.PITFALL_TRAP.get()));
			tempMap.put(Structures.BEAR_TRAP.get(), DimensionStructuresSettings.DEFAULTS.get(Structures.BEAR_TRAP.get()));
			tempMap.put(Structures.LANDMINE_TRAP.get(), DimensionStructuresSettings.DEFAULTS.get(Structures.LANDMINE_TRAP.get()));
			tempMap.put(Structures.UNDERGROUND_BUNKER.get(), DimensionStructuresSettings.DEFAULTS.get(Structures.UNDERGROUND_BUNKER.get()));
			tempMap.put(Structures.BATTLEFIELD_CAMP.get(), DimensionStructuresSettings.DEFAULTS.get(Structures.BATTLEFIELD_CAMP.get()));
			tempMap.put(Structures.BATTLEFIELD_VILLAGE.get(), DimensionStructuresSettings.DEFAULTS.get(Structures.BATTLEFIELD_VILLAGE.get()));
			tempMap.put(Structures.CLOUD_ISLAND.get(), DimensionStructuresSettings.DEFAULTS.get(Structures.CLOUD_ISLAND.get()));
			tempMap.put(Structures.CAMPSITE.get(), DimensionStructuresSettings.DEFAULTS.get(Structures.CAMPSITE.get()));
			serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
		}
	}
}