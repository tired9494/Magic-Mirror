package github.tired9494.config;

public class ConfigInstance {

	public float mirrorDamage;
	public String note;
	public int xpCost;
	public int hungerLength;
	public int weaknessLength;
	public int fatigueLength;
	public int blindnessLength;
	public int nauseaLength;
	public boolean recipeEnabled;
	public String note2;
	public boolean interdimensional;

	public ConfigInstance() {
		mirrorDamage = 5.0F;
		note = "Damage is in half-hearts (e.g. 5.0 will do 2.5 hearts of damage). All the lengths are in ticks (20 ticks is 1 second)";
		xpCost = 2;
		hungerLength = 0;
		weaknessLength = 1200;
		fatigueLength = 100;
		blindnessLength = 100;
		nauseaLength = 0;
		recipeEnabled = true;
		note2 = "Disabling this option will remove the recipe and the magic mirror will spawn in chest loot instead. Changing this option will delete all existing mirrors";
		interdimensional = false;
	}
	
}
