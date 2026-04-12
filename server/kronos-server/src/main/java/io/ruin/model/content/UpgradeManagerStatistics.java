//package io.ruin.model.content;
//
//import io.ruin.model.entity.player.Player;
//
//public class UpgradeManagerStatistics extends UpgradeManager {
//
//    public static int addSuccess(Player player) {
//        switch (getSelectedItem()) {
//            // Weapon
//            case ABYSSAL_BLOOD_WHIP:
//                return player.abyssalBloodwhipUpgrades++;
//            case DRAGON_SCIMITARE:
//                return player.dscimeUpgrades++;
//            case CREATION_CLAWS:
//                return player.creationClawUpgrades++;
//            case BLOODLINE_CHAINMACE:
//                return player.bloodlineChainmaceUpgrades++;
//            case BLOODLINE_BOW:
//                return player.bloodlineBowUpgrades++;
//            case CORRUPTED_ORB:
//                return player.corruptedOrbUpgrades++;
//            case ZAMORAK_GODSWORD:
//                return player.zamorakGodswordUpgrades++;
//            case SARADOMIN_GODSWORD:
//                return player.saradominGodswordUpgrades++;
//            case BANDOS_GODSWORD:
//                return player.bandosGodswordUpgrades++;
//            case CURSED_VESTAS_LONGSWORD:
//                return player.cursedVestasLongswordUpgrades++;
//            case ARMADYL_GODSWORD_OR:
//                return player.armadylgodswordUpgrades++;
//
//            case CORRUPTED_STAFF:
//                return player.corruptstaffUpgrades++;
//            case CORRUPTED_JAVELIN:
//                return player.corruptjavelinUpgrades++;
//
//            case GALVEK_CROSSBOW:
//                return player.galvekcrossbowUpgrades++;
//            case GALVEK_MACE:
//                return player.galvekmaceUpgrades++;
//            case INFERNAL_DINS:
//                return player.infernaldinsUpgrades++;
//
//            // Armour
//            case DRACO_SET:
//                return player.dracoSetUpgrades++;
//            case FIGHTER_TORSOE:
//                return player.fighterTorsoUpgrades++;
//            case FUSED_BOOTS:
//                return player.fusedbootsUpgrades++;
//            case PRIMORDIAL_BOOTS_OR:
//                return player.primbootsUpgrades++;
//            case PEGASIAN_BOOTS_OR:
//                return player.pegasianbootsUpgrades++;
//            case ETERNAL_BOOTS_OR:
//                return player.eternalbootsUpgrades++;
//            case VAMPYRIC_FACEGUARD:
//                return player.vampyricFaceguardUpgrades++;
//            case LAVA_DHIDE_COIF:
//                return player.lavaCoifUpgrades++;
//            case LAVA_DHIDE_BODY:
//                return player.lavaBodyUpgrades++;
//            case LAVA_DHIDE_CHAPS:
//                return player.lavaChapsUpgrades++;
//            case DRAGON_DEFENDER_T:
//                return player.dragonDefenderUpgrades++;
//
//            case CORRUPTED_HELM:
//                return player.corruptHelmUpgrades++;
//            case CORRUPTED_BODY:
//                return player.corruptBodyUpgrades++;
//            case CORRUPTED_LEGS:
//                return player.corruptLegsUpgrades++;
//
//
//
//            case NECROMANCER_HAT:
//                return player.necrohatUpgrades++;
//            case NECROMANCER_ROBE_TOP:
//                return player.necrobodyUpgrades++;
//            case NECROMANCER_ROBE_BOTTOMS:
//                return player.necrobottomsUpgrades++;
//
//            // Misc
//            case FUSED_HALO:
//                return player.fusedHaloUpgrades++;
//            case GOLDEN_FLIPPERS:
//                return player.goldenFlippersUpgrades++;
//            case CAPE_OF_SKULLS:
//                return player.capeofSkullsUpgrades++;
//            case OVERLOAD_HEART:
//                return player.overloadheartUpgrades++;
//            case COMBATANT_HEART:
//                return player.combatantheartUpgrades++;
//            case RANGERS_HEART:
//                return player.rangersheartUpgrades++;
//            case RING_OF_THE_UNDEAD:
//                return player.ringundeadUpgrades++;
//            case RING_OF_THE_BEASTS:
//                return player.ringbeastsUpgrades++;
//            case RING_OF_THE_ARACHNIDS:
//                return player.ringarachnidsUpgrades++;
//            case AMULET_OF_TORTURE_OR:
//                return player.amuletTortureUpgrades++;
//            case NECKLACE_OF_ANGUISH_OR:
//                return player.necklaceAnguishUpgrades++;
//            case TORMENTED_BRACELET_OR:
//                return player.tormentedBraceletUpgrades++;
//            case OCCULT_NECKLACE_OR:
//                return player.occultNecklaceUpgrades++;
//            case BLESSING_OF_THE_GODS:
//                return player.blessingUpgrades++;
//            case BRIMSTONE_RING_I:
//                return player.brimstoneRingUpgrades++;
//            case ULTIMATE_TOTEM:
//                return player.walkerTotemUpgrades++;
//
//            // Pets
//            case ENCHANTED_GOLDEN_LEPRECHAUN:
//                return player.goldenLeprechaunUpgrades++;
//
//        }
//        return 0;
//    }
//
//    public static int addAttempt(Player player) {
//        switch (getSelectedItem()) {
//            // Weapon
//            case ABYSSAL_BLOOD_WHIP:
//                return player.abyssalBloodwhipAttempts++;
//            case DRAGON_SCIMITARE:
//                return player.dscimeAttempts++;
//            case CREATION_CLAWS:
//                return player.creationClawAttempts++;
//            case BLOODLINE_CHAINMACE:
//                return player.bloodlineChainmaceAttempts++;
//            case BLOODLINE_BOW:
//                return player.bloodlineBowAttempts++;
//            case CORRUPTED_ORB:
//                return player.corruptedOrbAttempts++;
//            case ZAMORAK_GODSWORD:
//                return player.zamorakGodswordAttempts++;
//            case SARADOMIN_GODSWORD:
//                return player.saradominGodswordAttempts++;
//            case BANDOS_GODSWORD:
//                return player.bandosGodswordAttempts++;
//            case CURSED_VESTAS_LONGSWORD:
//                return player.cursedVestasLongswordAttempts++;
//            case ARMADYL_GODSWORD_OR:
//                return player.armadylgodswordAttempts++;
//
//            case CORRUPTED_STAFF:
//                return player.corruptstaffAttempts++;
//            case CORRUPTED_JAVELIN:
//                return player.corruptjavelinAttempts++;
//            case GALVEK_CROSSBOW:
//                return player.galvekcrossbowAttempts++;
//            case GALVEK_MACE:
//                return player.galvekmaceAttempts++;
//
//            case INFERNAL_DINS:
//                return  player.infernaldinsAttempts++;
//
//
//            // Armour
//
//            case DRACO_SET:
//                return player.dracoSetAttempts++;
//            case FIGHTER_TORSOE:
//                return player.fighterTorsoAttempts++;
//            case FUSED_BOOTS:
//                return player.fusedbootsAttempts++;
//            case PRIMORDIAL_BOOTS_OR:
//                return player.primbootsAttempts++;
//            case PEGASIAN_BOOTS_OR:
//                return player.pegasianbootsAttempts++;
//            case ETERNAL_BOOTS_OR:
//                return player.eternalbootsAttempts++;
//            case VAMPYRIC_FACEGUARD:
//                return player.vampyricFaceguardAttempts++;
//            case LAVA_DHIDE_COIF:
//                return player.lavaCoifAttempts++;
//            case LAVA_DHIDE_BODY:
//                return player.lavaBodyAttempts++;
//            case LAVA_DHIDE_CHAPS:
//                return player.lavaChapsAttempts++;
//            case DRAGON_DEFENDER_T:
//                return player.dragonDefenderAttempts++;
//
//            case CORRUPTED_HELM:
//                return player.corruptHelmAttempts++;
//            case CORRUPTED_BODY:
//                return player.corruptBodyAttempts++;
//            case CORRUPTED_LEGS:
//                return player.corruptLegsAttempts++;
//
//            case NECROMANCER_HAT:
//                return player.necrohatAttempts++;
//            case NECROMANCER_ROBE_TOP:
//                return player.necrobodyAttempts++;
//            case NECROMANCER_ROBE_BOTTOMS:
//                return player.necrobottomsAttempts++;
//
//            // Misc
//            case FUSED_HALO:
//                return player.fusedHaloAttempts++;
//            case GOLDEN_FLIPPERS:
//                return player.goldenFlippersAttempts++;
//            case CAPE_OF_SKULLS:
//                return player.capeofSkullsAttempts++;
//            case OVERLOAD_HEART:
//                return player.overloadheartAttempts++;
//            case COMBATANT_HEART:
//                return player.combatantheartAttempts++;
//            case RANGERS_HEART:
//                return player.rangersheartAttempts++;
//            case RING_OF_THE_UNDEAD:
//                return player.ringundeadAttempts++;
//            case RING_OF_THE_BEASTS:
//                return player.ringbeastsAttempts++;
//            case RING_OF_THE_ARACHNIDS:
//                return player.ringarachnidsAttempts++;
//            case AMULET_OF_TORTURE_OR:
//                return player.amuletTortureAttempts++;
//            case NECKLACE_OF_ANGUISH_OR:
//                return player.necklaceAnguishAttempts++;
//            case TORMENTED_BRACELET_OR:
//                return player.tormentedBraceletAttempts++;
//            case OCCULT_NECKLACE_OR:
//                return player.occultNecklaceAttempts++;
//            case BLESSING_OF_THE_GODS:
//                return player.blessingAttempts++;
//            case BRIMSTONE_RING_I:
//                return player.brimstoneRingAttempts++;
//            case ULTIMATE_TOTEM:
//                return player.walkerTotemAttempts++;
//
//            // Pets
//            case ENCHANTED_GOLDEN_LEPRECHAUN:
//                return player.goldenLeprechaunAttempts++;
//
//
//        }
//        return 0;
//    }
//
//    public static int checkSuccesses(Player player) {
//        switch (getSelectedItem()) {
//            // Weapon
//            case ABYSSAL_BLOOD_WHIP:
//                return player.abyssalBloodwhipUpgrades;
//            case DRAGON_SCIMITARE:
//                return player.dscimeUpgrades;
//            case CREATION_CLAWS:
//                return player.creationClawUpgrades;
//            case BLOODLINE_CHAINMACE:
//                return player.bloodlineChainmaceUpgrades;
//            case BLOODLINE_BOW:
//                return player.bloodlineBowUpgrades;
//            case CORRUPTED_ORB:
//                return player.corruptedOrbUpgrades;
//            case ZAMORAK_GODSWORD:
//                return player.zamorakGodswordUpgrades;
//            case SARADOMIN_GODSWORD:
//                return player.saradominGodswordUpgrades;
//            case BANDOS_GODSWORD:
//                return player.bandosGodswordUpgrades;
//            case CURSED_VESTAS_LONGSWORD:
//                return player.cursedVestasLongswordUpgrades;
//            case ARMADYL_GODSWORD_OR:
//                return player.armadylgodswordUpgrades;
//
//            case CORRUPTED_STAFF:
//                return player.corruptstaffUpgrades;
//            case CORRUPTED_JAVELIN:
//                return player.corruptjavelinUpgrades;
//
//            case GALVEK_CROSSBOW:
//                return player.galvekcrossbowUpgrades;
//            case GALVEK_MACE:
//                return player.galvekmaceUpgrades;
//
//
//            // Armour
//            case DRACO_SET:
//                return player.dracoSetUpgrades;
//            case FIGHTER_TORSOE:
//                return player.fighterTorsoUpgrades;
//            case FUSED_BOOTS:
//                return player.fusedbootsUpgrades;
//            case PRIMORDIAL_BOOTS_OR:
//                return player.primbootsUpgrades;
//            case PEGASIAN_BOOTS_OR:
//                return player.pegasianbootsUpgrades;
//            case ETERNAL_BOOTS_OR:
//                return player.eternalbootsUpgrades;
//            case VAMPYRIC_FACEGUARD:
//                return player.vampyricFaceguardUpgrades;
//            case LAVA_DHIDE_COIF:
//                return player.lavaCoifUpgrades;
//            case LAVA_DHIDE_BODY:
//                return player.lavaBodyUpgrades;
//            case LAVA_DHIDE_CHAPS:
//                return player.lavaChapsUpgrades;
//            case DRAGON_DEFENDER_T:
//                return player.dragonDefenderUpgrades;
//
//            case NECROMANCER_HAT:
//                return player.necrohatUpgrades;
//            case NECROMANCER_ROBE_TOP:
//                return player.necrobodyUpgrades;
//            case NECROMANCER_ROBE_BOTTOMS:
//                return player.necrobottomsUpgrades;
//
//            case CORRUPTED_HELM:
//                return player.corruptHelmUpgrades;
//            case CORRUPTED_BODY:
//                return player.corruptBodyUpgrades;
//            case CORRUPTED_LEGS:
//                return player.corruptLegsUpgrades;
//
//            // Misc
//            case FUSED_HALO:
//                return player.fusedHaloUpgrades;
//            case GOLDEN_FLIPPERS:
//                return player.goldenFlippersUpgrades;
//            case CAPE_OF_SKULLS:
//                return player.capeofSkullsUpgrades;
//            case OVERLOAD_HEART:
//                return player.overloadheartUpgrades;
//            case COMBATANT_HEART:
//                return player.combatantheartUpgrades;
//            case RANGERS_HEART:
//                return player.rangersheartUpgrades;
//            case RING_OF_THE_UNDEAD:
//                return player.ringundeadUpgrades;
//            case RING_OF_THE_BEASTS:
//                return player.ringbeastsUpgrades;
//            case RING_OF_THE_ARACHNIDS:
//                return player.ringarachnidsUpgrades;
//            case AMULET_OF_TORTURE_OR:
//                return player.amuletTortureUpgrades;
//            case NECKLACE_OF_ANGUISH_OR:
//                return player.necklaceAnguishUpgrades;
//            case TORMENTED_BRACELET_OR:
//                return player.tormentedBraceletUpgrades;
//            case OCCULT_NECKLACE_OR:
//                return player.occultNecklaceUpgrades;
//            case BLESSING_OF_THE_GODS:
//                return player.blessingUpgrades;
//            case BRIMSTONE_RING_I:
//                return player.brimstoneRingUpgrades;
//            case ULTIMATE_TOTEM:
//                return player.walkerTotemUpgrades;
//
//            // Pets
//            case ENCHANTED_GOLDEN_LEPRECHAUN:
//                return player.goldenLeprechaunUpgrades;
//
//        }
//        return 0;
//    }
//
//    public static int checkAttempts(Player player) {
//        switch (getSelectedItem()) {
//            // Weapon
//            case ABYSSAL_BLOOD_WHIP:
//                return player.abyssalBloodwhipAttempts;
//            case DRAGON_SCIMITARE:
//                return player.dscimeAttempts;
//            case CREATION_CLAWS:
//                return player.creationClawAttempts;
//            case BLOODLINE_CHAINMACE:
//                return player.bloodlineChainmaceAttempts;
//            case BLOODLINE_BOW:
//                return player.bloodlineBowAttempts;
//            case CORRUPTED_ORB:
//                return player.corruptedOrbAttempts;
//            case ZAMORAK_GODSWORD:
//                return player.zamorakGodswordAttempts;
//            case SARADOMIN_GODSWORD:
//                return player.saradominGodswordAttempts;
//            case BANDOS_GODSWORD:
//                return player.bandosGodswordAttempts;
//            case CURSED_VESTAS_LONGSWORD:
//                return player.cursedVestasLongswordAttempts;
//            case ARMADYL_GODSWORD_OR:
//                return player.armadylgodswordAttempts;
//
//            case CORRUPTED_STAFF:
//                return player.corruptstaffAttempts;
//            case CORRUPTED_JAVELIN:
//                return player.corruptjavelinAttempts;
//
//            case GALVEK_CROSSBOW:
//                return player.galvekcrossbowAttempts;
//            case GALVEK_MACE:
//                return player.galvekmaceAttempts;
//
//            // Armour
//            case DRACO_SET:
//                return player.dracoSetAttempts;
//            case FIGHTER_TORSOE:
//                return player.fighterTorsoAttempts;
//            case FUSED_BOOTS:
//                return player.fusedbootsAttempts;
//            case PRIMORDIAL_BOOTS_OR:
//                return player.primbootsAttempts;
//            case PEGASIAN_BOOTS_OR:
//                return player.pegasianbootsAttempts;
//            case ETERNAL_BOOTS_OR:
//                return player.eternalbootsAttempts;
//            case VAMPYRIC_FACEGUARD:
//                return player.vampyricFaceguardAttempts;
//            case LAVA_DHIDE_COIF:
//                return player.lavaCoifAttempts;
//            case LAVA_DHIDE_BODY:
//                return player.lavaBodyAttempts;
//            case LAVA_DHIDE_CHAPS:
//                return player.lavaChapsAttempts;
//            case DRAGON_DEFENDER_T:
//                return player.dragonDefenderAttempts;
//
//            case NECROMANCER_HAT:
//                return player.necrohatAttempts;
//            case NECROMANCER_ROBE_TOP:
//                return player.necrobodyAttempts;
//            case NECROMANCER_ROBE_BOTTOMS:
//                return player.necrobottomsAttempts;
//
//            case CORRUPTED_HELM:
//                return player.corruptHelmAttempts;
//            case CORRUPTED_BODY:
//                return player.corruptBodyAttempts;
//            case CORRUPTED_LEGS:
//                return player.corruptLegsAttempts;
//
//            // Misc
//            case FUSED_HALO:
//                return player.fusedHaloAttempts;
//            case GOLDEN_FLIPPERS:
//                return player.goldenFlippersAttempts;
//            case CAPE_OF_SKULLS:
//                return player.capeofSkullsAttempts;
//            case OVERLOAD_HEART:
//                return player.overloadheartAttempts;
//            case COMBATANT_HEART:
//                return player.combatantheartAttempts;
//            case RANGERS_HEART:
//                return player.rangersheartAttempts;
//            case RING_OF_THE_UNDEAD:
//                return player.ringundeadAttempts;
//            case RING_OF_THE_BEASTS:
//                return player.ringbeastsAttempts;
//            case RING_OF_THE_ARACHNIDS:
//                return player.ringarachnidsAttempts;
//            case AMULET_OF_TORTURE_OR:
//                return player.amuletTortureAttempts;
//            case NECKLACE_OF_ANGUISH_OR:
//                return player.necklaceAnguishAttempts;
//            case TORMENTED_BRACELET_OR:
//                return player.tormentedBraceletAttempts;
//            case OCCULT_NECKLACE_OR:
//                return player.occultNecklaceAttempts;
//            case BLESSING_OF_THE_GODS:
//                return player.blessingAttempts;
//            case BRIMSTONE_RING_I:
//                return player.brimstoneRingAttempts;
//            case ULTIMATE_TOTEM:
//                return player.walkerTotemAttempts;
//
//            // Pets
//            case ENCHANTED_GOLDEN_LEPRECHAUN:
//                return player.goldenLeprechaunAttempts;
//
//        }
//        return 0;
//    }
//
//}
