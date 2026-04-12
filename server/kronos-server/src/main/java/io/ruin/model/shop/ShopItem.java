package io.ruin.model.shop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.stat.StatRequirement;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@Getter
@ToString(callSuper = true)
@Slf4j
@JsonInclude(JsonInclude.Include.NON_DEFAULT)

public class ShopItem extends Item {

	public transient PlaceHolderRule placeholderRule = PlaceHolderRule.SHOW_ON_EMPTY;
	public int price;
	public transient boolean defaultStockItem;
	public transient int placeholderId = -1;
	public List<StatRequirement> requiredLevels;
	public List<Item> additionalItems;
	public transient Function<Player, String> additionalRequirements;
	public transient Consumer<Player> onBuy;
	public transient RequirementCheckType requirementCheckType = RequirementCheckType.NONE;

	public ShopItem() {
		this(0, 0, 0);
	}

	public ShopItem(int id, int amount, int price) {
		this(id, amount, price, null);
	}

	public ShopItem(int id) {
		super(id);
	}


	public ShopItem(int id, int amount, int price, Map<String, String> attributes) {
		super(id, amount, attributes);
		this.price = price;
	}

	@Builder
	public ShopItem(int id, int amount, boolean defaultStockItem, Map<String, String> attributes, PlaceHolderRule placeHolderRule, int price, int placeholderId, List<StatRequirement> requiredLevels, List<Item> additionalItems, Function<Player, String> additionalRequirements, Consumer<Player> onBuy, RequirementCheckType requirementCheckType) {
		super(id, amount, attributes);
		this.defaultStockItem = defaultStockItem;
		this.placeholderRule = placeHolderRule != null ? placeHolderRule : PlaceHolderRule.SHOW_ON_EMPTY;
		this.price = price;
		this.placeholderId = placeholderId;
		this.requiredLevels = requiredLevels;
		this.additionalItems = additionalItems;
		this.additionalRequirements = additionalRequirements;
		this.onBuy = onBuy;
		this.requirementCheckType = requirementCheckType != null ? requirementCheckType : RequirementCheckType.NONE;
	}

	@Override
	public ShopItem copy() {
		ShopItem shopItem = new ShopItem(getId(), getAmount(), getPrice(), copyOfAttributes());

		shopItem.placeholderRule = placeholderRule;
		shopItem.price = this.price;
		shopItem.defaultStockItem = this.defaultStockItem;
		shopItem.placeholderId = this.placeholderId;
		shopItem.requiredLevels = this.requiredLevels;
		shopItem.additionalItems = this.additionalItems;
		shopItem.additionalRequirements = this.additionalRequirements;
		shopItem.onBuy = this.onBuy;
		shopItem.requirementCheckType = this.requirementCheckType;

		return shopItem;
	}

	@Override
	public void remove() {
		if (!defaultStockItem) {
			super.remove();
		}
	}

	public int getDisplayId(Player player) {
		if (placeholderRule != null) {

			int placeHolder = placeholderId == -1 ? getDef().placeholderMainId : placeholderId;
			switch (placeholderRule) {
				case SHOW_ON_EMPTY:
					return getAmount() == 0 ? placeHolder : super.getId();
				case SHOW_ON_REQUIREMENT_MISSING:
					return !hasAllRequirements(player) ? placeHolder : super.getId();
				case SHOW_ON_REQUIREMENT_MET:
					return hasAllRequirements(player) ? placeHolder : super.getId();
				default:
				case NONE:
					break;
			}

		}
		return super.getId();
	}

	public boolean hasAllRequirements(Player player) {
		boolean hasStats = requiredLevels == null || requiredLevels.isEmpty() || requiredLevels.stream().allMatch(requirement -> requirement.hasRequirement(player));
		boolean meetsAdditionalRequirements = additionalRequirements == null || additionalRequirements.apply(player).isEmpty();
		return hasStats && meetsAdditionalRequirements;
	}

	public boolean printRequirements(Player player) {
		boolean hasStats = requiredLevels == null || requiredLevels.isEmpty() || requiredLevels.stream().allMatch(requirement -> requirement.hasRequirement(player));
		boolean meetsAdditionalRequirements = additionalRequirements == null || additionalRequirements.apply(player).isEmpty();


		if (!hasStats) {
			requiredLevels
				.stream()
				.filter(requirement -> !requirement.hasRequirement(player))
				.forEach(requirement -> player.sendMessage("You require level " + requirement.requiredLevel + " " + requirement.statType + " to buy this."));
		}
		if (!meetsAdditionalRequirements) {
			player.sendMessage(additionalRequirements.apply(player));
		}
		return hasStats && meetsAdditionalRequirements;
	}

	public void validate() {
		if (placeholderId != -1 && placeholderRule == PlaceHolderRule.NONE) {
			log.warn("Item {} has placeholder ID set but no rule, so will be ignored!", this);
		}
	}
}
