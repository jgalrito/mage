

package mage.abilities.common;

import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.effects.Effect;
import mage.constants.Zone;
import mage.game.Game;
import mage.game.events.DamagedCreatureEvent;
import mage.game.events.GameEvent;
import mage.game.permanent.Permanent;
import mage.target.targetpointer.FixedTarget;

/**
 * @author LevelX
 */
public class DealsDamageToACreatureAttachedTriggeredAbility extends TriggeredAbilityImpl {

    private boolean combatOnly;
    private final boolean setTargetPointer;
    private final String attachedDescription;

    public DealsDamageToACreatureAttachedTriggeredAbility(Effect effect, boolean combatOnly, String attachedDescription, boolean optional, boolean setTargetPointer) {
        super(Zone.BATTLEFIELD, effect, optional);
        this.setTargetPointer = setTargetPointer;
        this.attachedDescription = attachedDescription;
    }

    public DealsDamageToACreatureAttachedTriggeredAbility(final DealsDamageToACreatureAttachedTriggeredAbility ability) {
        super(ability);
        this.setTargetPointer = ability.setTargetPointer;
        this.combatOnly = ability.combatOnly;
        this.attachedDescription = ability.attachedDescription;
    }

    @Override
    public DealsDamageToACreatureAttachedTriggeredAbility copy() {
        return new DealsDamageToACreatureAttachedTriggeredAbility(this);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == GameEvent.EventType.DAMAGED_CREATURE;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        if (!combatOnly || ((DamagedCreatureEvent) event).isCombatDamage()) {
            Permanent attachment = game.getPermanent(this.getSourceId());
            if (attachment != null
                    && attachment.getAttachedTo() != null
                    && event.getSourceId().equals(attachment.getAttachedTo())) {
                if (setTargetPointer) {
                    for (Effect effect : this.getEffects()) {
                        effect.setTargetPointer(new FixedTarget(event.getTargetId()));
                        effect.setValue("damage", event.getAmount());
                    }
                }
                return true;
            }

        }
        return false;
    }

    @Override
    public String getRule() {
        return new StringBuilder("Whenever ").append(attachedDescription)
                .append(" deals ")
                .append(combatOnly ? "combat " : "")
                .append("damage to a creature, ")
                .append(super.getRule()).toString();
    }

}
