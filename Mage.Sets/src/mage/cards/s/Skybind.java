
package mage.cards.s;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.abilityword.ConstellationAbility;
import mage.abilities.common.delayed.AtTheBeginOfNextEndStepDelayedTriggeredAbility;
import mage.abilities.effects.Effect;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.ReturnToBattlefieldUnderOwnerControlTargetEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.filter.FilterPermanent;
import mage.filter.predicate.Predicates;
import mage.filter.predicate.mageobject.CardTypePredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.target.TargetPermanent;
import mage.target.targetpointer.FixedTarget;

/**
 *
 * @author LevelX2
 */
public final class Skybind extends CardImpl {

    private static final FilterPermanent filter = new FilterPermanent("nonenchantment permanent");

    static {
        filter.add(Predicates.not(new CardTypePredicate(CardType.ENCHANTMENT)));
    }

    public Skybind(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.ENCHANTMENT}, "{3}{W}{W}");

        // Constellation — When Skybind or another enchantment enters the battlefield under your control, exile target nonenchantment permanent. Return that card to the battlefield under its owner's control at the beginning of the next end step.
        Ability ability = new ConstellationAbility(new SkybindEffect(), false);
        ability.addTarget(new TargetPermanent(filter));
        this.addAbility(ability);
    }

    public Skybind(final Skybind card) {
        super(card);
    }

    @Override
    public Skybind copy() {
        return new Skybind(this);
    }
}

class SkybindEffect extends OneShotEffect {

    public SkybindEffect() {
        super(Outcome.Detriment);
        staticText = "exile target nonenchantment permanent. Return that card to the battlefield under its owner's control at the beginning of the next end step";
    }

    public SkybindEffect(final SkybindEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Permanent permanent = game.getPermanent(getTargetPointer().getFirst(game, source));
        Permanent sourcePermanent = game.getPermanentOrLKIBattlefield(source.getSourceId());
        if (permanent != null && sourcePermanent != null) {
            if (permanent.moveToExile(source.getSourceId(), sourcePermanent.getName(), source.getSourceId(), game)) {
                //create delayed triggered ability
                Effect effect = new ReturnToBattlefieldUnderOwnerControlTargetEffect();
                effect.setText("Return that card to the battlefield under its owner's control at the beginning of the next end step");
                effect.setTargetPointer(new FixedTarget(getTargetPointer().getFirst(game, source), game));
                game.addDelayedTriggeredAbility(new AtTheBeginOfNextEndStepDelayedTriggeredAbility(effect), source);
                return true;
            }
        }
        return false;
    }

    @Override
    public SkybindEffect copy() {
        return new SkybindEffect(this);
    }
}
